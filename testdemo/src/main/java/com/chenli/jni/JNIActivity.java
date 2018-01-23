package com.chenli.jni;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenli.commenlib.jni.JNICall;
import com.chenli.commenlib.jni.Native;
import com.chenli.commenlib.jni.Person;
import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.testmvp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/17.
 */

public class JNIActivity extends AppCompatActivity {

    @Bind(R.id.button1)
    Button button1;

    @Bind(R.id.button2)
    Button button2;

    @Bind(R.id.button3)
    Button button3;

    @Bind(R.id.button4)
    Button button4;

    @Bind(R.id.button5)
    Button button5;

    @Bind(R.id.button6)
    Button button6;

    @Bind(R.id.button7)
    Button button7;

    @Bind(R.id.button8)
    Button button8;

    @Bind(R.id.button9)
    Button button9;

    @Bind(R.id.imageview)
    ImageView imageview;

    @Bind(R.id.textview)
    TextView textview;

    @Bind(R.id.textview2)
    TextView textview2;

    @Bind(R.id.et_name)
    EditText et_name;

    @Bind(R.id.et_password)
    EditText et_password;

    @Bind(R.id.pay)
    EditText et_pay;

    @Bind(R.id.paymoney)
    Button paymoney;

    private ProgressDialog dialog;

    private Context context;



    Native aNative = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_layout);
        context = this;

        ButterKnife.bind(this);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JNICall jniCall = new JNICall();
                //jniCall.test();
                //JNICall.getStringFromJNI();

                JNICall call = new JNICall();
                String path = Environment.getExternalStorageDirectory().getPath() + "/input.mp4";
                call.getMediaInfo(path);

            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JNICall call = new JNICall();
                call.pauseAudioPlayer();

                //JNICall.setPersonToJNI(new Person(18, "jobs"));
//                aNative = new Native();
//                aNative.nativeInitilize();
//                aNative.nativeThreadStart();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LogUtils.e("hangce", "getPersonFromJNI ============= " + JNICall.getPersonFromJNI().getAge()+ "," + JNICall.getPersonFromJNI().getName());
                //aNative.nativeThreadStop();

                JNICall call = new JNICall();
                call.stopAudioPlayer();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delaJavaPicture1();
                JNICall call = new JNICall();
                call.startAudioPlayer();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delaJavaPicture2();
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delaJavaPicture3();
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview2.setText(JNICall.getStringFromProtocol());
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview2.setText(JNICall.getstringAvcodeInfo());
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview2.setText(JNICall.getStringFormatInfo());
            }
        });

        paymoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payMoney();
            }
        });

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            int result = msg.what;
            switch (result) {
                case 1:
                    Toast.makeText(context, "付款成功", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(context, "余额不足", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(context, "付款失败", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    private void payMoney() {
        final String name = et_name.getText().toString().trim();
        final String pwd = et_password.getText().toString();
        final String pay = et_pay.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pay)) {
            showToast("用户名，密码，金额一个都不能少");
            return;
        }

        //dialog.setMessage("正在处理中，请稍后...");
        //dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int payInt = Integer.parseInt(pay);
                int backNum = getInt(name,pwd,payInt);
                handler.sendEmptyMessage(backNum);
            }
        }).start();
    }

    public native int getInt(String name,String pwd,int payInt);
    static {
        System.loadLibrary("native-lib");
    }

    private void showToast(String s) {
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    public void showProgresstDialog(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                    dialog = null;
                }
                dialog = new ProgressDialog(context);
                dialog.setMessage(message);
                dialog.show();
            }
        });
    }

    private void delaJavaPicture3() {
        long current = System.currentTimeMillis();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img3);
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(copy);
        Paint paint = new Paint();
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(bitmap,0,0,paint);
        long end = System.currentTimeMillis();
        imageview.setImageBitmap(copy);
        textview.setText("消耗时间:" + (end-current));
    }

    private void delaJavaPicture2() {
        long current = System.currentTimeMillis();
        Bitmap bitmap = ConvertGrayImg2(R.mipmap.img3);
        long end = System.currentTimeMillis();
        imageview.setImageBitmap(bitmap);
        textview.setText("消耗时间:" + (end-current));
    }

    private void delaJavaPicture1() {
        long current = System.currentTimeMillis();
        Bitmap bitmap = ConvertGrayImg1(R.mipmap.img3);
        long end = System.currentTimeMillis();
        imageview.setImageBitmap(bitmap);
        textview.setText("消耗时间:" + (end-current));
    }

    private Bitmap ConvertGrayImg1(int imageId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w*h];
        bitmap.getPixels(pix,0,w,0,0,w,h);
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = bitmap.getPixel(i, j);
                int red = Color.red(color);
                int blue = Color.blue(color);
                int green = Color.green(color);
                int alpha = Color.alpha(color);
                int max = (red + blue + green)/3;
                int argb = Color.argb(alpha, max, max, max);
                copy.setPixel(i,j,argb);
            }
        }
        return copy;
    }

    private Bitmap ConvertGrayImg2(int imageId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w*h];
        bitmap.getPixels(pix,0,w,0,0,w,h);
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int col = bitmap.getPixel(i,j);
                int alpha = col & 0xFF000000;
                int red = (col&0x00FF0000)>>16;
                int green = (col&0x0000FF00)>>8;
                int blue = (col&0x000000FF);
                int max = (red + green + blue) / 3;
                int newColor = alpha | (max << 16) | (max << 8) | max ;
                copy.setPixel(i,j,newColor);
            }
        }
        return copy;
    }
}
