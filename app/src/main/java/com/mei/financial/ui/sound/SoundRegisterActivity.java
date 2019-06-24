package com.mei.financial.ui.sound;

import android.Manifest;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.mei.financial.R;
import com.mei.financial.common.UrlApi;
import com.mei.financial.entity.ParameterizedTypeImpl;
import com.mei.financial.entity.SoundInfo;
import com.mei.financial.entity.UserService;
import com.mei.financial.entity.VerifyResultInfo;
import com.mei.financial.ui.dialog.CustomRecognizerDialog;
import com.mei.financial.ui.dialog.SoundRegisterFailureDialog;
import com.mei.financial.ui.dialog.SoundRegisterSuccessDialog;
import com.mei.financial.ui.dialog.UploadSoundDialog;
import com.mei.financial.utils.JsonParser;
import com.mei.financial.utils.StringUtils;
import com.mei.financial.view.RecordWaveView;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.utils.Eyes;
import com.meis.base.mei.utils.ListUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxImageTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class SoundRegisterActivity extends BaseActivity implements CustomAdapt {

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
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.layout_content)
    RelativeLayout mLayoutContent;
    @BindView(R.id.iv_record)
    RecordWaveView mIvRecord;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.tv_text)
    TextView mTvText;
    @BindView(R.id.space_view)
    Space mSpaceView;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.tv_hint)
    TextView mTvHint;

    private int mIndexVerify = 0;
    private String mSessionId = "";
    private List<String> mTextsVerify = new ArrayList<>();
    private String mVerifyText = "中国工商银行提供资金管理、收费缴费、营销服务、金融理财、代理销售、电子商务六大类服务";

    private boolean mIsPlaySound = false;
    private boolean mSoundCompleted = true;

    private boolean mFinishRecord = false;
    private UploadSoundDialog mUploadSoundDialog;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("声纹注册");

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

        mIatDialog = new CustomRecognizerDialog(this, new InitListener() {
            @Override
            public void onInit(int code) {
            }
        });

        post(new Runnable() {
            @Override
            public void run() {
                handlerIatLayout();
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
                                    mIndexVerify = 0;
                                    if (null != soundInfoResult.getData() && soundInfoResult.getData().text != null) {
                                        mSoundCompleted = true;
                                        mSessionId = soundInfoResult.getData().session_id;
                                        // 添加2次文本
                                        soundInfoResult.getData().text.add(mVerifyText);
                                        soundInfoResult.getData().text.add(mVerifyText);

                                        mTextsVerify = soundInfoResult.getData().text;
                                        updateViews();
                                    }
                                } else {
                                    RxToast.error(soundInfoResult.getMsg());
                                }
                            }
                        });
                    }
                });
    }

    private void handlerIatLayout() {
        if (null != mIatDialog.getCustomView()) {
            View parent = mIatDialog.getCustomView();
            parent.setBackground(new ColorDrawable(0));
            if (parent instanceof LinearLayout) {
                LinearLayout linearParent = (LinearLayout) parent;
                linearParent.setBackground(new ColorDrawable(0));
                View firstChild = linearParent.getChildAt(0);
                if (null != firstChild && firstChild instanceof LinearLayout) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) firstChild.getLayoutParams();
                    lp.topMargin = RxImageTool.dip2px(64);
                    LinearLayout childParent = (LinearLayout) firstChild;
                    if (childParent.getChildCount() >= 1) {
                        childParent.getChildAt(childParent.getChildCount() - 1).setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    private void updateView(SpannableStringBuilder builder) {
        mTvCount.setText(builder);
    }

    @Override
    protected int layoutResId() {
        return R.layout.sound_register_activity;
    }

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private CustomRecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private boolean mTranslateEnable = false;
    private String resultType = "json";

    private long moreClickDuration = 500;
    private long currentTime = 0;

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
        mIat.setParameter(SpeechConstant.VAD_EOS, "3000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    @OnClick({R.id.iv_play, R.id.iv_record, R.id.btn_confirm, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                if (null == mTts) {
                    return;
                }
                if (ListUtils.isEmpty(mTextsVerify)) {
                    return;
                }

                mIsPlaySound = !mIsPlaySound;
                mIvPlay.setImageResource(mIsPlaySound ? R.mipmap.sound_register_play_ic : R.mipmap.sound_register_pause_ic);
                if (!mIsPlaySound) {
                    mTts.pauseSpeaking();
                } else {
                    mTts.resumeSpeaking();
                }
                if (!mSoundCompleted) {
                    return;
                }

                setPlayParam();
                char[] sounds = mTextsVerify.get(mIndexVerify).toCharArray();
                String speak = "";
                if (mIndexVerify > 2 && mIndexVerify < mTextsVerify.size()) {
                    speak = mTextsVerify.get(mIndexVerify);
                } else {
                    for (int i = 0; i < sounds.length; i++) {
                        speak += sounds[i] + ",";
                    }
                }
                int code = mTts.startSpeaking(speak, new SynthesizerListener() {
                    @Override
                    public void onSpeakBegin() {
                        mSoundCompleted = false;
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
                            restorePlayViewState();
                        } else if (error != null) {
                            RxToast.error(error.getPlainDescription(true));
                        }
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                });

                if (code != ErrorCode.SUCCESS) {
                    RxToast.error("语音合成失败");
                }
                break;
            case R.id.iv_record:
                if (System.currentTimeMillis() - currentTime < moreClickDuration) {
                    return;
                }
                currentTime = System.currentTimeMillis();

                if (null == mIat) {
                    // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                    return;
                }
                if (null != mTts && mTts.isSpeaking()) {
                    mTts.stopSpeaking();
                    restorePlayViewState();
                }

                if (mFinishRecord) {
                    mIvRecord.stopAnimator();
                    if (mIat.isListening()) {
                        mIat.stopListening();
                    }
                } else {
                    mIvRecord.startWaveAnimator();
                    // 设置参数
                    setParam();
                    int ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        RxToast.normal("听写失败");
                        resetListening();
                    } else {
                        RxToast.normal(getString(R.string.text_begin));
                    }
                }
//                mIatDialog.setListener(new RecognizerDialogListener() {
//                    @Override
//                    public void onResult(RecognizerResult results, boolean isLast) {
//                        printResult(results, isLast);
//                    }
//
//                    @Override
//                    public void onError(SpeechError error) {
//                        error.getPlainDescription(true);
//                    }
//                });
//                mIatDialog.show();
                mFinishRecord = !mFinishRecord;
                break;
            case R.id.btn_confirm:
                mBtnConfirm.setVisibility(View.GONE);
                mTvHint.setVisibility(View.GONE);
                mIvRecord.setVisibility(View.VISIBLE);
                mBtnCancel.setVisibility(View.GONE);
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void resetListening() {
        mFinishRecord = false;
        mIvRecord.stopAnimator();
    }

    private void restorePlayViewState() {
        mSoundCompleted = true;
        mIsPlaySound = false;
        mIvPlay.setImageResource(R.mipmap.sound_register_pause_ic);
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

    private void printResult(RecognizerResult results, boolean isLast) {
        String text = JsonParser.parseIatResult(results.getResultString());
        if (isLast) {
            resetListening();
            showUploadDialog();
            mIvRecord.postDelayed(new Runnable() {
                @Override
                public void run() {
                    uploadSoundFile();
                }
            }, 200);
        }
        // RxToast.normal(text);
    }

    private void showUploadDialog() {
        if (mUploadSoundDialog == null) {
            mUploadSoundDialog = new UploadSoundDialog(mContext);
        }
        mUploadSoundDialog.show();
    }

    private void dismissUploadDialog() {
        if (mUploadSoundDialog != null && mUploadSoundDialog.isShowing()) {
            mUploadSoundDialog.dismiss();
        }
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

    private void uploadSoundFile() {
        if (ListUtils.isEmpty(mTextsVerify)) return;
        String soundPath = Environment.getExternalStorageDirectory() + "/msc/iat.wav";
        final File file = new File(soundPath);

        int currentCount = mIndexVerify + 1;
        EasyHttp.post(UrlApi.UPLOAD_SOUND)
                .params("session_id", mSessionId)
                .params("text", mTextsVerify.get(mIndexVerify))
                .params("type", mIndexVerify > 2 ? "td" : "rd")
                .params("voice_number", mIndexVerify > 2 ? ("td_" + (currentCount - 3)) : ("rd_" + currentCount))
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
                if (e.getCode() != ApiException.ERROR.UNKNOWN) {
                    RxToast.error(e.getMessage());
                } else {
                    RxToast.normal(getString(R.string.repeat_verify_hint));
                }
                dismissUploadDialog();
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
                            if (!verifyResultInfoResult.getData().asr_result) {
                                RxToast.normal(getString(R.string.repeat_verify_hint));
                                return;
                            }
                            // 上传成功后删除文件
                            // if (file.exists() && file.isFile()) {
                            //     file.delete();
                            // }
                            mIndexVerify++;

                            if (mIndexVerify == 2) {
                                mIndexVerify++;
                            }
                            if (mIndexVerify == 5) {
                                deleteSound();
                                return;
                            }
                            updateViews();

                        } else {
                            RxToast.error(verifyResultInfoResult.getMsg());
                        }
                    }
                });
                dismissUploadDialog();
            }
        });
    }

    private void deleteSound() {
        // 先删除声纹
        EasyHttp.delete(UrlApi.DELETE_SOUND)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        RxToast.normal(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!StringUtils.isEmpty(s)) {
                            ApiResult apiResult = new Gson().fromJson(s, new TypeToken<ApiResult>() {
                            }.getType());
                            if (apiResult.isOk()) {
                                // 提交注册
                                commitRegister();
                            } else {
                                RxToast.normal(apiResult.getMsg());
                            }
                        }
                    }
                });
    }

    private void commitRegister() {
        EasyHttp.post(UrlApi.SOUND_REGISTER)
                .params("session_id", mSessionId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        RxToast.normal(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!StringUtils.isEmpty(s)) {
                            ApiResult apiResult = new Gson().fromJson(s, new TypeToken<ApiResult>() {
                            }.getType());
                            if (apiResult.isOk()) {
                                // 更改用户是否语音验证状态
                                UserService.getInstance().changeIsEnroll(true);
                                new SoundRegisterSuccessDialog(mContext, new SoundRegisterSuccessDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(SoundRegisterSuccessDialog dialog) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).show();
                            } else {
                                new SoundRegisterFailureDialog(mContext, new SoundRegisterFailureDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(SoundRegisterFailureDialog dialog) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).show();
                            }
                        }
                    }
                });
    }

    private void updateViews() {
        if (mIndexVerify >= mTextsVerify.size()) {
            mIndexVerify = mTextsVerify.size() - 1;
        }
        mTvDescription.setText(mIndexVerify > 2 ? "文本无关" : "动态数字");

        mTvSoundContent.setVisibility(mIndexVerify > 2 ? View.INVISIBLE : View.VISIBLE);
        mTvText.setVisibility(mIndexVerify > 2 ? View.VISIBLE : View.GONE);

        mTvSoundContent.setText(mIndexVerify > 2 ? "" : StringUtils.getCenterTwoSpace(mTextsVerify.get(mIndexVerify)));
        mTvText.setText(mTextsVerify.get(mIndexVerify));

        // mTvCount.setText((mIndexVerify + 1) + "/5"); (mIndexVerify + 1) + "/4";
        String contText = (mIndexVerify + 1) + "/4";
        if (mIndexVerify >= 3) {
            contText = mIndexVerify + "/4";
        }
        SpannableString spannableString = new SpannableString(contText);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_83DBFF));
        spannableString.setSpan(colorSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTvCount.setText(spannableString);
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                RxToast.normal(error.getErrorDescription() + "\n请确认是否已开通翻译功能");
            } else {
                RxToast.normal(error.getErrorDescription());
            }
            resetListening();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            resetListening();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results, isLast);
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 640;
    }
}
