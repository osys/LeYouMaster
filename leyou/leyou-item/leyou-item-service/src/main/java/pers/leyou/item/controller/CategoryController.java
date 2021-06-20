package pers.leyou.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pers.leyou.item.pojo.Category;
import pers.leyou.item.service.CategoryService;

import java.util.List;

/******************************************************************************
 * 分类管理
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/09/27 14:42 (CST)
 ******************************************************************************/
@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点的 id 查询子节点
     * @param pid parent's id
     * @return 状态码
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>>
    queryCategoriesByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        try {
            if (pid == null || pid < 0) {
                // 假如 pid 为空或者值小于0，返回响应值 400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            // 通过 pid 查询类别
            List<Category> categories = this.categoryService.queryCategoriesByPid(pid);
            if (CollectionUtils.isEmpty(categories)) {
                // 假如类别为空，或者为 null，返回响应值 404，即资源服务器未找到
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            // 查询成功，返回响应值 200
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 服务器内部错误，返回状态码 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 通过品牌 id 查询商品分类
     * @param bid 品牌 id
     * @return 响应数据 404 or 品牌分类
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        List<Category> list = this.categoryService.queryByBrandId(bid);
        if (list == null && list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 通过商品分类 id 集合，查询商品分类名称的集合
     * @param ids 商品分类 id 集合
     * @return 响应 404 or 商品分类名称的集合
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids) {
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }

    /**
     * 根据 3 级分类 id，查询 1 ~ 3 级的分类
     * @param id 3 级分类 id
     * @return 1 ~ 3 级分类 id
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id){
        List<Category> list = this.categoryService.queryAllByCid3(id);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}