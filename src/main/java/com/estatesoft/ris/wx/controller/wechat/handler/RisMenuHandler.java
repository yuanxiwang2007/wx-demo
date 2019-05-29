package com.estatesoft.ris.wx.controller.wechat.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class RisMenuHandler extends AbstractHandler {
    private final String ONLINE_SERVICE = "onlineService";

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {
        logger.debug("用户点击菜单事件:wxMessage:{}", wxMessage);
        String msg = String.format("type:%s, event:%s, key:%s",
                wxMessage.getMsgType(), wxMessage.getEvent(),
                wxMessage.getEventKey());
        if (WxConsts.MenuButtonType.VIEW.equals(wxMessage.getEvent())) {
            return null;
        }
        if (WxConsts.EventType.CLICK.equals(wxMessage.getEvent())) {
            if (ONLINE_SERVICE.equals(wxMessage.getEventKey())) {
                try {
                    sendText(wxMessage, weixinService);
                } catch (WxErrorException e) {
                    this.logger.error(e.getMessage(), e);
                }
            }
        }
        return WxMpXmlOutMessage.TEXT().content(msg)
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();
    }

    private void sendText(WxMpXmlMessage wxMessage, WxMpService wxMpService) throws WxErrorException {
        String msg = "{\n" +
                "    \"access_token\": \"" + wxMpService.getAccessToken() + "\",\n" +
                "    \"touser\": \"" + wxMessage.getFromUser() + "\", \n" +
                "    \"msgtype\": \"text\", \n" +
                "    \"text\": {" +
                "    \"content\": " + "您好，请直接在公众号中描述您的问题，我们将尽快安排客服人员为您解惑。" +
                "       }" +
                "}";
        //"您好，请直接在公众号中描述您的问题，我们将尽快安排客服人员为您解惑。"
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
        String responseContent = wxMpService.post(url, msg);
    }

}
