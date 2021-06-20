package pers.leyou.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.leyou.item.mapper.SpecGroupMapper;
import pers.leyou.item.mapper.SpecParamMapper;
import pers.leyou.item.pojo.SpecGroup;
import pers.leyou.item.pojo.SpecParam;

import java.util.List;

/******************************************************************************
 * 商品规格信息管理业务逻辑
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/11/23 20:21 (CST)
 ******************************************************************************/
@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 通过商品分类id查询规格组信息
     * @param cid 商品分类 id
     * @return 规格组信息 or null
     */
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.specGroupMapper.select(specGroup);
    }

    /**
     * 通过商品分类id查询规格组信息（包含规格组参数集合信息）
     * @param cid 商品分类 id
     * @return 规格组信息 or null
     */
    public List<SpecGroup> queryGroupsWithParam(Long cid) {
        // 通过商品分类id查询规格组信息
        // 这里是不包含规格组参数集合信息的
        List<SpecGroup> groups = this.queryGroupsByCid(cid);
        groups.forEach(group -> {
            // 根据规格组 id 查询规格组参数信息
            List<SpecParam> params = this.queryParams(group.getId(), null, null, null);
            group.setParams(params);
        });
        return groups;
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
     *                  /**
     *                   * 根据规格组id查询规格参数信息
     *                   * @param gid 规格组 id
     *                   * @return 规格参数信息 or null
     *                   * /
     *                   public List<SpecParam> queryParamsByGid(Long gid) {
     *                       SpecParam specParam = new SpecParam();
     *                       specParam.setGroupId(gid);
     *                       return this.specParamMapper.select(specParam);
     *                   }
     */
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        return this.specParamMapper.select(specParam);
    }

    /**
     * 新增规格模版分组
     * @param specGroup 规格分组信息 {"cid":商品分类ID,"name":"分组名称"}
     */
    public void saveSpecGroup(SpecGroup specGroup) {
        // null属性会使用默认值保存
        this.specGroupMapper.insertSelective(specGroup);
    }

    /**
     * 更新规格组信息
     * @param specGroup 规格分组信息 {name: "分组名称", id: 分组id, cid: 商品分类id, params: 规格参数}
     */
    public void updateSpecGroup(SpecGroup specGroup) {
        this.specGroupMapper.updateByPrimaryKey(specGroup);
    }

    /**
     * 通过id删除规格组
     * @param gid 规格组 id
     */
    public void deleteSpecGroup(Long gid) {
        // 先删除此规格模板分组下面的规格参数集合
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        this.specParamMapper.delete(specParam);
        // 再删除此规格模板
        this.specGroupMapper.deleteByPrimaryKey(gid);
    }

    /**
     * 新增规格模板下的规格参数
     * @param specParam 规格参数
     */
    public void saveSpecParam(SpecParam specParam) {
        this.specParamMapper.insertSelective(specParam);
    }

    /**
     * 更新规格模版下的规格参数信息
     * @param specParam 规格参数
     */
    public void updateSpecParam(SpecParam specParam) {
        this.specParamMapper.updateByPrimaryKey(specParam);
    }

    /**
     * 通过paramId删除规格模板下某一参数
     * @param pid 规格参数 id
     */
    public void deleteSpecParam(Long pid) {
        this.specParamMapper.deleteByPrimaryKey(pid);
    }
}
