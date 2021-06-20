package pers.leyou.item.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.Transient;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/******************************************************************************
 * 具体的商品实体类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/12/04 16:06 (CST)
 ******************************************************************************/
@Table(name = "tb_sku")
public class Sku {

    /** 具体商品的id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 商品集 id */
    private Long spuId;

    /** 商品标题 */
    private String title;

    /** 商品的图片 */
    private String images;

    /** 销售价格，单位为分 */
    private Long price;

    /** 商品特殊规格的键值对 */
    private String ownSpec;

    /** 商品特殊规格的下标 */
    private String indexes;

    /** 是否有效，0无效，1有效 */
    private Boolean enable;

    /** 添加时间 */
    private Date createTime;

    /** 最后修改时间 */
    private Date lastUpdateTime;

    /** 库存 */
    @Transient
    private Integer stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }

    public String getIndexes() {
        return indexes;
    }

    public void setIndexes(String indexes) {
        this.indexes = indexes;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
