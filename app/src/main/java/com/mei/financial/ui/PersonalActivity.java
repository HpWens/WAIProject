package com.mei.financial.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mei.financial.R;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxKeyboardTool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class PersonalActivity extends BaseActivity {
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.tv_credit)
    TextView mTvCredit;

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("个人信息详情");

        // 设置头像
        Glide.with(mContext)
                .load("")
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.default_avatar_bg)
                .into(mIvAvatar);


    }

    @Override
    protected int layoutResId() {
        return R.layout.personal_activity;
    }

    @OnClick({R.id.btn_confirm, R.id.user_phone_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_phone_edit:
                mEtPhone.setSelection(mEtPhone.getText().length());
                mEtPhone.setEnabled(true);
                mEtPhone.requestFocus();
                RxKeyboardTool.showSoftInput(mContext, mEtPhone);
                break;
        }
    }
}
