package pers.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/******************************************************************************
 * 文件上传启动类
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/10/17 22:55 (CST)
 ******************************************************************************/
@SpringBootApplication
@EnableDiscoveryClient
public class LeYouUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouUploadApplication.class, args);
    }
}
