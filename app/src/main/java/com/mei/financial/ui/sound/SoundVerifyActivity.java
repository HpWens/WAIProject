package com.mei.financial.ui.sound;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.mei.financial.R;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("声纹验证");

    }

    @Override
    protected int layoutResId() {
        return R.layout.sound_verify_activity;
    }


    @OnClick({R.id.iv_play, R.id.iv_record, R.id.btn_exit, R.id.btn_restart_verify, R.id.btn_failure_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                break;
            case R.id.iv_record:
                break;
            case R.id.btn_exit:
                break;
            case R.id.btn_restart_verify:
                break;
            case R.id.btn_failure_exit:
                break;
        }
    }
}
