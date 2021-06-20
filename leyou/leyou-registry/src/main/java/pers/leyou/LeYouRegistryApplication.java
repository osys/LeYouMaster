package pers.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/******************************************************************************
 * 注册中心启动类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/09/23 22:18 (CST)
 ******************************************************************************/
@SpringBootApplication
@EnableEurekaServer
public class LeYouRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeYouRegistryApplication.class, args);
    }
}
