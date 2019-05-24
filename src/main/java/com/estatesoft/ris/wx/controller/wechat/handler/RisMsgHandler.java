package com.estatesoft.ris.wx.controller.wechat.handler;

import com.estatesoft.ris.wx.controller.wechat.builder.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

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
            if (StringUtils.isNotBlank(content) && (content.equals("多点执业") || content.equals("执业") || content.equals("多點執業") || content.equals("執業"))) {

                String retContent = "多点的内容";
                return new TextBuilder().build(retContent, wxMessage, weixinService);
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

}
