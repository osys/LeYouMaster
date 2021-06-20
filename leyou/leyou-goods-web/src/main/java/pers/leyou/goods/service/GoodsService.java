package pers.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.leyou.goods.client.BrandClient;
import pers.leyou.goods.client.CategoryClient;
import pers.leyou.goods.client.GoodsClient;
import pers.leyou.goods.client.SpecificationClient;
import pers.leyou.item.pojo.*;

import java.util.*;

/******************************************************************************
 * 页面商品信息业务逻辑
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/04/06 22:58 (CST)
 ******************************************************************************/
@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    /**
     * 封装数据模型（商品信息）
     * @param spuId 商品集 id
     * @return 数据模型：
     *         spu（商品集）
     *         spuDetail（商品集详情）
     *         List<sku>（一商品集中商品的集合）
     *         category（商品分类）
     *         brand（品牌对象）
     *         specificationGroup（规格组）
     *         skuSpecificationParam （sku 的特有规格参数）
     */
    public Map<String, Object> loadData(Long spuId) {
        // 根据 spuId 查询 spu 对象
        Spu spu = this.goodsClient.querySpuById(spuId);
        // 根据 spuId 查询 spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
        // 根据 spuId 查询 sku 集合
        List<Sku> skuList = this.goodsClient.querySkusBySpuId(spuId);
        // 查询分类
        List<Long> categoryIds = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> categoryNames = this.categoryClient.queryNamesByIds(categoryIds);
        List<Map<String, Object>> categories = new ArrayList<>();
        for (int i = 0; i < categoryIds.size(); i++) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", categoryIds.get(i));
            categoryMap.put("name", categoryNames.get(i));
            categories.add(categoryMap);
        }
        // 根据 brandId 查询品牌
        Brand brand = this.brandClient.queryBrandsById(spu.getBrandId());
        // 查询规格参数组
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());
        // 查询特殊的规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), null, false);
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });

        // 封装
        Map<String, Object> model = new HashMap<>();
        // 封装 spu
        model.put("spu", spu);
        // 封装 spuDetail
        model.put("spuDetail", spuDetail);
        // 封装 sku 集合
        model.put("skus", skuList);
        // 封装分类
        model.put("categories", categories);
        // 封装品牌
        model.put("brand", brand);
        // 封装规格参数组
        model.put("groups", groups);
        // 封装特殊规格参数
        model.put("paramMap", paramMap);
        return model;
    }
}
