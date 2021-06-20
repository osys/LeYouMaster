package pers.leyou.search.reponsitory;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pers.leyou.search.pojo.Goods;

/******************************************************************************
 * 操作商品 Elasticsearch 资料库的接口
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/16 20:01 (CST)
 ******************************************************************************/
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
