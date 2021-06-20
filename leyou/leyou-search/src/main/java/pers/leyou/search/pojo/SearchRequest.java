package pers.leyou.search.pojo;

import java.util.Map;

/******************************************************************************
 * 搜索请求对象
 * 搜索请求对象有 key（搜索关键字）、page 等属性
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/18 22:26 (CST)
 ******************************************************************************/
public class SearchRequest {

    /** 搜索条件 */
    private String key;

    /** 当前页 */
    private Integer page;

    /** 排序字段 */
    private String sortBy;

    /** 是否降序 */
    private Boolean descending;

    /** 过滤条件 */
    private Map<String, String> filter;

    /** 每页大小，不从页面接收，而是固定大小 */
    private static final Integer DEFAULT_SIZE = 20;

    /** 默认页 */
    private static final Integer DEFAULT_PAGE = 1;

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public Integer getPage() { return (page == null) ? DEFAULT_PAGE : Math.max(DEFAULT_PAGE, page); }

    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return DEFAULT_SIZE; }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }
}
