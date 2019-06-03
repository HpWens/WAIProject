package com.mei.financial.ui.sound;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.mei.financial.R;
import com.mei.financial.common.UrlApi;
import com.mei.financial.entity.ParameterizedTypeImpl;
import com.mei.financial.entity.SoundInfo;
import com.mei.financial.entity.VerifyResultInfo;
import com.mei.financial.utils.JsonParser;
import com.mei.financial.utils.StringUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.utils.Eyes;
import com.meis.base.mei.utils.ListUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxEncodeTool;
import com.vondear.rxtool.RxEncryptTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class SoundVerifyActivity extends BaseActivity {

    @BindView(R.id.iv_header)
    ImageView mIvHeader;
    @BindView(R.id.fl_header)
    FrameLayout mFlHeader;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.fl_title)
    FrameLayout mFlTitle;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.tv_sound_content)
    TextView mTvSoundContent;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.layout_content)
    RelativeLayout mLayoutContent;
    @BindView(R.id.iv_record)
    ImageView mIvRecord;
    @BindView(R.id.tv_hint)
    TextView mTvHint;
    @BindView(R.id.tv_verify_success)
    TextView mTvVerifySuccess;
    @BindView(R.id.btn_exit)
    Button mBtnExit;
    @BindView(R.id.tv_verify_failure)
    TextView mTvVerifyFailure;
    @BindView(R.id.space_view)
    Space mSpaceView;
    @BindView(R.id.btn_restart_verify)
    Button mBtnRestartVerify;
    @BindView(R.id.btn_failure_exit)
    Button mBtnFailureExit;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private boolean mTranslateEnable = false;
    private String resultType = "json";

    private String mSessionId = "";
    private String mSoundContent = "";

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("声纹验证");

        new RxPermissions(mContext).request(new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO})
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {

                        }
                    }
                });

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    // 错误码
                }
            }
        });

        mIatDialog = new RecognizerDialog(this, new InitListener() {
            @Override
            public void onInit(int code) {

            }
        });

        mTts = SpeechSynthesizer.createSynthesizer(this, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    // 错误码
                }
            }
        });

        EasyHttp.get(UrlApi.SOUND_REGISTER_TEXT)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        RxToast.error(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        Observable.just(s).map(new Function<String, Result<SoundInfo>>() {
                            @Override
                            public Result<SoundInfo> apply(String s) throws Exception {
                                return new Gson().fromJson(s, new ParameterizedTypeImpl(Result.class, new Class[]{SoundInfo.class}));
                            }
                        }).subscribe(new Consumer<Result<SoundInfo>>() {
                            @Override
                            public void accept(Result<SoundInfo> soundInfoResult) throws Exception {
                                if (soundInfoResult.isOk()) {
                                    if (null != soundInfoResult.getData() && soundInfoResult.getData().text != null) {
                                        mSessionId = soundInfoResult.getData().session_id;
                                        mSoundContent = soundInfoResult.getData().text.get(0);
                                        mTvSoundContent.setText(StringUtils.getCenterTwoSpace(mSoundContent));
                                    }
                                } else {
                                    RxToast.error(soundInfoResult.getMsg());
                                }
                            }
                        });
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.sound_verify_activity;
    }


    @OnClick({R.id.iv_play, R.id.iv_record, R.id.btn_exit, R.id.btn_restart_verify, R.id.btn_failure_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                if (null == mTts) {
                    return;
                }
                if (StringUtils.isEmpty(mSoundContent)) {
                    return;
                }
                setPlayParam();
                String speak = "";
                final char[] sounds = mSoundContent.toCharArray();
                for (int i = 0; i < sounds.length; i++) {
                    speak += sounds[i] + ",";
                }
                int code = mTts.startSpeaking(speak, new SynthesizerListener() {
                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onBufferProgress(int i, int i1, int i2, String s) {

                    }

                    @Override
                    public void onSpeakPaused() {

                    }

                    @Override
                    public void onSpeakResumed() {

                    }

                    @Override
                    public void onSpeakProgress(int i, int i1, int i2) {

                    }

                    @Override
                    public void onCompleted(SpeechError error) {
                        if (error == null) {
                        } else if (error != null) {
                            RxToast.error(error.getPlainDescription(true));
                        }
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                });

                if (code != ErrorCode.SUCCESS) {
                    RxToast.error("语音合成失败,错误码: " + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                }
                break;
            case R.id.iv_record:
                if (null == mIat) {
                    // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                    return;
                }
                if (null != mTts) {
                    mTts.stopSpeaking();
                }

                // 设置参数
                setParam();
                mIatDialog.setListener(new RecognizerDialogListener() {
                    @Override
                    public void onResult(RecognizerResult results, boolean isLast) {
                        printResult(results);
                    }

                    @Override
                    public void onError(SpeechError error) {
                        error.getPlainDescription(true);
                    }
                });
                mIatDialog.show();
                break;
            case R.id.btn_exit:
                finish();
                break;
            case R.id.btn_restart_verify:
                againVerify(false);
                break;
            case R.id.btn_failure_exit:
                finish();
                break;
        }
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        if (!StringUtils.isEmpty(text)) {
            uploadSoundFile();
        }
        // RxToast.normal(text);
    }


    private void uploadSoundFile() {
        if (StringUtils.isEmpty(mSoundContent)) return;
        String soundPath = Environment.getExternalStorageDirectory() + "/msc/iat.wav";
        File file = new File(soundPath);

        EasyHttp.post(UrlApi.SOUND_VERIFY)
                .params("session_id", mSessionId)
                .params("text", mSoundContent)
                .params("type", "rd")
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
                RxToast.error(e.getMessage());
            }

            @Override
            public void onSuccess(String s) {
                Observable.just(s).map(new Function<String, Result<VerifyResultInfo>>() {
                    @Override
                    public Result<VerifyResultInfo> apply(String s) throws Exception {
                        return new Gson().fromJson(s, new ParameterizedTypeImpl(Result.class, new Class[]{VerifyResultInfo.class}));
                    }
                }).subscribe(new Consumer<Result<VerifyResultInfo>>() {
                    @Override
                    public void accept(Result<VerifyResultInfo> verifyResultInfoResult) throws Exception {
                        if (verifyResultInfoResult.isOk() && verifyResultInfoResult.getData() != null) {
                            // if (!verifyResultInfoResult.getData().asr_result) {
                            //     RxToast.normal(getString(R.string.repeat_verify_hint));
                            //     return;
                            // }
                            mTvHint.setVisibility(View.GONE);
                            mIvRecord.setVisibility(View.GONE);
                            mTvVerifySuccess.setVisibility(View.VISIBLE);
                            mBtnExit.setVisibility(View.VISIBLE);
                        } else {
                            againVerify(true);
                        }
                    }
                });
            }
        });
    }

    private void againVerify(boolean failure) {
        mTvHint.setVisibility(failure ? View.GONE : View.VISIBLE);
        mIvRecord.setVisibility(failure ? View.GONE : View.VISIBLE);
        mTvVerifyFailure.setVisibility(!failure ? View.GONE : View.VISIBLE);
        mBtnRestartVerify.setVisibility(!failure ? View.GONE : View.VISIBLE);
        mBtnFailureExit.setVisibility(!failure ? View.GONE : View.VISIBLE);
    }

    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    private void setPlayParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.pcm");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }

        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }
}
