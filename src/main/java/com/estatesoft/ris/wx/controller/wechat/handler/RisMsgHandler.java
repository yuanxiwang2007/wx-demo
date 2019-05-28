package com.estatesoft.ris.wx.controller.wechat.handler;

import com.estatesoft.ris.wx.controller.wechat.builder.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class RisMsgHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(WxConsts.XmlMsgType.EVENT)) {
            // 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            String content = wxMessage.getContent();
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
                    && weixinService.getKefuService().kfOnlineList()
                    .getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                        .fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
            }

            if (StringUtils.isNotBlank(content) && (content.equals("步") || content.equals("礼物") || content.equals("禮物"))) {

                String retContent = "礼物限量前100名，根据先来先送原则，没有收到的宝宝们也不要灰心，我们会不定期送出礼物";
                return new TextBuilder().build(retContent, wxMessage, weixinService);
            }
            if (StringUtils.isNotBlank(content) && (content.equals("文章") || content.equals("JAVA") || content.equals("java") || content.equals("mongodb"))) {
                return pushArticle(wxMessage);
            }


        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        // 组装回复消息

        String retContent = "您好，请问有什么可以帮助您的，小编同学正在快马加鞭的赶过来！有任何需求都可以后台留言哦！\r\n" +
                "1、健康咨询，请回复“企鹅健康咨询”\r\n" +
                "2、诊所信息化管理系统，请回复“企鹅医掌柜”";
        ///String content = wxMessage.getContent();

        return new TextBuilder().build(retContent, wxMessage, weixinService);

    }
    /**
     * 用户关注 推送欢迎文章列表
     * @param wxMessage
     * @return
     */
    public WxMpXmlOutMessage pushArticle(WxMpXmlMessage wxMessage){
        List<WxMpXmlOutNewsMessage.Item> items = new ArrayList<>();
        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        item.setTitle("MongoDB 高级查询-分组聚合的实践篇");
        item.setDescription("MongoDB中聚合(aggregate)主要用于统计数据(比如统计平均值（avg）,求和( avg）,求和(      avg）,求和(<math><semantics><mrow><mi>a</mi><mi>v</mi><mi>g</mi><mi mathvariant=\"normal\">）</mi><mo separator=\"true\">,</mo><mi mathvariant=\"normal\">求</mi><mi mathvariant=\"normal\">和</mi><mo>(</mo></mrow><annotation encoding=\"application/x-tex\">avg）,求和(</annotation></semantics></math>avg）,求和(sum) ,最大最小(min, min,      min,<math><semantics><mrow><mi>m</mi><mi>i</mi><mi>n</mi><mo separator=\"true\">,</mo></mrow><annotation encoding=\"application/x-tex\">min,</annotation></semantics></math>min,max)等分组函数)，并view计算后的统计结果。");
        item.setPicUrl("https://mmbiz.qpic.cn/mmbiz_jpg/gTMzyfAyziaLgAwFMaiaAyem40Qowg1Dmj5deaOMHDKRsGb9TGCLV4CvicmAomB6HTNQkufCqBFboA4Eq1dVvg4lw/0?wx_fmt=jpeg");
        item.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
        items.add(item);

        item = new WxMpXmlOutNewsMessage.Item();
        item.setTitle("Spring boot 中如何使用 MongoDB");
        item.setDescription("MongoDB中聚合(aggregate)主要用于统计数据(比如统计平均值（avg）,求和( avg）,求和(      avg）,求和(<math><semantics><mrow><mi>a</mi><mi>v</mi><mi>g</mi><mi mathvariant=\"normal\">）</mi><mo separator=\"true\">,</mo><mi mathvariant=\"normal\">求</mi><mi mathvariant=\"normal\">和</mi><mo>(</mo></mrow><annotation encoding=\"application/x-tex\">avg）,求和(</annotation></semantics></math>avg）,求和(sum) ,最大最小(min, min,      min,<math><semantics><mrow><mi>m</mi><mi>i</mi><mi>n</mi><mo separator=\"true\">,</mo></mrow><annotation encoding=\"application/x-tex\">min,</annotation></semantics></math>min,max)等分组函数)，并view计算后的统计结果。");
        item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
        item.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
        items.add(item);

        item = new WxMpXmlOutNewsMessage.Item();
        item.setTitle("Spring boot 中如何使用 MongoDB");
        item.setDescription("MongoDB中聚合(aggregate)主要用于统计数据(比如统计平均值（avg）,求和( avg）,求和(      avg）,求和(<math><semantics><mrow><mi>a</mi><mi>v</mi><mi>g</mi><mi mathvariant=\"normal\">）</mi><mo separator=\"true\">,</mo><mi mathvariant=\"normal\">求</mi><mi mathvariant=\"normal\">和</mi><mo>(</mo></mrow><annotation encoding=\"application/x-tex\">avg）,求和(</annotation></semantics></math>avg）,求和(sum) ,最大最小(min, min,      min,<math><semantics><mrow><mi>m</mi><mi>i</mi><mi>n</mi><mo separator=\"true\">,</mo></mrow><annotation encoding=\"application/x-tex\">min,</annotation></semantics></math>min,max)等分组函数)，并view计算后的统计结果。");
        item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
        item.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
        items.add(item);

        item = new WxMpXmlOutNewsMessage.Item();
        item.setTitle("Spring boot 中如何使用 MongoDB");
        item.setDescription("MongoDB中聚合(aggregate)主要用于统计数据(比如统计平均值（avg）,求和( avg）,求和(      avg）,求和(<math><semantics><mrow><mi>a</mi><mi>v</mi><mi>g</mi><mi mathvariant=\"normal\">）</mi><mo separator=\"true\">,</mo><mi mathvariant=\"normal\">求</mi><mi mathvariant=\"normal\">和</mi><mo>(</mo></mrow><annotation encoding=\"application/x-tex\">avg）,求和(</annotation></semantics></math>avg）,求和(sum) ,最大最小(min, min,      min,<math><semantics><mrow><mi>m</mi><mi>i</mi><mi>n</mi><mo separator=\"true\">,</mo></mrow><annotation encoding=\"application/x-tex\">min,</annotation></semantics></math>min,max)等分组函数)，并view计算后的统计结果。");
        item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
        item.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
        items.add(item);

        WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS().articles(items).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
        return m;
    }
}
