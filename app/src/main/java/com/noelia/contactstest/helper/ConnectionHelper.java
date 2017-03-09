package com.noelia.contactstest.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noelia.contactstest.model.BaseResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by nochita on 07/03/2017.
 */
public class ConnectionHelper {

    private static String TAG = ConnectionHelper.class.getName();
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static String get(String url, Context context){

        Log.d(TAG, "getting from: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = getClient();
        String responseBodyString = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            Log.d(TAG, "status code: " + response.code());
            ResponseBody responseBody = response.body();
            if(response.code() < 500){
                responseBodyString = responseBody.string();
                Log.d(TAG, " response: " + responseBodyString);
            } else{
                Log.e(TAG, "Error 500 in " + request.toString());
                // TODO implement firebase or other crash manager
//                FirebaseCrash.report(new Exception("Error 500 in " + request.toString()));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            // TODO implement firebase or other crash manager
//            FirebaseCrash.report(new Exception("Error 500 in " + request.toString()));
        }
        return responseBodyString;
    }

    private static OkHttpClient getClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

}
