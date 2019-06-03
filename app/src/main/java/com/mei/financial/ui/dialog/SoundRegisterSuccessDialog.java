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

    private OnPositiveListener mListener;

    public SoundRegisterSuccessDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);

        View view = LayoutInflater.from(mContext).inflate(R.layout.sound_register_success_dialog, null);
        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClick(SoundRegisterSuccessDialog.this);
                }
            }
        });
        setContentView(view);
    }

    public SoundRegisterSuccessDialog(Context context, OnPositiveListener listener) {
        this(context, R.style.register_dialog);
        this.mListener = listener;
    }

    public interface OnPositiveListener {
        void onClick(SoundRegisterSuccessDialog dialog);
    }

    public void setOnPositiveListener(OnPositiveListener listener) {
        this.mListener = listener;
    }
}
