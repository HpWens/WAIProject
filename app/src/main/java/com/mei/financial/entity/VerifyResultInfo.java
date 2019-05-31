package com.mei.financial.entity;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/31
 */
public class VerifyResultInfo {

    public String asr_text;
    public boolean asr_result;
    public boolean replay_result;
    public int replay_score;
    public boolean noise_result;
    public int noise_score;
    public String voiceprint_id;
    public String voice_group_id;
    public int upload_count;

    public String getAsr_text() {
        return asr_text;
    }

    public void setAsr_text(String asr_text) {
        this.asr_text = asr_text;
    }

    public boolean isAsr_result() {
        return asr_result;
    }

    public void setAsr_result(boolean asr_result) {
        this.asr_result = asr_result;
    }

    public boolean isReplay_result() {
        return replay_result;
    }

    public void setReplay_result(boolean replay_result) {
        this.replay_result = replay_result;
    }

    public int getReplay_score() {
        return replay_score;
    }

    public void setReplay_score(int replay_score) {
        this.replay_score = replay_score;
    }

    public boolean isNoise_result() {
        return noise_result;
    }

    public void setNoise_result(boolean noise_result) {
        this.noise_result = noise_result;
    }

    public int getNoise_score() {
        return noise_score;
    }

    public void setNoise_score(int noise_score) {
        this.noise_score = noise_score;
    }

    public String getVoiceprint_id() {
        return voiceprint_id;
    }

    public void setVoiceprint_id(String voiceprint_id) {
        this.voiceprint_id = voiceprint_id;
    }

    public String getVoice_group_id() {
        return voice_group_id;
    }

    public void setVoice_group_id(String voice_group_id) {
        this.voice_group_id = voice_group_id;
    }

    public int getUpload_count() {
        return upload_count;
    }

    public void setUpload_count(int upload_count) {
        this.upload_count = upload_count;
    }

    @Override
    public String toString() {
        return "VerifyResultInfo{" +
                "asr_text='" + asr_text + '\'' +
                ", asr_result=" + asr_result +
                ", replay_result=" + replay_result +
                ", replay_score=" + replay_score +
                ", noise_result=" + noise_result +
                ", noise_score=" + noise_score +
                ", voiceprint_id='" + voiceprint_id + '\'' +
                ", voice_group_id='" + voice_group_id + '\'' +
                ", upload_count=" + upload_count +
                '}';
    }
}
