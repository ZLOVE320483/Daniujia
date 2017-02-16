package com.xiaojia.daniujia.ui.views.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import com.xiaojia.daniujia.FileStorage;

import java.io.File;
import java.io.IOException;

public class AudioRecorder implements RecordStrategy {

	private MediaRecorder recorder;
	private String fileName;
	private String fileFolder = FileStorage.APP_VOICE_DIR;

	private boolean isRecording = false;

	@Override
	public void ready() {
		fileName = getCurrentDate();
		recorder = new MediaRecorder();
		File file = new File(fileFolder);
		if (!file.exists()) {
			file.mkdirs();
		}
		recorder.setOutputFile(fileFolder + "/" + fileName + ".amr");
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置MediaRecorder的音频源为麦克风
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);// 设置MediaRecorder录制的音频格式
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置MediaRecorder录制音频的编码为amr
	}

	// 以当前时间作为文件名
	private String getCurrentDate() {
		return FileStorage.VOICE_PREFIX + System.currentTimeMillis();
	}

	@Override
	public void start() {
		if (!isRecording) {
			try {
				recorder.prepare();
				recorder.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isRecording = true;
		}
	}

	@Override
	public void stop() {
		if (isRecording) {
			recorder.stop();
			recorder.release();
			isRecording = false;
		}
	}

	@Override
	public void deleteOldFile() {
		File file = new File(fileFolder + "/" + fileName + ".amr");
		file.deleteOnExit();
	}

	@Override
	public double getAmplitude() {
		if (!isRecording) {
			return 0;
		}
		try {
			return recorder.getMaxAmplitude();
		} catch (IllegalStateException e) {
			return 0;
		}
	}

	@Override
	public String getFilePath() {
		return fileFolder + "/" + fileName + ".amr";
	}
	
	@Override
	public int getDuration() {
		MediaPlayer mediaPlayer = new MediaPlayer();
        int duration = 0;
        try {
        	File file = new File(getFilePath());
            mediaPlayer.setDataSource(file.getAbsolutePath());

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            duration = mediaPlayer.getDuration() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mediaPlayer.release();
        }
        return duration;
	}
}
