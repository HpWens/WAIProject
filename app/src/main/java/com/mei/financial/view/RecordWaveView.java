package com.mei.financial.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mei.financial.R;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/6/10
 */
public class RecordWaveView extends FrameLayout {

    private ImageView ivWave1;
    private ImageView ivWave2;
    private ImageView ivWave3;

    private ImageView ivPlay;

    private ValueAnimator waveAnimator1;
    private ValueAnimator waveAnimator2;
    private ValueAnimator waveAnimator3;

    private static final int DURATION = 600;
    private static final float MAX_WAVE_SCALE = 1F;

    public RecordWaveView(Context context) {
        this(context, null);
    }

    public RecordWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.record_wave_view, this);
        initView();
    }

    private void initView() {
        ivWave1 = findViewById(R.id.iv_wave1);
        ivWave2 = findViewById(R.id.iv_wave2);
        ivWave3 = findViewById(R.id.iv_wave3);
        ivPlay = findViewById(R.id.iv_play);
    }

    public void startWaveAnimator() {
        if (waveAnimator1 != null && waveAnimator1.isRunning()) {
            waveAnimator1.cancel();
        }
        waveAnimator1 = ValueAnimator.ofFloat(0, 1.0F);
        waveAnimator1.setDuration(DURATION * 3);
        waveAnimator1.setRepeatCount(-1);
        waveAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ivWave1.setScaleX(1.0F + MAX_WAVE_SCALE * value);
                ivWave1.setScaleY(1.0F + MAX_WAVE_SCALE * value);
                ivWave1.setAlpha(1.0F - 0.9F * value);
            }
        });
        waveAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                ValueAnimator animator = ValueAnimator.ofFloat(1F, 1.05F, 1.0F);
                animator.setDuration(DURATION * 2);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        ivPlay.setScaleX(value);
                        ivPlay.setScaleY(value);
                    }
                });
                animator.start();
            }
        });
        waveAnimator1.start();

        startWave2Animator();

        startWave3Animator();
    }

    private void startWave2Animator() {
        if (waveAnimator2 != null && waveAnimator2.isRunning()) {
            waveAnimator2.cancel();
        }
        waveAnimator2 = ValueAnimator.ofFloat(0, 1.0F);
        waveAnimator2.setDuration(DURATION * 3);
        waveAnimator2.setRepeatCount(-1);
        waveAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ivWave2.setScaleX(1.0F + MAX_WAVE_SCALE * value);
                ivWave2.setScaleY(1.0F + MAX_WAVE_SCALE * value);
                ivWave2.setAlpha(1.0F - 0.9F * value);
            }
        });
        waveAnimator2.setStartDelay(DURATION);
        waveAnimator2.start();
    }

    private void startWave3Animator() {
        if (waveAnimator3 != null && waveAnimator3.isRunning()) {
            waveAnimator3.cancel();
        }
        waveAnimator3 = ValueAnimator.ofFloat(0, 1.0F);
        waveAnimator3.setDuration(DURATION * 3);
        waveAnimator3.setRepeatCount(-1);
        waveAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ivWave3.setScaleX(1.0F + MAX_WAVE_SCALE * value);
                ivWave3.setScaleY(1.0F + MAX_WAVE_SCALE * value);
                ivWave3.setAlpha(1.0F - 0.9F * value);
            }
        });
        waveAnimator3.setStartDelay(DURATION * 2);
        waveAnimator3.start();
    }

    public void stopAnimator() {
        if (waveAnimator1 != null && waveAnimator1.isRunning()) {
            waveAnimator1.cancel();
            waveAnimator1 = null;
        }
        if (waveAnimator2 != null) {
            waveAnimator2.cancel();
            waveAnimator2 = null;
        }
        if (waveAnimator3 != null) {
            waveAnimator3.cancel();
            waveAnimator3 = null;
        }

        ivWave1.setScaleX(1.0F);
        ivWave1.setScaleY(1.0F);

        ivWave2.setScaleX(1.0F);
        ivWave2.setScaleY(1.0F);

        ivWave3.setScaleX(1.0F);
        ivWave3.setScaleY(1.0F);

        ivPlay.setScaleX(1.0F);
        ivPlay.setScaleY(1.0F);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimator();
    }
}
