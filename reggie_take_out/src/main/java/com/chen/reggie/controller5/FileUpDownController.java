package com.chen.reggie.controller5;

import com.chen.reggie.commmon6.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 * @author chen
 * @create 2022/10/15 13:18
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class FileUpDownController {

    //将临时文件动态设置转存位置，在yml中配置reggie.path
    @Value("${reggie.path}")
    private String reggiePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，请求完成后会被删除，需要转存到指定位置
        log.info(file.toString());

        //原始文件名,substring:截取字符串 suffix:后缀：动态截取.jpg
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重新生成文件名再加上后缀suffix，防止文件名重复造成文件覆盖,randomUUID：随机全局唯一标识符
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(reggiePath);
        //判断当前目录是否存在，如果不存在需要创建
        if (!dir.exists()){
            dir.mkdirs();
        }


        try {
            file.transferTo(new File(reggiePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    //通过输出流向浏览器写回数据不需要返回值,通过HttpServletResponse获得输出流
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //通过输入流，读取文件内容 name：文件名
            FileInputStream fileInputStream = new FileInputStream(new File(reggiePath + name));

            //通过输出流，将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            //通过response设置响应文件的类型
            response.setContentType("image/jpeg");

            //将输入流读到的内容放到1024长度的byte[]数组里,循环读，length!=-1表示没读完
            int length = 0;
            byte[] bytes = new byte[1024];
          while ((length = fileInputStream.read(bytes)) != -1){
              //读后再用输出流向浏览器写从第一个0~length，并刷新
              outputStream.write(bytes,0,length);
              outputStream.flush();
          }
          //关闭资源
          outputStream.close();
          fileInputStream.close();





        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
