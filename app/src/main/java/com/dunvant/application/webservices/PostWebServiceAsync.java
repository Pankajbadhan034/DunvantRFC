package com.dunvant.application.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.dunvant.application.utils.Utilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class PostWebServiceAsync extends AsyncTask<String, Void, String> {

    Context context;
    ProgressDialog pDialog;
    List<NameValuePair> nameValuePairs;
    String tag;
    IWebServiceCallback webServiceCallback;

    public PostWebServiceAsync(Context context, List<NameValuePair> nameValuePairs, String tag, IWebServiceCallback webServiceCallback){
        this.context = context;
        this.nameValuePairs = nameValuePairs;
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

            HttpPost httpPost = new HttpPost(url);

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            System.out.println("DID :: "+Utilities.getDeviceId(context));

            System.out.println("URL :: "+url);
            System.out.println("Data :: "+nameValuePairs);

            HttpResponse response = httpClient.execute(httpPost);
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

        System.out.println("Response :: "+response);

        webServiceCallback.onWebServiceResponse(response, tag);
        try{
            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}