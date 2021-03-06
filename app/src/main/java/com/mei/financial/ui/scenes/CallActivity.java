package com.mei.financial.ui.scenes;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mei.financial.MyApplication;
import com.mei.financial.R;
import com.mei.financial.common.UrlApi;
import com.mei.financial.entity.UserService;
import com.mei.financial.utils.StringUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CancelAdapt;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author wenshi
 * @github
 * @Description 场景体验
 * @since 2019/5/23
 */
public class CallActivity extends BaseActivity implements CustomAdapt {

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
        setToolBarCenterTitle(getFlavorsTitle());
    }

    public String getFlavorsTitle() {
        String title = getString(R.string.financial_voice_review);
        int flavorsCode = ((MyApplication) getApplication()).getFlavorsCode();
        if (flavorsCode == 1) {
            title = getString(R.string.financial_voice_review);
        } else if (flavorsCode == 2) {
            title = getString(R.string.financial_voice_review2);
        }
        return title;
    }

    @Override
    protected int layoutResId() {
        return R.layout.call_activity;
    }


    @OnClick({R.id.iv_hang_up, R.id.iv_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_hang_up:
                mTvCalling.setText("手机");
                finish();
                break;
            case R.id.iv_answer:
                EasyHttp.get(UrlApi.SCENES_CALL)
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onError(ApiException e) {
                                RxToast.error(e.getMessage());
                            }

                            @Override
                            public void onSuccess(String s) {
                                if (!StringUtils.isEmpty(s)) {
                                    Result result = new Gson().fromJson(s, new TypeToken<Result>() {
                                    }.getType());
                                    if (result.isOk()) {
                                        mTvCalling.setText(getString(R.string.calling));
                                    } else {
                                        RxToast.error(result.getMsg());
                                    }
                                }
                            }
                        });
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
