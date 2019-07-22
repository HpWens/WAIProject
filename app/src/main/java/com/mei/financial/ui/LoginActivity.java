package com.mei.financial.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mei.financial.MyApplication;
import com.mei.financial.R;
import com.mei.financial.common.Constant;
import com.mei.financial.common.UrlApi;
import com.mei.financial.entity.ParameterizedTypeImpl;
import com.mei.financial.entity.UserInfo;
import com.mei.financial.entity.UserService;
import com.mei.financial.utils.StringUtils;
import com.mei.financial.view.RxCaptcha2;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.utils.Eyes;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxEncodeTool;
import com.vondear.rxtool.RxNetTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.jessyan.autosize.internal.CancelAdapt;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */

public class LoginActivity extends BaseActivity implements CancelAdapt {

    @BindView(R.id.logo)
    ImageView mLogo;
    @BindView(R.id.et_mobile)
    EditText mEtMobile;
    @BindView(R.id.iv_clean_phone)
    ImageView mIvCleanPhone;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.clean_password)
    ImageView mCleanPassword;
    @BindView(R.id.iv_show_pwd)
    ImageView mIvShowPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.register)
    TextView mRegister;
    @BindView(R.id.forget_password)
    TextView mForgetPassword;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;
    @BindView(R.id.service)
    LinearLayout mService;
    @BindView(R.id.root)
    RelativeLayout mRoot;
    @BindView(R.id.iv_code)
    ImageView mIvVerify;
    @BindView(R.id.et_verify_code)
    EditText mEtVerifyCode;
    @BindView(R.id.cb_account)
    CheckBox mCbAccount;
    @BindView(R.id.cb_password)
    CheckBox mCbPassword;
    @BindView(R.id.layout_verify)
    LinearLayout mLayoutVerify;
    @BindView(R.id.tv_account)
    TextView mTvAccount;
    @BindView(R.id.tv_password)
    TextView mTvPassword;
    @BindView(R.id.tv_error_hint)
    TextView mTvErrorHint;
    @BindView(R.id.tv_verify)
    TextView mTvVerify;
    @BindView(R.id.clean_verify)
    ImageView mCleanVerify;

    private static final int REGISTER_REQUEST_CODE = 0X03;
    private static final int FORGET_PASSWORD_REQUEST_CODE = 0X06;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.translucentStatusBar(mContext, true);

        mEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTvErrorHint.setVisibility(View.INVISIBLE);
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

        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTvErrorHint.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mCleanPassword.getVisibility() == View.GONE) {
                    mCleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mCleanPassword.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty()) {
                    return;
                }
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(mContext, "请输入数字或字母", Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    mEtPassword.setSelection(s.length());
                }
            }
        });

        mEtVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTvErrorHint.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mCleanVerify.getVisibility() == View.GONE) {
                    mCleanVerify.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mCleanVerify.setVisibility(View.GONE);
                }
            }
        });

        getVerifyCode();

        fillMobilePasswordData();

        mRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        mRegister.getPaint().setAntiAlias(true); // 抗锯齿

        setLogoRes();
    }

    private void setLogoRes() {
        int res = R.mipmap.login_header_ic;
        int flavorsCode = ((MyApplication) getApplication()).getFlavorsCode();
        if (flavorsCode == 1) {
            res = R.mipmap.login_header_ic;
        } else if (flavorsCode == 2) {
            res = R.mipmap.login_header_ic2;
        }
        mLogo.setImageResource(res);
    }

    private void fillMobilePasswordData() {
        String account = RxSPTool.getString(mContext, Constant.LOGIN_SAVE_ACCOUNT);
        if (!RxDataTool.isEmpty(account)) {
            mEtMobile.setText(account);
            mEtMobile.setSelection(account.length());
            mCbAccount.setChecked(true);
        }

        String password = RxSPTool.getString(mContext, Constant.LOGIN_SAVE_PASSWORD);
        if (!RxDataTool.isEmpty(password)) {
            mEtPassword.setText(new String(RxEncodeTool.base64Decode(password)));
            mCbPassword.setChecked(true);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.login_activity;
    }

    @OnClick({R.id.iv_clean_phone, R.id.clean_password, R.id.iv_show_pwd, R.id.clean_verify,
            R.id.iv_code, R.id.btn_login, R.id.register, R.id.forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_phone:
                mEtMobile.setText("");
                break;
            case R.id.clean_password:
                mEtPassword.setText("");
                break;
            case R.id.clean_verify:
                mEtVerifyCode.setText("");
                break;
            case R.id.iv_show_pwd:
                if (mEtPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIvShowPwd.setImageResource(R.drawable.pass_visuable);
                } else {
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIvShowPwd.setImageResource(R.drawable.pass_gone);
                }
                String pwd = mEtPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    mEtPassword.setSelection(pwd.length());
                break;
            case R.id.iv_code:
                getVerifyCode();
                break;
            case R.id.btn_login:
                final String mobile = mEtMobile.getText().toString();
                final String password = mEtPassword.getText().toString();
                if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
                    // RxToast.error(getString(R.string.account_password_no_empty));
                    showErrorHint(getString(R.string.account_password_no_empty));
                    return;
                }

                // if (!RxRegTool.isMobile(mobile)) {
                //     RxToast.error(getString(R.string.correct_phone_number));
                //     return;
                // }

                if (password.length() < 6 || password.length() > 16) {
                    // RxToast.error(getString(R.string.input_password_6_16));
                    showErrorHint(getString(R.string.input_password_6_16));
                    return;
                }

                if (mLayoutVerify.getVisibility() == View.VISIBLE) {
                    String code = mEtVerifyCode.getText().toString();
                    if (StringUtils.isEmpty(code)) {
                        // RxToast.error(getString(R.string.verify_code_no_empty));
                        showErrorHint(getString(R.string.verify_code_no_empty));
                        return;
                    }

                    if (!RxCaptcha2.build().getCode().equals(code.toLowerCase())) {
                        getVerifyCode();
                        // RxToast.error(getString(R.string.verify_code_input_error));
                        showErrorHint(getString(R.string.verify_code_input_error));
                        return;
                    }
                }

                if (!RxNetTool.isAvailable(mContext)) {
                    RxToast.error(getString(R.string.net_connection_error));
                    return;
                }

                final String passwordEncode = RxEncodeTool.base64Encode2String(password.getBytes());

                // 发起网络请求
                EasyHttp.post(UrlApi.USER_LOGIN)
                        .params("account", mobile)
                        .params("password", passwordEncode)
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onError(ApiException e) {
                                if (e.getCode() != ApiException.ERROR.UNKNOWN) {
                                    RxToast.error(e.getMessage());
                                }
                            }

                            @Override
                            public void onSuccess(String s) {
                                Observable.just(s).map(new Function<String, Result<UserInfo>>() {
                                    @Override
                                    public Result<UserInfo> apply(String s) throws Exception {
                                        return new Gson().fromJson(s, new ParameterizedTypeImpl(Result.class, new Class[]{UserInfo.class}));
                                    }
                                }).subscribe(new Consumer<Result<UserInfo>>() {
                                    @Override
                                    public void accept(Result<UserInfo> userInfoResult) throws Exception {
                                        if (userInfoResult.isOk()) {
                                            UserService.getInstance().saveUser(userInfoResult.data);
                                            if (getApplication() instanceof MyApplication) {
                                                ((MyApplication) getApplication()).addEasyTokenHeader();
                                            }

                                            // RxEncodeTool.base64Encode2String
                                            RxSPTool.putString(mContext, Constant.LOGIN_SAVE_ACCOUNT, mCbAccount.isChecked() ? mobile : "");

                                            RxSPTool.putString(mContext, Constant.LOGIN_SAVE_PASSWORD,
                                                    mCbPassword.isChecked() ? passwordEncode : "");

                                            // HomeActivity
                                            startActivity(new Intent(mContext, HomeActivity.class));
                                            finish();
                                        } else {
                                            // RxToast.error(userInfoResult.getMsg());
                                            showErrorHint(userInfoResult.getMsg());
                                            if (null != userInfoResult.getData()) {
                                                if (userInfoResult.getData().need_verify) {
                                                    mLayoutVerify.setVisibility(View.VISIBLE);
                                                    mTvVerify.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                break;
            case R.id.register:

                startActivityForResult(new Intent(mContext, RegisterActivity.class), REGISTER_REQUEST_CODE);
                break;
            case R.id.forget_password:
                startActivityForResult(new Intent(mContext, ForgetPasswordActivity.class), FORGET_PASSWORD_REQUEST_CODE);
                break;
        }
    }


    public void showErrorHint(String error) {
        if (mTvErrorHint.getVisibility() != View.VISIBLE) {
            mTvErrorHint.setVisibility(View.VISIBLE);
        }
        mTvErrorHint.setText(error);
        mTvErrorHint.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvErrorHint.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_REQUEST_CODE && resultCode == Constant.RESULT_OK) {
            fillMobilePasswordData();
        } else if (requestCode == FORGET_PASSWORD_REQUEST_CODE && resultCode == Constant.RESULT_OK) {
            fillMobilePasswordData();
        }
    }

    private void getVerifyCode() {
        int width = DensityUtil.dp2px(68);
        int height = DensityUtil.dp2px(26);
        RxCaptcha2.build()
                .backColor(0xffffff)
                .codeLength(4)
                .fontSize(36)
                .lineNumber(0)
                .size(width, height)
                .type(RxCaptcha2.TYPE.CHARS)
                .into(mIvVerify);
    }
}
