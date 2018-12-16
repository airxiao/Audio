package com.airxiao.audio.main;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

public class MediaManager {


    private static MediaPlayer mMediaPlayer;
    private static boolean isPause;
    private static boolean isPrepared;

    /**
     * 播放音乐
     *
     * @param filePath
     * @param onCompletionListener
     */
    public static void playSound(final String filePath,
                                 OnCompletionListener onCompletionListener,
                                 OnErrorListener errorListener, final AudioPlayer.AudioPlayStateListener listener) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            //设置一个error监听器
            mMediaPlayer.setOnErrorListener(errorListener);
        } else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    isPrepared = true;
                    listener.onPrepare();
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {

        }
    }

    /**
     * 暂停播放
     */
    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { //正在播放的时候
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 当前是isPause状态
     */
    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    public static int getCurrentPosition() {
        if(mMediaPlayer != null && isPrepared) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public static int getDuration() {
        if(mMediaPlayer != null && isPrepared) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 释放资源
     */
    public static void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    public static void stop() {
        if (mMediaPlayer != null) {
            isPrepared = false;
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
