package com.baker.tts.mix.utils;


import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PcmFileUtil {

    private PcmFileUtil() {
    }

    private static class InnerClass {
        private static final PcmFileUtil INSTANCE = new PcmFileUtil();
    }

    public static PcmFileUtil getInstance() {
        return InnerClass.INSTANCE;
    }

    private FileOutputStream fout;

    public String getConvertResultFile(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    public void init(Context context, String fileName, String mSpeakName) {
        try {
            if (fout != null) {
                fout.close();
                fout = null;
            }
            String path = context.getFilesDir().getAbsolutePath() + File.separator + mSpeakName + "/" + fileName;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

            fout = new FileOutputStream(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] data) {
        if (fout == null) {
            return;
        }
        try {
            fout.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (fout != null) {
            try {
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
