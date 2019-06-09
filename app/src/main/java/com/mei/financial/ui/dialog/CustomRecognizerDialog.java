package com.mei.financial.ui.dialog;

import android.content.Context;
import android.view.View;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.ui.RecognizerDialog;

/**
 * desc:
 * author: wens
 * date: 2019/6/9.
 */
public class CustomRecognizerDialog extends RecognizerDialog {

    public CustomRecognizerDialog(Context context, InitListener initListener) {
        super(context, initListener);
    }

    public View getCustomView() {
        return a;
    }

}
