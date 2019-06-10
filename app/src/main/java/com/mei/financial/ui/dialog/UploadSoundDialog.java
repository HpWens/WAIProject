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
 * @since 2019/6/10
 */
public class UploadSoundDialog extends RxDialog {

    public UploadSoundDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.upload_sound_dialog, null);
        setContentView(view);
    }

    public UploadSoundDialog(Context context) {
        this(context, R.style.register_dialog);
    }
}
