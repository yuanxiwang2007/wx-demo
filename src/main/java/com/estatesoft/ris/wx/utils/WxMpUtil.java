package com.estatesoft.ris.wx.utils;

import org.springframework.util.Assert;

import java.util.Hashtable;
import java.util.Map;

public class WxMpUtil {

    /**
     * 缓存MultiWxMpService，方便下次使用。
     */
    private static Map<String, MultiWxMpService> multiWxMpServiceCache = new Hashtable<>();

    private WxMpUtil() {

    }

    /**
     * 根据ID获取对应的MultiWxMpService
     * @param appId 公众号ID
     * @return
     */
    public static synchronized MultiWxMpService get(String appId) {
        Assert.hasText(appId, "企业ID不能为空");
        MultiWxMpService wxMpService = multiWxMpServiceCache.get(appId);
        if (wxMpService == null) {
            wxMpService = new MultiWxMpService(appId);
            multiWxMpServiceCache.put(appId, wxMpService);
        }
        return wxMpService;
    }


}
