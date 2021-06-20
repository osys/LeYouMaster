package pers.leyou.goods.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.leyou.goods.service.GoodsService;

import java.util.Map;

/******************************************************************************
 * 商品详情页面 controller
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/04/01 15:45 (CST)
 ******************************************************************************/
@Controller
@RequestMapping("item")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 跳转到商品详情页
     * @param model 数据模型
     * @param spuId 商品集 id
     * @return 跳转到商品详情页
     */
    @GetMapping("{spuId}.html")
    public String toItemPage(Model model, @PathVariable("spuId") Long spuId) {
        // 加载所需数据
        Map<String, Object> modelMap = this.goodsService.loadData(spuId);
        // 放入模型
        model.addAllAttributes(modelMap);
        return "item";
    }
}
