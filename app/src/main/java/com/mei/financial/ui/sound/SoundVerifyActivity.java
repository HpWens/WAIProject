package com.mei.financial.ui.sound;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mei.financial.R;
import com.meis.base.mei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class SoundVerifyActivity extends BaseActivity {
    @BindView(R.id.iv_header)
    ImageView mIvHeader;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.tv_sound_verify)
    TextView mTvSoundVerify;
    @BindView(R.id.tv_verify_num)
    TextView mTvVerifyNum;
    @BindView(R.id.layout_verify)
    LinearLayout mLayoutVerify;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.iv_record)
    ImageView mIvRecord;
    @BindView(R.id.tv_record)
    TextView mTvRecord;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.sound_verify_activity;
    }

}
