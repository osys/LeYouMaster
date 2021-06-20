package pers.leyou.item.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/******************************************************************************
 * 品牌管理中数据的实体类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/10/09 12:10 (CST)
 ******************************************************************************/
@Table(name = "tb_brand")
public class Brand {

    /** 品牌 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 品牌名称 */
    private String name;

    /** 品牌图片地址 */
    private String image;

    /** 品牌的首字母 */
    private Character letter;

    public Brand() {
    }

    public Brand(Long id, String name, String image, Character letter) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.letter = letter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Character getLetter() {
        return letter;
    }

    public void setLetter(Character letter) {
        this.letter = letter;
    }

}
