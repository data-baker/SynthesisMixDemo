package com.baker.tts.mix;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.baker.tts.base.component.BakerBaseConstants;
import com.baker.tts.base.component.bean.BakerError;
import com.baker.tts.mix.lib.SynthesisMixEngine;
import com.baker.tts.mix.lib.callback.SynthesisMixAuthCallback;
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
        if (editVoiceName.getText().toString().length() == 0) {
            editVoiceName.setText("JiaoJiao");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SynthesisMixEngine.getInstance().bakerStopPlay();
    }

    private SynthesizerMixMediaCallback mediaCallback = new SynthesizerMixMediaCallback() {
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

                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        toast("授权失败：" + errorMsg);
                    }
                });
    }

    public void onParamsClick(View view) {
        String voiceName = editVoiceName.getText().toString().trim();
        SharedPreferencesUtil.saveOnlineVoiceName(SynthesisOnlineActivity.this, voiceName);
        SynthesisMixEngine.getInstance().setVoiceNameOnline(voiceName);
        SynthesisMixEngine.getInstance().setOnLineConnectTimeOut(3);
        SynthesisMixEngine.getInstance().setVolume(5);
        SynthesisMixEngine.getInstance().setSpeed(5);
        SynthesisMixEngine.getInstance().setPitch(5);
    }

    public void onSynthesizerClick(View view) {
        SynthesisMixEngine.getInstance().setSynthesizerCallback(mediaCallback);
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
}