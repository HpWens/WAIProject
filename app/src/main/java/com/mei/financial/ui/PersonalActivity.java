package com.mei.financial.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mei.financial.R;
import com.mei.financial.entity.UserInfo;
import com.mei.financial.entity.UserService;
import com.mei.financial.utils.StringUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.status.ViewState;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxKeyboardTool;
import com.vondear.rxtool.RxNetTool;

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


    private UserInfo mUserInfo;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("个人信息详情");

        if (!RxNetTool.isAvailable(mContext)) {
            setState(ViewState.ERROR);
        }

        fillUserData();

        // 设置头像
        Glide.with(mContext)
                .load(mUserInfo.sex == 1 ? R.mipmap.user_woman_header : R.mipmap.user_man_header)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.default_avatar_bg)
                .into(mIvAvatar);

    }

    private void fillUserData() {
        mUserInfo = UserService.getInstance().getUserInfo();
        mTvName.setText(StringUtils.getFormatName(mUserInfo.name));
        mTvSex.setText(mUserInfo.sex == 1 ? "女" : "男");
        mTvCredit.setText("" + mUserInfo.getCredit_value());
        mEtPhone.setText(StringUtils.getFormatPrePhone(mUserInfo.phone_number));
    }

    @Override
    public void onErrorRetry() {
        super.onErrorRetry();
        if (RxNetTool.isAvailable(mContext)) {
            setState(ViewState.COMPLETED);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.personal_activity;
    }

    @OnClick({R.id.btn_confirm, R.id.user_phone_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_phone_edit:
                mEtPhone.setText(mUserInfo.phone_number);
                mEtPhone.setSelection(mEtPhone.getText().length());
                mEtPhone.setEnabled(true);
                mEtPhone.requestFocus();
                RxKeyboardTool.showSoftInput(mContext, mEtPhone);
                break;
            case R.id.btn_confirm:

                break;
        }
    }
}
