package com.mei.financial.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.mei.financial.R;
import com.vondear.rxui.view.dialog.RxDialog;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/29
 */
public class SoundRegisterSuccessDialog extends RxDialog {

    public SoundRegisterSuccessDialog(Context context, int themeResId) {
        super(context, themeResId);

        View view = LayoutInflater.from(mContext).inflate(R.layout.sound_register_success_dialog, null);
        setContentView(view);
    }

    public SoundRegisterSuccessDialog(Context context) {
        this(context, R.style.register_dialog);
    }

}
