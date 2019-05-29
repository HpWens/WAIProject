package com.mei.financial.ui;

import android.content.Intent;
import android.util.Log;

import com.mei.financial.common.UrlApi;
import com.mei.financial.entity.ApiResult;
import com.meis.base.mei.base.BaseActivity;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        // startActivity(new Intent(this, LoginActivity.class));

        EasyHttp.post(UrlApi.USER_REGISTER)
                .params("account", "test4")
                .params("password", "123456")
                .params("phone_number", "15874856421")
                .params("sex", "1")
                .params("name", "test")
                .execute(new SimpleCallBack<ApiResult>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("-------------", "***************" + e.toString());
                    }

                    @Override
                    public void onSuccess(ApiResult apiResult) {
                        Log.e("-------------", "***************" + apiResult.code);
                    }

                });
    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
