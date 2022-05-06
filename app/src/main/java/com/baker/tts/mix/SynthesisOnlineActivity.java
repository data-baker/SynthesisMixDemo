package com.baker.tts.mix;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.baker.tts.base.component.BakerBaseConstants;
import com.baker.tts.base.component.HLogger;
import com.baker.tts.base.component.bean.BakerError;
import com.baker.tts.mix.lib.SynthesisMixEngine;
import com.baker.tts.mix.lib.callback.SynthesisMixAuthCallback;
import com.baker.tts.mix.lib.callback.SynthesisMixCallback;
import com.baker.tts.mix.lib.callback.SynthesizerMixMediaCallback;

import java.util.List;

public class SynthesisOnlineActivity extends BakerBaseActivity {
    private EditText editText, editVoiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthesis_online);
        setTitle("在线合成");

        editText = findViewById(R.id.edit);
        editVoiceName = findViewById(R.id.edit_voice);
        editVoiceName.setText(SharedPreferencesUtil.getOnlineVoiceName(SynthesisOnlineActivity.this));
    }

    @Override
    public void onBack() {
        SynthesisMixEngine.getInstance().bakerStopPlay();
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

    private SynthesisMixCallback synthesisMixCallback = new SynthesisMixCallback() {
        @Override
        public void onSynthesisStarted() {
            HLogger.e("--onSynthesisCompleted--");
        }

        @Override
        public void onPrepared() {
            HLogger.e("--onSynthesisCompleted--");
        }

        @Override
        public void onBinaryReceived(byte[] data, String interval, String interval_x, boolean endFlag) {
            HLogger.e("--onBinaryReceived--, endFlag = " + endFlag + ", interval_x" + interval_x);
        }

        @Override
        public void onSynthesisCompleted() {
            HLogger.e("--onSynthesisCompleted--");
        }

        @Override
        public void onWarning(String warningCode, String warningMessage) {
            HLogger.e("--onSynthesisCompleted--warningCode = " + warningCode + ", warningMessage = " + warningMessage);
        }

        @Override
        public void onTaskFailed(BakerError error) {
            HLogger.e("--onTaskFailed--error.getCode() = " + error.getCode() + ", error.getMessage() = " + error.getMessage() + ", error.getTrace_id() = " + error.getTrace_id());
        }
    };

    public void onAuthClick(View view) {
        SynthesisMixEngine.getInstance().firstDoAuthentication(SynthesisOnlineActivity.this, BakerBaseConstants.SynthesisType.ONLINE,
                SharedPreferencesUtil.getOnlineClientId(SynthesisOnlineActivity.this),
                SharedPreferencesUtil.getOnlineSecret(SynthesisOnlineActivity.this), null, null, new SynthesisMixAuthCallback() {
                    /**
                     * @param synthesisType 回调结果表明哪种授权通过了
                     *                      SynthesisMixConstants.SynthesisType.ONLINE 只有在线合成授权通过
                     *                      SynthesisMixConstants.SynthesisType.OFFLINE 只有离线合成授权通过
                     *                      SynthesisMixConstants.SynthesisType.MIX 两种合成授权都通过
                     */
                    @Override
                    public void onSuccess(BakerBaseConstants.SynthesisType synthesisType) {
                        toast("授权成功");
                        HLogger.d("授权成功");
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        toast("授权失败：" + errorMsg);
                        HLogger.d("授权失败：" + errorMsg);
                    }
                });
    }

    public void onInitClick(View view) {
    }

    public void onSynthesizerClick(View view) {
        SynthesisMixEngine.getInstance().setSynthesizerCallback(mediaCallback);

        String voiceName = editVoiceName.getText().toString().trim();
        SharedPreferencesUtil.saveOnlineVoiceName(SynthesisOnlineActivity.this, voiceName);
        SynthesisMixEngine.getInstance().setVoiceNameOnline(voiceName);
        SynthesisMixEngine.getInstance().setOnLineConnectTimeOut(3);

        SynthesisMixEngine.getInstance().setVolume(5);
        SynthesisMixEngine.getInstance().setSpeed(5);
        SynthesisMixEngine.getInstance().setPitch(5);

        List<String> stringList = Util.splitText(editText.getText().toString().trim());
        SynthesisMixEngine.getInstance().startSynthesis(stringList);
    }
}