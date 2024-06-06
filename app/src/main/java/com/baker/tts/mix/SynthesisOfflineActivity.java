package com.baker.tts.mix;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.baker.tts.base.component.BakerBaseConstants;
import com.baker.tts.base.component.bean.BakerError;
import com.baker.tts.base.component.bean.BakerSpeaker;
import com.baker.tts.base.component.bean.TextInterval;
import com.baker.tts.mix.lib.SynthesisMixEngine;
import com.baker.tts.mix.lib.callback.SynthesisMixAuthCallback;
import com.baker.tts.mix.lib.callback.SynthesisMixCallback;
import com.baker.tts.mix.lib.callback.SynthesizerInitCallback;
import com.baker.tts.mix.lib.callback.SynthesizerMixMediaCallback;
import com.baker.tts.mix.utils.AssetsManager;
import com.baker.tts.mix.utils.CallBack;
import com.baker.tts.mix.utils.DataBean;
import com.baker.tts.mix.utils.ExcelManager;
import com.baker.tts.mix.utils.PcmFileUtil;
import com.baker.tts.mix.utils.PcmToWavUtils;
import com.baker.tts.mix.utils.SynthesisMixCallbackImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SynthesisOfflineActivity extends BakerBaseActivity {
    private EditText editText;
    private ProgressBar progressBar;
    long sendTime;
    public String TEST_TAG = "TAG--->Time";
    private final Executor workService = Executors.newSingleThreadExecutor();

    private final List<DataBean> dataList = new ArrayList<>();
    private String mCurrentFontFile, mFrontFile, mBackFile;
    private String mBeiRuMixPath, mBeiRuMgPath, mBeiHeMixPath, mBeiHeMgPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthesis_offline);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setTitle("离线合成");
        editText = findViewById(R.id.edit);
        progressBar = findViewById(R.id.progress);
        initData();

    }

    private void initData() {
        workService.execute(() -> {
            mFrontFile = Util.AssetsFileToString(SynthesisOfflineActivity.this, "tts_entry_1.0.0_release_front_chn_eng_ser.dat");
            mBackFile = Util.AssetsFileToString(SynthesisOfflineActivity.this, "tts_entry_1.0.0_release_back_chn_eng_hts_bb_f4180623_jm3_fix.dat");
            
            mBeiRuMixPath = Util.AssetsFileToString(SynthesisOfflineActivity.this, "beiru/mix005007128_16k_DB-CN-F-04_chn9k_eng2k_mix2k_188k.pb.tflite.x");
            mBeiRuMgPath = Util.AssetsFileToString(SynthesisOfflineActivity.this, "beiru/mg16000128_f4.pb.tflite.x");
            
            mBeiHeMixPath = Util.AssetsFileToString(SynthesisOfflineActivity.this, "beihe/mix005007128_16k_DB-CN-M-11_chn21k_175k.pb.tflite.x");
            mBeiHeMgPath = Util.AssetsFileToString(SynthesisOfflineActivity.this, "beihe/mg16000128_m11.pb.tflite.x");

        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SynthesisMixEngine.getInstance().bakerStopPlay();
    }

    private final SynthesizerMixMediaCallback mediaCallback = new SynthesizerMixMediaCallback() {
        @Override
        public void onPrepared() {
            super.onPrepared();
            Log.e("TAG--->", "onPrepared");
        }

        @Override
        public void onWarning(String warningCode, String warningMessage) {
            toast("onWarning:" + warningCode + ", " + warningMessage);
        }

        @Override
        public void playing() {
        }

        @Override
        public void noPlay() {

        }

        @Override
        public void onCompletion() {
        }

        @Override
        public void onError(BakerError error) {
            toast("onError:" + error.getCode() + ", " + error.getMessage());
        }
    };

    public void onAuthClick(View view) {
         SynthesisMixEngine.getInstance().setDebug(true);
        SynthesisMixEngine.getInstance().firstDoAuthentication(SynthesisOfflineActivity.this, BakerBaseConstants.SynthesisType.OFFLINE, null, null,
                SharedPreferencesUtil.getOfflineClientId(SynthesisOfflineActivity.this),
                SharedPreferencesUtil.getOfflineSecret(SynthesisOfflineActivity.this), new SynthesisMixAuthCallback() {
                    @Override
                    public void onSuccess(BakerBaseConstants.SynthesisType synthesisType) {
                        initOfflineEngine();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        toast("授权失败：" + errorMsg);
                    }
                });
    }

    public void onParamsClick(View view) {
        SynthesisMixEngine.getInstance().setVolume(5);
        SynthesisMixEngine.getInstance().setSpeed(5);
        SynthesisMixEngine.getInstance().setPitch(5);
    }

    private String mSpeakName = "beihanna";

    private void initOfflineEngine() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));

        SynthesisMixEngine.getInstance().secondInitMixEngine(new String[]{mFrontFile}, new String[]{mBackFile}, null, new SynthesizerInitCallback() {
            @Override
            public void onSuccess() {
                dismissProgress();
                mCurrentFontFile = mFrontFile;
                toast("授权和初始化成功");
            }

            @Override
            public void onTaskFailed(BakerError error) {
                dismissProgress();
                toast("初始化失败：" + error.getCode() + ", " + error.getMessage() + ", " + error.getTrace_id());
            }
        });
    }


    public void onBeiRuClick(View view) {
        workService.execute(() -> {
            mSpeakName = "beiru";
            SynthesisMixEngine.getInstance().loadModel("beiru", mBeiRuMixPath, mBeiRuMgPath);
            SynthesisMixEngine.getInstance().setLanguage("ZH");
            SynthesisMixEngine.getInstance().setOfflineVoiceName("beiru");
        });
    }

    public void onBeiRuUninstallClick(View view) {
        workService.execute(() -> {
            SynthesisMixEngine.getInstance().unInstallModel("beiru");
        });
    }

    public void onBeiHeUninstallClick(View view) {
        workService.execute(() -> {
            SynthesisMixEngine.getInstance().unInstallModel("beihe");
        });
    }

    public void onBeiHeClick(View view) {
        workService.execute(() -> {
            mSpeakName = "BeiHe";

            SynthesisMixEngine.getInstance().loadModel("beihe", mBeiHeMixPath, mBeiHeMgPath);
            SynthesisMixEngine.getInstance().setLanguage("ZH");
            SynthesisMixEngine.getInstance().setOfflineVoiceName("beihe");
        });
    }


    public void onReleaseClick(View view) {
        SynthesisMixEngine.getInstance().release();
    }

    public void onPauseClick(View view) {
        SynthesisMixEngine.getInstance().bakerPause();
    }

    public void onStopClick(View view) {
        SynthesisMixEngine.getInstance().bakerStopPlay();
    }


    public void onResumeClick(View view) {
        SynthesisMixEngine.getInstance().resumeSynthesis();
    }

    public void onSynthesizerClick(View view) {
        SynthesisMixEngine.getInstance().setSynthesizerCallback(mediaCallback);
        List<String> stringList = Util.splitText(editText.getText().toString().trim());
        sendTime = System.currentTimeMillis();
        SynthesisMixEngine.getInstance().startSynthesis(stringList);
    }


    private void dismissProgress() {
        runOnUiThread(() -> progressBar.setVisibility(View.INVISIBLE));
    }


    private ArrayList<String> textList;
    private long mStartTime, mFirstTime, mEndTime;
    private int mAudioLength = 0, index = 0;
    private boolean isFirstPackage = true;
    private String contentString;

    public void onTestClick(View view) {
        dataList.clear();
        SynthesisMixEngine.getInstance().setSynthesizerCallback(synthesisMixCallback);
        textList = AssetsManager.getInstance().getAssetsStrings(this, "tts.txt");
        if (textList != null && textList.size() != 0) {
            index = 0;
            mHandler.sendEmptyMessage(0);
        }
    }

    public void onWavClick(View view) {
        String path = getFilesDir().getAbsolutePath() + File.separator + String.format("audio-%s", mSpeakName) + File.separator;
        for (int i = 0; i < textList.size(); i++) {
            String filePath = PcmToWavUtils.pcmToWav(SynthesisOfflineActivity.this, path, 24000, 1, path + mSpeakName + "-第" + (i + 1) + "条.pcm");
            if (filePath != null) Log.e("TAG--->Time", "filePath:" + filePath);
        }
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mAudioLength = 0;
            if (index < textList.size() && textList.get(index) != null && !TextUtils.isEmpty(textList.get(index))) {
                isFirstPackage = true;
                PcmFileUtil.getInstance().init(SynthesisOfflineActivity.this, mSpeakName + "-第" + (index + 1) + "条.pcm", String.format("audio-%s", mSpeakName));
                contentString = textList.get(index);
                mStartTime = System.currentTimeMillis();
                SynthesisMixEngine.getInstance().startSynthesis(contentString);
            } else {
                ExcelManager.getInstance().doWriteXlsx(SynthesisOfflineActivity.this, String.format("audio-%s", mSpeakName), dataList, String.format("databaker-tts-offline-%s.xlsx", String.format("audio-%s", mSpeakName)), new CallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("TAG--->", "写入完成");
                    }

                    @Override
                    public void onError() {
                        Log.e("TAG--->", "写入失败");
                    }
                });
            }
        }
    };
    int errorCount = 0;
    private final SynthesisMixCallback synthesisMixCallback = new SynthesisMixCallbackImpl() {

        @Override
        public void onBinaryReceived(byte[] data, String interval, String interval_x, boolean endFlag, ArrayList<TextInterval> arrayList) {
            if (data != null && data.length > 0) {
                if (isFirstPackage) {
                    mFirstTime = System.currentTimeMillis();
                    isFirstPackage = false;
                }
                PcmFileUtil.getInstance().write(data);
                mAudioLength += data.length;
            }
        }

        @Override
        public void onSynthesisCompleted() {
            mEndTime = System.currentTimeMillis();
            Log.e(TEST_TAG, "第" + index + "条数据，" + "首包消耗：" + (mFirstTime - mStartTime) + " 合成消耗：" + (mEndTime - mStartTime) + " 开始时间:" + mStartTime + " 首包时间：" + mFirstTime + " 结束时间：" + mEndTime);
            float duration = (mAudioLength / (16000f * 16 / 8)) * 1000;
            dataList.add(new DataBean(contentString, Util.dataFormat2(duration), (mEndTime - mStartTime), Util.dataFormat5((mEndTime - mStartTime) / (duration)), (mFirstTime - mStartTime)));
            PcmFileUtil.getInstance().close();
            index++;
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void onTaskFailed(BakerError bakerError) {
            super.onTaskFailed(bakerError);
            index++;
            errorCount++;
            mHandler.sendEmptyMessage(0);
        }
    };


}