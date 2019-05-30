package com.mei.financial.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mei.financial.R;
import com.mei.financial.entity.MeetContentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/30
 */
public class MeetContentAdapter extends BaseQuickAdapter<MeetContentBean, BaseViewHolder> {

    public MeetContentAdapter() {
        super(R.layout.item_meet_content, new ArrayList<MeetContentBean>());
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetContentBean item) {
        helper.setText(R.id.tv_name,item.name)
                .setText(R.id.tv_content,item.content);
    }
}
