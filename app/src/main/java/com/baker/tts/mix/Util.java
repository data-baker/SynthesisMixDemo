package com.baker.tts.mix;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.baker.tts.base.component.HLogger;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    //sharedpreferences的表名（判断sdk有没有更新）
    private static final String SHARED_PREFERENCE_NAME = "tts";
    //第一次进入复制assets文件到sd卡的前缀
    public static final String FIRST_COPY_ASSERTS_VERSION = "first_copy_asserts_version";

    /**
     * 把Assets目录下的文件复制到sd卡中
     *
     * @param context  上下文
     * @param fileName Assets目录下的具体文件名
     * @return 文件路径名
     */
    public static synchronized String AssetsFileToString(Context context, String fileName) {
        if (context == null)
            return "";
        boolean isFirstByVersion = isFirstByVersion(context, FIRST_COPY_ASSERTS_VERSION + "_" + fileName);
        return AssetsFileToString(context, fileName, !isFirstByVersion);
    }

    /**
     * 是否是旧版本
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean isFirstByVersion(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String versionName = sp.getString(key, "");
        if (BuildConfig.VERSION_NAME.equals(versionName)) {
            return false;
        }
        return true;
    }

    /**
     * 把Assets目录下的文件复制到sd卡中
     *
     * @param context
     * @param fileName         Assets文件名
     * @param isForceCopyModle 是否复制到sd卡
     * @return 文件路径名
     */
    public static synchronized String AssetsFileToString(Context context, String fileName, boolean isForceCopyModle) {
        if (context == null)
            return "";
        String strOutFileName = context.getFilesDir().getAbsolutePath() + File.separator + fileName;
        File file = new File(strOutFileName);
        if (!file.getParentFile().exists()) {
            //创建多层目录
            file.getParentFile().mkdirs();
        }
        //文件存在&&不需要更新；直接返回路径
        if (file.exists() && isForceCopyModle) {
            return strOutFileName;
        }
        InputStream ips = null;
        OutputStream ops = null;
        try {
            ops = new FileOutputStream(strOutFileName);
            ips = context.getAssets().open(fileName);
            byte[] buffer = new byte[1024];
            int length = ips.read(buffer);
            while (length > 0) {
                ops.write(buffer, 0, length);
                length = ips.read(buffer);
            }
            setFirstInstall(context, FIRST_COPY_ASSERTS_VERSION + "_" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                close(ops);
                close(ips);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strOutFileName;
    }

    /**
     * 设置进sdk版本号
     *
     * @param context
     * @param key
     */
    public static void setFirstInstall(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, BuildConfig.VERSION_NAME).apply();
    }

    /**
     * 关闭IO流
     *
     * @param closeable
     * @throws IOException
     */
    private static void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }







    public static List<String> splitText(String text) {
        if (!TextUtils.isEmpty(text) && !"null".equals(text)) {
            List<String> resultList = new ArrayList<>();
            split(resultList, text, 20);
            HLogger.d("resultList.size()==" + resultList.size());
            return resultList;
        }
        return null;
    }

    private static void split(List<String> list, String text, int offSet) {
        if (text.length() > offSet) {
            String t1 = text.substring(0, offSet);
            Pattern p = Pattern.compile(GREP_SPLIT_PUNCTUATION_REGEX);
            Matcher m = p.matcher(t1);
            if (m.find()) {
                int i = m.end();
                if (list != null) {
                    list.add(text.substring(0, i));
                }
                split(list, text.substring(i), offSet);
            } else {
                list.add(t1);
                split(list, text.substring(offSet), offSet);
            }
        } else if (list != null) {
            list.add(text);
        }
    }

    private static final String GREP_SPLIT_PUNCTUATION_REGEX = ".*(，|。。。。。。|。|！|；|？|,|;|\\?|、)";
}
