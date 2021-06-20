package pers.leyou.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import pers.leyou.LeYouSearchApplication;
import pers.leyou.common.pojo.PageResult;
import pers.leyou.item.bo.SpuBo;
import pers.leyou.item.pojo.Spu;
import pers.leyou.search.client.GoodsClient;
import pers.leyou.search.pojo.Goods;
import pers.leyou.search.reponsitory.GoodsRepository;
import pers.leyou.search.service.SearchService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/******************************************************************************
 * description: TODO
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/16 20:07 (CST)
 ******************************************************************************/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeYouSearchApplication.class)
public class ElasticSearchTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SearchService searchService;
    @Test
    public void createIndex() {
        // 创建索引，以及映射
        this.template.createIndex(Goods.class);
        this.template.putMapping(Goods.class);
    }

    @Test
    public void putIndex() {
        Integer page = 1;
        Integer rows = 100;
        do {
            // 分批查询 spuBo
            PageResult<SpuBo> pageResult =
                    this.goodsClient.querySpuBoByPage(null, true, page, rows);
            // 遍历 SpuBo 集合，转化为 List<Goods>
            List<Goods> goodsList = pageResult.getItems().stream().map(
                    spuBo -> {
                        try {
                            return this.searchService.buildGoods((Spu) spuBo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
            ).collect(Collectors.toList());
            this.goodsRepository.saveAll(goodsList);
            // 获取当前页的数据条数，如果是最后一页，没有 100 条
            rows = pageResult.getItems().size();
            // 每次循环，页码 +1
            page ++;
        } while (rows == 100);
    }
}
