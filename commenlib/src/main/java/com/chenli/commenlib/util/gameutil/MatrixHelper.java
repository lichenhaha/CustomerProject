package com.chenli.commenlib.util.gameutil;

/**
 * Created by Administrator on 2018/1/26.
 */

public class MatrixHelper {

    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f){
        float angleInRadians = (float) (yFovInDegrees*Math.PI/180.0);
        float a = (float) (1.0/Math.tan(angleInRadians/2.0));

        m[0] = a/aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f+n)/(f-n));
        m[11] = 0f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f*f*n)/(f-n));
        m[15] = 0f;

    }

}
