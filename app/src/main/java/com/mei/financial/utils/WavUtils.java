package com.mei.financial.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/7/17
 */
public class WavUtils {
    private boolean isRecording = false;
    private int sampleRate = 16000;
    private boolean canRecord = true;

    private int dataLength = 0;
    private static int FILE_MAX_LENGTH = 8000;
    private OnRecordListener listener;

    private int fileNameIndex = 0;

    public WavUtils(OnRecordListener listener) {
        this.listener = listener;
    }


    public void stopRecord() {
        canRecord = true;
        isRecording = false;
    }

    private void deleteFile(String path) {
        try {
            File file = new File(path);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRecord() {
        if (!canRecord) {
            return;
        }
        dataLength = 0;
        canRecord = false;

        final int BUFFER_SIZE = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        final AudioRecord[] audioRecord = new AudioRecord[1];
        isRecording = true;

        String recordFilePath = Environment.getExternalStorageDirectory() + "/msc/mei" + fileNameIndex + ".wav";
        Observable.just(recordFilePath)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        WavWriter wavWriter = null;
                        String recordPath = null;

                        try {
                            recordPath = new File(s).getAbsolutePath();
                            wavWriter = new WavWriter(recordPath, 1, sampleRate, AudioFormat.ENCODING_PCM_16BIT);
                            audioRecord[0] = new AudioRecord(MediaRecorder.AudioSource.MIC,
                                    sampleRate, AudioFormat.CHANNEL_IN_MONO,
                                    AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
                            if (audioRecord[0].getState() != AudioRecord.STATE_INITIALIZED) {
                                Log.e("VerifyFragment", "un init audio record");
                                throw new Exception("un init audio record");
                            }

                            audioRecord[0].startRecording();
                            if (audioRecord[0].getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                                System.exit(0);
                                throw new Exception("AudioRecord error has occured.");
                            }

                            final short[] buffer = new short[BUFFER_SIZE];
                            while (isRecording) {
                                final int len = audioRecord[0].read(buffer, 0, BUFFER_SIZE);
                                if (len > 0) {
                                    wavWriter.writeToFile(buffer, len);
                                }
                                dataLength += len;
                                if (dataLength >= FILE_MAX_LENGTH) {
                                    dataLength = 0;
                                    // stopRecord();
                                    if (wavWriter != null) wavWriter.closeFile();
                                    if (listener != null) {
                                        listener.onGenerateWav(fileNameIndex);
                                    }
                                    // 新起文件
                                    fileNameIndex++;
                                    wavWriter = new WavWriter(Environment.getExternalStorageDirectory() + "/msc/mei" + fileNameIndex + ".wav", 1,
                                            sampleRate, AudioFormat.ENCODING_PCM_16BIT);
                                }
                            }
                        } catch (Exception e) {
                        } finally {
                            if (wavWriter != null) wavWriter.closeFile();
                            if (audioRecord[0] != null) {
                                audioRecord[0].stop();
                                audioRecord[0].release();
                                audioRecord[0] = null;
                            }
                            // 发送消息
                            // if (listener != null) {
                            //     listener.onGenerateWav(fileNameIndex);
                            // }
                        }
                    }
                });
    }

    public interface OnRecordListener {
        void onGenerateWav(int fileNameIndex);
    }

}
