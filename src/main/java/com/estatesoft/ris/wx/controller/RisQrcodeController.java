package com.estatesoft.ris.wx.controller;

import com.estatesoft.ris.wx.constant.RisErrorCode;
import com.estatesoft.ris.wx.service.redis.QrRedisService;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @GetMapping(value = "/redis")
    public HttpResult testredis(String deviceId, HttpServletResponse response) throws WxErrorException, IOException {
        qrRedisService.putDeviceIdTicketToCache(deviceId, UUID.randomUUID().toString());
        return success();
    }
}
