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

    // 声纹注册
    public final static String UPLOAD_SOUND = "/api/v1/app/voice/upload";

    // 修改手机号码
    public final static String CHANGE_PHONE = "/api/v1/app/phone_number";

    // 修改密码
    public final static String CHANGE_PASSWORD = "/api/v1/app/password";

    // 语音注册
    public final static String SOUND_REGISTER = "/api/v1/app/voice/enroll";

    // 声纹验证
    public final static String SOUND_VERIFY = "/api/v1/app/voice/verify";

    // 删除声纹
    public final static String DELETE_SOUND = "/api/v1/app/voice/delete";

    // 会见系统
    public final static String MEET_UPLOAD_VOICE = "/api/v1/app/voice/meeting/identify";
}
