package com.mei.financial.ui;

import android.graphics.Paint;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mei.financial.R;
import com.mei.financial.common.Constant;
import com.mei.financial.common.UrlApi;
import com.mei.financial.ui.dialog.RegisterSuccessDialog;
import com.mei.financial.ui.dialog.UserProtocolDialog;
import com.mei.financial.utils.StringUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxEncodeTool;
import com.vondear.rxtool.RxNetTool;
import com.vondear.rxtool.RxRegTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CancelAdapt;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class RegisterActivity extends BaseActivity implements CancelAdapt {

    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.rb_man)
    RadioButton mRbMan;
    @BindView(R.id.rb_woman)
    RadioButton mRbWoman;
    @BindView(R.id.btn_register)
    Button mBtnRegister;
    @BindView(R.id.cb_item)
    CheckBox mCbItem;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;
    @BindView(R.id.root)
    RelativeLayout mRoot;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.translucentStatusBar(mContext, true);

        mCbItem.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        mCbItem.getPaint().setAntiAlias(true); // 抗锯齿

        mCbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    new UserProtocolDialog(mContext).show();
                }
            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.register_activity;
    }


    @OnClick({R.id.cb_item, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                final String phone = mEtPhone.getText().toString();
                final String password = mEtPassword.getText().toString();
                final String name = mEtName.getText().toString();
                final String account = mEtAccount.getText().toString();

                if (StringUtils.isEmpty(account)) {
                    RxToast.error(getString(R.string.account_no_empty));
                    return;
                }

                if (StringUtils.isEmpty(phone)) {
                    RxToast.error(getString(R.string.phone_no_empty));
                    return;
                }

                if (StringUtils.isEmpty(password)) {
                    RxToast.error(getString(R.string.password_no_empty));
                    return;
                }

                if (!RxRegTool.isMobile(phone)) {
                    RxToast.error(getString(R.string.correct_phone_number));
                    return;
                }

                if (password.length() < 6 || password.length() > 16) {
                    RxToast.error(getString(R.string.input_password_6_16));
                    return;
                }

                if (name.length() < 2 || name.length() > 10) {
                    RxToast.error(getString(R.string.name_limit_hint));
                    return;
                }

                // 1 女 2 男
                String sex = mRbMan.isChecked() ? "2" : "1";

                if (!mCbItem.isChecked()) {
                    RxToast.error(getString(R.string.hook_note_item));
                    return;
                }

                if (!RxNetTool.isAvailable(mContext)) {
                    RxToast.error(getString(R.string.net_connection_error));
                    return;
                }

                final String encodePassword = RxEncodeTool.base64Encode2String(password.getBytes());

                EasyHttp.post(UrlApi.USER_REGISTER)
                        .params("account", account)
                        .params("password", encodePassword)
                        .params("phone_number", phone)
                        .params("sex", sex)
                        .params("name", name)
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onError(ApiException e) {
                                if (e.getCode() != ApiException.ERROR.UNKNOWN) {
                                    RxToast.error(e.getMessage());
                                }
                            }

                            @Override
                            public void onSuccess(String s) {
                                if (!StringUtils.isEmpty(s)) {
                                    ApiResult apiResult = new Gson().fromJson(s, new TypeToken<ApiResult>() {
                                    }.getType());
                                    if (apiResult.isOk()) {
                                        // 保存账号密码
                                        RxSPTool.putString(mContext, Constant.LOGIN_SAVE_ACCOUNT, account);
                                        RxSPTool.putString(mContext, Constant.LOGIN_SAVE_PASSWORD, encodePassword);

                                        new RegisterSuccessDialog(mContext, new RegisterSuccessDialog.OnPositiveListener() {
                                            @Override
                                            public void onClick(RegisterSuccessDialog dialog) {
                                                dialog.dismiss();
                                                setResult(Constant.RESULT_OK);
                                                finish();
                                            }
                                        }).show();


                                    } else {
                                        RxToast.error(apiResult.getMsg());
                                    }
                                }
                            }
                        });

                break;
        }
    }
}
