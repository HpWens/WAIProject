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
 * @Description 会见页面
 * @since 2019/5/23
 */
public class MeetActivity extends BaseActivity {
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_copy)
    TextView mTvCopy;
    @BindView(R.id.tv_clear)
    TextView mTvClear;
    @BindView(R.id.tv_record)
    TextView mTvRecord;
    @BindView(R.id.iv_record)
    ImageView mIvRecord;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.meet_activity;
    }

}
