package pers.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/******************************************************************************
 * 商品详情页服务引导类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2021/03/31 16:08 (CST)
 ******************************************************************************/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LeYouGoodsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouGoodsWebApplication.class, args);
    }
}
