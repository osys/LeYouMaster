package pers.leyou.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/******************************************************************************
 * Cors 配置（解决跨域问题）
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/10/20 22:52 (CST)
 ******************************************************************************/
@Configuration
public class LeYouCorsConfiguration {

    /**
     *  将 Cors 过滤器对象注入到 SpringBoot 容器中
     * @return Cors 过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        // 初始化 cors 配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许跨域的域名，如果要携带 cookie 字段，那么该参数不能写通配符 "*"
        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
        // 允许携带 cookie
        corsConfiguration.setAllowCredentials(true);
        // 允许请求的方法，如：get、post、put、delete、patch、head、options ......
        corsConfiguration.addAllowedMethod("*");
        // 允许携带任何头信息
        corsConfiguration.addAllowedHeader("*");

        // 初始化 cors 配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        // 注册 cors 配置对象
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);

        // 返回 corsFilter 实例，参数：cors 配置源对象
        return new CorsFilter(configurationSource);
    }

}