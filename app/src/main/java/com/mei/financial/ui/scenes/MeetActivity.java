package com.mei.financial.ui.scenes;

import android.Manifest;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.mei.financial.R;
import com.mei.financial.entity.MeetContentBean;
import com.mei.financial.ui.adapter.MeetContentAdapter;
import com.mei.financial.utils.JsonParser;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import me.jessyan.autosize.internal.CancelAdapt;
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
    @BindView(R.id.btn_copy)
    Button mBtnCopy;
    @BindView(R.id.btn_clear)
    Button mBtnClear;
    @BindView(R.id.iv_record)
    ImageView mIvRecord;
    @BindView(R.id.tv_record_hint)
    TextView mTvRecordHint;

    private MeetContentAdapter mAdapter;

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;

    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private boolean mTranslateEnable = false;
    private String resultType = "json";

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
    }

    @Override
    protected int layoutResId() {
        return R.layout.meet_activity;
    }

    @OnClick({R.id.btn_copy, R.id.btn_clear, R.id.iv_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_copy:

                MeetContentBean bean = new MeetContentBean();
                bean.name = "周杰伦：";
                bean.content = "哎呦不错哦，come on，来一首周杰伦的\n" +
                        "稻花香";
                mAdapter.addData(bean);

                mRecyclerView.smoothScrollToPosition(mAdapter.getData().size() - 1);

                break;
            case R.id.btn_clear:
                break;
            case R.id.iv_record:
                if (null == mIat) {
                    // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                    return;
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
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

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


    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 640;
    }
}
