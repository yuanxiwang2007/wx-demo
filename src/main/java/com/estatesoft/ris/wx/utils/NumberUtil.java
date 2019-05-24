package com.estatesoft.ris.wx.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ProjectName: qiedoctor-worker
 * Description: 数字工具
 * User: wangkai <wangkai@doctorwork.com>
 * Date: 2018/3/7
 * Time: 上午10:24
 */
public class NumberUtil {


    /**
     "^\\d+$"　　//非负整数（正整数   +   0）
     "^[0-9]*[1-9][0-9]*$"　　//正整数
     "^((-\\d+)|(0+))$"　　//非正整数（负整数   +   0）
     "^-[0-9]*[1-9][0-9]*$"　　//负整数
     "^-?\\d+$"　　　　//整数
     "^\\d+(\\.\\d+)?$"　　//非负浮点数（正浮点数   +   0）
     "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$"　　//正浮点数
     "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$"　　//非正浮点数（负浮点数   +   0）
     "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$"　　//负浮点数
     "^(-?\\d+)(\\.\\d+)?$"　　//浮点数
     */

    /**
     * 验证非负整数数字(不能包括数字之外的其它字符)的正则表达式
     */
    public static final String NONNEGATIVE_NUMBER_REG = "^([1-9]\\d*)$"; // ^(0|[1-9]\d*)$

    public static final String NEGATIVE_NUMBER_REG = "^(0|[1-9]\\d*)$";

    /**
     * 是否为正整数
     *
     * @param number
     * @return
     */
    public static boolean isNumber(String number) {
        Pattern pattern = Pattern.compile(NumberUtil.NONNEGATIVE_NUMBER_REG);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
