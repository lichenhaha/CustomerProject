package com.chenli.gameproject.newgl;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.http.Body;

/**
 * Created by Administrator on 2018/2/3.
 */

public class TextResourceReader {

    public static String readTextFileFromResource(Context context, int resourceId){
        StringBuilder body = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String nextLine = null;
        try {
            while ((nextLine = bufferedReader.readLine()) != null){
                body.append(nextLine);
                //body.append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return body.toString();
    }

}
