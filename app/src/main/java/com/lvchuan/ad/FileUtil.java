package com.lvchuan.ad;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtil {

    /**
     * 创建文件夹
     *
     * @return File
     */
    public static File createDir(String path) {
        File dir = new File(path);
        if (!isExist(dir)) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 判断当前文件是否存在
     *
     * @param file
     * @return
     */
    public static boolean isExist(File file) {
        return file.exists();
    }

    //写数据到SD中的文件
    public static void writeFileSdcardFile(String fileName, String write_str){
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = write_str.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读SD中的文件
    public static String readFileSdcardFile(String fileName){
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param dest   目标文件
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        String filenameTemp = dest.getPath() + "/SmartRecycling" + ".apk";
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            if (!dest.exists()) {
                try {
                    //按照指定的路径创建文件夹
                    dest.mkdirs();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            File dir = new File(filenameTemp);
            if (!dir.exists()) {
                try {
                    //在指定的文件夹中创建文件
                    dir.createNewFile();
                } catch (Exception e) {
                }
            }
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(filenameTemp).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    // 判断文件是否存在
    public static boolean judeFileExists(File file) {
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public static boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }

            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);    //读入原文件
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
