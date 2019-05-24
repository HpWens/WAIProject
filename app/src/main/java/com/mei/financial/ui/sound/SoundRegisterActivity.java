package com.mei.financial.ui.sound;

import android.Manifest;

import com.mei.financial.R;
import com.meis.base.mei.base.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class SoundRegisterActivity extends BaseActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        new RxPermissions(mContext).request(new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS})
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {

                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.sound_register_activity;
    }
}
