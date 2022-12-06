package com.dunvant.application.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dunvant.application.utils.Utilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class GetWebServiceAsync  extends AsyncTask<String, Void, String> {

    Context context;
    ProgressDialog pDialog;
    String tag;
    IWebServiceCallback webServiceCallback;

    public GetWebServiceAsync(Context context, String tag, IWebServiceCallback webServiceCallback){
        this.context = context;
        this.tag = tag;
        this.webServiceCallback = webServiceCallback;
    }

    @Override
    public void onPreExecute() {
        pDialog = Utilities.createProgressDialog(context);
    }

    @Override
    public String doInBackground(String... urls) {

        String strResponse = null;

        try {

            String url = urls[0];

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            httpGet.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpGet.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            Log.v("header:: x-access-did ",Utilities.getDeviceId(context));
            Log.v("header:: x-access-dtype",Utilities.DEVICE_TYPE);

            System.out.println("URL :: "+url);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                try {
                    strResponse = EntityUtils.toString(resEntity).trim();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strResponse;
    }

    @Override
    public void onPostExecute(String response) {

        if(response != null){
            int maxLogSize = 1000;
            for(int i = 0; i <= response.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > response.length() ? response.length() : end;
                Log.v("Complete_Response::", response.substring(start, end));
            }
        }

        System.out.println("Response "+response);
        webServiceCallback.onWebServiceResponse(response, tag);

        if(pDialog != null && pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}