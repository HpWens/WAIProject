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
public class SoundRegisterFailureDialog extends RxDialog {

    private OnPositiveListener mListener;

    public SoundRegisterFailureDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);

        View view = LayoutInflater.from(mContext).inflate(R.layout.sound_register_failure_dialog, null);
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(SoundRegisterFailureDialog.this);
                }
            }
        });
        setContentView(view);
    }

    public SoundRegisterFailureDialog(Context context, OnPositiveListener listener) {
        this(context, R.style.register_dialog);
        this.mListener = listener;
    }

    public interface OnPositiveListener {
        void onClick(SoundRegisterFailureDialog dialog);
    }

    public void setOnPositiveListener(OnPositiveListener listener) {
        this.mListener = listener;
    }
}
