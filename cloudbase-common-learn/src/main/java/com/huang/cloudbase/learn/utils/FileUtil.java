package com.huang.cloudbase.learn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

/**
 * @author huangjunbiao_cdv
 */
public class FileUtil {
    public final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * @param fileRoute              源文件地址
     * @param targetFileAbsolutePath 下载后的文件绝对路径
     * @return
     * @throws IOException
     */
    public static boolean downloadByURL(String fileRoute, String targetFileAbsolutePath) throws IOException {
        boolean result = true;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            java.io.File targetFile = new java.io.File(targetFileAbsolutePath);
            targetFile.getParentFile().mkdirs();
            targetFile.createNewFile();
            logger.debug("fileRoute: {}, targetFileAbsolutePath: {}", fileRoute, targetFileAbsolutePath);
            URL url = new URL(fileRoute);// fileRoute:文件URL路径
            // 通过URL的openStream方法获取URL对象所表示的字节输入流
            bis = new BufferedInputStream(url.openStream());
            int lastIndexOf = targetFileAbsolutePath.lastIndexOf(File.separator);
            String fileName = targetFileAbsolutePath.substring(lastIndexOf + 1);
            String dirName = targetFileAbsolutePath.substring(0, lastIndexOf);
            logger.debug("dirName: " + dirName + " ; fileName: " + fileName);
            File dirTemp = new File(dirName);
            if (!dirTemp.exists()) {
                dirTemp.mkdirs();
            }
            File fileTemp = new File(dirName, fileName);
            if (!fileTemp.exists()) {
                try {
                    fileTemp.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream out = new FileOutputStream(fileTemp);
            bos = new BufferedOutputStream(out);
            byte[] e = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(e, 0, e.length))) {
                bos.write(e, 0, bytesRead);
            }
        } catch (IOException e) {
            result = false;
            throw e;
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (bos != null) {
                bos.close();
            }

        }
        return result;

    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
