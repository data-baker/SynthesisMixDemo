package com.baker.tts.mix.utils;

import com.baker.tts.base.component.bean.BakerError;
import com.baker.tts.mix.lib.callback.SynthesisMixCallback;

public abstract class SynthesisMixCallbackImpl extends SynthesisMixCallback {
    @Override
    public void onSynthesisStarted() {

    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onWarning(String s, String s1) {

    }

    @Override
    public void onTaskFailed(BakerError bakerError) {

    }
}
