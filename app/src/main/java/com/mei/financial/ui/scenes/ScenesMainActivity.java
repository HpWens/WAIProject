package com.mei.financial.ui.scenes;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.mei.financial.R;
import com.mei.financial.ui.ForgetPasswordActivity;
import com.mei.financial.ui.HomeActivity;
import com.mei.financial.ui.RegisterActivity;
import com.meis.base.mei.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class ScenesMainActivity extends BaseActivity {
    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.scenes_main_activity;
    }


    @OnClick({R.id.btn_promise, R.id.btn_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_promise:
                startActivity(new Intent(mContext, MeetActivity.class));
                break;
            case R.id.btn_call:
                startActivity(new Intent(mContext, CallActivity.class));
                break;
        }
    }
}
