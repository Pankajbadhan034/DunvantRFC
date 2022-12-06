package com.dunvant.application.parent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.paymentGatewayUtilities.AvenuesParams;
import com.dunvant.application.parent.paymentGatewayUtilities.Constants;
import com.dunvant.application.parent.paymentGatewayUtilities.RSAUtility;
import com.dunvant.application.parent.paymentGatewayUtilities.ServiceHandler;
import com.dunvant.application.parent.paymentGatewayUtilities.ServiceUtility;
import com.dunvant.application.utils.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ParentPaymentGatewayWebViewScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    private ProgressDialog dialog;
    Intent mainIntent;
    String html, encVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_payment_gateway_web_view_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mainIntent = getIntent();

//        // Calling async task to get display content
//        new RenderView().execute();
    }

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(ParentPaymentGatewayWebViewScreen.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());
            headers.add("x-access-did:"+Utilities.getDeviceId(ParentPaymentGatewayWebViewScreen.this));
            headers.add("x-access-dtype:"+Utilities.DEVICE_TYPE);

            String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params, headers);
            //System.out.println("URL :: "+mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL));
            //System.out.println("Params :: "+params);
            //System.out.println("Response :: "+vResponse);

            if(!ServiceUtility.chkNull(vResponse).equals("") && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR")==-1){

                try {
                    JSONObject resObj = new JSONObject(vResponse);

                    String rsaKey = resObj.getString("rsa_key");

                    StringBuffer vEncVal = new StringBuffer("");
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                    encVal = RSAUtility.encrypt(vEncVal.substring(0,vEncVal.length()-1), rsaKey);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ParentPaymentGatewayWebViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (dialog.isShowing())
                dialog.dismiss();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    // process the html as needed by the app

                    //System.out.println("HTML "+html);

                    html = html.replace("<head><head></head><body>", "");
                    html = html.replace("</body></head>", "");

                    /*String status = null;
                    if(html.indexOf("Failure")!=-1){
                        status = "Transaction Declined!";
                    }else if(html.indexOf("Success")!=-1){
                        status = "Transaction Successful!";
                    }else if(html.indexOf("Aborted")!=-1){
                        status = "Transaction Cancelled!";
                    }else{
                        status = "Status Not Known!";
                    }*/
                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                    try{
                        JSONObject jsonObject = new JSONObject(html);
                        String response = jsonObject.getString("order_status");

                        System.out.println("payment response :: "+jsonObject);

                        if(response.equalsIgnoreCase("Failure")){
                            finish();
                        }else{
                            Intent intent = new Intent(getApplicationContext(), ParentStatusScreen.class);
                            intent.putExtra("response", html);
                            startActivity(intent);
                            finish();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

//                    Intent intent = new Intent(getApplicationContext(), ParentStatusScreen.class);
//                    intent.putExtra("response", html);
//                    startActivity(intent);
//                    finish();
                }
            }

            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);

                    //System.out.println("on page finished " + url);

//                    if(url.indexOf("/ccavResponseHandler.jsp")!=-1){
                    if (url.indexOf("/response_handler") != -1) {

                        //System.out.println("inside ccav jsp");

                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });

            try {
                /* An instance of this class will be registered as a JavaScript interface */
                StringBuffer params = new StringBuffer();
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));

                String vPostParams = params.substring(0,params.length()-1);

                webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
//                showToast("Exception occurred while opening webview.");
                showToast("Exception occurred while opening Payment Gateway.");
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }
}