package com.mei.financial.ui;

import android.content.Intent;
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

import com.mei.financial.R;
import com.mei.financial.common.Constant;
import com.mei.financial.common.UrlApi;
import com.mei.financial.entity.ApiResult;
import com.mei.financial.entity.UserInfo;
import com.mei.financial.ui.dialog.RegisterSuccessDialog;
import com.mei.financial.utils.ACache;
import com.mei.financial.utils.StringUtils;
import com.mei.financial.view.RxCaptcha2;
import com.meis.base.mei.base.BaseActivity;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxEncodeTool;
import com.vondear.rxtool.RxRegTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.RxCache;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */

public class LoginActivity extends BaseActivity {

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

    private static final int REGISTER_REQUEST_CODE = 0X03;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        mEtMobile.addTextChangedListener(new TextWatcher() {
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
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

        getVerifyCode();

        fillMobilePasswordData();
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

    @OnClick({R.id.iv_clean_phone, R.id.clean_password, R.id.iv_show_pwd,
            R.id.iv_code, R.id.btn_login, R.id.register, R.id.forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_phone:
                mEtMobile.setText("");
                break;
            case R.id.clean_password:
                mEtPassword.setText("");
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
                String mobile = mEtMobile.getText().toString();
                String password = mEtPassword.getText().toString();
                if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
                    RxToast.error(getString(R.string.account_password_no_empty));
                    return;
                }
                if (!RxRegTool.isMobile(mobile)) {
                    RxToast.error(getString(R.string.correct_phone_number));
                    return;
                }
                if (password.length() < 6 || password.length() > 16) {
                    RxToast.error(getString(R.string.input_password_6_16));
                    return;
                }

                String code = mEtVerifyCode.getText().toString();
                if (StringUtils.isEmpty(code)) {
                    RxToast.error(getString(R.string.verify_code_no_empty));
                    return;
                }

                if (!RxCaptcha2.build().getCode().equals(code.toLowerCase())) {
                    getVerifyCode();
                    RxToast.error(getString(R.string.verify_code_input_error));
                    return;
                }

                // RxEncodeTool.base64Encode2String
                RxSPTool.putString(mContext, Constant.LOGIN_SAVE_ACCOUNT, mCbAccount.isChecked() ? mobile : "");

                RxSPTool.putString(mContext, Constant.LOGIN_SAVE_PASSWORD,
                        mCbPassword.isChecked() ? RxEncodeTool.base64Encode2String(password.getBytes()) : "");
                // 发起网络请求
                EasyHttp.post(UrlApi.USER_LOGIN)
                        .params("account", mobile)
                        .params("password", password)
                        .execute(new SimpleCallBack<ApiResult<UserInfo>>() {

                            @Override
                            public void onError(ApiException e) {

                            }

                            @Override
                            public void onSuccess(ApiResult<UserInfo> userInfoApiResult) {
                                ACache.get(mContext).put(Constant.USER_INFO, userInfoApiResult.getData());
                            }
                        });

                startActivity(new Intent(mContext, HomeActivity.class));
                break;
            case R.id.register:
                startActivityForResult(new Intent(mContext, RegisterActivity.class), REGISTER_REQUEST_CODE);
                break;
            case R.id.forget_password:
                startActivity(new Intent(mContext, ForgetPasswordActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_REQUEST_CODE && resultCode == Constant.RESULT_OK) {
            fillMobilePasswordData();
        }
    }

    private void getVerifyCode() {
        int width = DensityUtil.dp2px(100);
        int height = DensityUtil.dp2px(35);
        RxCaptcha2.build()
                .backColor(0xffffff)
                .codeLength(5)
                .fontSize(48)
                .lineNumber(0)
                .size(width, height)
                .type(RxCaptcha2.TYPE.CHARS)
                .into(mIvVerify);
    }
}
