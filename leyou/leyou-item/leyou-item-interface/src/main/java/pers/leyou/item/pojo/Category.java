package pers.leyou.item.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/******************************************************************************
 * 商品管理中的分类管理实体类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/09/27 10:15 (CST)
 ******************************************************************************/
@Table(name="tb_category")
public class Category {

    /** 商品分类 id */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /** 商品分类名称 */
    private String name;

    /** 父分类 id */
    private Long parentId;

    /**
     * 注意isParent生成的getter和setter方法名称需要手动加上Is
     * 是否为父节点，0为否，1为是
     */
    private Boolean isParent;

    /** 排序指数，越小越靠前 */
    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean parent) {
        isParent = parent;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}