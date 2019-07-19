package com.mei.financial.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/7/16
 */
public class WaveHeader {
    public final char fileID[] = {'R', 'I', 'F', 'F'};
    public int fileLength;
    public char wavTag[] = {'W', 'A', 'V', 'E'};
    ;
    public char FmtHdrID[] = {'f', 'm', 't', ' '};
    public int FmtHdrLeth;
    public short FormatTag;
    public short Channels;
    public int SamplesPerSec;
    public int AvgBytesPerSec;
    public short BlockAlign;
    public short BitsPerSample;
    public char DataHdrID[] = {'d', 'a', 't', 'a'};
    public int DataHdrLeth;

    public byte[] getHeader() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WriteChar(bos, fileID);
        WriteInt(bos, fileLength);
        WriteChar(bos, wavTag);
        WriteChar(bos, FmtHdrID);
        WriteInt(bos, FmtHdrLeth);
        WriteShort(bos, FormatTag);
        WriteShort(bos, Channels);
        WriteInt(bos, SamplesPerSec);
        WriteInt(bos, AvgBytesPerSec);
        WriteShort(bos, BlockAlign);
        WriteShort(bos, BitsPerSample);
        WriteChar(bos, DataHdrID);
        WriteInt(bos, DataHdrLeth);
        bos.flush();
        byte[] r = bos.toByteArray();
        bos.close();
        return r;
    }

    private void WriteShort(ByteArrayOutputStream bos, int s)
            throws IOException {
        byte[] mybyte = new byte[2];
        mybyte[1] = (byte) ((s << 16) >> 24);
        mybyte[0] = (byte) ((s << 24) >> 24);
        bos.write(mybyte);
    }

    private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
        byte[] buf = new byte[4];
        buf[3] = (byte) (n >> 24);
        buf[2] = (byte) ((n << 8) >> 24);
        buf[1] = (byte) ((n << 16) >> 24);
        buf[0] = (byte) ((n << 24) >> 24);
        bos.write(buf);
    }

    private void WriteChar(ByteArrayOutputStream bos, char[] id) {
        for (int i = 0; i < id.length; i++) {
            char c = id[i];
            bos.write(c);
        }
    }

    public static File startPcm2Wav(String src, String target) {
        File file = new File(src);
        if (file != null) {
            try {
                @SuppressWarnings("resource")
                FileInputStream inputStream = new FileInputStream(file);
                // 计算长度
                byte[] buf = new byte[1024 * 100];
                int size = inputStream.read(buf);
                int pcmSize = 0;
                while (size != -1) {
                    pcmSize += size;
                    size = inputStream.read(buf);
                }
                inputStream.close();
                // 填入参数，比特率等。16位双声道，8000HZ
                WaveHeader header = new WaveHeader();
                // 长度字段 = 内容的大小（PCMSize) + 头部字段的大小
                // (不包括前面4字节的标识符RIFF以及fileLength本身的4字节
                header.fileLength = pcmSize + (44 - 8);
                header.FmtHdrLeth = 16;
                header.BitsPerSample = 16;
                header.Channels = 2;
                header.FormatTag = 0x0001;
                header.SamplesPerSec = 8000;
                header.BlockAlign = (short) (header.Channels
                        * header.BitsPerSample / 8);
                header.AvgBytesPerSec = header.BlockAlign
                        * header.SamplesPerSec;
                header.DataHdrLeth = pcmSize;

                byte[] h = header.getHeader();
                assert h.length == 44; // WAV标准，头部应该是44字节

                File targetFile = new File(target);
                if (!targetFile.exists()) {
                    targetFile.createNewFile();
                }
                FileOutputStream outputStream = new FileOutputStream(targetFile);
                inputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024 * 100];
                int targetSize = inputStream.read(buffer);
                outputStream.write(h, 0, h.length);
                while (targetSize != -1) {
                    outputStream.write(buffer, 0, targetSize);
                    targetSize = inputStream.read(buffer);
                }
                outputStream.close();
                return targetFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
