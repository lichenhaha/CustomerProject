package com.chenli.mediamodel;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chenli.commenlib.Media.MediaExtrAndMuxer;
import com.chenli.commenlib.util.mainutil.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.button1)
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcPaht = Environment.getExternalStorageDirectory() + "/input.mp4";
                String desPath = Environment.getExternalStorageDirectory() + "/output.aac";
                MediaExtrAndMuxer mediaExtrAndMuxer = new MediaExtrAndMuxer();
                mediaExtrAndMuxer.getAudioFromVideo(srcPaht, desPath, new MediaExtrAndMuxer.AudioDecodeListener() {
                    @Override
                    public void decodeOver() {
                        LogUtils.e("chenli","-----------------");
                    }

                    @Override
                    public void decodeFail() {
                        LogUtils.e("chenli","++++++++++++++++++");
                    }
                });
            }
        });
    }
}
