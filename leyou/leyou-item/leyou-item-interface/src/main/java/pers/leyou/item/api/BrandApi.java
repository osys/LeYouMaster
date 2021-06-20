package pers.leyou.item.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.leyou.common.pojo.PageResult;
import pers.leyou.item.pojo.Brand;

import java.util.List;

/******************************************************************************
 * 商品品牌服务类接口
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/16 16:22 (CST)
 ******************************************************************************/
@RequestMapping("brand")
public interface BrandApi {

    /**
     * 根据查询条件，分页并排序查询品牌信息
     * @param key 搜索条件
     * @param page 页码
     * @param rows 每页最多显示的数据条数
     * @param sortBy 排序条件
     * @param desc 是否降序
     * @return 查询结果：品牌信息
     */
    @GetMapping("page")
    public PageResult<Brand> queryBrandsByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", required = false) Boolean desc
    );

    /**
     * 新增品牌
     * @param brand 品牌数据
     * @param cids 商品分类 id 集合
     */
    @PostMapping
    public void saveBrand(Brand brand, @RequestParam("cids") List<Long> cids);

    /**
     * 删除品牌数据
     * @param bid 品牌 id
     */
    @DeleteMapping("bid/{bid}")
    public void deleteBrand(@PathVariable("bid") String bid);

    /**
     * 根据商品分类id查询品牌信息集合
     * @param cid 商品分类 id
     * @return 品牌信息集
     */
    @GetMapping("/cid/{cid}")
    public List<Brand> queryBrandsByCid(@PathVariable("cid") Long cid);

    /**
     * 根据品牌 id，查询商品品牌名称
     * @param id 品牌 id
     * @return 商品品牌名称
     */
    @GetMapping("{id}")
    public Brand queryBrandsById(@PathVariable("id") Long id);

}
