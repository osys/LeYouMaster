package pers.leyou.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/******************************************************************************
 * 封装要保存到索引库的数据的实体类
 *
 * 索引库名称：goods
 * 索引库中的数据类型：文档类型
 * 分片数量：1
 * 副本数量：0
 *
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/10 13:54 (CST)
 ******************************************************************************/
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    /** 商品集 id（spuID） */
    @Id
    private Long id;

    /**
     * 所有需要被搜索的信息，包含标题，分类，甚至品牌
     * 字段类型：文本类型，分词器：ik_max_word
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;

    /**
     * 卖点
     * 字段类型：关键字类型，不需要索引
     */
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;

    /** 品牌id */
    @Field(type = FieldType.Long)
    private Long brandId;

    /** 1级分类id */
    @Field(type = FieldType.Long)
    private Long cid1;

    /** 2级分类id */
    @Field(type = FieldType.Long)
    private Long cid2;

    /** 3级分类id */
    @Field(type = FieldType.Long)
    private Long cid3;

    /** 创建时间 */
    @Field(type = FieldType.Date)
    private Date createTime;

    /**
     * 价格数组
     * 是所有 SKU 的价格集合。方便根据价格进行筛选过滤
     */
    private List<Long> price;

    /**
     * List<sku> 信息的 json 结构
     * 包含：skuId、image、price、title 字段
     * 字段类型：关键字类型，不需要索引、不搜索
     */
    @Field(type = FieldType.Keyword, index = false)
    private String skus;

    /**
     * 所有规格参数的集合
     * 可搜索的规格参数，key是参数名，值是参数值
     */
    private Map<String, Object> specs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCid1() {
        return cid1;
    }

    public void setCid1(Long cid1) {
        this.cid1 = cid1;
    }

    public Long getCid2() {
        return cid2;
    }

    public void setCid2(Long cid2) {
        this.cid2 = cid2;
    }

    public Long getCid3() {
        return cid3;
    }

    public void setCid3(Long cid3) {
        this.cid3 = cid3;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Long> getPrice() {
        return price;
    }

    public void setPrice(List<Long> price) {
        this.price = price;
    }

    public String getSkus() {
        return skus;
    }

    public void setSkus(String skus) {
        this.skus = skus;
    }

    public Map<String, Object> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, Object> specs) {
        this.specs = specs;
    }
}
