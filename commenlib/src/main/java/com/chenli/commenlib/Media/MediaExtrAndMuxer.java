package com.chenli.commenlib.Media;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class MediaExtrAndMuxer {

    private MediaExtractor mMediaExtractor;

    int videoTrackIndex = -1;
    int audioTrackIndex = -1;
    private MediaMuxer mMediaMuxer;
    private int mForamerate;

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

}
