package com.mei.financial.ui;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.mei.financial.R;
import com.mei.financial.common.UrlApi;
import com.mei.financial.utils.StringUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxImageTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.utils.RxUtil;

import java.io.File;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

        Eyes.translucentStatusBar(mContext, true);

        startActivity(new Intent(this, LoginActivity.class));

        finish();
    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
