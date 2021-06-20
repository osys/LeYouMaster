package pers.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/******************************************************************************
 * 商品服务启动类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/09/24 14:30 (CST)
 ******************************************************************************/
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("pers.leyou.item.mapper")
public class LeYouItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouItemApplication.class, args);
    }
}
