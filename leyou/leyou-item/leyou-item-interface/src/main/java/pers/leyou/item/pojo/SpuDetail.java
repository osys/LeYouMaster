package pers.leyou.item.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/******************************************************************************
 * 商品集详情实现类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/11/26 19:54 (CST)
 ******************************************************************************/
@Table(name="tb_spu_detail")
public class SpuDetail {

    /** 商品集 id */
    @Id
    private Long spuId;

    /** 商品描述 */
    private String description;

    /** 商品特殊规格的名称及可选值模板 */
    private String specialSpec;

    /** 商品的全局规格属性 */
    private String genericSpec;

    /** 包装清单 */
    private String packingList;

    /** 售后服务 */
    private String afterService;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialSpec() {
        return specialSpec;
    }

    public void setSpecialSpec(String specialSpec) {
        this.specialSpec = specialSpec;
    }

    public String getGenericSpec() {
        return genericSpec;
    }

    public void setGenericSpec(String genericSpec) {
        this.genericSpec = genericSpec;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {
        this.afterService = afterService;
    }
}
