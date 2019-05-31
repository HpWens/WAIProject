package com.mei.financial.common;

import com.mei.financial.utils.StringUtils;

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

    // 获取信用值
    public static String GET_CREDIT_VALUE = "/api/v1/app/credit_value";

    // 呼入
    public final static String SCENES_CALL = "/api/v1/app/call";

    // 随机动态数字生成
    public final static String SOUND_REGISTER_TEXT = "/api/v1/app/voice/rand_digit_text";

    // 获取预留手机号
    public final static String GET_PRE_PHONE = "/api/v1/phone_number";

    // 保存密码
    public final static String COMMIT_SAVE_PASSWORD = "";

    public final static String UPLOAD_SOUND = "/api/v1/app/voice/upload";

}
