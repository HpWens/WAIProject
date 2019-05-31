package com.mei.financial.entity;

import java.util.List;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/31
 */
public class SoundInfo {

    public SoundInfo() {
    }

    public String session_id;
    public List<String> text;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SoundInfo{" +
                "session_id='" + session_id + '\'' +
                ", text=" + text +
                '}';
    }
}
