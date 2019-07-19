package com.mei.financial.utils;

import android.media.AudioFormat;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/7/17
 */
public class WavWriter {
    final static String TAG = WavWriter.class.getSimpleName();

    String mFilePath;
    DataOutputStream mDataOutputStream;
    int mFileSize;

    public WavWriter(String path, int channelCnt, int sampleRate, int audioEncoding) {
        mFilePath = path;
        File file = new File(path);
        try {
            file.createNewFile();
            mDataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

            ByteBuffer byteBuffer = ByteBuffer.allocate(44);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.putInt(0x46464952);
            byteBuffer.putInt(0);
            byteBuffer.putInt(0x45564157);
            byteBuffer.putInt(0x20746d66);
            byteBuffer.putInt(16);
            byteBuffer.putShort((short) 1);
            byteBuffer.putShort((short) channelCnt);
            byteBuffer.putInt(sampleRate);
            byteBuffer.putInt((sampleRate * channelCnt * (audioEncoding == AudioFormat.ENCODING_PCM_16BIT ? 2 : 1)));
            byteBuffer.putShort((short) (channelCnt * (audioEncoding == AudioFormat.ENCODING_PCM_16BIT ? 2 : 1)));
            byteBuffer.putShort((short) (audioEncoding == AudioFormat.ENCODING_PCM_16BIT ? 16 : 8));
            byteBuffer.putInt(0x61746164);
            byteBuffer.putInt(0);

            mFileSize = 44;
            mDataOutputStream.write(byteBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private byte[] shortToByteArray(short data) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(data).array();
    }

    public boolean writeToFile(short[] buf, int len) {
        byte[] cache = new byte[len * 2];
        for (int i = 0; i < len; i++) {
            byte[] temp = shortToByteArray(buf[i]);
            cache[i * 2] = temp[0];
            cache[i * 2 + 1] = temp[1];
        }
        return writeToFile(cache, len * 2);
    }

    public boolean writeToFile(byte[] buf, int len) {
        if (null == mDataOutputStream) {
            return false;
        }

        try {
            mDataOutputStream.write(buf, 0, len);
            mFileSize += len;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void closeFile() {
        try {
            mDataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RandomAccessFile ras = null;
        try {
            ras = new RandomAccessFile(mFilePath, "rw");
            ras.seek(4);

            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.putInt(mFileSize - 8);
            ras.write(byteBuffer.array());

            byteBuffer.rewind();
            byteBuffer.putInt(mFileSize - 42);
            ras.seek(40);
            ras.write(byteBuffer.array());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != ras) {
                try {
                    ras.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
