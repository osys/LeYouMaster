package pers.leyou.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import pers.leyou.item.api.SpecificationApi;

/******************************************************************************
 * 商品 Feign 客户端，Feign 会通过动态代理，帮我们生成实现类。
 * 接口中的定义方法，完全采用SpringMVC的注解，Feign会根据注解帮我们生成URL，并访问获取结果
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/16 19:29 (CST)
 ******************************************************************************/
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
