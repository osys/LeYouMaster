package pers.leyou.upload.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/******************************************************************************
 * 将 FDFS 配置引入项目
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/11/04 20:42 (CST)
 ******************************************************************************/
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING) // 解决jmx重复注册bean的问题
public class FastClientImporter {
    // 导入依赖组件
}
