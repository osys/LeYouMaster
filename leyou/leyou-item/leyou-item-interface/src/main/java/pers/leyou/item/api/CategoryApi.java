package pers.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pers.leyou.item.pojo.Category;

import java.util.List;

/******************************************************************************
 * 商品分类服务类接口
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/16 16:22 (CST)
 ******************************************************************************/
@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据商品分类父节点的 id 查询商品分类信息
     * @param pid 商品分类父节点的 id
     * @return 商品分类信息集合
     */
    @GetMapping("list")
    public List<Category>
    queryCategoriesByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid);

    /**
     * 通过品牌 id 查询商品分类信息
     * @param bid 品牌 id
     * @return 品牌分类信息
     */
    @GetMapping("bid/{bid}")
    public List<Category> queryByBrandId(@PathVariable("bid") Long bid);

    /**
     * 通过商品分类 id 集合，查询商品分类名称的集合
     * @param ids 商品分类 id 集合
     * @return 商品分类名称的集合
     */
    @GetMapping("names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);
}
