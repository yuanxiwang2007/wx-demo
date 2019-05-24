package com.estatesoft.ris.wx.utils;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.Data;

/**
 * ProjectName: qiedoctor-worker
 * Description: 二维码参数
 * User: wangkai <wangkai@doctorwork.com>
 * Date: 2018/4/10
 * Time: 下午4:27
 */
@Data
public class QRCodeParams {

    private String txt;                //二维码内容
    private String qrCodeUrl;          //二维码网络路径
    private String filePath;           //二维码生成物理路径
    private String fileName;           //二维码生成图片名称（包含后缀名）
    private String logoPath;           //logo图片
    private Integer width = 300;           //二维码宽度
    private Integer height = 300;          //二维码高度
    private Integer onColor = 0xFF000000;  //前景色
    private Integer offColor = 0xFFFFFFFF; //背景色
    private Integer margin = 1;            //白边大小，取值范围0~4
    private ErrorCorrectionLevel level = ErrorCorrectionLevel.L;  //二维码容错率

    public String getSuffixName() {
        String imgName = this.getFileName();
        if (imgName != null && !"".equals(imgName)) {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            return suffix;
        }
        return "";
    }

}
