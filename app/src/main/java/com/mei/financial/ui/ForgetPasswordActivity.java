package com.mei.financial.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

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
public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.bt_verify)
    Button mBtVerify;


    int mCountdown = 60;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mCountdown < 0) {
                mBtVerify.setText("发送验证码");
                mBtVerify.setClickable(true);
                mCountdown = 60;
            } else {
                mBtVerify.setClickable(false);
                mBtVerify.setText(mCountdown + " s 后重新发送");
                mCountdown--;
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.forget_password_activity;
    }

    @OnClick({R.id.bt_verify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_verify:
                mHandler.sendEmptyMessage(1);
                break;
        }
    }
}
