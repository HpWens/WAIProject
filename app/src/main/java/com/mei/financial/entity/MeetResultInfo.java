package com.mei.financial.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/6/11
 */
public class MeetResultInfo implements Serializable {

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

    public int index;

    public MeetResultInfo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeetResultInfo that = (MeetResultInfo) o;

        if (speaker_id != that.speaker_id) return false;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        int result = speaker_id;
        result = 31 * result + index;
        return result;
    }
}
