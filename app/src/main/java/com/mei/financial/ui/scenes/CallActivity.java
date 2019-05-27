package com.mei.financial.ui.scenes;

import android.widget.ImageView;
import android.widget.TextView;

import com.mei.financial.R;
import com.meis.base.mei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wenshi
 * @github
 * @Description 场景体验
 * @since 2019/5/23
 */
public class CallActivity extends BaseActivity {
    @BindView(R.id.iv_call)
    ImageView mIvCall;
    @BindView(R.id.tv_call_name)
    TextView mTvCallName;
    @BindView(R.id.tv_calling)
    TextView mTvCalling;
    @BindView(R.id.tv_hang_up)
    TextView mTvHangUp;
    @BindView(R.id.tv_answer)
    TextView mTvAnswer;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.call_activity;
    }

}
