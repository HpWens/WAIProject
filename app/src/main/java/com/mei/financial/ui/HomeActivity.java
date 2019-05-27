package com.mei.financial.ui;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mei.financial.R;
import com.mei.financial.ui.scenes.ScenesMainActivity;
import com.mei.financial.ui.sound.SoundRegisterActivity;
import com.mei.financial.ui.sound.SoundVerifyActivity;
import com.meis.base.mei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class HomeActivity extends BaseActivity {
    @BindView(R.id.iv_header)
    ImageView mIvHeader;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.cv_personal)
    CardView mCvPersonal;
    @BindView(R.id.cv_sound_register)
    CardView mCvSoundRegister;
    @BindView(R.id.cv_sound_verify)
    CardView mCvSoundVerify;
    @BindView(R.id.cv_credit)
    CardView mCvCredit;
    @BindView(R.id.cv_scenes)
    CardView mCvScenes;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int layoutResId() {
        return R.layout.home_activity;
    }

    @OnClick({R.id.cv_personal, R.id.cv_sound_register, R.id.cv_sound_verify,
            R.id.cv_credit, R.id.cv_scenes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_personal:
                startActivity(new Intent(mContext, PersonalActivity.class));
                break;
            case R.id.cv_sound_register:
                startActivity(new Intent(mContext, SoundRegisterActivity.class));
                break;
            case R.id.cv_sound_verify:
                startActivity(new Intent(mContext, SoundVerifyActivity.class));
                break;
            case R.id.cv_credit:
                startActivity(new Intent(mContext, CreditActivity.class));
                break;
            case R.id.cv_scenes:
                startActivity(new Intent(mContext, ScenesMainActivity.class));
                break;
        }
    }
}
