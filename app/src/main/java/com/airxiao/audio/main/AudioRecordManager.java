package com.airxiao.audio.main;

import android.media.MediaRecorder;
import android.util.Log;


import com.airxiao.audio.Utils;

import java.io.File;

public class AudioRecordManager {
	private MediaRecorder mMediaRecorder;
	private String mFilePath;
	private String mDir;
	private boolean isPrepared = false;
	public AudioStateListener mListener;

	private static AudioRecordManager mInstance;

	private AudioRecordManager(){
	}

	public interface AudioStateListener{
		void wellPrepared(String path);
	}

	public void setOnAudioStateListener(AudioStateListener listener){
		mListener = listener;
	}

	public static AudioRecordManager getInstance(){
		if (mInstance ==  null) {
			synchronized (AudioRecordManager.class) {
				if (mInstance == null) {
					mInstance = new AudioRecordManager();
				}
			}
		}
		return mInstance;
	}

	public void prepareAudio(String rootPath){
		isPrepared = false;
		mDir = rootPath;
		String date = Utils.getDateYMD(System.currentTimeMillis());
		File dir = new File(mDir + "/" + date);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		try {
			String fileName = generateFileName();
			File file = new File(dir,fileName);
			if (!Utils.isFile(file)) {
				file.createNewFile();
			}
			mFilePath = file.getAbsolutePath();
			mMediaRecorder = new MediaRecorder();
			mMediaRecorder.setOutputFile(file.getAbsolutePath());
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置mediarecord的音频源
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//设置音频格式     //AAC_ADTS
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置编码为amr //AAC
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			isPrepared = true;
			//准备结束
			if (mListener != null) {
				mListener.wellPrepared(file.getAbsolutePath());
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private String generateFileName() {
		return System.currentTimeMillis()+".mp3";//acc
	}

	public int getVoiceLevel(int maxLevel){
		if(!isPrepared){
			return 1;
		}
		try {
			if (mMediaRecorder != null) {
				int amplitude = mMediaRecorder.getMaxAmplitude();
				int ratio = amplitude / 100;
				if(ratio < 1){
					return 1;
				}
				int db = (int) (20 * Math.log10(ratio));
				int level = maxLevel * db / 50;
				if(level > maxLevel){
					level = maxLevel;
				}
				return level;
			} else {
				Log.e("mMediaRecorder","mMediaRecorder null" +Thread.currentThread().getId());
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public void release(){
		mFilePath = null;
		try {
			mMediaRecorder.stop();
			mMediaRecorder.reset();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		try {
			mMediaRecorder.release();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		mMediaRecorder = null;

	}

	public void cancel(){
		release();
		if (mFilePath != null) {
			File file = new File(mFilePath);
			file.delete();
			mFilePath = null;
		}
	}

	public String getCurrentFilePath() {
		return mFilePath;
	}
}
