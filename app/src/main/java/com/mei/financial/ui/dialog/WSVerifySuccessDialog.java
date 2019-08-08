package com.mei.financial.ui.dialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mei.financial.R;
import com.vondear.rxui.view.dialog.RxDialog;

/**
 * desc:
 * author: wens
 * date: 2019/8/6.
 */
public class WSVerifySuccessDialog extends RxDialog {

    private OnPositiveListener mListener;
    private ImageView mIvSuccess;
    private ImageView mIvSuccessDigital;

    public WSVerifySuccessDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_verify_success, null);
        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClick(WSVerifySuccessDialog.this);
                }
            }
        });
        mIvSuccessDigital = view.findViewById(R.id.iv_success_digital);
        mIvSuccess = view.findViewById(R.id.iv_success);
        setContentView(view);
    }

    @Override
    public void show() {
        super.show();
        if (mIvSuccess != null && mIvSuccessDigital != null) {
            Glide.with(getContext()).load(R.mipmap.ic_credit_add_digital)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    if (resource instanceof GifDrawable) {
                        //加载一次
                        ((GifDrawable) resource).setLoopCount(1);
                    }
                    return false;
                }
            }).into(mIvSuccessDigital);

            Glide.with(getContext()).load(R.mipmap.ic_credit_add_25).into(mIvSuccess);
        }
    }

    public WSVerifySuccessDialog(Context context, OnPositiveListener listener) {
        this(context, R.style.register_dialog);
        this.mListener = listener;
    }

    public interface OnPositiveListener {
        void onClick(WSVerifySuccessDialog dialog);
    }

    public void setOnPositiveListener(OnPositiveListener listener) {
        this.mListener = listener;
    }
}