package com.baker.tts.mix;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.baker.tts.base.component.BakerBaseConstants;
import com.baker.tts.base.component.bean.BakerError;
import com.baker.tts.base.component.bean.BakerSpeaker;
import com.baker.tts.mix.lib.SynthesisMixEngine;
import com.baker.tts.mix.lib.callback.SynthesisMixAuthCallback;
import com.baker.tts.mix.lib.callback.SynthesizerInitCallback;
import com.baker.tts.mix.lib.callback.SynthesizerMixMediaCallback;

import java.util.ArrayList;
import java.util.List;

public class SynthesisMixActivity extends BakerBaseActivity {
    private EditText editText, editVoiceName;
    private ProgressBar progressBar;
    private Spinner spinnerOfflineVoiceName;
    boolean isFirst = true; //仅用作控制onCreate方法执行时，spinner的onItemSelected回调会自动执行一次，首次无效。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthesis_mix);
        setTitle("Mix合成");

        editText = findViewById(R.id.edit);
        progressBar = findViewById(R.id.progress);
        editVoiceName = findViewById(R.id.edit_voice);
        editVoiceName.setText(SharedPreferencesUtil.getOnlineVoiceName(SynthesisMixActivity.this));

        spinnerOfflineVoiceName = findViewById(R.id.spinner_voice);
        spinnerOfflineVoiceName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SynthesisMixEngine.getInstance().setOfflineVoiceName(speakerNames[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SynthesisMixEngine.getInstance().bakerStopPlay();
    }

    private final SynthesizerMixMediaCallback mediaCallback = new SynthesizerMixMediaCallback() {
        @Override
        public void onWarning(String warningCode, String warningMessage) {
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
            toast(error.getCode() + ", " + error.getMessage());
        }
    };

    public void onAuthClick(View view) {
        SynthesisMixEngine.getInstance().firstDoAuthentication(SynthesisMixActivity.this, BakerBaseConstants.SynthesisType.MIX,
                SharedPreferencesUtil.getOnlineClientId(SynthesisMixActivity.this),
                SharedPreferencesUtil.getOnlineSecret(SynthesisMixActivity.this),
                SharedPreferencesUtil.getOfflineClientId(SynthesisMixActivity.this),
                SharedPreferencesUtil.getOfflineSecret(SynthesisMixActivity.this), new SynthesisMixAuthCallback() {
                    @Override
                    public void onSuccess(BakerBaseConstants.SynthesisType synthesisType) {
                        if (synthesisType == BakerBaseConstants.SynthesisType.MIX || synthesisType == BakerBaseConstants.SynthesisType.OFFLINE) {
                            initOfflineEngine();
                        } else {
                            toast("可以合成，仅在线授权成功");
                        }
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        toast("授权失败：" + errorMsg);
                    }
                });
    }

    private void initOfflineEngine() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));

        String mFrontFile = Util.AssetsFileToString(SynthesisMixActivity.this, "tts_entry_1.0.0_release_front_chn_eng_ser.dat");
        String mBackFile = Util.AssetsFileToString(SynthesisMixActivity.this, "tts_entry_1.0.0_release_back_chn_eng_hts_bb_f4180623_jm3_fix.dat");

        String mBeiRuMixPath = Util.AssetsFileToString(SynthesisMixActivity.this, "beiru/mix005007128_16k_DB-CN-F-04_chn9k_eng2k_mix2k_188k.pb.tflite.x");
        String mBeiRuMgPath = Util.AssetsFileToString(SynthesisMixActivity.this, "beiru/mg16000128_f4.pb.tflite.x");


        List<BakerSpeaker> speakerList = new ArrayList<>();
        speakerList.add(new BakerSpeaker("beiru", mBeiRuMixPath, mBeiRuMgPath));


        SynthesisMixEngine.getInstance().secondInitMixEngine(new String[]{mFrontFile}, new String[]{mBackFile}, speakerList, new SynthesizerInitCallback() {
            @Override
            public void onSuccess() {
                dismissProgress();
                showOfflineVoiceNameSpinner();
                toast("授权和初始化成功");
            }

            @Override
            public void onTaskFailed(BakerError error) {
                dismissProgress();
                toast("初始化失败：" + error.getCode() + ", " + error.getMessage() + ", " + error.getTrace_id());
            }
        });
    }

    public void onParamsClick(View view) {
        SynthesisMixEngine.getInstance().setSynthesizerCallback(mediaCallback);

        String voiceName = editVoiceName.getText().toString().trim();
        SharedPreferencesUtil.saveOnlineVoiceName(SynthesisMixActivity.this, voiceName);
        SynthesisMixEngine.getInstance().setVoiceNameOnline(voiceName);
        SynthesisMixEngine.getInstance().setOnLineConnectTimeOut(3);
        SynthesisMixEngine.getInstance().setVolume(5);
        SynthesisMixEngine.getInstance().setSpeed(5);
        SynthesisMixEngine.getInstance().setPitch(5);


    }

    public void onSynthesizerClick(View view) {
        List<String> stringList = Util.splitText(editText.getText().toString().trim());
        SynthesisMixEngine.getInstance().startSynthesis(stringList);
    }

    public void onStopClick(View view) {
        SynthesisMixEngine.getInstance().bakerStopPlay();
    }

    public void onPauseClick(View view) {
        SynthesisMixEngine.getInstance().bakerPause();
    }

    public void onResumeClick(View view) {
        SynthesisMixEngine.getInstance().resumeSynthesis();
    }

    private void dismissProgress() {
        runOnUiThread(() -> progressBar.setVisibility(View.INVISIBLE));
    }

    String[] speakerNames = new String[]{"beiru"};

    private void showOfflineVoiceNameSpinner() {
        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter(SynthesisMixActivity.this, android.R.layout.simple_spinner_item, speakerNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerOfflineVoiceName.setAdapter(adapter);
        });
    }
}