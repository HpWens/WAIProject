package com.mei.financial.ui;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mei.financial.R;
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
public class PersonalActivity extends BaseActivity {
    @BindView(R.id.iv_header)
    ImageView mIvHeader;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.tv_credit)
    TextView mTvCredit;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.personal_activity;
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
    }
}
