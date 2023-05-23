package com.baker.tts.mix;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.baker.tts.base.component.BakerBaseConstants;
import com.baker.tts.base.component.HLogger;
import com.baker.tts.base.component.bean.BakerError;
import com.baker.tts.base.component.bean.BakerSpeaker;
import com.baker.tts.mix.lib.SynthesisMixEngine;
import com.baker.tts.mix.lib.callback.SynthesisMixAuthCallback;
import com.baker.tts.mix.lib.callback.SynthesizerInitCallback;
import com.baker.tts.mix.lib.callback.SynthesizerMixMediaCallback;

import java.util.ArrayList;
import java.util.List;

public class SynthesisOfflineActivity extends BakerBaseActivity {
    private EditText editText;
    private ProgressBar progressBar;
    private Spinner spinnerOfflineVoiceName;
    boolean isFirst = true; //仅用作控制onCreate方法执行时，spinner的onItemSelected回调会自动执行一次，首次无效。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthesis_offline);

        setTitle("离线合成");

        editText = findViewById(R.id.edit);
        progressBar = findViewById(R.id.progress);
        spinnerOfflineVoiceName = findViewById(R.id.spinner_voice);
        spinnerOfflineVoiceName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    isFirst = false;
                    return;
                }
                //底层sdk只需要设置列表的索引0-1-2即可
                SynthesisMixEngine.getInstance().setVoiceNameOffline3(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBack() {
        SynthesisMixEngine.getInstance().bakerStopPlay();
        SynthesisMixEngine.getInstance().release();
        finish();
    }

    private SynthesizerMixMediaCallback mediaCallback = new SynthesizerMixMediaCallback() {
        @Override
        public void onWarning(String warningCode, String warningMessage) {
            HLogger.e("--onWarning--");
        }

        @Override
        public void playing() {
            HLogger.e("--playing--");
        }

        @Override
        public void noPlay() {
            HLogger.e("--noPlay--");
        }

        @Override
        public void onCompletion() {
            HLogger.e("--onCompletion--");
        }

        @Override
        public void onError(BakerError error) {
            HLogger.e("--onError--" + error.getCode() + ", " + error.getMessage());
            toast(error.getCode() + ", " + error.getMessage());
        }
    };

    public void onAuthClick(View view) {
        SynthesisMixEngine.getInstance().firstDoAuthentication(SynthesisOfflineActivity.this, BakerBaseConstants.SynthesisType.OFFLINE, null, null,
                SharedPreferencesUtil.getOfflineClientId(SynthesisOfflineActivity.this),
                SharedPreferencesUtil.getOfflineSecret(SynthesisOfflineActivity.this), new SynthesisMixAuthCallback() {
                    /**
                     * @param synthesisType 回调结果表明哪种授权通过了
                     *                      SynthesisMixConstants.SynthesisType.ONLINE 只有在线合成授权通过
                     *                      SynthesisMixConstants.SynthesisType.OFFLINE 只有离线合成授权通过
                     *                      SynthesisMixConstants.SynthesisType.MIX 两种合成授权都通过
                     */
                    @Override
                    public void onSuccess(BakerBaseConstants.SynthesisType synthesisType) {
                        initOfflineEngine();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        toast("授权失败：" + errorMsg);
                        HLogger.d("授权失败：" + errorMsg);
                    }
                });
    }

    private void initOfflineEngine() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });


        String frontFile = Util.AssetsFileToString(SynthesisOfflineActivity.this, "tts_entry_1.0.0_release_front_chn_eng_ser.dat");
        ;
        String backFile = Util.AssetsFileToString(SynthesisOfflineActivity.this, "tts_entry_1.0.0_release_back_chn_eng_hts_bb_f4180623_jm3_fix.dat");

        //贝茹
        String beiRu_Chn = Util.AssetsFileToString(SynthesisOfflineActivity.this, "beiru/mix005007128_16k_DB-CN-F-04_chn9k_eng2k_mix2k_188k.pb.tflite.x");
        String beiRu_Mgvocoder = Util.AssetsFileToString(SynthesisOfflineActivity.this, "beiru/mg16000128_f4.pb.tflite.x");

        List<BakerSpeaker> speakerList = new ArrayList<>();
        speakerList.add(new BakerSpeaker(beiRu_Chn, beiRu_Mgvocoder));
        SynthesisMixEngine.getInstance().secondInitMixEngine(frontFile, backFile, speakerList, new SynthesizerInitCallback() {
            @Override
            public void onSuccess() {
                dismissProgress();
                showOfflineVoiceNameSpinner();
                toast("授权和初始化成功");
                HLogger.d("授权和初始化成功");
            }

            @Override
            public void onTaskFailed(BakerError error) {
                dismissProgress();
                toast("初始化失败：" + error.getCode() + ", " + error.getMessage() + ", " + error.getTrace_id());
                HLogger.d("初始化失败：" + error.getCode() + ", " + error.getMessage() + ", " + error.getTrace_id());
            }
        });
    }

    public void onInitClick(View view) {

    }

    public void onSynthesizerClick(View view) {
        SynthesisMixEngine.getInstance().setSynthesizerCallback(mediaCallback);

        SynthesisMixEngine.getInstance().setVolume(5);
        SynthesisMixEngine.getInstance().setSpeed(5);
        SynthesisMixEngine.getInstance().setPitch(5);

        List<String> stringList = Util.splitText(editText.getText().toString().trim());
        SynthesisMixEngine.getInstance().startSynthesis(stringList);
    }

    private void dismissProgress() {
        runOnUiThread(() -> progressBar.setVisibility(View.INVISIBLE));
    }

    public void onStopClick(View view){
        SynthesisMixEngine.getInstance().bakerStopPlay();
    }
    private void showOfflineVoiceNameSpinner() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] speakerNames = new String[]{"贝茹"};
                ArrayAdapter<String> adapter = new ArrayAdapter(SynthesisOfflineActivity.this, android.R.layout.simple_spinner_item, speakerNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerOfflineVoiceName.setAdapter(adapter);
            }
        });
    }
}