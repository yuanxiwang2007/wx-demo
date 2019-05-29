package com.estatesoft.ris.wx.controller;

import com.alibaba.fastjson.JSON;

import com.estatesoft.ris.wx.service.redis.QrRedisService;
import com.estatesoft.ris.wx.utils.MD5Util;
import com.estatesoft.ris.wx.utils.SHA1Util;
import com.rms.common.controller.BaseController;
import com.rms.common.result.HttpResult;
import com.rms.common.session.UserSession;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 认证控制器
 * Created by wuke on 2017/6/21.
 */
@RestController
@RequestMapping("/wechat/auth")
public class RisWxAuthController extends BaseController {

    @Value("${project.path}")
    private String projectPath;

    @Value("${ris.wechat.mp.appId}")
    private String wxAppID;

    // @Value("${sso.path.login}")
    private String SSO_PATH_LOGIN;

    @Resource(name = "risWxMpService")
    private WxMpService risWxMpService;

//    @Autowired
//    private UserSessionService userSessionService;

    @Autowired
    private QrRedisService qrRedisService;


    /**
     * 认证并转发到url
     *
     * @param scope       0静默 1-授权
     * @param redirectUrl 前段要求跳转地址
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/access", method = RequestMethod.GET)
    public HttpResult access(int scope, String redirectUrl, String deviceId, String ticket, HttpServletResponse response, HttpSession httpSession) throws Exception {
//        if(checkTicketIfExpire(deviceId, ticket)){
//            return error("二维码已过期");
//        }

        logger.info("access授权请求:scope:{},redirectUrl:{}", scope, redirectUrl);
        String snsapi = "snsapi_base";
        if (scope == 1) {
            snsapi = "snsapi_userinfo";
        }
        String wxAuthUrl = risWxMpService.oauth2buildAuthorizationUrl(
                projectPath + "/wechat/auth/accessHandler",
                snsapi, "");
        logger.info("access拼装微信访问地址:,wxAuthUrl:{}", wxAuthUrl);
        httpSession.setAttribute("redirectUrl", redirectUrl);
        response.sendRedirect(wxAuthUrl);
        return success();
    }

    /**
     * @param code
     * @param state 页面参数,即重定向页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/accessHandler", method = {RequestMethod.GET, RequestMethod.POST})
    public HttpResult accessHandler(String code, String state, HttpServletResponse response, HttpSession httpSession) throws Exception {
        logger.info("微信认证回调:state=" + state);
        // 请求微信token
        WxMpOAuth2AccessToken wxToken = risWxMpService.oauth2getAccessToken(code);
        String openId = wxToken.getOpenId();

        // 获取用户信息
        WxMpUser wxMpUser = risWxMpService.getUserService().userInfo(openId);

        //绑定session
//        UserSession user = new UserSession();
//        user.setOpenID(openId);
//        user.setLoginType(WechartConstants.LOGIN_TYPE_DOCTOR);
//        user.se(wxMpUser);
        // 放入session


        //转发页面
        state = (String) httpSession.getAttribute("redirectUrl");
        if (state.indexOf("?") != -1) {
            logger.info("微信认证成功,跳转地址:" + state + "&openId=" + openId);
            response.sendRedirect(state + "&openId=" + openId);
        } else {
            logger.info("微信认证成功,跳转地址:" + state + "?openId=" + openId);
            response.sendRedirect(state + "?openId=" + openId);
        }
        return success();
    }

    /**
     * 签名接口
     *
     * @return
     */
    @RequestMapping(value = "/sign-data", method = RequestMethod.GET)
    public HttpResult configData(String url, String app) throws Exception {
        logger.info("configData签名逻辑返回:url:{},app:{}", url, app);
        SortedMap<String, String> map = new TreeMap<>();
        map.put("jsapi_ticket", risWxMpService.getJsapiTicket());
        map.put("noncestr", getNonceStr());
        map.put("timestamp", getTimeStamp());
        map.put("url", url);
        String signature = SHA1Util.createSHA1Sign(map);
        map.put("signature", signature);
        map.put("appId", wxAppID);
        logger.info(JSON.toJSONString("签名结果返回" + map));
        return success(map);
    }

    /**
     * 微信公众号开发的随机字串
     */
    private String getNonceStr() throws Exception {
        Random random = new Random();
        return MD5Util.Md5Base64(String.valueOf(random.nextInt(10000)));
    }

    /**
     * 微信公众号开发的获取时间戳
     */
    private String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }



   /* public void loginSSO(WxMpUser wxMpUser){
        Map<String, String> params = new HashMap<>();
        params.put("authType", "3");// 认证类型，1:密码，2:短信，3:微信

        params.put("openId", wxMpUser.getOpenId());
        params.put("unionId", wxMpUser.getUnionId());
        params.put("extraAppId", wxAppID);
        params.put("extraNickName", wxMpUser.getNickname());
        params.put("avatar", wxMpUser.getHeadImgUrl());

        JSONObject jsonObject = HttpRequestUtils.httpPost(SSO_PATH_LOGIN, params);

        Object errCode = jsonObject.get("errcode");
        if (errCode != null && "0".equals(errCode.toString())){
            JSONObject jsonData = (JSONObject)jsonObject.get("data");

        }

    }*/
}
