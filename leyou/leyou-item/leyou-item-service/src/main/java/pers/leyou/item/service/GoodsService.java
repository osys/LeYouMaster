package pers.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pers.leyou.common.pojo.PageResult;
import pers.leyou.item.bo.SpuBo;
import pers.leyou.item.mapper.BrandMapper;
import pers.leyou.item.mapper.SkuMapper;
import pers.leyou.item.mapper.SpuDetailMapper;
import pers.leyou.item.mapper.SpuMapper;
import pers.leyou.item.mapper.StockMapper;
import pers.leyou.item.pojo.Sku;
import pers.leyou.item.pojo.Spu;
import pers.leyou.item.pojo.SpuDetail;
import pers.leyou.item.pojo.Stock;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/******************************************************************************
 * 商品相关的业务逻辑
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/11/26 21:41 (CST)
 ******************************************************************************/
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 查询本页面的商品集信息（id、标题、商品分类、品牌）
     * @param key 过滤条件
     * @param saleable 上架/下架
     * @param page 当前页
     * @param rows 每页大小
     * @return 商品集信息
     */
    public PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable,
                                              Integer page, Integer rows) {
        // 创建通用 Example 查询对象（根据 Spu 生成的）
        Example example = new Example(Spu.class);
        // 创建标准 Example 查询对象
        Example.Criteria exampleCriteria = example.createCriteria();

        // 搜索条件
        if (StringUtils.isNotBlank(key)) {
            // 添加字段值为 key 的模糊查询条件
            exampleCriteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            // 添加字段等于 saleable 条件
            exampleCriteria.andEqualTo("saleable", saleable);
        }

        // 分页条件
        PageHelper.startPage(page, rows);

        // 执行查询
        List<Spu> spus = this.spuMapper.selectByExample(example);
        // 对查询到的结果进行封装
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        List<SpuBo> spuBos = new ArrayList<>();
        spus.forEach(
            spu -> {
                SpuBo spuBo = new SpuBo();
                // copy共同属性的值到新的对象
                // 将 spu 和 spuBo 共同的属性值复制到 spuBo 对象中
                BeanUtils.copyProperties(spu, spuBo);
                // 根据商品分类id，查询商品分类名称
                List<String> names = this.categoryService.queryNamesByIds(
                        Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
                // 将查询到的商品分类名称集合用 "/" 隔开，并设置为 spuBo 对象中的 CategoryName 成员
                spuBo.setCategoryName(StringUtils.join(names, "/"));
                // 查询品牌的名称
                spuBo.setBrandName(
                        // 根据主键（品牌id）字段进行查询品牌名称
                        this.brandMapper.selectByPrimaryKey(spu.getBrandId()).getName()
                );
                // 将查询了 CategoryName、BrandName，拷贝了 spu 中共有属性值的 spuBo 添加到 spuBos 数组的末尾。
                spuBos.add(spuBo);
            }
        );
        // 商品集信息（id、标题、商品分类、品牌）
        return new PageResult<>(pageInfo.getTotal(), spuBos);
    }

    /**
     * 保存商品集、商品集详情、具体商品信息、库存数据
     * @param spuBo 扩展了部分信息的商品集对象
     */
    public void saveGoods(SpuBo spuBo) {
        /*
            新增 SPU（商品集），并设置默认字段
            id：商品集 id
            saleable：是否上架，0下架，1上架
            valid：是否有效，0已删除，1有效
            createTime：添加时间
            lastUpdateTime：最后修改时间
         */
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        // 保存 spuBo，null的属性不会保存，会使用数据库默认值
        this.spuMapper.insertSelective(spuBo);

        // 新增 SpuDetail（商品详情）
        SpuDetail spuDetail = spuBo.getSpuDetail();
        // 设置商品集 id
        spuDetail.setSpuId(spuBo.getId());
        // 保存 spuDetail，null的属性不会保存，会使用数据库默认值
        this.spuDetailMapper.insertSelective(spuDetail);

        // 保存 SKU（商品信息） 和 Stock（库存）
        saveSkuAndStock(spuBo);
    }

    /**
     * 保存 SKU（商品信息） 和 Stock（库存）
     * @param spuBo 扩展了部分信息的商品集对象
     */
    private void saveSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {
            // 新增sku
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            // 新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据 spuId 查询 spuDetail
     * @param spuId 商品集 id
     * @return 商品集详情信息 or null
     */
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据商品集id（spuId）查询具体商品信息的集合
     * @param spuId 商品集 id
     * @return 具体商品信息的集合
     */
    public List<Sku> querySkusBySpuId(Long spuId) {
        // 创建 sku 对象
        Sku sku = new Sku();
        // 设置 spu id
        sku.setSpuId(spuId);
        // 根据 sku 中的 spuId 属性值进行查询，查询条件使用等号
        List<Sku> skus = this.skuMapper.select(sku);
        skus.forEach(s -> {
            // 根据 sku id 查询库存（根据主键字段进行查询）
            Stock stock = this.stockMapper.selectByPrimaryKey(s.getId());
            // 在 sku 对象中，设置库存字段的值
            s.setStock(stock.getStock());
        });
        return skus;
    }

    /**
     * 修改商品信息
     * @param spuBo 扩展了其他数据的 spu（商品集）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoods(SpuBo spuBo) {
        // 根据商品集id，查询以前的具体商品信息（sku）集合
        List<Sku> skus = this.querySkusBySpuId(spuBo.getId());
        // 如果之前存在具体商品信息（sku），那么就把它删除
        if (!CollectionUtils.isEmpty(skus)) {
            // 返回以 skus 集合为源的流
            Stream<Sku> skusStream = skus.stream();
            // 返回一个流，该流包括将给定函数 Sku::getId 应用于 skusStream 流的元素的结果。（具体商品的 id 集合流）
            Stream<Long> idsStream = skusStream.map(Sku::getId);
            // 将 ids 流转化为 id 集合（具体商品的 id 集合）
            List<Long> ids = idsStream.collect(Collectors.toList());

            // 对于该商品集，删除以前存储的商品库存信息（stock）
            // 创建通用 Example 查询对象（根据 Stock 生成的）
            Example example = new Example(Stock.class);
            // 创建标准 Example 查询对象
            // 添加 skuId 字段值能在 ids 中找到的模糊查询条件（skuId in ids）
            example.createCriteria().andIn("skuId", ids);
            // 根据Example条件删除库存（Stock）数据
            this.stockMapper.deleteByExample(example);

            // 删除以前的sku
            Sku record = new Sku();
            record.setSpuId(spuBo.getId());
            this.skuMapper.delete(record);

        }
        // 新增具体商品（SKU）和库存（Stock）
        this.saveSkuAndStock(spuBo);

        // 更新商品集（SPU）
        spuBo.setLastUpdateTime(new Date());
        spuBo.setCreateTime(null);
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        // 根据主键（商品集 id）更新属性不为null的值
        this.spuMapper.updateByPrimaryKeySelective(spuBo);

        // 更新商品集详情（SpuDetail）
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
    }

    /**
     * 通过商品集 id 删除商品
     * @param spuId 商品集id
     */
    public void deleteGoods(Long spuId) {
        // 删除具体的商品（SKU）、商品库存（Stock）
        // 需要先删除商品库存（Stock），然后再删除具体的商品，因为删除商品库存要以 skuId 为条件
        this.deleteStockBySpuIdQuerySkuId(spuId);
        this.deleteSkuBySpuId(spuId);
        // 根据主键字段进行删除商品集（SPU）、商品集详情（SpuDetail）
        this.spuMapper.deleteByPrimaryKey(spuId);
        this.spuDetailMapper.deleteByPrimaryKey(spuId);
    }

    /**
     * 通过商品集 id 删除对应的那些具体的商品（Sku）
     * @param spuId 商品集 id
     */
    private void deleteSkuBySpuId(Long spuId) {
        // 设置 sku 对应的 spuId
        Sku sku = new Sku();
        sku.setId(spuId);
        // 根据实体 sku 属性 spuId 作为条件进行删除，查询条件使用等号
        this.skuMapper.delete(sku);
    }

    /**
     * 根据商品集 id（SpuId）查询该商品集下的所有具体商品的 id（SkuId）。
     * 然后通过那些具体的商品 ID，删除对应的商品
     * @param spuId 商品集 id
     */
    private void deleteStockBySpuIdQuerySkuId(Long spuId) {
        // 通过商品集 id 查询具体的商品（SKU）
        Sku querySku = new Sku();
        querySku.setId(spuId);
        List<Sku> skus = this.skuMapper.select(querySku);
        // 删除 sku
        if (!CollectionUtils.isEmpty(skus)) {
            // 获取具体的商品 id 的集合
            /// 获得sku_id集合
            List<Long> ids = skus.stream().map(Sku::getId).collect(Collectors.toList());

            // 对于该商品集，删除以前存储的商品库存信息（stock）
            // 创建通用 Example 查询对象（根据 Stock 生成的）
            Example example = new Example(Stock.class);
            // 创建标准 Example 查询对象
            // 添加 skuId 字段值能在 ids 中找到的模糊查询条件（skuId in ids）
            example.createCriteria().andIn("skuId", ids);
            // 根据Example条件删除库存（Stock）数据
            this.stockMapper.deleteByExample(example);
        }
    }

    /**
     * 通过商品集 id 修改商品的上架/下架状态
     * @param spuId 商品集 id
     */
    public void changeSaleableBySpuId(Long spuId) {
        // 根据主键字段（spuId）进行查询，查询商品集信息
        Spu spu = this.spuMapper.selectByPrimaryKey(spuId);
        // 存在该商品集
        if (spu != null) {
            spu.setSaleable(!spu.getSaleable());
            // 根据主键更新实体全部字段，null值会被更新
            this.spuMapper.updateByPrimaryKey(spu);
        }
    }

    /**
     * 根据商品集 id，查询商品集
     * @param spuId 商品集 id
     * @return 商品集
     */
    public Spu querySpuById(Long spuId) {
        return this.spuMapper.selectByPrimaryKey(spuId);
    }
}
