package com.estatesoft.ris.wx.controller;

import com.estatesoft.ris.wx.service.HouseService;
import com.rms.common.controller.BaseController;
import com.rms.common.result.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseController extends BaseController {

    @Autowired
    private HouseService houseService;

    @GetMapping("/bantchUpdate")
    public String bantchUpdate() {

        List<String> list = new ArrayList<>();
        houseService.bantchUpdate();
        return "hello" + new Date().getTime();
    }

    @GetMapping("/hello")
    public String hello() {

        List<String> list = new ArrayList<>();

        return "hello" + new Date().getTime();
    }

    /**
     * 上传附件
     */
    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public HttpResult uploadFile(HttpServletRequest request) {
        //判断是否为Multipart实例
        if (!(request instanceof MultipartHttpServletRequest)) {
            logger.warn("upload-dct-avatar fail: contentType={}", request.getContentType());
            return error("upload file fail");
        }
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "temp";
        //this.getClass().getClassLoader().get
        String fileName = this.getClass().getClassLoader().getResource("").getPath();//获取文件路径
        String fileUtl = this.getClass().getResource("").getFile();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String token = multipartRequest.getParameter("token");


        //String patientId = multipartRequest.getParameter("patientId");


        //判断是否有文件
        MultipartFile multipartFile = multipartRequest.getFile("uploadFile");
        if (multipartFile == null) {
            logger.warn("upload-dct-avatar fail: contentType={}", request.getContentType());
            return error("get file fail");
        }

        //设置储存的文件名
        fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename();
        String filePath = path + "/" + fileName;
        FileOutputStream fos = null;
        try {
            InputStream input = multipartFile.getInputStream();
            //File file=new File(filePath);
            fos = new FileOutputStream(filePath);
            byte[] b = new byte[1024];
            int byteRead;
            while ((byteRead = input.read(b)) != -1) {
                fos.write(b, 0, byteRead);
            }
            fos.close();
        } catch (Exception e) {
            //log.error("upload-file fail", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success();

    }

}
