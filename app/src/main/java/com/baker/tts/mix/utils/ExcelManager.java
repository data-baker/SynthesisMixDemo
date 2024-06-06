package com.baker.tts.mix.utils;


import android.content.Context;
import android.util.Log;

import com.excel.utils.Excel;
import com.excel.utils.service.IWriteListener;

import java.io.File;
import java.util.List;

public class ExcelManager {

    private static volatile ExcelManager mInstance;

    private ExcelManager() {
    }

    public static ExcelManager getInstance() {
        if (mInstance == null) {
            synchronized (ExcelManager.class) {
                if (mInstance == null) {
                    mInstance = new ExcelManager();
                }
            }
        }
        return mInstance;
    }


    public void doWriteXlsx(Context mContext, String mSpeakName, List<DataBean> dataList, String fileName, CallBack callBack) {
        //databaker-tts-中英文.xlsx
        File file = new File(mContext.getFilesDir().getAbsolutePath() + File.separator + mSpeakName + File.separator, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            boolean isSuccess = file.createNewFile();
            if (!isSuccess) {
                Log.e("TAG--->", "创建文件失败1");
                return;
            }
        } catch (Exception e) {
            Log.e("TAG--->", "创建文件失败2");
            return;
        }
        Log.e("TAG--->", dataList.toString());
        Excel.get()
                .writeWith(file)
                .doWrite(new IWriteListener() {
                    @Override
                    public void onStartWrite() {
                        Log.e("TAG--->xlsx", "onStartWrite");
                    }

                    @Override
                    public void onWriteError(Exception e) {
                        Log.e("TAG--->xlsx", "Exception:" + e.getMessage());
                        if (callBack != null) {
                            callBack.onError();
                        }
                    }

                    @Override
                    public void onEndWrite() {
                        Log.e("TAG--->xlsx", "onEndWrite");
                        if (callBack != null) {
                            callBack.onSuccess();
                        }
                    }
                }, dataList);
    }

}
