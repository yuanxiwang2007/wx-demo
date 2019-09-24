package com.estatesoft.ris.wx.utils.qr;


import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;

public class TwoDimensionCode {

    /**
     * 像素块大小
     */
    private int pixelSize = 3;
    /**
     * 复杂度
     */
    private int complex = 6;
    /**
     * 默认图片像素
     */
    private int picSize = 127;

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     */
    public void encoderQRCode(String content, String imgPath) {
        this.encoderQRCode(content, imgPath, "png", picSize);
    }


    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     * @param imgType 图片类型
     */
    public void encoderQRCode(String content, String imgPath, String imgType) {
        this.encoderQRCode(content, imgPath, imgType, picSize);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 图片类型
     */
    public void encoderQRCode(String content, OutputStream output, String imgType) {
        this.encoderQRCode(content, output, imgType, picSize, pixelSize);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     */
    public void encoderQRCode(String content, OutputStream output) {
        this.encoderQRCode(content, output, "png", picSize, pixelSize);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     * @param imgType 图片类型
     * @param size    二维码尺寸
     */
    public void encoderQRCode(String content, String imgPath, String imgType, int size) {
        try {
            BufferedImage bufImg = this.qRCodeCommon(content, size, pixelSize, complex);

            File imgFile = new File(imgPath);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 图片类型
     * @param size    二维码尺寸
     */
    public void encoderQRCode(String content, OutputStream output, String imgType, int size, int pixelSize) {
        try {
            BufferedImage bufImg = this.qRCodeCommon(content, size, pixelSize, complex);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 图片类型
     * @param size    二维码尺寸
     */
    public void encoderQRCode(String content, OutputStream output, String imgType, int size, int pixelSize, int complex) {
        try {
            BufferedImage bufImg = this.qRCodeCommon(content, size, pixelSize, complex);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(QRCode)图片的公共方法
     *
     * @param content   存储内容
     * @param size      二维码尺寸
     * @param pixelSize 每个像素的大小
     * @param complex   复杂度(重复度)
     * @return
     */
    private BufferedImage qRCodeCommon(String content, int size, int pixelSize, int complex) {
        BufferedImage bufImg = null;
        try {
            Qrcode qrcodeHandler = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect('M');
            qrcodeHandler.setQrcodeEncodeMode('B');
            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
            qrcodeHandler.setQrcodeVersion(complex);
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");
            // 图片尺寸
            int imgSize = size;
            bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            // 设置背景颜色
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgSize, imgSize);

            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            if (contentBytes.length > 0 && contentBytes.length < 800) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * pixelSize + pixoff, i * pixelSize + pixoff, pixelSize, pixelSize);
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
            }
            gs.dispose();
            bufImg.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImg;
    }

    /**
     * 解析二维码（QRCode）
     *
     * @param imgPath 图片路径
     * @return
     */
    public String decoderQRCode(String imgPath) {
        // QRCode 二维码图片的文件
        File imageFile = new File(imgPath);
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(imageFile);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return content;
    }

    /**
     * 解析二维码（QRCode）
     *
     * @param input 输入流
     * @return
     */
    public String decoderQRCode(InputStream input) {
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(input);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return content;
    }

    /**
     * 方法说明 encoderQRCode 生成带背景图的二维码
     *
     * @param content   生成二维码数据
     * @param output    输出流
     * @param backImg   背景图片信息
     * @param size      二维码大小
     * @param pixelSize 二维码像素块大小
     * @param complex   复杂度
     */
    public void encoderQRCode(String content, OutputStream output, TwoDimensionCodeBackImg backImg, int size, int pixelSize, int complex) {
        try {
            String backPath = backImg.getBackPath();
            if (backPath.contains("classpath:")) {
                backPath = backPath.replace("classpath:", "src/main/resources/");
            }
            BufferedImage buffImg;
            int titleHeight = 40;
            if (StringUtils.isEmpty(backPath)) {
                buffImg = new BufferedImage(backImg.getIcoSize() + 1, backImg.getIcoSize() + titleHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D gs = buffImg.createGraphics();
                // 设置背景颜色
                gs.setBackground(Color.WHITE);
                gs.clearRect(0, 0, backImg.getIcoSize() + 1, backImg.getIcoSize() + titleHeight);

                backImg.setIcoX(0);
                backImg.setIcoY(titleHeight - 1);
            } else {
                buffImg = ImageIO.read(new File(backPath));
            }
            //得到画笔对象
            Graphics g = buffImg.getGraphics();
            //创建你要附加的二维码图象
            BufferedImage bufImg = this.qRCodeCommon(content, size, pixelSize, complex);
            double wr = 0;
            double hr = 0;
            //获取图片缩放比例
            wr = backImg.getIcoSize() * 1.0 / bufImg.getWidth();
            hr = backImg.getIcoSize() * 1.0 / bufImg.getHeight();
            AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
            Image image = ato.filter(bufImg, null);
            //将小图片根据传入坐标绘到大图片上。
            g.drawImage(image, backImg.getIcoX(), backImg.getIcoY(), null);
            //设置颜色。
            g.setColor(Color.BLACK);

            if (StringUtils.isEmpty(backPath)) {
                //最后一个参数用来设置字体的大小
                Font f = new Font("宋体", Font.BOLD, backImg.getTitleFontSize());
                Color wordsColor = Color.BLACK;//new Color(0, 0, 255);
                g.setColor(wordsColor);
                g.setFont(f);
                g.drawString(backImg.getTitle(), 10, 25);
            }

            g.dispose();
            ImageIO.write(buffImg, "png", output);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 背景二维码实例
     *
     * @RequestMapping(value = "/promotionCodeDemo")
     * public HttpResult promotionCodeDemo(int size,int pixelSize,int complex, HttpServletResponse response) throws Exception {
     * TwoDimensionCodeBackImg backImg = new TwoDimensionCodeBackImg();
     * TwoDimensionCode handler = new TwoDimensionCode();
     * backImg.setBackPath("classpath:qrCodeBackImg.png");
     * //backImg.setBackPath("");
     * backImg.setIcoSize(250);
     * backImg.setIcoX(250);
     * backImg.setIcoY(603);
     * //backImg.setTitle("扫码解锁红包");
     * //backImg.setTitleX(10);
     * //backImg.setTitleX(10);
     * String randomStr ="12010101200100481540435520077";
     * String path = URLEncoder.encode(randomStr, "UTF-8");
     * String projectPath = "https://urine-analysis.doctorwork.com/urine-miniapp";
     * handler.encoderQRCode(projectPath + "/unknown?path=" + path, response.getOutputStream(), backImg, size, pixelSize, complex);
     * <p>
     * return success();
     * }
     **/

    public static void main(String[] args) {
        String imgPath = "D:\\zhaochao.png";
        String encoderContent = "";
        for (int i = 0; i < 40; i++)
            encoderContent += "赵";

        TwoDimensionCode handler = new TwoDimensionCode();

        TwoDimensionCodeBackImg backImg = new TwoDimensionCodeBackImg();

        backImg.setBackPath("classpath:qrCodeBackImg.png");
        backImg.setIcoSize(250);
        backImg.setIcoX(250);
        backImg.setIcoY(603);
        handler.encoderQRCode("qeqwesdasdasd", null, backImg, 250, 6, 6);

        handler.encoderQRCode(encoderContent, imgPath, "png", 130);

        System.out.println("=============编码成功！图片为于：" + imgPath + "===============");

        String decoderContent = handler.decoderQRCode(imgPath);
        System.out.println("============解析结果如下：===============");
        System.out.println(decoderContent);
        System.out.println("=========解码成功===========");
    }


}