package pers.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.leyou.common.pojo.PageResult;
import pers.leyou.item.mapper.BrandMapper;
import pers.leyou.item.pojo.Brand;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/******************************************************************************
 * 品牌管理实现类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/10/09 12:29 (CST)
 ******************************************************************************/
@Service
public class BrandService {

    /** 从 SpringCloud 容器中注入品牌管理实现类 */
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据查询条件，分页并排序查询品牌信息
     * @param key 搜索条件
     * @param page 页码
     * @param rows 每页最多显示的数据条数
     * @param sortBy 排序条件
     * @param desc 是否降序
     * @return 查询结果：品牌信息/notFound
     */
    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows,
                                              String sortBy, Boolean desc) {
        // 初始化 example 对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        // 根据 name 模糊查询，或者根据首字母查询
        if (StringUtils.isNotBlank(key)) {
            // 首先判断搜索条件是否为空
            // 搜索条件不为空，定义根据 name 或者 letter 查询
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }

        // 添加分页
        PageHelper.startPage(page, rows);

        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }

        // 页面数据
        List<Brand> brands = this.brandMapper.selectByExample(example);
        // 包装城成 pageInfo
        PageInfo<Brand> brandPageInfo = new PageInfo<>(brands);
        // 包装成分页结果集返回
        return new PageResult<>(brandPageInfo.getTotal(), brandPageInfo.getList());
    }

    /**
     * 向数据库中添加品牌数据、中间表数据
     * 中间表存储的是商品分类 id和品牌 id
     * @param brand 品牌数据
     * @param cids 商品分类的 id 数组
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBrand(Brand brand, List<Long> cids) {
        // 先新增 brand
        this.brandMapper.insertSelective(brand);

        // 再新增中间表 tb_category_brand 数据
        cids.forEach(
                cid -> this.brandMapper.insertCategoryAndBrand(cid, brand.getId())
        );
    }

    /**
     * 删除品牌信息
     * @param bid 品牌 id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBrand(Long bid) {
        // 维护中间表
        brandMapper.deleteCategoryAndBrandByBid(bid);
        // 删除品牌
        brandMapper.deleteByPrimaryKey(bid);
    }

    /**
     * 根据商品分类id查询品牌信息集合
     * @param cid 商品分类 id
     * @return 品牌信息集
     */
    public List<Brand> queryBrandsByCid(Long cid) {
        return this.brandMapper.selectBrandByCid(cid);
    }

    /**
     * 根据品牌 id，查询商品品牌信息
     * @param id 品牌 id
     * @return null or 商品品牌信息
     */
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }
}
