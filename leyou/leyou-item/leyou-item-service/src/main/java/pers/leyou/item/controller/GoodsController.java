package pers.leyou.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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
import pers.leyou.item.service.GoodsService;

import java.util.List;

/******************************************************************************
 * 商品管理
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/11/26 21:37 (CST)
 ******************************************************************************/
@Controller

public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 查询本页面的商品集信息（id、标题、商品分类、品牌）
     * @param key 过滤条件
     * @param saleable 上架/下架
     * @param page 当前页
     * @param rows 每页大小
     * @return 商品集信息
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuBoByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows
    ) {
        // 根据条件，查询商品集信息
        PageResult<SpuBo> pageResult = this.goodsService.querySpuBoByPage(key, saleable, page, rows);
        if (CollectionUtils.isEmpty(pageResult.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 商品新增接口（保存商品集、商品集详情、具体商品信息、库存）
     * @param supBo 扩展了部分信息的商品集对象
     * @return 响应状态码（新增商品信息是否成功）
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo supBo) {
        this.goodsService.saveGoods(supBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询商品集详情信息
     * @param spuId 商品集 id
     * @return 商品集详情信息 or notFound
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long spuId) {
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(spuId);
        if (spuDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 查询具体的商品集合（sku 集合）
     * @param spuId 商品集 id（spu id）
     * @return sku 集合
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("id") Long spuId) {
        List<Sku> skus = this.goodsService.querySkusBySpuId(spuId);
        if (CollectionUtils.isEmpty(skus)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * 修改商品信息
     * @param spuBo 扩展了其他数据的 spu（商品集）
     * @return 响应状态码（修改是否成功）
     */
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo) {
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 通过商品集 id 删除商品
     * @param spuId 商品集 id
     * @return 响应状态码（删除是否成功）
     */
    @DeleteMapping("/goods/spu/{spuId}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("spuId") Long spuId) {
        try {
            this.goodsService.deleteGoods(spuId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            // 500
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 通过商品集 id 修改商品的上架/下架状态
     * @param spuId 商品集 id
     * @return 响应状态码（上架/下架是否成功）
     */
    @PutMapping("/goods/spu/out/{spuId}")
    public ResponseEntity<Void> changeSaleableBySpuId(@PathVariable("spuId") Long spuId) {
        this.goodsService.changeSaleableBySpuId(spuId);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据商品集 id，查询商品集
     * @param spuId 商品集 id
     * @return 商品集
     */
    @GetMapping("{spuId}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("spuId") Long spuId) {
        Spu spu = this.goodsService.querySpuById(spuId);
        return spu == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(spu);
    }
}
