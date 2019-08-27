package com.estatesoft.ris.wx.utils;

import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ImageToBase64 {

    /**
     * 将网络图片编码为base64
     *
     * @param imgUrl
     * @return
     * @throws Exception
     */
    public static String encodeImageToBase64(String imgUrl) throws Exception {

        URL url = new URL(imgUrl);

        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        System.out.println("图片的路径为:" + url.toString());

        //打开链接
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();

            //设置请求方式为"GET"
            conn.setRequestMethod("GET");

            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);

            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();

            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];

            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;

            //使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {

                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }

            //关闭输入流
            inStream.close();
            byte[] data = outStream.toByteArray();

            //对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            String base64 = encoder.encode(data);
            System.out.println("网络文件[{}]编码成base64字符串:[{}]" + url.toString() + base64);
            return base64;//返回Base64编码过的字节数组字符串
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("图片上传失败,请联系客服!");
        }
    }


    /**
     * 本地图片转换Base64的方法
     *
     * @param imgPath     
     */

    private static void ImageToBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        System.out.println("本地图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));
    }
}
