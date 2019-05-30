package com.mei.financial.ui.scenes;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.mei.financial.R;
import com.mei.financial.entity.MeetContentBean;
import com.mei.financial.ui.adapter.MeetContentAdapter;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wenshi
 * @github
 * @Description 会见页面
 * @since 2019/5/23
 */
public class MeetActivity extends BaseActivity {

    @BindView(R.id.recycler_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_hint)
    TextView mTvHint;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.space_view)
    Space mSpaceView;
    @BindView(R.id.btn_copy)
    Button mBtnCopy;
    @BindView(R.id.btn_clear)
    Button mBtnClear;
    @BindView(R.id.iv_record)
    ImageView mIvRecord;
    @BindView(R.id.tv_record_hint)
    TextView mTvRecordHint;

    private MeetContentAdapter mAdapter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("智能会见系统");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new MeetContentAdapter());
    }

    @Override
    protected int layoutResId() {
        return R.layout.meet_activity;
    }

    @OnClick({R.id.btn_copy, R.id.btn_clear, R.id.iv_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_copy:

                MeetContentBean bean = new MeetContentBean();
                bean.name = "周杰伦：";
                bean.content = "哎呦不错哦，come on，来一首周杰伦的\n" +
                        "稻花香";
                mAdapter.addData(bean);

                mRecyclerView.smoothScrollToPosition(mAdapter.getData().size() - 1);

                break;
            case R.id.btn_clear:
                break;
            case R.id.iv_record:
                break;
        }
    }
}
