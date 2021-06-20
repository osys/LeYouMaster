package pers.leyou.item.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/******************************************************************************
 * 规格参数实体类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/11/23 19:50 (CST)
 ******************************************************************************/
@Table(name = "tb_spec_param")
public class SpecParam {

    /** 规格参数 id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 商品分类 id */
    private Long cid;

    /** 规格组 id */
    private Long groupId;

    /** 参数名 */
    private String name;

    /** 是否是数字类型参数 */
    @Column(name = "`numeric`")
    private Boolean numeric;

    /** 数字类型参数的单位，非数字类型可以为空 */
    private String unit;

    /** 是否是sku通用属性 */
    private Boolean generic;

    /** 是否用于搜索过滤 */
    private Boolean searching;

    /** 数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0 */
    private String segments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNumeric() {
        return numeric;
    }

    public void setNumeric(Boolean numeric) {
        this.numeric = numeric;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getGeneric() {
        return generic;
    }

    public void setGeneric(Boolean generic) {
        this.generic = generic;
    }

    public Boolean getSearching() {
        return searching;
    }

    public void setSearching(Boolean searching) {
        this.searching = searching;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }
}
