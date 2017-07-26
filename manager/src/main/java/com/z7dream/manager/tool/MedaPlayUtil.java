package com.z7dream.manager.tool;

import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;

/**
 * Created by Z7Dream on 2017/3/1 17:36.
 * Email:zhangxyfs@126.com
 */

public class MedaPlayUtil implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private boolean isPrepared;
    private String mediaUrl;
    private PlayVoiceListener mPlayVoiceListener;

    public MedaPlayUtil() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayVoiceListener != null)
            mPlayVoiceListener.onComplete();
    }

    public boolean isSame(String url) {
        return !TextUtils.isEmpty(mediaUrl) && !TextUtils.isEmpty(url) && TextUtils.equals(mediaUrl, url);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void start() {
        if (isPrepared && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (isPrepared && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void toStart(String url) {
        mediaUrl = url;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        isPrepared = false;
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destory() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void setOnPlayVoiceListener(PlayVoiceListener playVoiceListener) {
        mPlayVoiceListener = playVoiceListener;
    }

    public interface PlayVoiceListener {
        void onComplete();
    }
}
