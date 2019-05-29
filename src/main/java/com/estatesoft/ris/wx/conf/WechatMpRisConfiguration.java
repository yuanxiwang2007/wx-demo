package com.estatesoft.ris.wx.conf;

import com.estatesoft.ris.wx.controller.wechat.handler.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(WxMpService.class)
@EnableConfigurationProperties(WechatMpRisProperties.class)
public class WechatMpRisConfiguration {
    @Autowired
    private WechatMpRisProperties properties;

    @Autowired
    private RisSubscribeHandler risSubscribeHandler;

    @Autowired
    private RisUnsubscribeHandler risUnsubscribeHandler;

    @Autowired
    private RisMsgHandler risMsgHandler;

    @Autowired
    private RisScanHandler risScanHandler;

    @Autowired
    private RisNullHandler risNullHandler;

    @Autowired
    private RisMenuHandler risMenuHandler;

    @Autowired
    private RisKfSessionHandler risKfSessionHandler;

    @Autowired
    private RisLogHandler risLogHandler;

    @Autowired
    private RisStoreCheckNotifyHandler risStoreCheckNotifyHandler;

    @Autowired
    private RisLocationHandler risLocationHandler;


    @Bean("risConfigStorage")
    public WxMpConfigStorage risConfigStorage() {
        System.out.println("初始化 ris configStorage");
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(this.properties.getAppId());
        configStorage.setSecret(this.properties.getSecret());
        configStorage.setToken(this.properties.getToken());
        configStorage.setAesKey(this.properties.getAesKey());
        return configStorage;
    }

    //
    @Bean("risWxMpService")
    public WxMpService risWxMpService(@Qualifier("risConfigStorage") WxMpConfigStorage configStorage) {
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.okhttp.WxMpServiceImpl();
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.jodd.WxMpServiceImpl();
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.apache.WxMpServiceImpl();
        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
        wxMpService.initHttp();
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
            wxMpService.post(url, "");
        } catch (WxErrorException e) {
            System.out.println("初始化 doctor尝试发模板");
        }
        return wxMpService;
    }

    //
    @Bean("risRouter")
    public WxMpMessageRouter risRouter(@Qualifier("risWxMpService") WxMpService doctorWxMpService) {
        System.out.println("初始化 ris router");
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(doctorWxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.risLogHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION)
                .handler(this.risKfSessionHandler).end();
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION)
                .handler(this.risKfSessionHandler)
                .end();
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION)
                .handler(this.risKfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.POI_CHECK_NOTIFY)
                .handler(this.risStoreCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.CLICK).handler(this.getRisMenuHandler()).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.VIEW).handler(this.risNullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE).handler(this.getRisSubscribeHandler())
                .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.UNSUBSCRIBE)
                .handler(this.getRisUnsubscribeHandler()).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.LOCATION).handler(this.risLocationHandler)
                .end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION)
                .handler(this.risLocationHandler).end();
//
        // 扫码事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SCAN).handler(this.getRisScanHandler()).end();
//
        // 默认
        newRouter.rule().async(false).handler(this.getRisMsgHandler()).end();

        return newRouter;
    }

    protected RisUnsubscribeHandler getRisUnsubscribeHandler() {
        return this.risUnsubscribeHandler;
    }

    protected RisSubscribeHandler getRisSubscribeHandler() {
        return this.risSubscribeHandler;
    }

    protected RisMsgHandler getRisMsgHandler() {
        return this.risMsgHandler;
    }
    protected RisScanHandler getRisScanHandler() {
        return this.risScanHandler;
    }
    protected RisMenuHandler getRisMenuHandler() {
        return this.risMenuHandler;
    }


}
