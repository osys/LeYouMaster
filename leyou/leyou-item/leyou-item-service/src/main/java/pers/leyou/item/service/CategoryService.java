package pers.leyou.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.leyou.item.mapper.CategoryMapper;
import pers.leyou.item.pojo.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/******************************************************************************
 * 分类管理实现类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/09/27 15:05 (CST)
 ******************************************************************************/
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父节点，查询子节点
     * @param pid 父节点
     * @return 子节点
     */
    public List<Category> queryCategoriesByPid(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        return this.categoryMapper.select(record);
    }

    /**
     * 通过品牌id查询商品分类
     * @param bid 品牌id
     * @return 商品分类数据 或 null
     */
    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    /**
     * 根据商品分类id查询分类名称
     * @param ids 商品分类id 集合
     * @return 商品分类名称集合
     */
    public List<String> queryNamesByIds(List<Long> ids) {
        // 根据给定的商品分类id集合，查询商品分类名称等信息
        List<Category> list = this.categoryMapper.selectByIdList(ids);
        // 获取商品分类名称集合
        List<String> names = new ArrayList<>();
        for (Category category : list) {
            names.add(category.getName());
        }
        return names;
    }

    /**
     * 根据 3 级分类 id，查询 1 ~ 3 级的分类
     * @param id 3 级分类 id
     * @return 1 ~ 3 级分类 id
     */
    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.categoryMapper.selectByPrimaryKey(id);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }
}
