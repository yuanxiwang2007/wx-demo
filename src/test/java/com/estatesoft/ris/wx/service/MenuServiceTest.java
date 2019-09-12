package com.estatesoft.ris.wx.service;

import com.estatesoft.ris.wx.Application;
import com.estatesoft.ris.wx.conf.WechatMpRisProperties;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wk on 2017/7/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class MenuServiceTest {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceTest.class);

    @Value("${project.h5}")
    private String h5page;
    @Value("${project.path}")
    private String backendUrl;

//    /**
//     * 医生端公众号service
//     */
//    //@Resource(name = "doctorWxMpService")
//    @Autowired
//    private WxMpService doctorService;
//
//
//    /**
//     * c端公众号service
//     */
//    @Resource(name = "patientWxMpService")
//    private WxMpService patientWxMpService;
//    /**
//     * 经纪人公众号
//     * service
//     */
//    @Resource(name = "agentWxMpService")
//    private WxMpService agentWxMpService;

    //    @Test
//    public void delete() throws WxErrorException {
//        doctorService.getMenuService().menuDelete();
//    }

    @Autowired
    private WechatMpRisProperties wechatMpRisProperties;

    @Test
    public void AddMenuTest() {

        //String backendUrl = "http://mojing.developer.doctorwork.com/qie-doctor";
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(wechatMpRisProperties.getAppId());
        configStorage.setSecret(wechatMpRisProperties.getSecret());
        configStorage.setToken(wechatMpRisProperties.getToken());
        configStorage.setAesKey(wechatMpRisProperties.getAesKey());

        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
        wxMpService.initHttp();
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
            wxMpService.post(url, "");
        } catch (WxErrorException e) {
            System.out.println("初始化 doctor尝试发模板");
        }

        WxMenu menu = new WxMenu();
        List<WxMenuButton> allButtons = new ArrayList<>();
        WxMenuButton jiaruqielianmengButton = new WxMenuButton();
        jiaruqielianmengButton.setName("宝贝的健康");
        jiaruqielianmengButton.setType("view");
        String myHealthUrl = null;
        try {
            myHealthUrl = URLEncoder.encode("http://ssr-qa.doctorwork.com/magicmirror/report?channel=5", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        jiaruqielianmengButton.setUrl("http://wechat-qa.developer.doctorwork.com/mg-mp-trade" + "/wechat/auth/access?scope=1&redirectUrl=" + myHealthUrl);


        allButtons.add(jiaruqielianmengButton);
//        jiaruqielianmengButton = new WxMenuButton();
//        jiaruqielianmengButton.setName("JAVA");
//        jiaruqielianmengButton.setType("view");
//        myHealthUrl = "http%3a%2f%2fweb-dev.doctorwork.com%2frapp%2factivity%2fmagicmirror%2farchive";//URLEncoder.encode(h5page + "/app/mpworker/alliance/guide", "UTF-8");
//        jiaruqielianmengButton.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
//
//        List<WxMenuButton> subMenu = new ArrayList<>();
//        WxMenuButton subone = new WxMenuButton();
//        subone.setName("MongoDB");
//        subone.setType("view");
//        subone.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
//        subMenu.add(subone);
//
//        subone = new WxMenuButton();
//        subone.setName("Redis");
//        subone.setType("view");
//        subone.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
//        subMenu.add(subone);
//
//        subone = new WxMenuButton();
//        subone.setName("ES");
//        subone.setType("view");
//        subone.setUrl("https://blog.csdn.net/yuanxiwang/article/details/89218476");
//        subMenu.add(subone);
//        jiaruqielianmengButton.setSubButtons(subMenu);
//
//        allButtons.add(jiaruqielianmengButton);
//        jiaruqielianmengButton = new WxMenuButton();
//        jiaruqielianmengButton.setName("云销客");
//        jiaruqielianmengButton.setType("click");
//        jiaruqielianmengButton.setKey("cloudsale");
//        myHealthUrl = "http%3a%2f%2fweb-dev.doctorwork.com%2frapp%2factivity%2fmagicmirror%2farchive";//URLEncoder.encode(h5page + "/app/mpworker/alliance/guide", "UTF-8");
//        //jiaruqielianmengButton.setUrl(backendUrl + "/wechat/auth/access?scope=1&redirectUrl=" + myHealthUrl);
//        allButtons.add(jiaruqielianmengButton);
        menu.setButtons(allButtons);
        try {
//            File file=new File("d:\\FkNs8qwAEXfd2HN4d5RNMk15NMoO.png");
//            WxMpMaterial material=new WxMpMaterial();
//            material.setFile(file);
//            material.setName("dddddd");
//            material.setVideoIntroduction("ccc");
//            material.setVideoTitle("fff");
//            WxMpMaterialUploadResult result= wxMpService.getMaterialService().materialFileUpload(WxConsts.XmlMsgType.IMAGE,material);
//


            wxMpService.getMenuService().menuCreate(menu);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void mainTest() {
        String backendUrl = "http://api.doctorwork.com/mg-mp-trade";
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId("wx2b724cc6e6610b47");
        configStorage.setSecret("f219847d8090f10109e92a99d3eb2a2d");
        configStorage.setToken("hongyuyey111222333");
        configStorage.setAesKey("NfbIZ9s7ZXCH7rIXUhOOGi0eUyoyLkQQv8Ytcbu6f98");

        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
        wxMpService.initHttp();
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
            wxMpService.post(url, "");
        } catch (WxErrorException e) {
            System.out.println("初始化 doctor尝试发模板");
        }

        WxMenu menu = new WxMenu();
        List<WxMenuButton> allButtons = new ArrayList<>();
        WxMenuButton jiaruqielianmengButton = new WxMenuButton();
        jiaruqielianmengButton.setName("宝贝的健康");
        jiaruqielianmengButton.setType("view");
        String myHealthUrl = "http%3A%2F%2Fssr.doctorwork.com%2Fmagicmirror%2Freport%3Fchannel%3D5";//URLEncoder.encode(h5page + "/app/mpworker/alliance/guide", "UTF-8");
        jiaruqielianmengButton.setUrl(backendUrl + "/wechat/auth/access?scope=1&redirectUrl=" + myHealthUrl);


        allButtons.add(jiaruqielianmengButton);
        menu.setButtons(allButtons);

        try {
            wxMpService.getMenuService().menuCreate(menu);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }


    }

//    public static void main(String[] args) throws WxErrorException {
//        String backendUrl="http://mojing.developer.doctorwork.com/qie-doctor";
//        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
//        configStorage.setAppId("wx2d352d72afc10db6");
//        configStorage.setSecret("bb57e8b01f4591dcee3fc2493e8adf9f");
//        configStorage.setToken("111222333");
//        configStorage.setAesKey("VVKUIHwdwdm0YiDQfFk1vOvfTfCq0atOtZX2L5w3ma5");
//
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
//        wxMpService.setWxMpConfigStorage(configStorage);
//        wxMpService.initHttp();
//        try {
//            String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
//            wxMpService.post(url, "");
//        } catch (WxErrorException e) {
//            System.out.println("初始化 doctor尝试发模板");
//        }
//
//        WxMenu menu = new WxMenu();
//        List<WxMenuButton> allButtons = new ArrayList<>();
//        WxMenuButton jiaruqielianmengButton = new WxMenuButton();
//        jiaruqielianmengButton.setName("魔镜档案");
//        jiaruqielianmengButton.setType("view");
//        String myHealthUrl = "http%3a%2f%2fweb-dev.doctorwork.com%2frapp%2factivity%2fmagicmirror%2farchive";//URLEncoder.encode(h5page + "/app/mpworker/alliance/guide", "UTF-8");
//        jiaruqielianmengButton.setUrl(backendUrl + "/wechat/auth/access?scope=1&redirectUrl=" + myHealthUrl);
//
//
//
//        allButtons.add(jiaruqielianmengButton);
//        menu.setButtons(allButtons);
//        wxMpService.getMenuService().menuCreate(menu);
//
//    }


//    /**
//     * 医生端菜单配置
//     *
//     * @throws Exception
//     */
//    @Test
//    public void addDoctorMenu() throws Exception {
//        WxMenu menu = new WxMenu();
//        List<WxMenuButton> allButtons = new ArrayList<>();
//        WxMenuButton jiaruqielianmengButton = new WxMenuButton();
//        jiaruqielianmengButton.setName("魔镜档案");
//        jiaruqielianmengButton.setType("view");
//        String myHealthUrl = "http%3A%2F%2F%2Fweb-dev.doctorwork.com%2Fr%2Frapp%2Factivity%2Fmagicmirror%2Farchive";//URLEncoder.encode(h5page + "/app/mpworker/alliance/guide", "UTF-8");
//        jiaruqielianmengButton.setUrl(backendUrl + "/wechat/doctor/access?scope=1&redirectUrl=" + myHealthUrl);
//
////        WxMenuButton qieyishenglianmengButton = new WxMenuButton();
////        qieyishenglianmengButton.setName("企鹅转诊");
////        qieyishenglianmengButton.setType("click");
////        qieyishenglianmengButton.setKey("HEALTHCHECK");
////
////        List<WxMenuButton> qieyishenglianmengButtonSubButtons = new ArrayList<>();
////        qieyishenglianmengButton.setSubButtons(qieyishenglianmengButtonSubButtons);
////        WxMenuButton jisuzhuanzhenButton = new WxMenuButton();
////        jisuzhuanzhenButton.setName("极速转诊");
////        jisuzhuanzhenButton.setType("view");
////        String recordUrl = URLEncoder.encode(h5page + "/app/mpworker/referral/appoint", "UTF-8");
////        jisuzhuanzhenButton.setUrl(backendUrl + "/wechat/doctor/access?scope=1&redirectUrl=" + recordUrl);
////
////        WxMenuButton patientScanButton = new WxMenuButton();
////        patientScanButton.setName("患者扫码");
////        patientScanButton.setType("view");
////        String scanUrl = URLEncoder.encode(h5page + "/app/mpworker/referral/qrcode", "UTF-8");
////        patientScanButton.setUrl(backendUrl + "/wechat/doctor/access?scope=1&redirectUrl=" + scanUrl);
////        qieyishenglianmengButtonSubButtons.add(patientScanButton);
////        qieyishenglianmengButtonSubButtons.add(jisuzhuanzhenButton);
////
////        // 菜单按钮 -企鹅中心
////        WxMenuButton lianmengzhongxinButton = new WxMenuButton();
////        lianmengzhongxinButton.setName("企鹅中心");
////        lianmengzhongxinButton.setType("click");
////        lianmengzhongxinButton.setKey("QIECLINIC");
////        List<WxMenuButton> lianmengzhongxinButtonButtons = new ArrayList<>();
////        lianmengzhongxinButton.setSubButtons(lianmengzhongxinButtonButtons);
////
////        //个人中心
////        WxMenuButton gerenzhongxinButton = new WxMenuButton();
////        gerenzhongxinButton.setName("个人中心");
////        gerenzhongxinButton.setType("view");
////        String gerenzhongxinUrl = URLEncoder.encode(h5page + "/app/mpworker/mine", "UTF-8");
////        gerenzhongxinButton.setUrl(backendUrl + "/wechat/doctor/access?scope=1&redirectUrl=" + gerenzhongxinUrl);
////
////        // 我的订单
////        WxMenuButton myPatientButton = new WxMenuButton();
////        myPatientButton.setName("我的订单");// 我的订单
////        myPatientButton.setType("view");
////        String myPatientUrl = URLEncoder.encode(h5page + "/app/mpworker/patient", "UTF-8");
////        myPatientButton.setUrl(backendUrl + "/wechat/doctor/access?scope=1&redirectUrl=" + myPatientUrl);
////        // 我的名片
////        WxMenuButton myBusinessCardButton = new WxMenuButton();
////        myBusinessCardButton.setName("我的名片");
////        myBusinessCardButton.setType("view");
////        String myBusinessCardUrl = URLEncoder.encode(h5page + "/app/mpworker/mine/qrcode", "UTF-8");
////        myBusinessCardButton.setUrl(backendUrl + "/wechat/doctor/access?scope=1&redirectUrl=" + myBusinessCardUrl);
////
////        WxMenuButton duodiankongjianButton = new WxMenuButton();
////        duodiankongjianButton.setName("多点空间");
////        duodiankongjianButton.setType("view");
////        String duodiankongjianUrl = URLEncoder.encode(h5page + "/app/mpworker/mine/multispace", "UTF-8");
////        duodiankongjianButton.setUrl(backendUrl + "/wechat/doctor/access?scope=1&redirectUrl=" + duodiankongjianUrl);
//        // 联盟介绍
//        /*WxMenuButton lianmengjieshaoButton = new WxMenuButton();
//
//        lianmengjieshaoButton.setName("联盟介绍");
//        lianmengjieshaoButton.setType("view");
//        lianmengjieshaoButton.setUrl(h5page + "/app/mpworker/alliance/about");*/
//
//        //  lianmengzhongxinButtonButtons.add(gerenzhongxinButton); // 个人中心
//        // lianmengzhongxinButtonButtons.add(myPatientButton);// 我的患者
//        // lianmengzhongxinButtonButtons.add(myBusinessCardButton);// 我的名片
//        //lianmengzhongxinButtonButtons.add(lianmengjieshaoButton);// 联盟介绍
//        // lianmengzhongxinButtonButtons.add(duodiankongjianButton);// 多点空间
//        //备份原来的咨询医生
////        WxMenuButton zixunyishengButton=new WxMenuButton();
////        zixunyishengButton.setName("咨询医生");
////        zixunyishengButton.setType("view");
////        zixunyishengButton.setUrl("http://web.medlinker.com/h5/interlocution-pay/index.html?med_channel=qieyishengfuwuhao");
////        qiezhensuoButtons.add(zixunyishengButton);
//
//        allButtons.add(jiaruqielianmengButton);
////        allButtons.add(qieyishenglianmengButton);
////        allButtons.add(lianmengzhongxinButton);
//        menu.setButtons(allButtons);
//        //System.out.println(doctorService.getWxMpConfigStorage().getAppId());
//        //System.out.println(doctorService.getWxMpConfigStorage().getToken());
//        doctorService.getMenuService().menuCreate(menu);
//    }
//
//    /**
//     * 患者端菜单配置
//     *
//     * @throws Exception
//     */
//    @Test
//    public void addPatientMenu() throws Exception {
//        WxMenu menu = new WxMenu();
//        List<WxMenuButton> allButtons = new ArrayList<>();
//        WxMenuButton jiaruqielianmengButton = new WxMenuButton();
//        jiaruqielianmengButton.setName("预约医生");
//        jiaruqielianmengButton.setType("view");
//        String myHealthUrl = URLEncoder.encode(h5page + "/app/mppatient/appoint", "UTF-8");
//        jiaruqielianmengButton.setUrl(backendUrl + "/wechat/patient/access?scope=1&redirectUrl=" + myHealthUrl);
//
//        WxMenuButton yishengkongjianButton = new WxMenuButton();
//        yishengkongjianButton.setName("医生空间");
//        yishengkongjianButton.setType("view");
//        String yishengkongjianUrl = URLEncoder.encode(h5page + "/app/mppatient/doctor", "UTF-8");
//        yishengkongjianButton.setUrl(backendUrl + "/wechat/patient/access?scope=1&redirectUrl=" + yishengkongjianUrl);
//
//        WxMenuButton wodeyuyueButton = new WxMenuButton();
//        wodeyuyueButton.setName("我的预约");
//        wodeyuyueButton.setType("view");
//        String wodeyuyueUrl = URLEncoder.encode(h5page + "/app/mppatient/mine", "UTF-8");
//        wodeyuyueButton.setUrl(backendUrl + "/wechat/patient/access?scope=1&redirectUrl=" + wodeyuyueUrl);
//
//
//        allButtons.add(jiaruqielianmengButton);
//        allButtons.add(yishengkongjianButton);
//        allButtons.add(wodeyuyueButton);
//        menu.setButtons(allButtons);
//        System.out.println(patientWxMpService.getWxMpConfigStorage().getAppId());
//        System.out.println(patientWxMpService.getWxMpConfigStorage().getToken());
//        System.out.println("===============" + JSON.toJSONString(menu));
//        patientWxMpService.getMenuService().menuCreate(menu);
//    }
//
//
//    /**
//     * 经纪人-销售公众号的菜单配置
//     *
//     * @throws Exception
//     */
//    @Test
//    public void addAgentMenu() throws Exception {
//        WxMenu agentMenu = new WxMenu();
//
//        List<WxMenuButton> allButtons = Lists.newArrayList();
//
//        // 加入企鹅
//        WxMenuButton joinTencentButton = new WxMenuButton();
//        joinTencentButton.setName("加入企鹅");
//        joinTencentButton.setType("view");
//        String myHealthUrl = URLEncoder.encode(h5page + "/app/mpagent/marketing/center", "UTF-8");
//        joinTencentButton.setUrl(backendUrl + "/wechat/agent/access?scope=1&redirectUrl=" + myHealthUrl);
//
//        allButtons.add(joinTencentButton);
//
//        // 推广二维码
//
//        WxMenuButton qrcodeButton = new WxMenuButton();
//        qrcodeButton.setName("推广二维码");
//        qrcodeButton.setType("view");
//        String marketingQrcode = URLEncoder.encode(h5page + "/app/mpagent/marketing/qrcode", "UTF-8");
//        qrcodeButton.setUrl(backendUrl + "/wechat/agent/access?scope=1&redirectUrl=" + marketingQrcode);
//
//        allButtons.add(qrcodeButton);
//
//        // 菜单按钮 -分销中心
//        WxMenuButton fenxiaozhongxinButton = new WxMenuButton();
//        fenxiaozhongxinButton.setName("分销中心");
//        fenxiaozhongxinButton.setType("click");
//        fenxiaozhongxinButton.setKey("QIECLINIC");
//
//        List<WxMenuButton> fenxiaozhongxinButtonButtons = new ArrayList<>();
//        fenxiaozhongxinButton.setSubButtons(fenxiaozhongxinButtonButtons);
//
//        // 分销中心下的二级菜单
//
//        //个人中心
//        WxMenuButton gerenzhongxinButton = new WxMenuButton();
//        gerenzhongxinButton.setName("个人中心");
//        gerenzhongxinButton.setType("view");
//        String gerenzhongxinUrl = URLEncoder.encode(h5page + "/app/mpagent/mine", "UTF-8");
//        gerenzhongxinButton.setUrl(backendUrl + "/wechat/agent/access?scope=1&redirectUrl=" + gerenzhongxinUrl);
//
//        // 我的收益
//        WxMenuButton myIncomeButton = new WxMenuButton();
//        myIncomeButton.setName("我的收益");// 我的收益
//        myIncomeButton.setType("view");
//        String myIncomeUrl = URLEncoder.encode(h5page + "/app/mpagent/income", "UTF-8");
//        myIncomeButton.setUrl(backendUrl + "/wechat/agent/access?scope=1&redirectUrl=" + myIncomeUrl);
//
//        fenxiaozhongxinButtonButtons.add(gerenzhongxinButton);
//        fenxiaozhongxinButtonButtons.add(myIncomeButton);
//
//        allButtons.add(fenxiaozhongxinButton);
//
//        agentMenu.setButtons(allButtons);
//
//        // 配置信息
//
//        LOGGER.debug(agentWxMpService.getWxMpConfigStorage().getAppId());
//        LOGGER.debug(agentWxMpService.getWxMpConfigStorage().getToken());
//        LOGGER.debug("====>" + JSON.toJSONString(agentMenu));
//        // 创建经纪人公众号的菜单
//        String menuId = agentWxMpService.getMenuService().menuCreate(agentMenu);
//        if (StringUtils.isNotEmpty(menuId)) {
//            // 创建成功
//            LOGGER.debug("====>经纪人端公众号菜单创建成功");
//        } else {
//            LOGGER.error("====>经纪人端公众号菜单创建失败");
//        }
//
//
//    }


}
