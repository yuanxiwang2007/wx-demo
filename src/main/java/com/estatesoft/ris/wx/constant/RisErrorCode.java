package com.estatesoft.ris.wx.constant;

/**
 * 错误编码
 * Created by wk on 2017/10/16.
 */
public enum RisErrorCode implements com.rms.common.exception.ErrorCode {
    DEVICE_NOT_REGISTER(10001, "设备未注册", ""),
    DEVICE_DISABLED(10002, "设备已禁用", ""),
    PARAM_NOTNULL_ERROR(10003, "请求参数{%s}不能为空", ""),
    CREATEQRCODE_ERROR(10004, "创建二维码失败", ""),
    APPVERSION_ERROR(10005, "设备版本号未找到", ""),
    APPVERSION_EXIST(10006, "该版本号已存在", ""),
    INVALID_TOKEN(10007, "非法TOKEN", ""),
    SYSTEMSETTING_EXIST(10008, "该系统配置已经存在", ""),
    SCORE_SECTION_EXIST(10009, "该体检评分段已经存在", "");

    private int code;
    private String message;
    private String messageEn;

    RisErrorCode(int code, String message, String messageEn) {
        this.code = code;
        this.message = message;
        this.messageEn = messageEn;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageEn() {
        return this.messageEn;
    }
}
