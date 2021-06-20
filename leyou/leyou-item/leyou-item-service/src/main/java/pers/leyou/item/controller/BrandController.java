package pers.leyou.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import pers.leyou.common.pojo.PageResult;
import pers.leyou.item.pojo.Brand;
import pers.leyou.item.service.BrandService;

import java.util.List;

/******************************************************************************
 * 品牌管理
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/10/09 12:35 (CST)
 ******************************************************************************/
@RestController
@RequestMapping("brand")
public class BrandController {

    /** 从 SpringCloud 容器中注入品牌管理实现类 */
    @Autowired
    private BrandService brandService;

    /**
     * 根据查询条件，分页并排序查询品牌信息
     * @param key 搜索条件
     * @param page 页码
     * @param rows 每页最多显示的数据条数
     * @param sortBy 排序条件
     * @param desc 是否降序
     * @return 查询结果：品牌信息/notFound
     */
    @GetMapping("page") // get 请求路径
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", required = false) Boolean desc
    ) {
        PageResult<Brand> result = this.brandService.queryBrandByPage(key, page, rows, sortBy, desc);
        if (result == null || CollectionUtils.isEmpty(result.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增品牌
     * @param brand 品牌数据
     * @param cids 商品分类 id 集合
     * @return 状态码
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        this.brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除品牌数据
     * @param bid 品牌 id
     * @return 状态码
     */
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") String bid){
        String separator = "-";
        // 假如 bid 包含指定的字符 "-" 时，我们通过该字符对 bid 进行分割（即获取品牌id集合）
        if(bid.contains(separator)){
            String[] ids = bid.split(separator);
            for (String id:ids){
                // 删除品牌信息
                this.brandService.deleteBrand(Long.parseLong(id));
            }
        } else {
            // 只有删除一个品牌信息时
            this.brandService.deleteBrand(Long.parseLong(bid));
        }
        // 删除成功，返回状态码
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据商品分类id查询品牌信息集合
     * @param cid 商品分类 id
     * @return 品牌信息集
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid") Long cid) {
        List<Brand> brands = this.brandService.queryBrandsByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    /**
     * 根据品牌 id，查询商品品牌名称
     * @param id 品牌 id
     * @return 响应 404 or 商品品牌名称
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandsById(@PathVariable("id") Long id) {
        Brand brand = this.brandService.queryBrandById(id);
        if (brand == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }
}
