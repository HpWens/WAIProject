package com.mei.financial.entity;

import java.io.Serializable;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/29
 */
public class UserInfo implements Serializable {

    public String token;
    public int id;
    public String name;
    public int sex;
    // 是否进行了语音注册
    public boolean is_enroll;
    public String phone_number;
    public int call_times;
    // 呼叫状态1:已接 2:未接 3:占线 4:空号 5:关机 6:其他
    public int call_status;
    // 信用值
    public int credit_value;
    // 时候在黑名单 1:白  2:黑
    public int is_black_list;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isIs_enroll() {
        return is_enroll;
    }

    public void setIs_enroll(boolean is_enroll) {
        this.is_enroll = is_enroll;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getCall_times() {
        return call_times;
    }

    public void setCall_times(int call_times) {
        this.call_times = call_times;
    }

    public int getCall_status() {
        return call_status;
    }

    public void setCall_status(int call_status) {
        this.call_status = call_status;
    }

    public int getCredit_value() {
        return credit_value;
    }

    public void setCredit_value(int credit_value) {
        this.credit_value = credit_value;
    }

    public int getIs_black_list() {
        return is_black_list;
    }

    public void setIs_black_list(int is_black_list) {
        this.is_black_list = is_black_list;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "token='" + token + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", is_enroll=" + is_enroll +
                ", phone_number='" + phone_number + '\'' +
                ", call_times=" + call_times +
                ", call_status=" + call_status +
                ", credit_value=" + credit_value +
                ", is_black_list=" + is_black_list +
                '}';
    }
}
