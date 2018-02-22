package com.example.helpme.mvpandroid.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Created by helpme on 2018/1/26.
 * @Description
 */
public class FileUtils {
    
    public static boolean isExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment
                .MEDIA_MOUNTED);
    }
    
    /**
     * 如果存在外部SD卡则返回外部缓存路径
     *
     * @param context
     * @return
     */
    public static String getAppCacheDir(Context context) {
        if (context.getExternalCacheDir() != null && isExistSDCard()) {
            return context.getExternalCacheDir().toString();
        } else {
            return context.getCacheDir().toString();
        }
    }
    
    public static String getInternalCacheDir(Context context) {
        return context.getCacheDir().toString();
    }
    
    public static String getExternalCacheDir(Context context) {
        if (context.getExternalCacheDir() != null && isExistSDCard()) {
            return context.getExternalCacheDir().toString();
        } else
            return null;
    }
    
    public static boolean delete(String path) {
        return delete(new File(path));
    }
    
    public static boolean delete(File file) {
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return file.delete();
            }
            for (File childFile : childFiles) {
                delete(childFile);
            }
            return file.delete();
        }
        return false;
    }
    
    public static String getFolderName(String filePath) {
        
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }
    
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }
        
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
    
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.write("\r\n");
            fileWriter.write("\r\n");
            fileWriter.close();
            return true;
        } catch (IOException e) {
            Log.e("IOException occurred. ", e.getMessage(), e);
            return false;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    Log.e("IOException occurred. ", e.getMessage(), e);
                }
            }
        }
    }
    
    public static String readFile(String filePath) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (!file.isFile()) {
            return null;
        }
        
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file));
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent.toString();
        } catch (IOException e) {
            Log.e("IOException occurred. ", e.getMessage(), e);
            return "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("IOException occurred. ", e.getMessage(), e);
                }
            }
        }
    }
    
    public static void writeLogCrashContent(final String content,final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeFile(getFileDir("Log",context) + "/crash.log", TimeUtils.getCurTimeString() + " : "
                        + content, true);
            }
        }).start();
    }
    
    public static File getCrashLog(Context context) {
        return new File(getFileDir("Log/crash.log", context));
    }
    
    public static String getFileDir(String filePath, Context context) {
        String dir;
        if (isExistSDCard()) {
            dir = context.getExternalFilesDir("").getAbsolutePath();
        } else {
            dir = context.getFilesDir().getAbsolutePath();
        }
        
        if (TextUtils.isEmpty(filePath))
            return dir;
        else {
            if (filePath.startsWith(File.separator)) {
                dir += filePath;
            } else
                dir += File.separator + filePath;
            
            
            makeDirs(dir);
            
            return dir;
        }
    }
    
    /**
     * 将json数据变成字符串
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getJson(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    
    /**
     * 将json数据变成Bean
     *
     * @param fileName
     * @param context
     * @param classOfT
     * @param <T>
     * @return
     */
    public static  <T> T getJson(String fileName, Context context, Class<T> classOfT) {
        //将json数据变成字符串
        BufferedReader bf = null;
        Gson gson = new Gson();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bf == null ? null : gson.fromJson(bf, classOfT);
    }
    
}
