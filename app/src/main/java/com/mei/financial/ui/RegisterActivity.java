package com.mei.financial.ui;

import android.support.v4.widget.NestedScrollView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.mei.financial.R;
import com.meis.base.mei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;
    @BindView(R.id.root)
    RelativeLayout mRoot;
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
    @BindView(R.id.cb_item)
    CheckBox mCbItem;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.register_activity;
    }
}
