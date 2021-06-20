package pers.leyou.item.bo;

import pers.leyou.item.pojo.Sku;
import pers.leyou.item.pojo.Spu;
import pers.leyou.item.pojo.SpuDetail;

import javax.persistence.Transient;
import java.util.List;

/******************************************************************************
 * 继承商品集实现类，并且拓展 categoryName、brandName、spuDetail、List<Sku> 属性
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/11/26 21:28 (CST)
 ******************************************************************************/
public class SpuBo extends Spu {

    /** 商品分类名称 */
    @Transient
    String categoryName;

    /** 品牌名称 */
    @Transient
    String brandName;

    /** 商品详情 */
    @Transient
    SpuDetail spuDetail;

    /** 具体商品列表 */
    @Transient
    List<Sku> skus;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
