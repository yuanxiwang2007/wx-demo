package com.estatesoft.ris.wx.utils.qr;

import lombok.Data;

import java.io.Serializable;

@Data
public class TwoDimensionCodeBackImg implements Serializable {
    /**
     * 背景图地址
     */
    private String backPath;

    /**
     * 嵌套图标的 X坐标
     */
    private int icoX;

    /**
     * 嵌套图标的 Y坐标
     */
    private int icoY;

    /**
     * 嵌套图标的 size大小
     */
    private int icoSize;

    /**
     * 标题内容
     */
    private String title;

    /**
     * 标题内容字体大小
     */
    private int titleFontSize = 24;

    /**
     * 标题内容X
     */
    private int titleX;

    /**
     * 标题内容Y
     */
    private int titleY;

}
