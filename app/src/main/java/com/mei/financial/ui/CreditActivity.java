package com.mei.financial.ui;

import android.view.View;
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
 * @Description 信用界面
 * @since 2019/5/23
 */
public class CreditActivity extends BaseActivity {
    @BindView(R.id.iv_header)
    ImageView mIvHeader;
    @BindView(R.id.tv_default)
    TextView mTvDefault;
    @BindView(R.id.btn_default)
    Button mBtnDefault;
    @BindView(R.id.tv_promise)
    TextView mTvPromise;
    @BindView(R.id.btn_promise)
    Button mBtnPromise;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.credit_activity;
    }

    @OnClick({R.id.btn_default, R.id.btn_promise})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_default:
                break;
            case R.id.btn_promise:
                break;
        }
    }
}
