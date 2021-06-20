package pers.leyou.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import pers.leyou.item.pojo.SpecGroup;
import pers.leyou.item.pojo.SpecParam;
import pers.leyou.item.service.SpecificationService;

import java.util.List;

/******************************************************************************
 * 商品规格信息管理
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/11/23 20:20 (CST)
 ******************************************************************************/
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 通过商品分类id查询规格组信息
     * @param cid 商品分类 id
     * @return 查询结果：
     *                查询到规格组信息，返回规格组信息
     *                没有查询到规格组信息，返回404
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid") Long cid) {
        List<SpecGroup> groups = this.specificationService.queryGroupsByCid(cid);
        if (CollectionUtils.isEmpty(groups)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 通过商品分类id查询规格组信息（包含规格组参数集合信息）
     * @param cid 商品分类 id
     * @return 规格组信息
     */
    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("cid") Long cid) {
        List<SpecGroup> groups = this.specificationService.queryGroupsWithParam(cid);
        if(groups == null || groups.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 根据条件，查询规格参数信息
     * @param gid 规格组id
     * @param cid 商品分类 id
     * @param generic 是否是sku通用属性
     * @param searching 是否用于搜索过滤
     * @return 查询结果：
     *                 查询到规格参数信息，返回规格参数信息
     *                 没有查询到规格参数信息，返回404
     *
     *
     * 更新前的代码：
     *                   /**
     *                    * 根据规格组id查询规格参数信息
     *                    * @param gid 规格组 id
     *                    * @return 查询结果：
     *                    *                查询到规格参数信息，返回规格参数信息
     *                    *                没有查询到规格参数信息，返回404
     *                    * /
     *                   @GetMapping("params")
     *                   public ResponseEntity<List<SpecParam>> queryParamsByGid(@RequestParam("gid") Long gid) {
     *                       List<SpecParam> specParams = this.specificationService.queryParamsByGid(gid);
     *                       if (CollectionUtils.isEmpty(specParams)) {
     *                           return ResponseEntity.notFound().build();
     *                       }
     *                       return ResponseEntity.ok(specParams);
     *                   }
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(value = "gid", required = false)Long gid,
            @RequestParam(value = "cid", required = false)Long cid,
            @RequestParam(value = "generic", required = false)Boolean generic,
            @RequestParam(value = "searching", required = false)Boolean searching
    ) {
        List<SpecParam> params = this.specificationService.queryParams(gid, cid, generic, searching);
        if (CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

    /**
     * 新增规格模版分组
     * @param specGroup 规格分组 {"cid":商品分类ID,"name":"分组名称"}
     * @return 响应状态码
     */
    @PostMapping("/group")
    public ResponseEntity<Void> saveSpecGroup(@RequestBody SpecGroup specGroup) {
        this.specificationService.saveSpecGroup(specGroup);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 更新规格模板信息
     * @param specGroup 规格分组信息 {name: "分组名称", id: 分组id, cid: 商品分类id, params: 规格参数}
     * @return 响应状态码
     */
    @PutMapping("/group")
    public ResponseEntity<Void> updateSpecGroup(@RequestBody SpecGroup specGroup) {
        this.specificationService.updateSpecGroup(specGroup);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 根据规格组id删除该规格组信息
     * @param gid 规格组 id
     * @return 响应状态码
     */
    @DeleteMapping("/group/{gid}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("gid") Long gid) {
        specificationService.deleteSpecGroup(gid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 新增规格模板下的规格参数
     * @param specParam 规格参数
     * @return 响应状态码
     */
    @PostMapping("/param")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParam specParam) {
        specificationService.saveSpecParam(specParam);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 更新规格模版下的规格参数信息
     * @param specParam 规格参数
     * @return 响应状态码
     */
    @PutMapping("/param")
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParam specParam) {
        specificationService.updateSpecParam(specParam);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 通过paramId删除规格模板下某一参数
     * @param pid 规格参数 id
     * @return 响应状态码
     */
    @DeleteMapping("/param/{pid}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("pid") Long pid) {
        specificationService.deleteSpecParam(pid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
