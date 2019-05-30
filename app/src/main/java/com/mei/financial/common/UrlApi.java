package com.mei.financial.common;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/27
 */
public class UrlApi {

    // 注册
    public final static String USER_REGISTER = "/api/v1/app_sign_up";

    // 登录
    public final static String USER_LOGIN = "/api/v1/app_login";

    // 信用值 /api/v1/app/credit_value/:value
    // :value 为目标值
    public static String CREDIT_URL = "/api/v1/app/credit_value/";

    // 呼入
    public final static String SCENES_CALL = "/api/v1/app/call";

}
