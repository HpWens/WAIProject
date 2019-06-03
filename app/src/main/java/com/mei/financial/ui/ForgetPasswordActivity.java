package com.mei.financial.ui;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mei.financial.R;
import com.mei.financial.common.Constant;
import com.mei.financial.common.UrlApi;
import com.mei.financial.entity.ParameterizedTypeImpl;
import com.mei.financial.entity.UserInfo;
import com.mei.financial.utils.StringUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.utils.Eyes;
import com.mob.tools.utils.ResHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxEncodeTool;
import com.vondear.rxtool.RxRegTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class ForgetPasswordActivity extends BaseActivity {


    int mCountdown = 60;
    @BindView(R.id.tv_account)
    TextView mTvAccount;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.iv_clean_account)
    ImageView mIvCleanAccount;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.iv_clean_phone)
    ImageView mIvCleanPhone;
    @BindView(R.id.tv_pre_phone)
    TextView mTvPrePhone;
    @BindView(R.id.et_verify)
    EditText mEtVerify;
    @BindView(R.id.iv_clean_verify)
    ImageView mIvCleanVerify;
    @BindView(R.id.tv_verify)
    TextView mTvVerify;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.iv_clean_password)
    ImageView mIvCleanPassword;
    @BindView(R.id.et_password_again)
    EditText mEtPasswordAgain;
    @BindView(R.id.iv_clean_password_again)
    ImageView mIvCleanPasswordAgain;
    @BindView(R.id.bt_save)
    Button mBtSave;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;
    @BindView(R.id.root)
    RelativeLayout mRoot;

    private EventHandler mEventHandler;
    private String mPrePhone = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mCountdown < 0) {
                mTvVerify.setText("发送验证码");
                mTvVerify.setClickable(true);
                mCountdown = 60;
            } else {
                mTvVerify.setClickable(false);
                mTvVerify.setText(mCountdown + " s 后重新发送");
                mCountdown--;
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.translucentStatusBar(mContext, true);

        getEventHandler();

        setListener();

    }


    @Override
    protected int layoutResId() {
        return R.layout.forget_password_activity;
    }

    @OnClick({R.id.tv_verify, R.id.bt_save, R.id.iv_clean_account, R.id.iv_clean_phone,
            R.id.iv_clean_verify, R.id.iv_clean_password, R.id.iv_clean_password_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_verify:

                final String phone = mEtPhone.getText().toString();

                if (!RxRegTool.isMobile(phone)) {
                    RxToast.error(getString(R.string.error_phone_hint));
                    return;
                }

                if (!phone.equals(mPrePhone)) {
                    RxToast.error(getString(R.string.identical_pre_phone));
                    return;
                }

                new RxPermissions(mContext).request(new String[]{
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            SMSSDK.getVerificationCode("86", phone);
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                });
                break;
            case R.id.bt_save:
                // 提交验证码
                final String phoneNum = mEtPhone.getText().toString();
                final String account = mEtAccount.getText().toString();
                final String verify = mEtVerify.getText().toString();
                final String password = mEtPassword.getText().toString();
                final String passwordAgain = mEtPasswordAgain.getText().toString();

                if (StringUtils.isEmpty(account)) {
                    RxToast.error(getString(R.string.account_no_empty));
                    return;
                }

                if (StringUtils.isEmpty(password) || StringUtils.isEmpty(passwordAgain)) {
                    RxToast.error(getString(R.string.password_no_empty));
                    return;
                }

                if (StringUtils.isEmpty(verify)) {
                    RxToast.error(getString(R.string.verify_code_no_empty));
                    return;
                }

                if (!RxRegTool.isMobile(phoneNum)) {
                    RxToast.error(getString(R.string.error_phone_hint));
                    return;
                }

                if (!phoneNum.equals(mPrePhone)) {
                    RxToast.error(getString(R.string.identical_pre_phone));
                    return;
                }

                if (!password.equals(passwordAgain)) {
                    RxToast.error(getString(R.string.password_compare_correct));
                    return;
                }

                SMSSDK.submitVerificationCode("86", phoneNum, verify);
                break;
            case R.id.iv_clean_account:
                mEtAccount.setText("");
                break;
            case R.id.iv_clean_phone:
                mEtPhone.setText("");
                break;
            case R.id.iv_clean_verify:
                mEtVerify.setText("");
                break;
            case R.id.iv_clean_password:
                mEtPassword.setText("");
                break;
            case R.id.iv_clean_password_again:
                mEtPasswordAgain.setText("");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(mEventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mEventHandler);
    }

    private void getEventHandler() {
        mEventHandler = new EventHandler() {
            @Override
            public void afterEvent(final int event, final int result,
                                   final Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 主线程中执行
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 发送验证码 后续业务处理
                            if (result == SMSSDK.RESULT_COMPLETE) {

                            } else {
                                int status = 0;
                                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE
                                        && data != null
                                        && (data instanceof UserInterruptException)) {
                                    // 由于此处是开发者自己决定要中断发送的，因此什么都不用做
                                    return;
                                }

                                // 根据服务器返回的网络错误，给toast提示
                                try {
                                    ((Throwable) data).printStackTrace();
                                    Throwable throwable = (Throwable) data;

                                    JSONObject object = new JSONObject(
                                            throwable.getMessage());
                                    String des = object.optString("detail");
                                    status = object.optInt("status");
                                    RxToast.normal(des);
                                } catch (Exception e) {
                                }

                                // 如果木有找到资源，默认提示
                                int resId = 0;
                                if (status >= 400) {
                                    resId = ResHelper.getStringRes(mContext, "smssdk_error_desc_" + status);
                                } else {
                                    resId = ResHelper.getStringRes(mContext,
                                            "smssdk_network_error");
                                }
                                if (resId > 0) {
                                    Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            /** 提交验证码 */
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // 请求接口 保存密码
                                commitSavePassword();
                            } else {
                                ((Throwable) data).printStackTrace();
                                // 验证码不正确
                                String message = ((Throwable) data).getMessage();
                                int resId = 0;
                                try {
                                    JSONObject json = new JSONObject(message);
                                    int status = json.getInt("status");
                                    resId = ResHelper.getStringRes(mContext,
                                            "smssdk_error_detail_" + status);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (resId == 0) {
                                    resId = ResHelper.getStringRes(mContext, "smssdk_virificaition_code_wrong");
                                }
                                if (resId > 0) {
                                    Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
                                }
                            }

                        } else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                            /** 获取语音版验证码成功后的执行动作 */

                        }

                    }
                });
            }
        };
    }

    // 请求接口
    private void commitSavePassword() {
        final String password = mEtPassword.getText().toString();
        final String account = mEtAccount.getText().toString();
        final String passwordEncode = RxEncodeTool.base64Encode2String(password.getBytes());

        EasyHttp.put(UrlApi.CHANGE_PASSWORD)
                .params("account", account)
                .params("password", passwordEncode)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        RxToast.error(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!StringUtils.isEmpty(s)) {
                            ApiResult apiResult = new Gson().fromJson(s, new TypeToken<ApiResult>() {
                            }.getType());
                            if (apiResult.isOk()) {
                                RxToast.normal(getString(R.string.change_success));
                                RxSPTool.putString(mContext, Constant.LOGIN_SAVE_PASSWORD, passwordEncode);
                                setResult(Constant.RESULT_OK);
                                finish();
                            } else {
                                RxToast.error(apiResult.getMsg());
                            }
                        }
                    }
                });
    }

    public void setListener() {

        mEtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mIvCleanAccount.getVisibility() == View.GONE) {
                    mIvCleanAccount.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mIvCleanAccount.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(s)) {
                    EasyHttp.get(UrlApi.GET_PRE_PHONE)
                            .params("account", s.toString())
                            .execute(new SimpleCallBack<String>() {
                                @Override
                                public void onError(ApiException e) {
                                    RxToast.error(e.getMessage());
                                }

                                @Override
                                public void onSuccess(String s) {
                                    handlerPerPhone(s);
                                }
                            });
                }
            }
        });

        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mIvCleanPhone.getVisibility() == View.GONE) {
                    mIvCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mIvCleanPhone.setVisibility(View.GONE);
                }
            }
        });

        mEtVerify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mIvCleanVerify.getVisibility() == View.GONE) {
                    mIvCleanVerify.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mIvCleanVerify.setVisibility(View.GONE);
                }
            }
        });

        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mIvCleanPassword.getVisibility() == View.GONE) {
                    mIvCleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mIvCleanPassword.setVisibility(View.GONE);
                }
            }
        });

        mEtPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mIvCleanPasswordAgain.getVisibility() == View.GONE) {
                    mIvCleanPasswordAgain.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mIvCleanPasswordAgain.setVisibility(View.GONE);
                }
            }
        });

    }

    private void handlerPerPhone(String s) {
        Observable.just(s).map(new Function<String, Result<UserInfo>>() {
            @Override
            public Result<UserInfo> apply(String s) throws Exception {
                return new Gson().fromJson(s, new ParameterizedTypeImpl(Result.class, new Class[]{UserInfo.class}));
            }
        }).subscribe(new Consumer<Result<UserInfo>>() {
            @Override
            public void accept(Result<UserInfo> userInfoResult) throws Exception {
                if (userInfoResult.isOk() && null != userInfoResult.getData()) {
                    mPrePhone = userInfoResult.getData().phone_number;
                    mTvPrePhone.setVisibility(View.VISIBLE);
                    mTvPrePhone.setText("预留号码：" + StringUtils.getFormatPrePhone(mPrePhone));
                } else {
                    mPrePhone = "";
                    RxToast.error(userInfoResult.getMsg());
                    mTvPrePhone.setVisibility(View.GONE);
                }
            }
        });
    }


}
