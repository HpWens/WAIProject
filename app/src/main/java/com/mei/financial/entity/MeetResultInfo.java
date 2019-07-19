package com.mei.financial.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/6/11
 */
public class MeetResultInfo {

    public List<MeetContentInfo> chat_record = new ArrayList<>();
    public int count = 0;

    public int id = 0;
    public int session_id = 0;
    public String speaker_name = "";
    public int speaker_id = 0;
    public String context = "";
    public long create_time = 0;
    public String task_id = "";
    public boolean pass;

    public MeetResultInfo() {
    }
}
