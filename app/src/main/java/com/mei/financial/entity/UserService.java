package com.mei.financial.entity;

import android.app.Application;

import com.mei.financial.common.Constant;
import com.mei.financial.utils.ACache;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/30
 */
public class UserService {

    private volatile static UserService singleton = null;

    private static Application mContext;

    public UserService() {

    }

    public static UserService getInstance() {
        testInitialize();
        if (singleton == null) {
            synchronized (UserService.class) {
                if (singleton == null) {
                    singleton = new UserService();
                }
            }
        }
        return singleton;
    }

    private static void testInitialize() {
        if (mContext == null)
            throw new ExceptionInInitializerError("请先在全局Application中调用 UserService.init() 初始化！");
    }

    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     */
    public static void init(Application app) {
        mContext = app;
    }

    public void saveUser(UserInfo userInfo) {
        ACache.get(mContext).put(Constant.USER_INFO, userInfo);
    }

    public void clearUser() {
        ACache.get(mContext).put(Constant.USER_INFO, new UserInfo());
    }

    public UserInfo getUserInfo() {
        Object userObj = ACache.get(mContext).getAsObject(Constant.USER_INFO);
        if (null != userObj && userObj instanceof UserInfo) {
            return (UserInfo) userObj;
        }
        return new UserInfo();
    }

    public int getCreditValue() {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            return userInfo.credit_value;
        }
        return 0;
    }

    public void changeCreditValue(int value) {
        UserInfo userInfo = getUserInfo();
        userInfo.credit_value = value;
        saveUser(userInfo);
    }

    public void addCreditValue(int add) {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            userInfo.credit_value += add;
            if (userInfo.credit_value >= 100) {
                userInfo.credit_value = 100;
            }
            saveUser(userInfo);
        }
    }

    public void reduceCreditValue(int reduce) {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            userInfo.credit_value -= reduce;
            if (userInfo.credit_value <= 0) {
                userInfo.credit_value = 0;
            }
            saveUser(userInfo);
        }
    }

    public void changePhone(String phone) {
        UserInfo userInfo = getUserInfo();
        userInfo.phone_number = phone;
        saveUser(userInfo);
    }

}
