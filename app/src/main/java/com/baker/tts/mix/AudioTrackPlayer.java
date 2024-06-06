package com.baker.tts.mix;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;


import java.util.concurrent.LinkedBlockingQueue;

public class AudioTrackPlayer {
    private static final String TAG = "AudioTrackPlayer";
    private final int SAMPLE_RATE = 16000;
    private boolean playing = false;
    private final LinkedBlockingQueue<byte[]> audioQueue = new LinkedBlockingQueue<>();

    // 初始化播放器
    private int iMinBufSize;

    private AudioTrack audioTrack;
    private byte[] tempData;

    private final Thread ttsPlayerThread;

    public AudioTrackPlayer() {
        release();
        iMinBufSize = AudioTrack.getMinBufferSize( SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        if (audioTrack == null) {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,  SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO
                    , AudioFormat.ENCODING_PCM_16BIT,
                    iMinBufSize * 10, AudioTrack.MODE_STREAM);
        }
        if (tempData != null) {
            tempData = null;
        }
        playing = true;
        ttsPlayerThread = new Thread(() -> {
            while (playing) {
                tempData = audioQueue.poll();
                if (tempData == null) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
                        audioTrack.play();
                    }
                    audioTrack.write(tempData, 0, tempData.length);
                }
            }
        });
        ttsPlayerThread.start();
    }

    public void cleanAudioData() {
        audioQueue.clear();
    }

    public void setAudioData(byte[] data) {
        audioQueue.offer(data);
    }

    public void play() {
        if (audioTrack != null && audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack.play();
        }
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void stop() {
        audioTrack.pause();
        audioTrack.flush();
        audioQueue.clear();
    }

    public void release() {
        playing = false;
        if (audioTrack != null) {
            audioTrack.flush();
            audioQueue.clear();
            audioTrack.release();
            audioTrack = null;
        }
    }
}