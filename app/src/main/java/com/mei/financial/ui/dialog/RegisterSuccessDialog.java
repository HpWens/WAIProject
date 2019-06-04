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
 * @since 2019/5/31
 */
public class RegisterSuccessDialog extends RxDialog {

    private OnPositiveListener mListener;

    public RegisterSuccessDialog(Context context, int themeResId) {
        super(context, themeResId);

        View view = LayoutInflater.from(mContext).inflate(R.layout.register_success_dialog, null);
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(RegisterSuccessDialog.this);
                }
            }
        });
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(RegisterSuccessDialog.this);
                }
            }
        });
        setContentView(view);
    }

    public RegisterSuccessDialog(Context context, OnPositiveListener listener) {
        this(context, R.style.register_dialog);
        this.mListener = listener;
    }


    public interface OnPositiveListener {
        void onClick(RegisterSuccessDialog dialog);
    }

    public void setOnPositiveListener(OnPositiveListener listener) {
        this.mListener = listener;
    }

}
