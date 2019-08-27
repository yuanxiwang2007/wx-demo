package com.estatesoft.ris.wx;

import com.alibaba.fastjson.JSONObject;
import com.estatesoft.ris.wx.utils.HttpUtil;
import com.estatesoft.ris.wx.utils.MD5Util;
import com.estatesoft.ris.wx.utils.UuidUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class maintest {
    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        String emailAddress = "danielling.income@gmail.com";
        String reg = "\\w+[\\w|\\.]+[\\w]*@[\\w]+\\.[\\w]+$";
        //利用正则表达式（可改进）验证邮箱是否符合邮箱的格式
        if (!emailAddress.matches("^\\w+[\\w|\\.]+@(\\w+\\.)+\\w+$")) {
            System.out.println("111111");
        }

        if (!emailAddress.matches(reg)) {
            System.out.println("111111");
        }
        emailAddress = "danielling .income@gmail.com";
        if (!emailAddress.matches(reg)) {
            System.out.println("111111");
        }
        emailAddress = "danielling income@gmail.com";
        if (!emailAddress.matches(reg)) {
            System.out.println("111111");
        }
        emailAddress = "danielling..income@gmail.com";
        if (!emailAddress.matches(reg)) {
            System.out.println("111111");
        }
        if (!emailAddress.matches("^\\w+[\\w|\\.]+@(\\w+\\.)+\\w+$")) {
            System.out.println("111111");
        }
        emailAddress = "daniellingincome@gmail.com";
        if (!emailAddress.matches(reg)) {
            System.out.println("111111");
        }
        String id = UuidUtil.getUUID();
        try {
            id = MD5Util.toBase64(id);
            id = MD5Util.toBase64(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try
//        {
//            //GetUrlS();
//            getAccessToken();
//            //cont="22_dfcQpxOom37Y88_M-IVOTCyxOvLSpm-FYqkHfqb6VWo5RJwnmMf0Eyn6V6Kn4bZCJJs4By60Ap_SGtQfbf9Y7wFI_y29BoSmNK2Ftmk5HgIUA_kOJC6U5bQDjvfjgj0uatIcbbq_-r8gsWRbJQMhADAYFB";
//            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+cont);
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setRequestMethod("POST");// 提交模式
//            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
//            // conn.setReadTimeout(2000);//读取超时 单位毫秒
//            // 发送POST请求必须设置如下两行
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
//            // 发送请求参数
//            JSONObject paramJson = new JSONObject();
//            paramJson.put("scene", "a=1234567890");
//            paramJson.put("page", "pages/index/index");
//            paramJson.put("width", 430);
//            paramJson.put("auto_color", false);
//            /**
//             * line_color生效
//             * paramJson.put("auto_color", false);
//             * JSONObject lineColor = new JSONObject();
//             * lineColor.put("r", 0);
//             * lineColor.put("g", 0);
//             * lineColor.put("b", 0);
//             * paramJson.put("line_color", lineColor);
//             * */
//
//            printWriter.write(paramJson.toString());
//            // flush输出流的缓冲
//            printWriter.flush();
//            //开始获取数据
//            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
//            OutputStream os = new FileOutputStream(new File("D:/abc.png"));
//            int len;
//            byte[] arr = new byte[1024];
//            while ((len = bis.read(arr)) != -1)
//            {
//                os.write(arr, 0, len);
//                os.flush();
//            }
//            os.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }


//        //第一个:把网络图片装换成Base64
//        String netImagePath = "http://beauty-public.zone1.meitudata.com/open/facecharacter/4.jpg";// = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559812846591&di=55a641c65ae34c168dc41040221c134b&imgtype=0&src=http%3A%2F%2Fs8.sinaimg.cn%2Fmw690%2F003j9jhPgy6Qe9EJDGT77%26690";
//
//        try {
//            String APPINFO = "?api_key=nRSpsqdooDVwoew3XljC_bMTiGKlMgWN&api_secret=PnFqO31LAdT0m7UFt4vO0UkEji6NrZSz";
//            String ApiUrl = "https://openapi.mtlab.meitu.com/v1/skinanlysis" + APPINFO;
//            try {
//
//                //人脸属性分析
//                ApiUrl = "https://openapi.mtlab.meitu.com/v1/facedetect" + APPINFO;
//                Map<String, Object> map = new HashMap<>();
//                Map<String, Object> parameter = new HashMap<>();
//                parameter.put("return_attributes", "gender,age,race,beauty,glasses,mustache,emotion,eyelid");
//                parameter.put("return_landmark", 3);
//                map.put("parameter", parameter);
//
//                List<Map<String, Object>> media_info_list = new ArrayList<>();
//                Map<String, Object> file1 = new HashMap<>();
//                file1.put("media_data", netImagePath);
//                map.put("extra", new HashMap<>());
//
//                Map<String, Object> media_profiles = new HashMap<>();
//                media_profiles.put("media_data_type", "url");
//                file1.put("media_profiles", media_profiles);
//                media_info_list.add(file1);
//                map.put("media_info_list", media_info_list);
//
//                String param ="{\n" +
//                        "\"parameter\": {\n" +
//                        "\"return_attributes\": \"gender,age,race,beauty,glasses,mustache,emotion,eyelid\",\n" +
//                        "\"return_landmark\": 3\n" +
//                        "},\n" +
//                        "\"extra\": {\n" +
//                        "},\n" +
//                        "\"media_info_list\": [\n" +
//                        "{\n" +
//                        "\"media_data\": \""+netImagePath+"\",\n" +
//                        "\"media_profiles\": {\n" +
//                        "\"media_data_type\": \"url\"\n" +
//                        "}\n" +
//                        "}\n" +
//                        "]\n" +
//                        "}" ;//JSON.toJSONString(map);
//                String res = sendPost(ApiUrl, param);
//                System.out.println(res);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    static String cont = "";

    public static void getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + "wxa8fd8ce4c0466690" + "&secret=" + "3895b2a65fb96a25d730ca1e7a22af38";
        String result = HttpUtil.sendPost(url, "");
        JSONObject jsons = JSONObject.parseObject(result);
        String expires_in = jsons.getString("expires_in");
        //缓存
        if (Integer.parseInt(expires_in) == 7200) {
            //ok
            String access_token = jsons.getString("access_token");
            System.out.println("access_token:" + access_token);

            cont = access_token;
        } else {
            System.out.println("出错获取token失败！");
        }
    }

    public static void GetUrlS() throws IOException {
        HttpGet httpGet = new HttpGet(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                        + "wxa8fd8ce4c0466690" + "&secret=" + "3895b2a65fb96a25d730ca1e7a22af38");
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse res = httpClient.execute(httpGet);
        HttpEntity entity = res.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsons = JSONObject.parseObject(result);
        String expires_in = jsons.getString("expires_in");

        //缓存
        if (Integer.parseInt(expires_in) == 7200) {
            //ok
            String access_token = jsons.getString("access_token");
            System.out.println("access_token:" + access_token);

            cont = access_token;
        } else {
            System.out.println("出错获取token失败！");
        }

    }

}
