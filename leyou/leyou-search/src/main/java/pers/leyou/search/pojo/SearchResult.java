package pers.leyou.search.pojo;

import pers.leyou.common.pojo.PageResult;
import pers.leyou.item.pojo.Brand;

import java.util.List;
import java.util.Map;

/******************************************************************************
 * PageResult 的扩展类（用来表示分页结果）
 * 扩展两个新的属性：分类集合 、 品牌集合、规格参数集合
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/25 15:57 (CST)
 ******************************************************************************/
public class SearchResult extends PageResult<Goods> {

    /** 分类集合 */
    private List<Map<String, Object>> categories;

    /** 品牌集合 */
    private List<Brand> brands;

    /** 规格参数 */
    private List<Map<String, Object>> specs;

    public SearchResult() { }

    public SearchResult(List<Map<String, Object>> categories,
                        List<Brand> brands, List<Map<String, Object>> specs) {
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, List<Goods> items,
                        List<Map<String, Object>> categories,
                        List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, int totalPage, List<Goods> items,
                        List<Map<String, Object>> categories, List<Brand> brands,
                        List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, int totalPage, List<Goods> items,
                        List<Map<String, Object>> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public List<Map<String, Object>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, Object>> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }
}