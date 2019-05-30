package com.mei.financial.ui.scenes;

import android.widget.ImageView;
import android.widget.TextView;

import com.mei.financial.R;
import com.mei.financial.common.UrlApi;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

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
    @BindView(R.id.iv_hang_up)
    ImageView mIvHangUp;
    @BindView(R.id.iv_answer)
    ImageView mIvAnswer;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("金融信用审核");

        EasyHttp.get(UrlApi.SCENES_CALL)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {

                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.call_activity;
    }

}
