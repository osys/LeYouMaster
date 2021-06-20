package pers.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import pers.leyou.common.pojo.PageResult;
import pers.leyou.item.bo.SpuBo;
import pers.leyou.item.pojo.Sku;
import pers.leyou.item.pojo.Spu;
import pers.leyou.item.pojo.SpuDetail;

import java.util.List;

/******************************************************************************
 * 商品服务类接口
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/16 16:22 (CST)
 ******************************************************************************/
public interface GoodsApi {

    /**
     * 查询本页面的商品集信息（id、标题、商品分类、品牌）
     * @param key 过滤条件
     * @param saleable 上架/下架
     * @param page 当前页
     * @param rows 每页大小
     * @return 商品集信息
     */
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuBoByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows
    );

    /**
     * 商品新增接口（保存商品集、商品集详情、具体商品信息、库存）
     * @param supBo 扩展了部分信息的商品集对象
     */
    @PostMapping("goods")
    public void saveGoods(@RequestBody SpuBo supBo);

    /**
     * 查询商品集详情信息
     * @param spuId 商品集 id
     * @return 商品集详情信息
     */
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 查询具体的商品集合（sku 集合）
     * @param spuId 商品集 id（spu id）
     * @return sku 集合
     */
    @GetMapping("sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id") Long spuId);

    /**
     * 修改商品信息
     * @param spuBo 扩展了其他数据的 spu（商品集）
     */
    @PutMapping("/goods")
    public void updateGoods(@RequestBody SpuBo spuBo);

    /**
     * 通过商品集 id 删除商品
     * @param spuId 商品集 id
     */
    @DeleteMapping("/goods/spu/{spuId}")
    public void deleteGoods(@PathVariable("spuId") Long spuId);

    /**
     * 通过商品集 id 修改商品的上架/下架状态
     * @param spuId 商品集 id
     */
    @PutMapping("/goods/spu/out/{spuId}")
    public void changeSaleableBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 根据spu的id查询spu
     * @param spuId spuId
     * @return spu
     */
    @GetMapping("{spuId}")
    public Spu querySpuById(@PathVariable("spuId") Long spuId);
}
