package pers.leyou.item.mapper;

import org.apache.ibatis.annotations.Select;
import pers.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/******************************************************************************
 * 分类管理 mapper，简化开发
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/09/27 14:50 (CST)
 ******************************************************************************/
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category, Long> {

    /**
     * 根据品牌id查询商品分类
     * @param bid 品牌 id
     * @return 品牌分类数据 或 未找到
     */
    @Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> queryByBrandId(Long bid);

}
