package com.estatesoft.ris.wx.utils;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

public class MultiWxMpService extends WxMpServiceImpl {
    /**
     * 区分微信公众号多帐号的唯一ID，我这里用的企业ID
     */
    private String appId = null;
    private WxMpMessageRouter router = null;

    public MultiWxMpService(String appId) {
        this(appId, null);
    }

    public MultiWxMpService(String appId, WxMpMessageRouter router) {
        this.appId = appId;
        setWxMpConfigStorage();
        if (router == null) {
            setWxMpMessageRouterRule();
        } else {
            this.router = router;
        }
    }

    public String getAppId() {
        return appId;
    }

    public WxMpMessageRouter getMessageRouter() {
        return router;
    }

    private void setWxMpConfigStorage() {

        //TODO 根据companyId读取公众号参数配置 这里可以从配置文件、数据库、缓存任意一个源读取
        String appId = "";
        String secret = "";
        String token = "";
        String aesKey = "";

        // 设置公众号参数
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(appId); // 设置微信公众号的appid
        config.setSecret(secret); // 设置微信公众号的app corpSecret
        config.setToken(token); // 设置微信公众号的token
        config.setAesKey(aesKey); // 设置微信公众号的EncodingAESKey
        this.setWxMpConfigStorage(config);
    }

    private void setWxMpMessageRouterRule() {
        router = new WxMpMessageRouter(this);

        // 记录所有事件的日志
        //router.rule().handler(new LogHandler()).next();
    }
}
