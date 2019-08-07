package com.mei.financial.ui;

import android.content.Intent;

import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        Eyes.translucentStatusBar(mContext, true);

        startActivity(new Intent(this, LoginActivity.class));

        finish();
    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
