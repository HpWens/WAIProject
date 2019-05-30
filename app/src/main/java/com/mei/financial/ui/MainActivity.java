package com.mei.financial.ui;

import android.content.Intent;

import com.meis.base.mei.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
