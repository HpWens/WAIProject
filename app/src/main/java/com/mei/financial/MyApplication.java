package com.mei.financial;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.mei.financial.entity.UserInfo;
import com.mei.financial.entity.UserService;
import com.mei.financial.utils.StringUtils;
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

    String urlHeader = "http://119.3.70.106:8030";

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(MyApplication.this, "appid=" + getString(R.string.app_id));
        RxTool.init(this);

        UserService.init(this);

        // 初始化网络框架
        EasyHttp.init(this);
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        // headers.put("User-Agent", "mei");
        // String token = RxSPTool.getString(this, Constant.GLOBAL_TOKEN);
        UserInfo userInfo = UserService.getInstance().getUserInfo();
        if (null != userInfo && !StringUtils.isEmpty(userInfo.token)) {
            headers.put("Authorization", "Bearer " + userInfo.token);
        }
        //设置请求参数
        HttpParams params = new HttpParams();
        // params.put("appId", "10010");
        EasyHttp.getInstance()
                //.debug("RxEasyHttp", BuildConfig.DEBUG)
                .setReadTimeOut(10 * 60 * 1000)
                .setWriteTimeOut(10 * 60 * 1000)
                .setConnectTimeout(10 * 60 * 1000)
                .setRetryCount(3) // 默认网络不好自动重试3次
                .setRetryDelay(500) // 每次延时500ms重试
                .setRetryIncreaseDelay(500) // 每次延时叠加500ms
                .setBaseUrl(urlHeader)
                .setCacheDiskConverter(new SerializableDiskConverter()) // 默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024) // 设置缓存大小为50M
                .setCacheVersion(1) // 缓存版本为1
                .setCertificates() // 信任所有证书
                // .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCommonHeaders(headers) // 设置全局公共头
                .addCommonParams(params); // 设置全局公共参数

        super.onCreate();
    }

    public void addEasyTokenHeader() {
        HttpHeaders headers = new HttpHeaders();
        UserInfo userInfo = UserService.getInstance().getUserInfo();
        if (null != userInfo && !StringUtils.isEmpty(userInfo.token)) {
            headers.put("Authorization", "Bearer " + userInfo.token);
        }
        EasyHttp.getInstance().getCommonHeaders().clear();
        EasyHttp.getInstance().addCommonHeaders(headers);
    }
}
