package com.mei.financial.ui;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mei.financial.R;
import com.meis.base.mei.base.BaseActivity;
import com.mob.tools.utils.ResHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.view.RxToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import io.reactivex.functions.Consumer;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.bt_verify)
    Button mBtVerify;
    @BindView(R.id.bt_save)
    Button mBtSave;
    @BindView(R.id.et_verify)
    EditText mEtVerify;

    int mCountdown = 60;

    private EventHandler mEventHandler;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mCountdown < 0) {
                mBtVerify.setText("发送验证码");
                mBtVerify.setClickable(true);
                mCountdown = 60;
            } else {
                mBtVerify.setClickable(false);
                mBtVerify.setText(mCountdown + " s 后重新发送");
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

    @Override
    protected int layoutResId() {
        return R.layout.forget_password_activity;
    }

    @OnClick({R.id.bt_verify, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_verify:
                new RxPermissions(mContext).request(new String[]{
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            SMSSDK.getVerificationCode("86", "15985841233");
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                });
                break;
            case R.id.bt_save:
                // 提交验证码
                String verify = mEtVerify.getText().toString();
                SMSSDK.submitVerificationCode("86", "15985841233", verify);
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


}
