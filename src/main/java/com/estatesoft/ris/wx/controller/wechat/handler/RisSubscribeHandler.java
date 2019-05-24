package com.estatesoft.ris.wx.controller.wechat.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.estatesoft.ris.wx.conf.WechatMpRisProperties;
import com.estatesoft.ris.wx.controller.wechat.builder.TextBuilder;
import com.estatesoft.ris.wx.utils.UuidUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.bean.WxCpXmlOutNewsMessage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class RisSubscribeHandler extends AbstractHandler {
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RisSubscribeHandler.class);


    @Autowired
    private WechatMpRisProperties properties;

    /**
     * 扫码进入场景名称
     */
    private List<String> sceneNames = Arrays.asList("qrscene_magicMirrorDeviceId");

    private final static String FRONDEND_URL = "/rapp/activity/magicmirror/start?deviceId=";

    @Value("${project.path}")
    private String projectPath;

    @Value("${project.h5}")
    private String projectH5;

    @Value("${access.pre.path}")
    private String accessPrePath;

//    @Autowired
//    private QrRedisService qrRedisService;
//
//    @Autowired
//    private RedissonClient redisson;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> map,
                                    WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) throws WxErrorException {
        this.logger.debug("普通用户用户 OPENID: " + wxMessage.getFromUser());
        //记录关注用户数据
        String day = DateFormatUtils.ISO_DATE_FORMAT.format(wxMessage.getCreateTime() * 1000);
        String sceneId = "";
        if (StringUtils.isNotBlank(wxMessage.getEventKey())) {
            sceneId = wxMessage.getEventKey().replace("qrscene_", "");
        }
//        if (StringUtils.isBlank(sceneId) || (StringUtils.isNotBlank(sceneId) && sceneId.startsWith("MD_")) ) {
//            redisson.getMap("MOTHERSDAY").addAndGet("SUBSCRIBE:"+ day + ":" + (StringUtils.isNotBlank(sceneId)?sceneId:"MD_DOCTORWORK"),1);
//        }
        try {
            // 获取微信用户基本信息
            //WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), null);

            //if (userWxInfo != null) {
            //  可以添加关注用户到本地

            String appId = properties.getAppId();
            String openId = wxMessage.getFromUser();
            Date gmtAction = new Date(wxMessage.getCreateTime() * 1000);
            this.logger.debug("关注用户参数 appId:={} openId={},gmtAction={}", appId, openId, gmtAction);
            //mpSubscribedUserApi.subscribe(appId, openId, gmtAction);

            //}
        } catch (Exception ex) {
            logger.error("保存微信关注用户失败!", ex);
        }
        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = handleSpecial(wxMessage, wxMpService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage, WxMpService wxMpService)
            throws Exception {
        logger.info("接受消息：" + JSON.toJSONString(wxMessage));
        if (wxMessage.getEventKey() != null) { //扫码进入
            String[] keyValue = wxMessage.getEventKey().split(":");
            LOGGER.info("扫描进入===> {}", JSON.toJSONString(keyValue));
            if (keyValue.length < 2) {
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("欢迎关注企鹅医生——专注为个人和家庭提供优质、安全的健康服务：\r\n\r\n");
//                stringBuilder.append("<a href=\"https://webprod.doctorwork.com/rapp/activity/health/member?sku=MP2018001&from=singlemessage&isappinstalled=0\">【家庭医生】</a>\r\n");
//                stringBuilder.append("家人健康的安心托付\r\n\r\n");
//                stringBuilder.append("【企鹅诊所】\r\n");
//                stringBuilder.append("健康检查+治疗+管理，一站式全流程\r\n\r\n");
//                stringBuilder.append("<a href=\"\" data-miniprogram-appid=\"wx5876f10f9a7e8b68\" data-miniprogram-path=\"pages/mainIndex/index\">【企鹅心选】</a>\r\n");
//                stringBuilder.append("饮好食/养天然/缓初老…\r\n");
//                stringBuilder.append("探寻健康好物及健康轻服务\r\n\r\n");
//                stringBuilder.append("<a href=\"https://webprod.doctorwork.com/rapp/activity/miniapp\">【健康套餐】</a>\r\n");
//                stringBuilder.append("居家自测/诊所服务，日常检验检测，预知健康风险\r\n\r\n");
//                stringBuilder.append("<a href=\"https://wx068301c1ace1af0e.h5.xiaoe-tech.com/\">【企鹅日课】</a>\r\n");
//                stringBuilder.append("生活与身体的难题，都需要潜心学习，一起来做生活学习者\r\n\r\n");
//                stringBuilder.append("—\r\n");
//                stringBuilder.append("预约香港诊所HVP9价疫苗，请添加 QE-Eva\r\n");
                String content = "欢迎关注JAVA学习平台";
//                if (LocalDate.now().isAfter(LocalDate.of(2019, 5, 12))) {
//                    content = languagePackageService.SimplifiedToTraditionalByThreadLang(LanguagePackageConstant.WECHAT_PUBLIC_SUBSCRIBE_REPLY.getCode());// stringBuilder.toString();
//                    //"近段时间，由于不能及时回复信息，如您有健康问题咨询，医疗咨询，可以直接拨打400-606-5999电话咨询，我们是7*24小时家庭医生值班制度。\r\n" +"或者请您添加企鹅医生助手的微信：QE-Eva\r\n"
//                } else {
//                    content = languagePackageService.SimplifiedToTraditionalByThreadLang(LanguagePackageConstant.WECHAT_PUBLIC_SUBSCRIBE_REPLY_MOTHERSDAY.getCode());
//                    content = String.format(content, StringUtils.isNotBlank(wxMessage.getEventKey()) ? keyValue[0] : "MD_DOCTORWORK");
//                }
               return putNews(wxMessage);
               // return new TextBuilder().build(content, wxMessage, wxMpService);
                //sendWelcomeText(wxMessage, wxMpService, content);
                //return null;
            }
            String sceneActionName = keyValue[0];
            String deviceId = keyValue[1];

            LOGGER.info("sceneActionName1===> {}", sceneActionName);
            logger.info("sceneActionName2===> {}", sceneActionName);
            if (sceneNames.contains(sceneActionName)) {
                // 推送卡片
                //sendText(wxMessage, wxMpService, deviceId);
                pushWelcomeMsg(wxMessage, wxMpService, deviceId);
            }
        } else {


        }
        return null;
    }

    /**
     * 推送图片欢迎消息
     *
     * @param wxMessage
     * @param wxMpService
     * @param deviceId
     * @throws Exception
     */
    public void pushWelcomeMsg(WxMpXmlMessage wxMessage, WxMpService wxMpService, String deviceId) throws Exception {

        WxMpKefuMessage.WxArticle article1 = new WxMpKefuMessage.WxArticle();

        StringBuilder stringBuilder = new StringBuilder();
        String linkToken = UuidUtil.getUUID().toUpperCase();
        //qrRedisService.putDeviceIdLinkTokenToCache(deviceId, linkToken);

        String urlEncoder = URLEncoder.encode(projectH5.concat(FRONDEND_URL).concat(deviceId).concat("&linkToken=").concat(linkToken), "utf8");

        stringBuilder.append(accessPrePath)
                .append("&redirectUrl=")
                .append(urlEncoder);

        LOGGER.info("小卡片链接地址==>{}", stringBuilder.toString());

        article1.setUrl(stringBuilder.toString());
        article1.setPicUrl("https://avatar-qiniu.doctorwork.com/o_1cnqs3ktf1m0a1kkj1e9m1k1n1o19c.png");
        article1.setTitle("tITELL");
        article1.setDescription("描述信息");


//        WxMpKefuMessage message = WxMpKefuMessage.NEWS()
//                .toUser(wxMessage.getFromUser())
//                .addArticle(article1)
//                .build();

        // wxMpService.post(url, jsonStr);
        //wxMpService.getKefuService().sendKefuMessage(message);
    }

    public WxMpXmlOutMessage putNews(WxMpXmlMessage wxMessage) throws Exception {
        WxMpKefuMessage.WxArticle article1 = new WxMpKefuMessage.WxArticle();
        StringBuilder stringBuilder = new StringBuilder();
        String linkToken = UuidUtil.getUUID().toUpperCase();
        //qrRedisService.putDeviceIdLinkTokenToCache(deviceId, linkToken);

        String urlEncoder = URLEncoder.encode(projectH5.concat(FRONDEND_URL).concat("asdasdwewew").concat("&linkToken=").concat(linkToken), "utf8");

        stringBuilder.append(accessPrePath)
                .append("&redirectUrl=")
                .append(urlEncoder);

        LOGGER.info("小卡片链接地址==>{}", stringBuilder.toString());
        article1.setUrl(stringBuilder.toString());
        article1.setPicUrl("https://avatar-qiniu.doctorwork.com/o_1cnqs3ktf1m0a1kkj1e9m1k1n1o19c.png");
        article1.setTitle("点击体验健康微体测");
        article1.setDescription("请先确认个人信息");

        Item item = new Item();
        item.setDescription(article1.getDescription());
        item.setPicUrl(article1.getPicUrl());
        item.setTitle(article1.getTitle());
        item.setUrl(article1.getUrl());
        WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS().addArticle(item,item).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
        return m;
    }


//    private void sendTextOld(WxMpXmlMessage wxMessage, WxMpService wxMpService, String deviceId) throws WxErrorException, IOException {
//        StringBuilder stringBuilder = new StringBuilder();
//        String linkToken = UuidUtil.getUUID().toUpperCase();
//
//        stringBuilder.append(projectH5)
//                .append(FRONDEND_URL)
//                .append(deviceId)
//                .append("&openId=")
//                .append(wxMessage.getFromUser())
//                .append("&linkToken=")
//                .append(linkToken);
//
//        logger.info("小卡片链接地址==>{}", stringBuilder.toString());
//
//        JSONObject gjson = new JSONObject();
//        gjson.put("touser", wxMessage.getFromUser());
//        gjson.put("msgtype", "text");
//        gjson.put("access_token", wxMpService.getAccessToken());
//        JSONObject text = new JSONObject();
//        text.put("content", "<a href='" + stringBuilder.toString() + "'>您已成功关注企鹅医生,开启魔镜测试</a>");
//        gjson.put("text", text);
//
//        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
//        String responseContent = wxMpService.post(url, gjson.toJSONString(gjson));
//
//        qrRedisService.putDeviceIdLinkTokenToCache(deviceId, linkToken);
//    }

//    private void sendText(WxMpXmlMessage wxMessage, WxMpService wxMpService, String deviceId) throws WxErrorException, IOException {
//        StringBuilder stringBuilder = new StringBuilder();
//        String linkToken = UuidUtil.getUUID().toUpperCase();
//        qrRedisService.putDeviceIdLinkTokenToCache(deviceId, linkToken);
//
//        String urlEncoder = URLEncoder.encode(projectH5.concat(FRONDEND_URL).concat(deviceId).concat("&linkToken=").concat(linkToken), "utf8");
//
//        stringBuilder.append(projectPath)
//                .append("/wechat/auth/access?scope=1")
//                .append("&redirectUrl=")
//                .append(urlEncoder);
//
//        logger.info("小卡片链接地址==>{}", stringBuilder.toString());
//
//        JSONObject gjson = new JSONObject();
//        gjson.put("touser", wxMessage.getFromUser());
//        gjson.put("msgtype", "text");
//        gjson.put("access_token", wxMpService.getAccessToken());
//        JSONObject text = new JSONObject();
//        text.put("content", "<a href='" + stringBuilder.toString() + "'>您已成功关注企鹅医生,开启魔镜测试</a>");
//        gjson.put("text", text);
//
//        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
//
//        String responseContent = wxMpService.post(url, gjson.toJSONString(gjson));
//    }

    private void sendWelcomeText(WxMpXmlMessage wxMessage, WxMpService wxMpService, String msg) throws WxErrorException {


        JSONObject gjson = new JSONObject();
        gjson.put("touser", wxMessage.getFromUser());
        gjson.put("msgtype", "text");
        gjson.put("access_token", wxMpService.getAccessToken());
        JSONObject text = new JSONObject();
        text.put("content", msg);
        gjson.put("text", text);

        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";

        String responseContent = wxMpService.post(url, gjson.toJSONString(gjson));
    }

}
