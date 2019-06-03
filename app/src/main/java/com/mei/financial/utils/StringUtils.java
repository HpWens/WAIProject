package com.mei.financial.utils;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/27
 */
public class StringUtils {
    /**
     * 是否为空
     *
     * @param str 字符串
     * @return true 空 false 非空
     */
    public static Boolean isEmpty(String str) {
        if (str == null || str.equals("")) {
            return true;
        }

        return false;
    }

    public static String getFormatPrePhone(String phone) {
        if (null == phone || phone.length() != 11) {
            return "";
        }
        return phone.substring(0, 3) + "***" + phone.substring(8, 11);
    }

    public static String getFormatName(String name) {
        if (null == name || name.equals("")) {
            return "";
        }
        int length = name.length();
        if (length > 2) {
            return name.substring(0, 1) + "*" + name.substring(length - 1, length);
        } else {
            return name;
        }
    }

    public static String getCenterTwoSpace(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        int half = content.length() / 2;
        return content.substring(0, half) + "  " + content.substring(half, content.length());
    }

}
