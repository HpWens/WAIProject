package com.mei.financial;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.iflytek.cloud.SpeechUtility;
import com.vondear.rxtool.RxTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class MyApplication extends Application {

    String urlHeader = "http://www.mei.com";

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(MyApplication.this, "appid=" + getString(R.string.app_id));
        RxTool.init(this);

        // 初始化网络框架
        EasyHttp.init(this);
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.put("User-Agent", "com.mei.financial");
        //设置请求参数
        HttpParams params = new HttpParams();
        params.put("appId", "10010");
        EasyHttp.getInstance()
                .debug("RxEasyHttp", BuildConfig.DEBUG)
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setRetryCount(3) // 默认网络不好自动重试3次
                .setRetryDelay(500) // 每次延时500ms重试
                .setRetryIncreaseDelay(500) // 每次延时叠加500ms
                .setBaseUrl(urlHeader)
                .setCacheDiskConverter(new SerializableDiskConverter()) // 默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024) // 设置缓存大小为50M
                .setCacheVersion(1) // 缓存版本为1
                .setCertificates() // 信任所有证书
                .addCommonHeaders(headers) // 设置全局公共头
                .addCommonParams(params); // 设置全局公共参数

        super.onCreate();
    }
}
