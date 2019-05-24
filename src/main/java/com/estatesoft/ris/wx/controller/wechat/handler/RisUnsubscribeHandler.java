package com.estatesoft.ris.wx.controller.wechat.handler;

import com.estatesoft.ris.wx.conf.WechatMpRisProperties;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class RisUnsubscribeHandler extends AbstractHandler {


    @Autowired
    private WechatMpRisProperties properties;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        this.logger.info("取消关注用户 OPENID: " + openId);
        //  可以更新本地数据库为取消关注状态
        try {
            String appId = properties.getAppId();
            Date gmtAction = new Date(wxMessage.getCreateTime() * 1000);
            //mpSubscribedUserApi.unSubscribe(appId, openId, gmtAction);
        } catch (Exception ex) {
            logger.error("取消微信关注用户失败!", ex);
        }
        return null;
    }

}