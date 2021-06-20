package pers.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.leyou.item.pojo.*;
import pers.leyou.search.client.BrandClient;
import pers.leyou.search.client.CategoryClient;
import pers.leyou.search.client.GoodsClient;
import pers.leyou.search.client.SpecificationClient;
import pers.leyou.search.pojo.Goods;
import pers.leyou.search.pojo.SearchRequest;
import pers.leyou.search.pojo.SearchResult;
import pers.leyou.search.reponsitory.GoodsRepository;

import java.io.IOException;
import java.util.*;

/******************************************************************************
 * 与 elasticsearch 交互的逻辑服务类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/16 20:22 (CST)
 ******************************************************************************/
@Service
public class SearchService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 根据商品集信息，创建并编辑商品（goods）信息
     * @param spu 商品集
     * @return goods
     * @throws IOException IOException
     */
    public Goods buildGoods(Spu spu) throws IOException {
        // 创建 goods  对象
        Goods goods = new Goods();
        // 查询品牌
        Brand brand = this.brandClient.queryBrandsById(spu.getBrandId());
        // 查询分类名称
        List<String> names = this.categoryClient.queryNamesByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())
        );
        // 查询 Spu 下的所有 Sku
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spu.getId());
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuMapList = new ArrayList<>();
        // 遍历 skus，获取价格集合
        skus.forEach(
                sku -> {
                    prices.add(sku.getPrice());
                    Map<String, Object> skuMap = new HashMap<>();
                    skuMap.put("id", sku.getId());
                    skuMap.put("title", sku.getTitle());
                    skuMap.put("price", sku.getPrice());
                    skuMap.put(
                            "image",
                            // 获取 sku 中的图片，数据库的图片可能是多张，多张图片是用逗号来分隔的
                            // 所以也以逗号来切割返回图片数组，获取第一张图片
                            StringUtils.isNotBlank(sku.getImages())
                                    ? StringUtils.split(sku.getImages(), ",")[0]
                                    : ""
                    );
                    skuMapList.add(skuMap);
                }
        );
        // 查询出所有搜索规格参数
        List<SpecParam> params = this.specificationClient.
                queryParams(null, spu.getCid3(), null, true);
        // 查询spuDetail。获取规格参数数据
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
        // 获取通用的规格参数（把通用的规格参数值，进行反序列化）
        Map<Long, Object> genericSpecMap = MAPPER.readValue(
                spuDetail.getGenericSpec(),
                new TypeReference<Map<Long, Object>>() {}
        );
        // 获取特殊的规格参数（把特殊的规格参数值，进行反序列化）
        Map<Long, List<Object>> specialSpecMap = MAPPER.readValue(
                spuDetail.getSpecialSpec(),
                new TypeReference<Map<Long, List<Object>>>() {}
        );
        // 定义 Map 接收 {规格参数名， 规格参数值}
        Map<String, Object> paramMap = new HashMap<>();
        params.forEach(
                param -> {
                    // 判断规格参数的类型，是否是通用规格参数
                    if (param.getGeneric()) {
                        // 获取通用规格参数值
                        String value = genericSpecMap.get(param.getId()).toString();
                        // 判断是否是数值类型
                        if (param.getNumeric()) {
                            // 如果是数值的话，判断该数值落在哪个区间
                            value = chooseSegment(value, param);
                        }
                        // 把参数名和值放入结果集中
                        paramMap.put(param.getName(), value);
                    } else {
                        paramMap.put(param.getName(), specialSpecMap.get(param.getId()));
                    }
                }
        );
        // 设置参数
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        // 拼接 all 字段，需要商品分类名称以及商品品牌名称
        goods.setAll(
                spu.getTitle() + brand.getName() + StringUtils.join(names, " ")
        );
        // 获取 spu 下的所有 sku 的价格
        goods.setPrice(prices);
        // 获取 spu 下的所有 sku，并转化为 json 字符串
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        // 获取所有查询的规格参数 {name: value}
        goods.setSpecs(paramMap);
        return goods;
    }

    /**
     * 选择区间
     * @param value 数值
     * @param param 参数
     * @return 数值所在区间
     */
    private String chooseSegment(String value, SpecParam param) {
        double valueD = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : param.getSegments().split(",")) {
            String[] segments = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segments[0]);
            double end = Double.MAX_VALUE;
            if (segments.length == 2) {
                end = NumberUtils.toDouble(segments[1]);
            }
            // 判断是否在范围内
            if (valueD >= begin && valueD < end) {
                if (segments.length == 1) {
                    result = segments[0] + param.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segments[1] + param.getUnit() + "以下";
                } else {
                    result = segment + param.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 搜索商品
     * @param request 搜索请求
     * @return 搜索商品结果
     */
    public SearchResult search(SearchRequest request) {
        String key = request.getKey();
        // 判断是否有搜索条件，如果没有，直接返回 null。不允许搜索全部商品
        if (StringUtils.isBlank(key)) {
            return null;
        }
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 基本查询条件
        // MatchQueryBuilder basicQuery = QueryBuilders.matchQuery("all", key).operator(Operator.AND);
        BoolQueryBuilder boolQueryBuilder = this.buildBooleanQueryBuilder(request);
        // 对 key 进行全文检索查询
        // queryBuilder.withQuery(basicQuery);
        queryBuilder.withQuery(boolQueryBuilder);
        // 通过 sourceFilter 设置返回的结果字段，我们只需要 id、skus、subTitle
        queryBuilder.withSourceFilter(
                new FetchSourceFilter(new String[] {"id", "skus", "subTitle"}, null)
        );

        // 商品分类集合、品牌集合 ————> 聚合
        String categoryAggregationName = "categories";
        String brandAggregationName = "brands";
        queryBuilder.addAggregation(
                AggregationBuilders.terms(categoryAggregationName).field("cid3"));
        queryBuilder.addAggregation(
                AggregationBuilders.terms(brandAggregationName).field("brandId"));

        // 排序
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if (StringUtils.isNotBlank(sortBy)) {
            queryBuilder.withSort(
                    SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC)
            );
        }

        // 查询，获取结果
        // 执行搜索，获取搜索的结果集
        AggregatedPage<Goods> goodsPage =
                (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        // 解析聚合结果集
        List<Map<String, Object>> categories =
                getCategoryAggResult(goodsPage.getAggregation(categoryAggregationName));
        List<Brand> brands = getBrandAggResult(goodsPage.getAggregation(brandAggregationName));

        // 判断是否是一个分类，只有一个分类时，才做规格参数聚合
        List<Map<String, Object>> specs = null;
        if (!CollectionUtils.isEmpty(categories) && categories.size() == 1) {
            // 对规格参数进行聚合
            // specs = this.getParamAggResult((Long) categories.get(0).get("id"), basicQuery);
            specs = this.getParamAggResult((Long) categories.get(0).get("id"), boolQueryBuilder);
        }
        // 准备分页参数
        int page = request.getPage();
        int size = request.getSize();
        // 分页，根据请求参数中的当前页面、页面大小进行分页
        queryBuilder.withPageable(PageRequest.of(page - 1, size));
        // 总条数、总页数
        Long total = goodsPage.getTotalElements();
        int totalPage = (total.intValue() + size - 1) / size;

        // 封装结果，并返回
        return new SearchResult(
                total,
                totalPage,
                goodsPage.getContent(),
                categories,
                brands,
                specs
        );
    }

    /**
     * 构建 bool 查询构建器
     * @param request 搜索请求对象
     * @return 有查询条件的 bool 查询构建器
     */
    private BoolQueryBuilder buildBooleanQueryBuilder(SearchRequest request) {
        // 自定义布尔查询对象构建
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 给布尔查询添加基本查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        // 添加过滤条件
        if (CollectionUtils.isEmpty(request.getFilter())){
            return boolQueryBuilder;
        }
        for (Map.Entry<String, String> entry : request.getFilter().entrySet()) {
            String key = entry.getKey();
            // 如果过滤条件是“品牌”, 过滤的字段名：brandId
            if (StringUtils.equals("品牌", key)) {
                key = "brandId";
            } else if (StringUtils.equals("分类", key)) {
                // 如果是“分类”，过滤字段名：cid3
                key = "cid3";
            } else {
                // 如果是规格参数名，过滤字段名：specs.key.keyword
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }
        return boolQueryBuilder;
    }

    /**
     * 根据查询条件聚合规格参数
     * @param categoryId 商品分类 id
     * @param boolQueryBuilder 基本查询条件
     * @return 聚合后的规格参数
     */
    private List<Map<String, Object>> getParamAggResult(Long categoryId, BoolQueryBuilder boolQueryBuilder) {
        // 自定义查询对象构建
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本查询条件
        queryBuilder.withQuery(boolQueryBuilder);
        // 查询要聚合的规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, categoryId, null, true);
        // 添加规格参数的聚合
        params.forEach(param ->
            queryBuilder.addAggregation(AggregationBuilders.terms(param.getName())
                            .field("specs." + param.getName() + ".keyword"))
        );
        // 添加结果集过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        // 执行聚合查询，获取聚合结果集
        AggregatedPage<Goods> goodsAggregatedPage =
                (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());
        // 初始化一个存放规格参数参数 {k: 规格参数名, options: 聚合规格参数值} 的集合
        List<Map<String, Object>> specs = new ArrayList<>();
        // 解析聚合结果集，key - 聚合名称（规格参数名），value - 聚合对象
        Map<String, Aggregation> stringAggregationMap = goodsAggregatedPage.getAggregations().asMap();
        // 遍历聚合结果集
        Set<Map.Entry<String, Aggregation>> aggregationEntries = stringAggregationMap.entrySet();
        aggregationEntries.forEach(aggregationMap -> {
            // 初始化一个 map，{k: 规格参数名, options: 聚合规格参数值}
            Map<String, Object> kOptionsMap = new HashMap<>();
            // 从聚合 map 中，获取规格参数名，放到新的 Map 中
            kOptionsMap.put("k", aggregationMap.getKey());
            // 获取聚合（规格参数值聚合）
            StringTerms terms = (StringTerms) aggregationMap.getValue();
            // 初始化一个 options 集合，收集桶中的 key（规格参数值）
            List<String> optionList = new ArrayList<>();
            // 获取桶
            List<StringTerms.Bucket> bucketList = terms.getBuckets();
            // 收集桶中的规格参数值
            bucketList.forEach(bucket -> optionList.add(bucket.getKeyAsString()));
            // 将规格参数值集合，放到新的 Map 中
            kOptionsMap.put("options", optionList);
            // {k: 规格参数名, options: 聚合规格参数值} 放到规格参数集合中
            specs.add(kOptionsMap);
        });
        return specs;
    }

    /**
     * 解析品牌聚合结果集
     * @param aggregation 聚合
     * @return 商品品牌聚合的解析结果
     */
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        // 处理聚合结果集
        LongTerms terms = (LongTerms) aggregation;
        // 获取所有的品牌id桶
        List<LongTerms.Bucket> buckets = terms.getBuckets();
        // 定义一个品牌集合，搜集所有的品牌对象
        List<Brand> brands = new ArrayList<>();
        // 解析所有的id桶，查询品牌
        buckets.forEach(bucket -> {
            Brand brand = this.brandClient.queryBrandsById(bucket.getKeyAsNumber().longValue());
            brands.add(brand);
        });
        return brands;
    }

    /**
     * 解析分类
     * @param aggregation 聚合
     * @return 商品分类聚合的解析结果
     */
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        // 处理聚合结果集
        LongTerms terms = (LongTerms) aggregation;
        // 获取所有的分类id桶
        List<LongTerms.Bucket> buckets = terms.getBuckets();
        // 定义一个品牌集合，搜集所有的品牌对象
        List<Map<String, Object>> categories = new ArrayList<>();
        List<Long> categoryIdList = new ArrayList<>();
        // 解析所有的id桶，查询品牌
        buckets.forEach(bucket -> {
            categoryIdList.add(bucket.getKeyAsNumber().longValue());
        });
        List<String> names = this.categoryClient.queryNamesByIds(categoryIdList);
        for (int i = 0; i < categoryIdList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", categoryIdList.get(i));
            map.put("name", names.get(i));
            categories.add(map);
        }
        return categories;
    }
}
