package com.mei.financial.ui.scenes;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mei.financial.MyApplication;
import com.mei.financial.R;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CancelAdapt;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class ScenesMainActivity extends BaseActivity implements CustomAdapt {
    @BindView(R.id.tv_scenes)
    TextView mTvScenes;
    @BindView(R.id.btn_call)
    Button mBtnCall;
    @BindView(R.id.btn_promise)
    Button mBtnPromise;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("场景体验");

        int code = ((MyApplication) getApplication()).getFlavorsCode();
        mBtnCall.setVisibility(code == 3 ? View.GONE : View.VISIBLE);
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

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 640;
    }
}
