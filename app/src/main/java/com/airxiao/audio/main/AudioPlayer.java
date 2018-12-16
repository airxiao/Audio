package com.airxiao.audio.main;

import android.media.MediaPlayer;
import android.text.TextUtils;


public class AudioPlayer {

    private static AudioPlayer mInstance;
    private AudioPlayStateListener mStateListener = null;
    private String mCurrentPath = null;
    public static AudioPlayer getInstance(){
        if (mInstance ==  null) {
            synchronized (AudioPlayer.class) {
                if (mInstance == null) {
                    mInstance = new AudioPlayer();
                }
            }
        }
        return mInstance;
    }

    public void play(String localPath, AudioPlayStateListener audioPlayStateListener) {
        if (TextUtils.isEmpty(localPath)) {
            return;
        }
        //释放上一次资源
        if (TextUtils.equals(localPath, mCurrentPath)) {
            MediaManager.stop();
            //当前重复点击
            if (audioPlayStateListener != null) {
                audioPlayStateListener.onFinish();
            }
            mStateListener = null;
            mCurrentPath = null;
            return;
        }
        if (mStateListener != null) {
            mStateListener.onFinish();
            mStateListener = null;
        }
        //记录本次
        mCurrentPath = localPath;
        mStateListener = audioPlayStateListener;
        if (mStateListener != null) {
            mStateListener.onStart();
        }
        MediaManager.playSound(localPath, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mStateListener != null) {
                    mStateListener.onFinish();
                }
                mStateListener = null;
                mCurrentPath = null;

            }
        }, new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mStateListener != null) {
                    mStateListener.onError();
                    stop();
                }
                return false;
            }
        }, mStateListener);

    }

    public void pause() {
        if (mStateListener != null) {
            MediaManager.pause();
        }
    }

    public void resume() {
        if (mStateListener != null) {
            MediaManager.resume();
        }
    }

    public void stop() {
        if (mStateListener != null) {
            mStateListener.onFinish();
        }
        mStateListener = null;
        mCurrentPath = null;
        MediaManager.stop();
    }

    public int getCurrentPosition() {
        return MediaManager.getCurrentPosition();
    }

    public interface AudioPlayStateListener {
        void onStart();

        void onPrepare();

        void onFinish();

        void onError();
    }
}
