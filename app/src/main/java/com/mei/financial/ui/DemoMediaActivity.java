package com.mei.financial.ui;

import android.Manifest;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mei.financial.R;
import com.mei.financial.common.UrlApi;
import com.mei.financial.utils.MediaRecorderTask;
import com.mei.financial.utils.WavUtils;
import com.meis.base.mei.base.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/7/12
 */
public class DemoMediaActivity extends BaseActivity {

    private MediaRecorderTask mMediaRecorderTask;
    private TextView mTvStart;
    private TextView mTvEnd;

    private EditText mEtDuration;


    private boolean mStartRecord = false;

    WavUtils mWavUtils;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        new RxPermissions(mContext).request(new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO})
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            // finish();
                        }
                    }
                });

        mWavUtils = new WavUtils(new WavUtils.OnRecordListener() {
            @Override
            public void onGenerateWav(int fileNameIndex) {
                // 上传文件
                String target = Environment.getExternalStorageDirectory() + "/msc/mei" + fileNameIndex + ".wav";
                File file = new File(target);
                if (file == null || !file.exists()) return;
                EasyHttp.post(UrlApi.VOICE_MEETING_STREAM)
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
                        Log.e("-------------", "***************" + e.getMessage());
                        //mWavUtils.startRecord();
                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.e("-------------", "***************1111" + s);
                        //mWavUtils.startRecord();
                    }
                });
            }
        });

        mMediaRecorderTask = new MediaRecorderTask();

        mEtDuration = findViewById(R.id.et_duration);
        mTvStart = findViewById(R.id.tv_start);
        mTvEnd = findViewById(R.id.tv_end);
        mTvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWavUtils.startRecord();

                //startInterval();
            }
        });

    }

    private void startInterval() {
        long interval = 500;
        if (mEtDuration.getText().equals("") || mEtDuration.getText() == null) {
            interval = 500;
        } else {
            interval = Long.parseLong(mEtDuration.getText().toString());
        }
        Observable.interval(500, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .retry(3)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mStartRecord = !mStartRecord;
                        if (mStartRecord) {
                            // startRecord();
                            mWavUtils.startRecord();
                        } else {
                            //stopRecode();
                            mWavUtils.stopRecord();
                            String target = Environment.getExternalStorageDirectory() + "/msc/mei.wav";
                            File file = new File(target);
                            if (file == null || !file.exists()) return;
                            EasyHttp.post(UrlApi.VOICE_MEETING_STREAM)
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
                                    RxToast.normal(e.getMessage());
                                    Log.e("-------------", "***************" + e.getMessage());
                                }

                                @Override
                                public void onSuccess(String s) {
                                    Log.e("-------------", "***************1111" + s);
                                    RxToast.normal(s);
                                }
                            });

                        }
                    }
                });
    }

    /**
     * 开启录音
     */
    private void startRecord() {
        //创建录音文件---这里创建文件不是重点，我直接用了
        String path = Environment.getExternalStorageDirectory() + "/msc/iat.pcm";
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mMediaRecorderTask.start(file);
    }

    /**
     * 停止录制
     */
    private void stopRecode() {
        mMediaRecorderTask.stop();
        // mTvEnd.setText("录制" + mMediaRecorderTask.getAllTime() + "秒");
    }

    @Override
    protected int layoutResId() {
        return R.layout.demo_media_activity;
    }

    @Override
    protected void onDestroy() {
        // stopRecode();
        super.onDestroy();
    }
}
