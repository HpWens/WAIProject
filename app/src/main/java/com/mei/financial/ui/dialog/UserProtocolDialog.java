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
public class UserProtocolDialog extends RxDialog {

    public UserProtocolDialog(Context context, int themeResId) {
        super(context, themeResId);

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_protocol_dialog, null);
        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);
    }

    public UserProtocolDialog(Context context) {
        this(context, R.style.register_dialog);
    }
}
