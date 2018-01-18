package com.chenli.commenlib.Media;

import android.icu.util.IslamicCalendar;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by Administrator on 2018/1/18.
 */

public class AudioCapturer {
    public static final String TAG = "chenli";

    private static final int DEFAULT_SOURCE = MediaRecorder.AudioSource.MIC;//麦克风
    private static final int DEFAULT_SAMPLE_RATE = 44100;//采样率
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;//单声道
    private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;//位宽

    private boolean mIsCaptureStarted = false;
    private AudioRecord mAudioRecord;
    private volatile boolean mIsLoopExit = false;
    private Thread mCaptureThread;
    private int bufferSizeInBytes;

    public boolean isCaptureStarted() {
        return mIsCaptureStarted;
    }

    /**
     * 启动
     * @return
     */
    public boolean startCapture(){
        return startCapture(DEFAULT_SOURCE,DEFAULT_SAMPLE_RATE,DEFAULT_CHANNEL_CONFIG,DEFAULT_AUDIO_FORMAT);
    }

    public boolean startCapture(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat){
        if (mIsCaptureStarted){
            Log.e(TAG, "Capture already started !");
            return false;
        }
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        if (bufferSizeInBytes == AudioRecord.ERROR_BAD_VALUE){
            Log.e(TAG, "Invalid parameter !");
            return false;
        }
        mAudioRecord = new AudioRecord(audioSource,sampleRateInHz,channelConfig, audioFormat, bufferSizeInBytes);
        if (mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED){
            Log.e(TAG, "AudioRecord initialize fail !");
            return false;
        }
        mAudioRecord.startRecording();//启动录音
        //启动线程
        mCaptureThread = new Thread(new AudioCaptureRunnable());
        mCaptureThread.start();
        mIsCaptureStarted = true;
        Log.e(TAG, "Start audio capture success !");
        return true;
    }

    public void stopCapture(){
        //判断录音是否启动
        if (!mIsCaptureStarted){
            return;
        }
        mIsLoopExit = true;
        try {
            mCaptureThread.interrupt();
            mCaptureThread.join(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            mAudioRecord.stop();
        }
        mAudioRecord.release();//释放资源
        mIsCaptureStarted = false;
        mAudioFrameCapturedListener = null;
        Log.d(TAG, "Stop audio capture success !");

    }

    private class AudioCaptureRunnable implements Runnable{
        @Override
        public void run() {
            while (!mIsLoopExit){
                byte[] buffer = new byte[bufferSizeInBytes];
                int read = mAudioRecord.read(buffer, 0, bufferSizeInBytes);
                if (read == AudioRecord.ERROR_INVALID_OPERATION) {
                    Log.e(TAG , "Error ERROR_INVALID_OPERATION");
                }else if (read == AudioRecord.ERROR_BAD_VALUE) {
                    Log.e(TAG , "Error ERROR_BAD_VALUE");
                }else {
                    if (mAudioFrameCapturedListener != null) {
                        mAudioFrameCapturedListener.onAudioFrameCaptured(buffer);
                    }
                    Log.d(TAG , "OK, Captured "+read+" bytes !");
                }
            }
        }
    }

    private OnAudioFrameCapturedListener mAudioFrameCapturedListener;
    public void setOnAudioFrameCapturedListener(OnAudioFrameCapturedListener listener) {
        mAudioFrameCapturedListener = listener;
    }

    /**
     * 回调方法。
     */
    public interface OnAudioFrameCapturedListener {
        public void onAudioFrameCaptured(byte[] audioData);
    }



}
