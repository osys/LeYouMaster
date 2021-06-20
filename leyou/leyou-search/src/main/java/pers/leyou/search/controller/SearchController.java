package pers.leyou.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.leyou.common.pojo.PageResult;
import pers.leyou.search.pojo.Goods;
import pers.leyou.search.pojo.SearchRequest;
import pers.leyou.search.service.SearchService;

/******************************************************************************
 * 搜索服务接口
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/17 16:25 (CST)
 ******************************************************************************/
@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 搜索商品
     * @param request 搜索请求
     * @return 搜索商品结果
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request) {
        PageResult<Goods> result = this.searchService.search(request);
        return (result == null)
                ? new ResponseEntity<>((HttpStatus.NOT_FOUND))
                : ResponseEntity.ok(result);
    }
}
