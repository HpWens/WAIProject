package com.mei.financial;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(MyApplication.this, "appid=" + getString(R.string.app_id));
        super.onCreate();
    }
}
