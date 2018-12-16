package com.airxiao.audio;

import android.content.res.Resources;
import android.util.TypedValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static long lastClickTime;

    public static float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static long getFileSize(String path) {
        long size = 0;
        File file = new File(path);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    public static String parseTime(int times) {
        int mins = times / 60;
        int seconds = times % 60;
        return String.format("%d:%02d", mins, seconds).toString();
    }

    public static void deleteDir(String delePath) {
        File dir = new File(delePath);
        dir = dir.getParentFile();
        if (dir == null || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
        dir.delete();
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (dir == null || !dir.isDirectory()) {
            return;
        }

        if (file.isFile()) {
            file.delete();
        }
    }

    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isFile(File file) {
        return file.isFile();
    }

    public static String getDateYMD(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

}
