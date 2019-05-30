package com.mei.financial.ui;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

/**
 * @author wenshi
 * @github
 * @Description 信用界面
 * @since 2019/5/23
 */
public class CreditActivity extends BaseActivity {

    @BindView(R.id.view_banner)
    View mViewBanner;
    @BindView(R.id.tv_break_contract_hint)
    TextView mTvBreakContractHint;
    @BindView(R.id.btn_break_contract)
    Button mBtnBreakContract;
    @BindView(R.id.tv_break_contract_value)
    TextView mTvBreakContractValue;
    @BindView(R.id.layout_break_contract)
    RelativeLayout mLayoutBreakContract;
    @BindView(R.id.tv_promise_hint)
    TextView mTvPromiseHint;
    @BindView(R.id.btn_promise)
    Button mBtnPromise;
    @BindView(R.id.tv_promise_value)
    TextView mTvPromiseValue;

    private int creditValue = 100;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.setStatusBarColor(mContext, getResources().getColor(R.color.color_163DC1));
        autoFillToolBarLeftIcon();
        setToolBarCenterTitle("信用值");

        // get credit value

        creditValue = UserService.getInstance().getUserInfo().credit_value;

        if (creditValue >= 100) {
            mBtnPromise.setEnabled(false);
        } else if (creditValue <= 0) {
            mBtnBreakContract.setEnabled(false);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.credit_activity;
    }

    @OnClick({R.id.btn_break_contract, R.id.btn_promise})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_break_contract:
                // break a contract
                creditValue -= 25;
                if (creditValue <= 0) {
                    creditValue = 0;
                    mBtnBreakContract.setEnabled(false);
                }

                if (creditValue < 100) {
                    mBtnPromise.setEnabled(true);
                }

                EasyHttp.put(UrlApi.CREDIT_URL + creditValue)
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
                                        mTvBreakContractValue.setVisibility(View.VISIBLE);
                                        mTvBreakContractValue.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mTvBreakContractValue.setVisibility(View.GONE);
                                            }
                                        }, 500);
                                        UserService.getInstance().changeCreditValue(creditValue);
                                    } else {
                                        RxToast.error(result.getMsg());
                                    }
                                }
                            }
                        });
                break;
            case R.id.btn_promise:

                creditValue += 25;
                if (creditValue >= 100) {
                    creditValue = 100;
                    mBtnPromise.setEnabled(false);
                }

                if (creditValue > 0) {
                    mBtnBreakContract.setEnabled(true);
                }

                EasyHttp.put(UrlApi.CREDIT_URL + creditValue)
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
                                        mTvPromiseValue.setVisibility(View.VISIBLE);
                                        mTvPromiseValue.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mTvPromiseValue.setVisibility(View.GONE);
                                            }
                                        }, 500);
                                        UserService.getInstance().changeCreditValue(creditValue);
                                    } else {
                                        RxToast.error(result.getMsg());
                                    }
                                }
                            }
                        });


                break;
        }
    }

}
