package com.mei.financial.ui.scenes;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.mei.financial.MyApplication;
import com.mei.financial.R;
import com.mei.financial.common.UrlApi;
import com.mei.financial.entity.MeetContentInfo;
import com.mei.financial.entity.MeetResultInfo;
import com.mei.financial.entity.ParameterizedTypeImpl;
import com.mei.financial.entity.UserService;
import com.mei.financial.ui.adapter.MeetContentAdapter;
import com.mei.financial.ui.dialog.UploadSoundDialog;
import com.mei.financial.utils.JsonParser;
import com.mei.financial.utils.StringUtils;
import com.mei.financial.utils.WavUtils;
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

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author wenshi
 * @github
 * @Description 会见页面
 * @since 2019/5/23
 */
public class MeetActivity extends BaseActivity implements CustomAdapt {

    @BindView(R.id.recycler_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_hint)
    TextView mTvHint;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.space_view)
    Space mSpaceView;
    @BindView(R.id.btn_keep_on)
    Button mBtnKeepOn;
    @BindView(R.id.btn_clear)
    Button mBtnClear;
    @BindView(R.id.iv_record)
    RecordWaveView mIvRecord;
    @BindView(R.id.tv_record_hint)
    TextView mTvRecordHint;
    @BindView(R.id.btn_copy)
    Button mBtnCopy;
    @BindView(R.id.btn_switch)
    Button mBtnSwitch;
    @BindView(R.id.iv_start_meet)
    ImageView mIvStartMeet;
    @BindView(R.id.iv_end_meet)
    ImageView mIvEndMeet;

    private boolean mIsStartMeet = false;
    private MeetContentAdapter mAdapter;
    private boolean mIsRecordStop = false;

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;

    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private boolean mTranslateEnable = false;
    private String resultType = "json";
    private WebSocketClient mSocketClient;

    private boolean mFinishRecord = false;
    private UploadSoundDialog mUploadSoundDialog;

    private WavUtils mWavUtils;

    private HashMap<Integer, List<MeetResultInfo>> mVoiceMap = new HashMap<>();
    private int mCurrentPosition = 0;
    // 已经保存数据的索引值
    private int mSavedPosition = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!mVoiceMap.isEmpty()) {
                // if (msg.what == 1 && msg.obj instanceof MeetResultInfo) {
                //     MeetResultInfo result = (MeetResultInfo) msg.obj;
                //     addData(result);
                //     mVoiceMap.clear();
                // }

                if (msg.what == 1) {
                    mCurrentPosition = mAdapter.getData().size() >= 1 ? mAdapter.getData().size() - 1 : 0;
                }
            }
        }
    };

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("智能会见系统");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new MeetContentAdapter());
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == mAdapter.getData().size() - 1) {
                    outRect.bottom = RxImageTool.dip2px(8);
                }
            }
        });


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

        // 创建长连接
        try {
            mSocketClient = new WebSocketClient(new URI(((MyApplication) getApplication()).isRelease() ?
                    MyApplication.urlHeader : MyApplication.testHeader + "/api/v1/app_ws")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                }

                @Override
                public void onMessage(String message) {
                    Observable.just(message).map(new Function<String, Result<MeetResultInfo>>() {
                        @Override
                        public Result<MeetResultInfo> apply(String s) throws Exception {
                            Log.e("MeetActivity", " Meet Data = " + s);
                            return new Gson().fromJson(s, new ParameterizedTypeImpl(Result.class, new Class[]{MeetResultInfo.class}));
                        }
                    }).subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Result<MeetResultInfo>>() {
                                @Override
                                public void accept(Result<MeetResultInfo> meetResultInfoResult) throws Exception {
                                    if (meetResultInfoResult.isOk() && meetResultInfoResult.getData() != null) {
                                        MeetResultInfo resultInfo = meetResultInfoResult.getData();
                                        if (!ListUtils.isEmpty(resultInfo.chat_record)) {
                                            // addData(resultInfo.chat_record);
                                        }
                                    } else if (meetResultInfoResult.getCode() == 1 && meetResultInfoResult.getData() != null
                                            && mIsStartMeet) {
                                        // 处理实时语音转换规则
                                        MeetResultInfo resultInfo = meetResultInfoResult.getData();

                                        mHandler.removeCallbacksAndMessages(null);
                                        //Message msg = new Message();
                                        //msg.what = 1;
                                        //msg.obj = resultInfo;
                                        //mHandler.sendMessageDelayed(msg, 2000);
                                        mHandler.sendEmptyMessageDelayed(1, 2000);

                                        if (mAdapter.getData().isEmpty()) {
                                            addData(resultInfo);
                                            return;
                                        }

                                        for (int i = mCurrentPosition; i < mAdapter.getData().size(); i++) {
                                            MeetContentInfo meetData = mAdapter.getData().get(i);
                                            if (meetData.speaker_id == resultInfo.speaker_id) {
                                                if (meetData.index == resultInfo.index) {
                                                    meetData.context = resultInfo.context;
                                                    meetData.speaker_name = resultInfo.speaker_name;
                                                    meetData.pass = resultInfo.pass;
                                                    mAdapter.notifyItemChanged(i);
                                                    return;
                                                }
                                            }
                                        }

                                        addData(resultInfo);

                                        // if (mVoiceMap.containsKey((Integer) resultInfo.speaker_id)) {
                                        //     List<MeetResultInfo> meetDatas = mVoiceMap.get(resultInfo.speaker_id);
                                        //     meetDatas.add(resultInfo);
                                        //     if (meetDatas.size() > 1) {
                                        //         // pre index
                                        //         if (meetDatas.get(meetDatas.size() - 1).index != meetDatas.get(meetDatas.size() - 2).index) {
                                        //             addData(meetDatas.get(meetDatas.size() - 2));
                                        //         }
                                        //     }
                                        // } else {
                                        //     List<MeetResultInfo> addDatas = new ArrayList<>();
                                        //     addDatas.add(resultInfo);
                                        //     mVoiceMap.put(resultInfo.speaker_id, addDatas);
                                        // }
                                    }
                                }
                            });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {

                }

                @Override
                public void onError(Exception ex) {
                }
            };
            mSocketClient.connect();
        } catch (Exception e) {
        }

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
                        //mWavUtils.startRecord();
                    }

                    @Override
                    public void onSuccess(String s) {
                        //mWavUtils.startRecord();
                    }
                });
            }
        });

        notifyMeetRecordData();
    }

    private void notifyMeetRecordData() {
        List<MeetContentInfo> saveData = UserService.getInstance().getMeetData();
        mSavedPosition = mCurrentPosition = saveData.isEmpty() ? 0 : saveData.size() - 1;
        addData(saveData);
    }

    private void addData(MeetResultInfo resultInfo) {
        MeetContentInfo contentInfo = new MeetContentInfo();
        List<MeetContentInfo> array = new ArrayList<>();

        contentInfo.userId = resultInfo.id;
        contentInfo.speaker_name = resultInfo.speaker_name;
        contentInfo.context = resultInfo.context;
        contentInfo.create_time = resultInfo.create_time;
        contentInfo.task_id = resultInfo.task_id;
        contentInfo.pass = resultInfo.pass;
        contentInfo.index = resultInfo.index;
        contentInfo.speaker_id = resultInfo.speaker_id;

        array.add(contentInfo);
        addData(array);
    }

    private void addData(List<MeetContentInfo> array) {
        if (mIsRecordStop) return;
        mAdapter.addData(array);
        if (mAdapter.getData().isEmpty()) {
            mTvHint.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mTvHint.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        if (mAdapter.getData().size() > 1) {
            mRecyclerView.smoothScrollToPosition(mAdapter.getData().size() - 1);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.meet_activity;
    }

    public String getCopyContent() {
        StringBuilder builder = new StringBuilder();
        for (MeetContentInfo info : mAdapter.getData()) {
            builder.append(info.speaker_name + "\n");
            builder.append(info.context + "\n");
        }
        return builder.toString();
    }

    @OnClick({R.id.btn_copy, R.id.btn_clear, R.id.iv_record, R.id.btn_switch, R.id.btn_keep_on})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_copy:
                if (mAdapter.getData().isEmpty()) {
                    RxToast.normal("无复制内容");
                    return;
                }
                copyTextToClipboard(getCopyContent());
                RxToast.normal("复制成功");
                break;
            case R.id.btn_clear:
                mCurrentPosition = 0;
                mAdapter.setNewData(new ArrayList<MeetContentInfo>());
                mTvHint.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);

                // 清理会议记录数据
                mSavedPosition = 0;
                UserService.getInstance().clearMeetData();

                break;
            case R.id.iv_record:
                if (null == mIat) {
                    // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                    return;
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
                        // RxToast.normal("听写失败");
                        resetListening();
                    } else {
                        // RxToast.normal(getString(R.string.text_begin));
                    }
                }
//                mIatDialog.setListener(new RecognizerDialogListener() {
//                    @Override
//                    public void onResult(RecognizerResult results, boolean isLast) {
//                        printResult(results);
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
            case R.id.btn_switch:
                startMeeting();
                break;
//            case R.id.btn_switch:
//                if (mIsStartMeet) {
//                    mIsRecordStop = true;
//                    mBtnSwitch.setVisibility(View.GONE);
//                    mIvStartMeet.setVisibility(View.GONE);
//                    mIvEndMeet.setVisibility(View.VISIBLE);
//                    mBtnClear.setVisibility(View.VISIBLE);
//                    mBtnKeepOn.setVisibility(View.VISIBLE);
//                    Glide.with(this).load(R.mipmap.sound_stop_ic).into(mIvEndMeet);
//                    break;
//                }
//                mIsStartMeet = !mIsStartMeet;
//                mBtnSwitch.setText("结束会议");
//                mIvStartMeet.setVisibility(View.VISIBLE);
//
//                // startRecord();
//                mWavUtils = new WavUtils(new WavUtils.OnRecordListener() {
//                    @Override
//                    public void onGenerateWav(int fileNameIndex) {
//                        // 上传文件
//                        String target = Environment.getExternalStorageDirectory() + "/msc/mei" + fileNameIndex + ".wav";
//                        File file = new File(target);
//                        if (file == null || !file.exists()) return;
//                        EasyHttp.post(UrlApi.VOICE_MEETING_STREAM)
//                                .params("wave_file", file, file.getName(), new ProgressResponseCallBack() {
//                                    @Override
//                                    public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {
//                                        int progress = (int) (bytesWritten * 100 / contentLength);
//                                        if (done) {
//                                        }
//                                    }
//                                }).execute(new SimpleCallBack<String>() {
//                            @Override
//                            public void onError(ApiException e) {
//                                //mWavUtils.startRecord();
//                            }
//
//                            @Override
//                            public void onSuccess(String s) {
//                                //mWavUtils.startRecord();
//                            }
//                        });
//                    }
//                });
//                mWavUtils.startRecord();
//
//                Glide.with(this).load(R.mipmap.meet_sound_start_ic).diskCacheStrategy(DiskCacheStrategy.ALL).into(mIvStartMeet);
//                break;
            case R.id.btn_keep_on:
                // mIsRecordStop = false;
                mBtnClear.setVisibility(View.GONE);
                mBtnKeepOn.setVisibility(View.GONE);
                mBtnSwitch.setVisibility(View.VISIBLE);
                mIvStartMeet.setVisibility(View.VISIBLE);
                mIvEndMeet.setVisibility(View.GONE);
                startMeeting();
                break;
        }
    }

    private void startMeeting() {
        mIsStartMeet = !mIsStartMeet;
        if (mIsStartMeet) {
            // 开始会议
            mBtnSwitch.setText("结束会议");
            mIvStartMeet.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.mipmap.meet_sound_start_ic).diskCacheStrategy(DiskCacheStrategy.ALL).into(mIvStartMeet);
            mWavUtils.startRecord();
        } else {
            // 结束会议
            mWavUtils.stopRecord();
            mBtnSwitch.setVisibility(View.GONE);
            mIvStartMeet.setVisibility(View.GONE);
            mIvEndMeet.setVisibility(View.VISIBLE);
            mBtnClear.setVisibility(View.VISIBLE);
            mBtnKeepOn.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.mipmap.sound_stop_ic).into(mIvEndMeet);
        }
    }

    private void startRecord() {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            return;
        }
        // 设置参数
        setParam();
        int ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            // RxToast.normal("听写失败");
            // resetListening();
        } else {
            // RxToast.normal(getString(R.string.text_begin));
        }
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
        mIat.setParameter(SpeechConstant.VAD_BOS, "2000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "2000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        RxToast.normal(text);
    }

    private void resetListening() {
        // mFinishRecord = false;
        // mIvRecord.stopAnimator();
    }

    private void showUploadDialog() {
        if (mUploadSoundDialog == null) {
            mUploadSoundDialog = new UploadSoundDialog(mContext);
        }
        // mUploadSoundDialog.show();
    }

    private void dismissUploadDialog() {
        if (mUploadSoundDialog != null && mUploadSoundDialog.isShowing()) {
            mUploadSoundDialog.dismiss();
        }
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

    private void uploadSoundFile() {
        String soundPath = Environment.getExternalStorageDirectory() + "/msc/iat.wav";
        File file = new File(soundPath);
        EasyHttp.post(UrlApi.MEET_UPLOAD_VOICE)
                .params("type", "ti")
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
                    RxToast.normal(e.getMessage());
                }
                dismissUploadDialog();

                startRecord();
            }

            @Override
            public void onSuccess(String s) {
                if (!StringUtils.isEmpty(s)) {
                    ApiResult apiResult = new Gson().fromJson(s, new TypeToken<ApiResult>() {
                    }.getType());
                    if (apiResult.isOk()) {
                        // 上传成功 长连接接受消息
                    } else {
                        RxToast.error(apiResult.getMsg());
                    }
                }
                dismissUploadDialog();

                startRecord();
            }
        });
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
                // RxToast.normal(error.getErrorDescription());
                startRecord();
            }
            resetListening();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
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
    protected void onDestroy() {
        // 保存记录数据
        List<MeetContentInfo> meetData = mAdapter.getData();
        if (mSavedPosition != (meetData.size() - 1) && mSavedPosition < meetData.size()) {
            UserService.getInstance().saveMeetRecord(meetData.subList(mSavedPosition, meetData.size()));
        }

        if (mWavUtils != null) {
            mWavUtils.stopRecord();
        }
        if (mSocketClient != null) {
            mSocketClient.close();
        }
        super.onDestroy();
    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 640;
    }

    public void copyTextToClipboard(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("playerId", text);
                clipboardManager.setPrimaryClip(clipData);
            }
        });
    }
}
