package pers.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/******************************************************************************
 * 文件上传实现类(业务逻辑)
 * @author: YiHua Lee
 * @version: java version "1.8.0_201"
 * @application: IntelliJ IDEA
 * @createTime: 2020/10/18 14:03 (CST)
 ******************************************************************************/
@Service
public class UploadService {

    /** 初始化一个文件类型集合 */
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/gif");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    /** 注入 Fast 存储客户端对象 */
    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 上传图片
     * @param file 图片
     * @return url
     */
    public String uploadImage(MultipartFile file) {
        // 获取原文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件类型
        String contentType = file.getContentType();
        // 校验文件类型
        if (!CONTENT_TYPES.contains(contentType)) {
            LOGGER.info("文件类型不合法：{}", originalFilename);
            return null;
        }
        try {
            // 加载二进制文件
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            // 校验文件内容
            if (bufferedImage == null) {
                // 判断文件内容是否为空
                LOGGER.info("文件内容不合法：{}", originalFilename);
                return null;
            }
            // 保存到本地服务器
            // file.transferTo(new File("/Users/liyihua/Downloads/LeYouMaster/source/image/" + originalFilename));
            // 获取文件名后缀
            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            // 将文件上传到服务器，并获取存储文件路径
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            // 返回 url 进行回显
            // return "http://image.leyou.com/" + originalFilename;
            return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内部错误：{}", originalFilename);
            e.printStackTrace();
        }
        return null;
    }

}
