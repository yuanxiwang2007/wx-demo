package com.estatesoft.ris.wx.controller;

import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import com.alibaba.fastjson.JSONObject;
import com.estatesoft.ris.wx.constant.RisErrorCode;
import com.estatesoft.ris.wx.service.redis.QrRedisService;
import com.estatesoft.ris.wx.utils.HttpUtil;
import com.rms.common.controller.BaseController;
import com.rms.common.result.HttpResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.UUID;

/**
 * 认证控制器
 * Created by wuke on 2017/6/21.
 * <pre>
 * 医生端二维码相关操作接口
 * 文档地址：https://mp.weixin.qq.com/wiki?action=doc&id=mp1443433542&t=0.9274944716856435
 * </pre>
 */
@RestController
@RequestMapping("/wechat/ris/qr")
public class RisQrcodeController extends BaseController {

    @Resource(name = "risWxMpService")
    private WxMpService risWxMpService;

    @Autowired
    private QrRedisService qrRedisService;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private MagicMirrorDeviceConfigService magicMirrorDeviceConfigService;

    /**
     * 带场景值二维码
     *
     * @param deviceId
     * @param response
     * @return
     */
    @GetMapping(value = "/create")
    public HttpResult createQrCodeWithSceneId(String deviceId, HttpServletResponse response) throws WxErrorException, IOException {

        logger.info("魔镜请求生成二维码===>deviceId:{}", deviceId);
        Objects.requireNonNull(deviceId, "魔镜设备id为空!");
        String ticket = qrRedisService.getDeviceIdTicketFromCache(deviceId);
        String url;
        if (StringUtils.isEmpty(ticket)) {
            WxMpQrCodeTicket wxMpQrCodeTicket = risWxMpService.getQrcodeService().qrCodeCreateTmpTicket("deviceId:" + deviceId, 30 * 1000);
            ticket = wxMpQrCodeTicket.getTicket();
            qrRedisService.putDeviceIdTicketToCache(deviceId, ticket);
        }
        try {
            logger.info("新的请求二维码");
            url = risWxMpService.getQrcodeService().qrCodePictureUrl(URLEncoder.encode(ticket, "utf-8"));
        } catch (Exception e) {
            logger.error("请求二维码失败：deviceId={},Error={}", deviceId, e.getMessage());
            return error(RisErrorCode.CREATEQRCODE_ERROR);
        }
        logger.info("前设备id：{},{}", deviceId, url);
        return success(url);
    }

    /**
     * 带场景值二维码
     *
     * @param deviceId
     * @param response
     * @return
     */
    @GetMapping(value = "/v2/create")
    public HttpResult createQrCodeWithSceneIdV2(String deviceId, HttpServletResponse response) throws WxErrorException, IOException {

        logger.info("魔镜请求生成二维码===>deviceId:{}", deviceId);
        Objects.requireNonNull(deviceId, "魔镜设备id为空!");
        String access_token= getAccessToken();
        URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+access_token);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");// 提交模式
        // conn.setConnectTimeout(10000);//连接超时 单位毫秒
        // conn.setReadTimeout(2000);//读取超时 单位毫秒
        // 发送POST请求必须设置如下两行
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
        // 发送请求参数
        JSONObject paramJson = new JSONObject();
        paramJson.put("scene", "a1234567890");
        paramJson.put("page", "pages/index/index?deviceId="+deviceId+"&link="+deviceId);
        paramJson.put("width", 430);
        paramJson.put("auto_color", false);

        printWriter.write(paramJson.toString());
        // flush输出流的缓冲
        printWriter.flush();
        //开始获取数据
        BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int byteread;
        //ServletOutputStream outputStream = response.getOutputStream();
        while ((byteread = bis.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteread);
        }
        outputStream.close();
        bis.close();
        return success(url);
    }

    @GetMapping(value = "/redis")
    public HttpResult testredis(String deviceId, HttpServletResponse response) throws WxErrorException, IOException {
        qrRedisService.putDeviceIdTicketToCache(deviceId, UUID.randomUUID().toString());
        return success();
    }

    @GetMapping(value = "/indexTest")
    public HttpResult indexTest(String deviceId, HttpServletResponse response) throws Exception{
        WxMaCodeLineColor color = new WxMaCodeLineColor("2f", "29", "29");
        String linkToken ="34234";
        String scene = deviceId + "$" + linkToken + "$zh_CN";
        //scene = URLEncoder.encode(scene, "UTF-8");
        logger.debug(scene);
        //String page = "pages/mgStart/index?deviceId=" + deviceId + "&linkToken=" + linkToken + "&lang=zh_CN";
        String page = "pages/introduce/index";// "pages/mgStart/index";// URLEncoder.encode("pages/mgStart/index?deviceId=" + deviceId + "&linkToken=" + linkToken + "&lang=zh_CN", "UTF-8");
        File wxCode =new File("d:\\miniapp.jpg") ;//this.wxService.getQrcodeService().createWxCodeLimit(scene, page, 430, false, color);
        BufferedImage bufferedImage;// = transferImgForRoundImgage(wxCode);
        FileInputStream is = new FileInputStream(wxCode);
        byte[] buffer = new byte[1024];
        int byteread;
        ServletOutputStream outputStream = response.getOutputStream();
        BufferedImage in = ImageIO.read(wxCode);
        BufferedImage buffImg = new BufferedImage(430 + 10, 430 + 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = buffImg.createGraphics();
        // 设置背景颜色
        gs.setBackground(Color.WHITE);
        gs.clearRect(0, 0, 430 + 10, 430 + 10);



        BufferedImage newImage = new BufferedImage(
                in.getWidth()+10, in.getHeight()+10, BufferedImage.TYPE_INT_ARGB);
        //Graphics2D g = newImage.createGraphics();
        // g.drawImage(in, 5, 5, null);
        // g.dispose();
        //得到画笔对象
        Graphics g = buffImg.getGraphics();
        //将小图片根据传入坐标绘到大图片上。
        g.drawImage(in, 5, 5, null);

        g.dispose();
        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        //boolean flag = ImageIO.write(bufferedImage, "jpeg", out);
        //byte[] b = out.toByteArray();
        //outputStream.write(b);
        ImageIO.write(buffImg, "jpg", outputStream);
//        while ((byteread = is.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, byteread);
//        }
        outputStream.close();
        is.close();
        return success();
    }

    private  String getAccessToken(){
        String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + "wxa8fd8ce4c0466690" + "&secret=" + "3895b2a65fb96a25d730ca1e7a22af38";
        String result= HttpUtil.sendPost(url,"");
        JSONObject jsons = JSONObject.parseObject(result);
        String expires_in = jsons.getString("expires_in");
        //缓存
        if(Integer.parseInt(expires_in)==7200){
            //ok
            String access_token = jsons.getString("access_token");
            System.out.println("access_token:"+access_token);

            return access_token;
        }else{
            System.out.println("出错获取token失败！");
            return "";
        }
    }
}
