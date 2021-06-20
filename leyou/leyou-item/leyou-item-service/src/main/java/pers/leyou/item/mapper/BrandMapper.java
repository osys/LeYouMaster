package pers.leyou.item.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pers.leyou.item.pojo.Brand;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/******************************************************************************
 * 品牌管理中数据的实体类接口
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/10/09 12:19 (CST)
 ******************************************************************************/
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 向 tb_category_brand 表中，插入数据
     * @param cid 商品分类 id
     * @param bid 品牌 id
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid}, #{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 通过品牌 id 删除中间表
     * @param bid 品牌 id
     */
    @Delete("DELETE FROM tb_category_brand WHERE brand_id = #{bid}")
    void deleteCategoryAndBrandByBid(@Param("bid") Long bid);

    /**
     * 根据商品分类id查询品牌信息集合
     * @param cid 商品分类 id
     * @return 品牌信息集
     */
    @Select(
            "SELECT b.* " +
            "from tb_brand b " +
            "INNER JOIN tb_category_brand cb " +
            "on b.id=cb.brand_id " +
            "where cb.category_id=#{cid}"
    )
    List<Brand> selectBrandByCid(Long cid);
}
