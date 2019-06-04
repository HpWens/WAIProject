package com.mei.financial.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mei.financial.R;
import com.mei.financial.ui.adapter.HomeViewPagerAdapter;
import com.mei.financial.ui.scenes.ScenesMainActivity;
import com.mei.financial.ui.sound.SoundRegisterActivity;
import com.mei.financial.ui.sound.SoundVerifyActivity;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CancelAdapt;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/23
 */
public class HomeActivity extends BaseActivity implements CancelAdapt {
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_agency)
    TextView mTvAgency;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.layout_indicator)
    LinearLayout mLayoutIndicator;
    @BindView(R.id.tv_personal)
    TextView mTvPersonal;
    @BindView(R.id.tv_sound_register)
    TextView mTvSoundRegister;
    @BindView(R.id.tv_sound_verify)
    TextView mTvSoundVerify;
    @BindView(R.id.tv_credit)
    TextView mTvCredit;
    @BindView(R.id.tv_scenes)
    TextView mTvScenes;

    private String[] mVpContents1 = {"以声识人", "迅速、快捷", "如闻其声"};
    private String[] mVpContents2 = {"如闻其声", "安全可靠", "如见其人"};
    private List<LinearLayout> mViewPagerLayouts = new ArrayList<>();
    private int indicatorSpacing;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Eyes.translucentStatusBar(mContext, true);
        setTextViewGradient(mTvAgency, getResources().getColor(R.color.color_AFE6FC),
                getResources().getColor(R.color.color_29BFFC));

        // 处理viewpager
        indicatorSpacing = DensityUtil.dp2px(3.5F);
        initViewPagerData();
        mViewPager.setAdapter(new HomeViewPagerAdapter(mViewPagerLayouts));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mLayoutIndicator.getChildCount(); i++) {
                    View child = mLayoutIndicator.getChildAt(i);
                    if (child instanceof ImageView) {
                        ImageView iv = (ImageView) child;
                        iv.setBackgroundResource(position % mLayoutIndicator.getChildCount() == i ?
                                R.mipmap.home_vp_selected_ic : R.mipmap.home_vp_unselected_ic);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // 设置字体渐变
        setTextViewGradient(mTvPersonal, getResources().getColor(R.color.color_AFE6FC), getResources().getColor(R.color.color_29BFFC));
        setTextViewGradient(mTvSoundRegister, getResources().getColor(R.color.color_AFE6FC), getResources().getColor(R.color.color_29BFFC));
        setTextViewGradient(mTvSoundVerify, getResources().getColor(R.color.color_AFE6FC), getResources().getColor(R.color.color_29BFFC));
        setTextViewGradient(mTvCredit, getResources().getColor(R.color.color_AFE6FC), getResources().getColor(R.color.color_29BFFC));
        setTextViewGradient(mTvScenes, getResources().getColor(R.color.color_AFE6FC), getResources().getColor(R.color.color_29BFFC));
    }

    private void initViewPagerData() {
        mViewPagerLayouts.clear();
        mLayoutIndicator.removeAllViews();
        for (int i = 0; i < mVpContents1.length; i++) {
            LinearLayout itemLayout = (LinearLayout) View.inflate(mContext, R.layout.item_home_vp, null);
            TextView topTv = itemLayout.findViewById(R.id.tv_top);
            TextView bottomTv = itemLayout.findViewById(R.id.tv_bottom);
            topTv.setText(mVpContents1[i]);
            bottomTv.setText(mVpContents2[i]);

            setTextViewGradient(topTv, getResources().getColor(R.color.color_2094E2),
                    getResources().getColor(R.color.color_992AF3));
            setTextViewGradient(bottomTv, getResources().getColor(R.color.color_2094E2),
                    getResources().getColor(R.color.color_992AF3));
            mViewPagerLayouts.add(itemLayout);

            ImageView indicator = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            params.leftMargin = params.rightMargin = indicatorSpacing;
            indicator.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            indicator.setBackgroundResource(i == 0 ? R.mipmap.home_vp_selected_ic : R.mipmap.home_vp_unselected_ic);
            mLayoutIndicator.addView(indicator, params);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.home_activity;
    }

    @OnClick({R.id.cv_personal, R.id.cv_sound_register, R.id.cv_sound_verify,
            R.id.cv_credit, R.id.cv_scenes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_personal:
                startActivity(new Intent(mContext, PersonalActivity.class));
                break;
            case R.id.cv_sound_register:
                startActivity(new Intent(mContext, SoundRegisterActivity.class));
                break;
            case R.id.cv_sound_verify:
                startActivity(new Intent(mContext, SoundVerifyActivity.class));
                break;
            case R.id.cv_credit:
                startActivity(new Intent(mContext, CreditActivity.class));
                break;
            case R.id.cv_scenes:
                startActivity(new Intent(mContext, ScenesMainActivity.class));
                break;
        }
    }
}
