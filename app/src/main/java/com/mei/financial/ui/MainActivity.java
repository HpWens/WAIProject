package com.mei.financial.ui;

import com.mei.financial.ui.dialog.WSVerifySuccessDialog;
import com.meis.base.mei.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

//        Eyes.translucentStatusBar(mContext, true);
//
//        startActivity(new Intent(this, LoginActivity.class));
//
//        finish();

        new WSVerifySuccessDialog(this, new WSVerifySuccessDialog.OnPositiveListener() {
            @Override
            public void onClick(WSVerifySuccessDialog dialog) {

            }
        }).show();

    }

    @Override
    protected int layoutResId() {
        return 0;
    }
}
