package com.novelreader.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import java.util.Locale;

public class TTSManager implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private boolean isInitialized = false;
    private boolean isPlaying = false;
    private Context context;

    public TTSManager(Context context) {
        this.context = context;
        this.tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true;
            tts.setLanguage(Locale.CHINESE);
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    isPlaying = true;
                }

                @Override
                public void onDone(String utteranceId) {
                    isPlaying = false;
                }

                @Override
                public void onError(String utteranceId) {
                    isPlaying = false;
                }
            });
        } else {
            Log.e("TTS", "TTS 初始化失败");
        }
    }

    public void speak(String text) {
        if (isInitialized && !isPlaying) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_utterance");
        }
    }

    public void stop() {
        if (isInitialized) {
            tts.stop();
            isPlaying = false;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setSpeechRate(float rate) {
        if (isInitialized) {
            tts.setSpeechRate(rate);
        }
    }

    public void setPitch(float pitch) {
        if (isInitialized) {
            tts.setPitch(pitch);
        }
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}