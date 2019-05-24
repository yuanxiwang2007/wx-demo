package com.estatesoft.ris.wx.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 魔镜腾讯数据对接工具类
 */
public class TencentUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TencentUtil.class);


    private static final String DES_GET_URL_PRE="http://172.16.4.17:81/Default.aspx?";
    private static final String TENCENT_SERVER_URL_PRE = "https://xz.m.tencent.com/adm_apps/magicmirror/api/";
    private static final String USER_INFO_URL="GetUserInfoByCardID/Post";
    private static final String PUT_DATA_URL="Report/Post";
    private static final String SECURITY = "r0d0myh908anwcqcywmcdxfxeyvpgr7i";//不会改变

    static {
    	System.setProperty ("jsse.enableSNIExtension", "false");
    }
    public static String getSign(String security,String nonce,String time){

        if(StringUtils.isBlank(security)){
            throw new IllegalArgumentException("security参数不能为空");
        }
        if(StringUtils.isBlank(nonce)){
            throw new IllegalArgumentException("nonce参数不能为空");
        }
        if(StringUtils.isBlank(time)){
            throw new IllegalArgumentException("time参数不能为空");
        }
       String ret =  doGet(String.format(DES_GET_URL_PRE+"security=%s,&nonce=%s&time=%s",security,nonce,time),null);
        return ret;
    }

    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    /**
     * 根据cardid获取用户信息
     * @param cardid
     * @return
     */
    public static String getTencentUserInfoByCardId(String cardid){
    	System.setProperty ("jsse.enableSNIExtension", "false");
        LOGGER.info("cardId:{}",cardid);
    	if(cardid.startsWith("0")){
    	    cardid = cardid.substring(1);
        }
        LOGGER.info("cardId:{}",cardid);
    	
        String time = getTime();
        String nonce= getRandomString(10);
        String sign = getSign(SECURITY,nonce,time);
        Map<String,String> headers = new HashMap<>();
        headers.put("HEALTH-NONCE",nonce);
        headers.put("HEALTH-TIME",time);
        headers.put("HEALTH-SIGN",sign);
        LOGGER.info("headers:"+ JSON.toJSONString(headers));
        LOGGER.info("url:"+TENCENT_SERVER_URL_PRE+USER_INFO_URL);
        Map<String,Object> params = new HashMap<>();
        params.put("cardid",cardid);
        LOGGER.info("cardId:"+cardid);
            String ret = doPost(TENCENT_SERVER_URL_PRE+USER_INFO_URL,params,headers);
        LOGGER.info("ret:"+ret);
        return ret;
    }

    /**
     * 推送检查结果给腾讯服务器
     * @param params
     * @return
     */
    public static String sendMagicResult(Map<String,Object> params){
    	System.setProperty ("jsse.enableSNIExtension", "false");

        String time = getTime();
        String nonce= getRandomString(10);
        String sign = getSign(SECURITY,nonce,time);
        Map<String,String> headers = new HashMap<>();
        headers.put("HEALTH-NONCE",nonce);
        headers.put("HEALTH-TIME",time);
        headers.put("HEALTH-SIGN",sign);
        LOGGER.info("headers:"+ JSON.toJSONString(headers));
        LOGGER.info("url:"+TENCENT_SERVER_URL_PRE+PUT_DATA_URL);
        LOGGER.info("params:"+ JSON.toJSONString(params));
        String ret = doPost(TENCENT_SERVER_URL_PRE+PUT_DATA_URL,params,headers);
        LOGGER.info("ret:"+ret);
        return ret;
    }

    /**
     * 发送GET请求 可以添加Header
     * @param url
     * @param addHeaderMapper
     * @return
     */
    public static String  doGet(String url,Map<String,String> addHeaderMapper){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            if(addHeaderMapper!=null && addHeaderMapper.size()>0){
                Set<String> keys = addHeaderMapper.keySet();
                for(String key :keys){
                    httpGet.addHeader(key,addHeaderMapper.get(key));
                }
            }
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 发送POST请求可以添加Header
     * @param url
     * @param paramMap
     * @param addHeaderMapper
     * @return
     */
    public static String doPost(String url, Map<String, Object> paramMap,Map<String,String> addHeaderMapper) {
    	System.setProperty("jsse.enableSNIExtension", "false");
    	CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json");
        if(addHeaderMapper!=null && addHeaderMapper.size()>0){
            Set<String> keys = addHeaderMapper.keySet();
            for(String key :keys){
                httpPost.addHeader(key,addHeaderMapper.get(key));
            }
        }
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
        	String jsonParam = JSON.toJSONString(paramMap);
   
        	// 循环遍历，获取迭代器
          System.out.println("====>>>"+jsonParam);
            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new StringEntity(jsonParam));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(),e);
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity,Charset.forName("UTF-8"));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(),e);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(),e);
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 生成随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(str.length());
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }


    public static void main(String[] args) throws Exception{
         //System.out.println(getSign("r0d0myh908anwcqcywmcdxfxeyvpgr7i","4LC09Ui5B2","20180927104557"));
        //System.out.println(getRandomString(10));
    	System.setProperty ("jsse.enableSNIExtension", "false");
    	String cardId="196911395";
        String ret=TencentUtil.getTencentUserInfoByCardId(cardId);
        System.out.println("----"+ret);
//        Map<String,Object> params = new HashMap<String,Object>();
//        params.put("height", 0);
//        params.put("weight", 1);
//        params.put("bmi", 2);
//        params.put("bloodOxygen", 3);
//        params.put("heartbeat", 5);
//        params.put("breathing", 4);
//        params.put("bfr", 5);
//        params.put("tfr", 6);
//        params.put("bm", 8);
//        params.put("uvi", 9);
//        params.put("mr", 10);
//        params.put("pr", 11);
//        params.put("smr", 12);
//        params.put("bpm", 13);
//        params.put("openId", "ei9KUmJWeU5uVXBFdXBwWkMwZVB2VEFpNUZYZDhmb3lId2NCOTZpenVrNGFjSy93dDh3WUZxUldaRE9VWjQ4dw==");
//        params.put("deviceId", null);
//        params.put("id", "1");
//        params.put("gmtCreate",new Date());
//        TencentUtil.sendMagicResult(params);
    }
    
    


}
