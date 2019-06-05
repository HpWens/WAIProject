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

        findViewById(R.id.view_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soundPath = Environment.getExternalStorageDirectory() + "/msc/iat.wav";
                final File file = new File(soundPath);

                EasyHttp.post(UrlApi.UPLOAD_SOUND)
                        .params("session_id", "5183f209876611e9b91cfa163e91236e")
                        .params("text", "79285340")
                        .params("type", "rd")
                        .params("voice_number", "rd_1")
                        .params("wave_file", file, file.getName(), new ProgressResponseCallBack() {
                            @Override
                            public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {
                                int progress = (int) (bytesWritten * 100 / contentLength);
                                if (done) {
                                }
                            }
                        }).execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {

                    }
                });

            }
        });

    }

    @Override
    protected void initData() {

        Eyes.translucentStatusBar(mContext, true);

        startActivity(new Intent(this, LoginActivity.class));

        finish();

    }

    @Override
    protected int layoutResId() {
        return R.layout.main_activity;
    }
}
