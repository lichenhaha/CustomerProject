package com.chenli.commenlib.Media;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.chenli.commenlib.util.mainutil.LogUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class MediaExtrAndMuxer {

    private static final String TAG = "chenli";
    private MediaExtractor mMediaExtractor;

    int videoTrackIndex = -1;
    int audioTrackIndex = -1;
    private MediaMuxer mMediaMuxer;
    private int mForamerate;

    private MediaExtractor mediaExtractor;
    private MediaMuxer mediaMuxer;

    private Handler handler = new Handler(Looper.getMainLooper());

    public void getAudioFromVideo(String srcPath, final String desPath, final AudioDecodeListener listener){
        mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(srcPath);
            LogUtils.e("chenli", " , TrackCount = " + mediaExtractor.getTrackCount());
            for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
                MediaFormat format = mediaExtractor.getTrackFormat(i);
                String mine = format.getString(MediaFormat.KEY_MIME);
                if (mine.startsWith("audio")){
                    mediaExtractor.selectTrack(i);
                    audioTrackIndex = i;
                    break;
                }
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mediaMuxer = new MediaMuxer(desPath,MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null){
                                    listener.decodeFail();
                                }
                            }
                        });
                    }
                    MediaFormat format = mediaExtractor.getTrackFormat(audioTrackIndex);
                    int writeAudioIndex = mediaMuxer.addTrack(format);
                    mediaMuxer.start();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE));
                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    //mediaExtractor.readSampleData(byteBuffer,0);
                    //if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC){
                    //   mediaExtractor.advance();
                    //}
                    while (true){
                        int readSampleSize = mediaExtractor.readSampleData(byteBuffer, 0);
                        Log.e(TAG, "---读取音频数据，当前读取到的大小-----：：："+readSampleSize);
                        if (readSampleSize < 0){
                            break;
                        }
                        bufferInfo.size = readSampleSize;
                        bufferInfo.flags = mediaExtractor.getSampleFlags();
                        bufferInfo.offset = 0 ;
                        bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                        Log.e(TAG, "----写入音频数据---当前的时间戳：：：" + mediaExtractor.getSampleTime());
                        mediaMuxer.writeSampleData(writeAudioIndex,byteBuffer,bufferInfo);
                        mediaExtractor.advance();//移动到下一帧
                    }
                    mediaMuxer.release();
                    mediaMuxer = null;
                    mediaExtractor.release();
                    mediaExtractor = null;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                           if (listener != null){
                               listener.decodeOver();
                           }
                        }
                    });
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null){
                listener.decodeFail();
            }
        }
    }

    //将原文件解码出来
    public void getPCMFromAudio(String srcPath, final String desPath, final AudioDecodeListener listener){
        final MediaExtractor extractor = new MediaExtractor();
        int audioTrack = -1;
        try {
            extractor.setDataSource(srcPath);
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (mime.startsWith("audio")){
                    audioTrack = i;
                    break;
                }
            }
            extractor.selectTrack(audioTrack);
            final int finalAudioTrack = audioTrack;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaFormat trackFormat = extractor.getTrackFormat(finalAudioTrack);
                    try {
                        //初始化音频的解码器
                        MediaCodec audioCodec = MediaCodec.createEncoderByType(trackFormat.getString(MediaFormat.KEY_MIME));
                        audioCodec.configure(trackFormat,null,null,0);
                        audioCodec.start();

                        ByteBuffer[] inputBuffers = audioCodec.getInputBuffers();
                        ByteBuffer[] outputBuffers = audioCodec.getOutputBuffers();
                        MediaCodec.BufferInfo decodeBufferInfo = new MediaCodec.BufferInfo();
                        MediaCodec.BufferInfo inputInfo = new MediaCodec.BufferInfo();

                        FileOutputStream fos = new FileOutputStream(desPath);
                        boolean codeOver = false;
                        boolean inputDone = false;//整体输入结束标志
                        while (!codeOver){
                            if (!inputDone){
                                for (int i = 0; i < inputBuffers.length; i++) {
                                    //遍历所以的编码器 然后将数据传入之后 再去输出端取数据
                                    int inputIndex = audioCodec.dequeueInputBuffer(0);
                                    if (inputIndex > 0){
                                        ByteBuffer inputBuffer = inputBuffers[inputIndex];
                                        inputBuffer.clear();
                                        int sampleSize = extractor.readSampleData(inputBuffer, 0);
                                        if (sampleSize < 0) {
                                            audioCodec.queueInputBuffer(inputIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                            inputDone = true;
                                        } else {
                                            inputInfo.offset = 0;
                                            inputInfo.size = sampleSize;
                                            inputInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                                            inputInfo.presentationTimeUs = extractor.getSampleTime();
                                            Log.e("hero","往解码器写入数据---当前帧的时间戳----"+inputInfo.presentationTimeUs);
                                            audioCodec.queueInputBuffer(inputIndex, inputInfo.offset, sampleSize, inputInfo.presentationTimeUs, 0);//通知MediaDecode解码刚刚传入的数据
                                            extractor.advance();//MediaExtractor移动到下一取样处
                                        }
                                    }
                                }
                            }
                            boolean decodeOutputDone = false;
                            byte[] chunkPCM;
                            while (!decodeOutputDone) {
                                int outputIndex = audioCodec.dequeueOutputBuffer(decodeBufferInfo, 0);
                                if (outputIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                                    /**没有可用的解码器output*/
                                    decodeOutputDone = true;
                                } else if (outputIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                                    outputBuffers = audioCodec.getOutputBuffers();
                                } else if (outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                                    MediaFormat newFormat = audioCodec.getOutputFormat();
                                } else if (outputIndex < 0) {
                                } else {
                                    ByteBuffer outputBuffer;
                                    if (Build.VERSION.SDK_INT >= 21) {
                                        outputBuffer = audioCodec.getOutputBuffer(outputIndex);
                                    } else {
                                        outputBuffer = outputBuffers[outputIndex];
                                    }

                                    chunkPCM = new byte[decodeBufferInfo.size];
                                    outputBuffer.get(chunkPCM);
                                    outputBuffer.clear();

                                    fos.write(chunkPCM);//数据写入文件中
                                    fos.flush();
                                    Log.e("hero","---释放输出流缓冲区----:::"+outputIndex);
                                    audioCodec.releaseOutputBuffer(outputIndex, false);
                                    if ((decodeBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                                        /**
                                         * 解码结束，释放分离器和解码器
                                         * */
                                        extractor.release();

                                        audioCodec.stop();
                                        audioCodec.release();
                                        codeOver = true;
                                        decodeOutputDone = true;
                                    }
                                }

                            }
                        }
                        fos.close();//输出流释放
                        if (listener != null){
                            listener.decodeOver();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.decodeFail();
            }
        }
    }


    public void setSourceData(String path,String despath){
        mMediaExtractor = new MediaExtractor();
        try {
            mMediaExtractor.setDataSource(path);
            for (int i = 0; i < mMediaExtractor.getTrackCount(); i++) {
                //获取码流的详细格式/配置信息
                MediaFormat format = mMediaExtractor.getTrackFormat(i);
                String mine = format.getString(MediaFormat.KEY_MIME);
                if (!mine.startsWith("video/")){
                    continue;
                }
                mForamerate = format.getInteger(MediaFormat.KEY_FRAME_RATE);
                mMediaExtractor.selectTrack(i);
                mMediaMuxer = new MediaMuxer(despath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                videoTrackIndex = mMediaMuxer.addTrack(format);
                mMediaMuxer.start();
            }

            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            info.presentationTimeUs = 0 ;
            ByteBuffer buffer = ByteBuffer.allocate(500 * 1024);
            while (true){
                int sampleSize = mMediaExtractor.readSampleData(buffer, 0);
                if (sampleSize < 0){
                    break;
                }
                mMediaExtractor.advance();
                info.offset = 0 ;
                info.size = sampleSize;
                info.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME;
                info.presentationTimeUs += 1000*1000/mForamerate;
                mMediaMuxer.writeSampleData(videoTrackIndex,buffer,info);
            }
            mMediaExtractor.release();
            mMediaMuxer.stop();
            mMediaMuxer.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface AudioDecodeListener{
        void decodeOver();
        void decodeFail();
    }


}
