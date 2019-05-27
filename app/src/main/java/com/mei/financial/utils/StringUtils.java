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
}
