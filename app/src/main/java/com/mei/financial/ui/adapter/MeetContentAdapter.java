package com.mei.financial.ui.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mei.financial.R;
import com.mei.financial.entity.MeetContentInfo;
import com.mei.financial.entity.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/30
 */
public class MeetContentAdapter extends BaseQuickAdapter<MeetContentInfo, BaseViewHolder> {

    private int userId = -1;

    public MeetContentAdapter() {
        super(R.layout.item_meet_content, new ArrayList<MeetContentInfo>());
        userId = UserService.getInstance().getUserInfo().id;
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetContentInfo item) {
        helper.setText(R.id.tv_name, (item.userId == userId ? "当前用户" : item.speaker_name) + "   " + convertCalendar(item.create_time))
                .setText(R.id.tv_content, item.context)
                .setTextColor(R.id.tv_name, !item.pass ? Color.parseColor("#EF8C66") : Color.parseColor("#4BC9FC"));
    }

    public String convertCalendar(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String date = sdf.format(time * 1000);
            return date;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

}
