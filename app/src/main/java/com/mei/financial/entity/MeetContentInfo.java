package com.mei.financial.entity;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/30
 */
public class MeetContentInfo {

    public String speaker_name = "";
    public String context = "";
    public long create_time = 0;
    public int userId = 0;
    public String task_id = "";
    public boolean pass = false;
    public int index;
    public int speaker_id = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeetContentInfo that = (MeetContentInfo) o;

        if (index != that.index) return false;
        if (speaker_id != that.speaker_id) return false;
        return speaker_name != null ? speaker_name.equals(that.speaker_name) : that.speaker_name == null;
    }

    @Override
    public int hashCode() {
        int result = speaker_name != null ? speaker_name.hashCode() : 0;
        result = 31 * result + index;
        result = 31 * result + speaker_id;
        return result;
    }

    public MeetContentInfo() {
    }
}
