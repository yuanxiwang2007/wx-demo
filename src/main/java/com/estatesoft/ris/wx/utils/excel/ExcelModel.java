package com.estatesoft.ris.wx.utils.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : hechao
 * @date 2019/8/16
 */
public class ExcelModel extends BaseRowModel implements Serializable {

    @ExcelProperty(value = "姓名", index = 0)
    private String userName;

    @ExcelProperty(value = "性别", index = 1)
    private String gender;

    @ExcelProperty(value = "生日", index = 2)
    private Date birthday;

    @ExcelProperty(value = "年级", index = 3)
    private String gradeName;

    @ExcelProperty(value = "班次", index = 4)
    private String className;

    @ExcelProperty(value = "家长手机", index = 5)
    private String phone;

    @ExcelProperty(value = "入学日期", index = 6)
    private Date entryTime;

    @ExcelProperty(value = "别名", index = 7)
    private String nickName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
