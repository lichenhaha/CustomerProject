package com.chenli.media;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chenli.commenlib.util.mainutil.FileUtils;
import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.testmvp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/17.
 */

public class TakeActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Bind(R.id.button1)
    Button button1;

    @Bind(R.id.imageview)
    ImageView imageView;
    private static String currentPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_layout);

        ButterKnife.bind(this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

    }

    public static File getTakePicture() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp + "_";
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",file);
        currentPath = "file:" + image.getAbsolutePath();
        LogUtils.e("hangce", "currentPath = " + currentPath);
        return image;
    }

    private void takePhoto() {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeIntent.resolveActivity(getPackageManager())!= null){
            File picture =null;
            try {
                 picture = getTakePicture();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (picture != null){
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                startActivityForResult(takeIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE){
            LogUtils.e("++++++++++++++++");
            galleryAddPic();
        }
    }
}
