package pers.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/******************************************************************************
 * 网关启动类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/09/23 22:57 (CST)
 ******************************************************************************/
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class LeYouGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouGatewayApplication.class, args);
    }
}
