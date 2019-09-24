package com.estatesoft.ris.wx.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.doctorwork.framework.exception.BusinessException;
import com.estatesoft.ris.wx.utils.excel.ExcelModel;
import com.estatesoft.ris.wx.utils.excel.ExcelUtil;
import com.rms.common.controller.BaseController;
import com.rms.common.result.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author : hechao
 * @date 2019/8/15
 */
@RestController
@RequestMapping("/template")
public class TemplateController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TemplateController.class);


    @GetMapping("/download")
    public void download(HttpServletRequest request,
                         HttpServletResponse response) throws BusinessException {
        ServletOutputStream out;
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("bin");
        String fileName = "学生批量导入模板.xlsx";

        String userAgent = request.getHeader("User-Agent");
        try {
            String path = ResourceUtils.getURL("classpath:").getPath() + "template/" + fileName;
            if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Chrome") || userAgent.contains("Edge")
                    || userAgent.contains("Safari")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String((fileName).getBytes("UTF-8"), "ISO-8859-1");
            }

            path = URLDecoder.decode(path, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            InputStream inputStream = this.getClass().getResourceAsStream("/template/幼儿批量导入模板.xlsx");
            out = response.getOutputStream();
            int b = 0;
            byte[] buffer = new byte[1024];
            while ((b = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, b);
            }

            inputStream.close();
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下载模板文件发生异常,异常原因为: {}", e.getMessage());
            throw new BusinessException(11060, "文件异常");
        }


    }

    @PostMapping("/upload")
    public HttpResult upload(@RequestParam("gradeId") String gradeId, @RequestParam("classId") String classId, @RequestParam("file") MultipartFile multipartFile) throws BusinessException {

        try {
            List<ExcelModel> list = ExcelUtil.readExcelWithModel(multipartFile.getInputStream(), ExcelTypeEnum.XLSX, ExcelModel.class);
            return success(list);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("excel导入异常, 原因为: {}", e.getMessage());
        }
        return success();
    }

}
