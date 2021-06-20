package pers.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.leyou.item.pojo.SpecGroup;
import pers.leyou.item.pojo.SpecParam;

import java.util.List;

/******************************************************************************
 * 商品规格信息服务类接口
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/16 16:23 (CST)
 ******************************************************************************/
@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 通过商品分类id查询规格组信息
     * @param cid 商品分类 id
     * @return 规格组信息
     */
    @GetMapping("groups/{cid}")
    public List<SpecGroup> queryGroupsByCid(@PathVariable("cid") Long cid);

    /**
     * 通过商品分类id查询规格组信息（包含规格组参数集合信息）
     * @param cid 商品分类 id
     * @return 规格组信息
     */
    @GetMapping("group/param/{cid}")
    public List<SpecGroup> queryGroupsWithParam(@PathVariable("cid") Long cid);

    /**
     * 根据条件，查询规格参数信息
     * @param gid 规格组id
     * @param cid 商品分类 id
     * @param generic 是否是sku通用属性
     * @param searching 是否用于搜索过滤
     * @return 规格参数信息
     */
    @GetMapping("params")
    public List<SpecParam> queryParams(
            @RequestParam(value = "gid", required = false)Long gid,
            @RequestParam(value = "cid", required = false)Long cid,
            @RequestParam(value = "generic", required = false)Boolean generic,
            @RequestParam(value = "searching", required = false)Boolean searching
    );

    /**
     * 新增规格模版分组
     * @param specGroup 规格分组 {"cid":商品分类ID,"name":"分组名称"}
     */
    @PostMapping("/group")
    public void saveSpecGroup(@RequestBody SpecGroup specGroup);

    /**
     * 更新规格模板信息
     * @param specGroup 规格分组信息 {name: "分组名称", id: 分组id, cid: 商品分类id, params: 规格参数}
     */
    @PutMapping("/group")
    public void updateSpecGroup(@RequestBody SpecGroup specGroup);

    /**
     * 根据规格组id删除该规格组信息
     * @param gid 规格组 id
     */
    @DeleteMapping("/group/{gid}")
    public void deleteSpecGroup(@PathVariable("gid") Long gid);

    /**
     * 新增规格模板下的规格参数
     * @param specParam 规格参数
     */
    @PostMapping("/param")
    public void saveSpecParam(@RequestBody SpecParam specParam);

    /**
     * 更新规格模版下的规格参数信息
     * @param specParam 规格参数
     */
    @PutMapping("/param")
    public void updateSpecParam(@RequestBody SpecParam specParam);

    /**
     * 通过paramId删除规格模板下某一参数
     * @param pid 规格参数 id
     */
    @DeleteMapping("/param/{pid}")
    public void deleteSpecParam(@PathVariable("pid") Long pid);
}
