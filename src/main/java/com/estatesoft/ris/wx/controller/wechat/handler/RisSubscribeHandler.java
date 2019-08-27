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
import java.util.*;

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
    private List<String> sceneNames = Arrays.asList("qrscene_deviceId");

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

                String content = "欢迎关注JAVA学习平台";
//                if (LocalDate.now().isAfter(LocalDate.of(2019, 5, 12))) {
//                    content = languagePackageService.SimplifiedToTraditionalByThreadLang(LanguagePackageConstant.WECHAT_PUBLIC_SUBSCRIBE_REPLY.getCode());// stringBuilder.toString();
//                    //"近段时间，由于不能及时回复信息，如您有健康问题咨询，医疗咨询，可以直接拨打400-606-5999电话咨询，我们是7*24小时家庭医生值班制度。\r\n" +"或者请您添加企鹅医生助手的微信：QE-Eva\r\n"
//                } else {
//                    content = languagePackageService.SimplifiedToTraditionalByThreadLang(LanguagePackageConstant.WECHAT_PUBLIC_SUBSCRIBE_REPLY_MOTHERSDAY.getCode());
//                    content = String.format(content, StringUtils.isNotBlank(wxMessage.getEventKey()) ? keyValue[0] : "MD_DOCTORWORK");
//                }
               return pushWelcomeArticle(wxMessage);
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
     * 用户关注 推送欢迎文章列表
     * @param wxMessage
     * @return
     */
    public WxMpXmlOutMessage pushWelcomeArticle(WxMpXmlMessage wxMessage){
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setTitle("MongoDB 高级查询-分组聚合的实践篇");
        item.setDescription("MongoDB中聚合(aggregate)主要用于统计数据(比如统计平均值（avg）,求和( avg）,求和(      avg）,求和(<math><semantics><mrow><mi>a</mi><mi>v</mi><mi>g</mi><mi mathvariant=\"normal\">）</mi><mo separator=\"true\">,</mo><mi mathvariant=\"normal\">求</mi><mi mathvariant=\"normal\">和</mi><mo>(</mo></mrow><annotation encoding=\"application/x-tex\">avg）,求和(</annotation></semantics></math>avg）,求和(sum) ,最大最小(min, min,      min,<math><semantics><mrow><mi>m</mi><mi>i</mi><mi>n</mi><mo separator=\"true\">,</mo></mrow><annotation encoding=\"application/x-tex\">min,</annotation></semantics></math>min,max)等分组函数)，并view计算后的统计结果。");
        item.setPicUrl("https://mmbiz.qpic.cn/mmbiz_jpg/gTMzyfAyziaLgAwFMaiaAyem40Qowg1Dmj5deaOMHDKRsGb9TGCLV4CvicmAomB6HTNQkufCqBFboA4Eq1dVvg4lw/0?wx_fmt=jpeg");
        item.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
        items.add(item);

        item = new Item();
        item.setTitle("Spring boot 中如何使用 MongoDB");
        item.setDescription("MongoDB中聚合(aggregate)主要用于统计数据(比如统计平均值（avg）,求和( avg）,求和(      avg）,求和(<math><semantics><mrow><mi>a</mi><mi>v</mi><mi>g</mi><mi mathvariant=\"normal\">）</mi><mo separator=\"true\">,</mo><mi mathvariant=\"normal\">求</mi><mi mathvariant=\"normal\">和</mi><mo>(</mo></mrow><annotation encoding=\"application/x-tex\">avg）,求和(</annotation></semantics></math>avg）,求和(sum) ,最大最小(min, min,      min,<math><semantics><mrow><mi>m</mi><mi>i</mi><mi>n</mi><mo separator=\"true\">,</mo></mrow><annotation encoding=\"application/x-tex\">min,</annotation></semantics></math>min,max)等分组函数)，并view计算后的统计结果。");
        item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
        item.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
        items.add(item);

        item = new Item();
        item.setTitle("Spring boot 中如何使用 MongoDB");
        item.setDescription("MongoDB中聚合(aggregate)主要用于统计数据(比如统计平均值（avg）,求和( avg）,求和(      avg）,求和(<math><semantics><mrow><mi>a</mi><mi>v</mi><mi>g</mi><mi mathvariant=\"normal\">）</mi><mo separator=\"true\">,</mo><mi mathvariant=\"normal\">求</mi><mi mathvariant=\"normal\">和</mi><mo>(</mo></mrow><annotation encoding=\"application/x-tex\">avg）,求和(</annotation></semantics></math>avg）,求和(sum) ,最大最小(min, min,      min,<math><semantics><mrow><mi>m</mi><mi>i</mi><mi>n</mi><mo separator=\"true\">,</mo></mrow><annotation encoding=\"application/x-tex\">min,</annotation></semantics></math>min,max)等分组函数)，并view计算后的统计结果。");
        item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
        item.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
        items.add(item);

        item = new Item();
        item.setTitle("Spring boot 中如何使用 MongoDB");
        item.setDescription("MongoDB中聚合(aggregate)主要用于统计数据(比如统计平均值（avg）,求和( avg）,求和(      avg）,求和(<math><semantics><mrow><mi>a</mi><mi>v</mi><mi>g</mi><mi mathvariant=\"normal\">）</mi><mo separator=\"true\">,</mo><mi mathvariant=\"normal\">求</mi><mi mathvariant=\"normal\">和</mi><mo>(</mo></mrow><annotation encoding=\"application/x-tex\">avg）,求和(</annotation></semantics></math>avg）,求和(sum) ,最大最小(min, min,      min,<math><semantics><mrow><mi>m</mi><mi>i</mi><mi>n</mi><mo separator=\"true\">,</mo></mrow><annotation encoding=\"application/x-tex\">min,</annotation></semantics></math>min,max)等分组函数)，并view计算后的统计结果。");
        item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
        item.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
        items.add(item);

        WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS().articles(items).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
        return m;
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

//        Item item = new Item();
//        item.setDescription(article1.getDescription());
//        item.setPicUrl(article1.getPicUrl());
//        item.setTitle(article1.getTitle());
//        item.setUrl(article1.getUrl());


        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setTitle("照片墙");
        item.setDescription("阿狸照片墙");
        item.setPicUrl("http://changhaiwx.pagekite.me/photo-wall/a/iali11.jpg");
        item.setUrl("http://changhaiwx.pagekite.me/page/photowall");
        items.add(item);

        item = new Item();
        item.setTitle("哈哈");
        item.setDescription("一张照片");
        item.setPicUrl("http://changhaiwx.pagekite.me/images/me.jpg");
        item.setUrl("http://changhaiwx.pagekite.me/page/index");
        items.add(item);

        item = new Item();
        item.setTitle("小游戏2048");
        item.setDescription("小游戏2048");
        item.setPicUrl("http://changhaiwx.pagekite.me/images/2048.jpg");
        item.setUrl("http://changhaiwx.pagekite.me/page/game2048");
        items.add(item);

        item = new Item();
        item.setTitle("百度");
        item.setDescription("百度一下");
        item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
        item.setUrl("http://www.baidu.com");
        items.add(item);

        WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS().articles(items).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
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
