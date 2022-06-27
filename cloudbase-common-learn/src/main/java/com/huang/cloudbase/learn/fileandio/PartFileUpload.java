package com.huang.cloudbase.learn.fileandio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * 对于大文件的分片以及合并
 *
 * @author huangjunbiao_cdv
 */
public class PartFileUpload {

    // 每个分片最大长度
    private static int partSizeMax = 20 * 1024 * 1024;

    // 每个分片最小长度
    private static int partSizeMin = 5 * 1024 * 1024;

    // 采用分片的临界值
    private static int videoSizeLimit = 30;

    /**
     * 视频文件分片上传
     *
     * @param video  视频
     * @param openId openId
     */
    private void partUpload(File video, String openId) {

        long length;
        try {
            FileInputStream fin = new FileInputStream(video);
            FileChannel fc = fin.getChannel();
            length = video.length();
            fc.size();
            long partNum = length / partSizeMax;
            long remain = length % partSizeMax;
            long lastSize = remain;
            if (remain < partSizeMin) {
                lastSize = partSizeMax + remain;
            }
            long startPoint = 0;
            long splitSize = partSizeMax;
            for (long i = 1; i <= partNum; i++) {
                FileOutputStream fout = new FileOutputStream(i + video.getName());
                FileChannel foutChannel = fout.getChannel();
                if (i == partNum) {
                    splitSize = lastSize;
                }
                fc.transferTo(startPoint, splitSize, foutChannel);
                System.out.println(startPoint);
                startPoint += splitSize;
                foutChannel.close();
                fout.close();
            }
            fc.close();
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将分片的文件合并
     *
     * @param files       分片文件集合
     * @param newFilePath 新文件路径
     */
    public void mergePartFile(List<File> files, String newFilePath) {
        FileChannel outChannel = null;
        FileChannel inChannel = null;
        try {
            outChannel = new FileOutputStream("video.mp4").getChannel();
            for (File file : files) {
                inChannel = new FileInputStream(file).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(1024 * 1024);
                while (inChannel.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                inChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
                if (inChannel != null) {
                    inChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
