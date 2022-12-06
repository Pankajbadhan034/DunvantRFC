package com.dunvant.application.coach.adapters;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.DocumentBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachUploadDocumentsScreen;
import com.dunvant.application.coach.fragments.CoachUploadedDocumentsFragment;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CoachDocumentListingAdapter extends BaseAdapter implements IWebServiceCallback{
    String documentId;
    String fileName;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<DocumentBean> documentsList;
    LayoutInflater layoutInflater;

    private final String DELETE_DOCUMENT = "DELETE_DOCUMENT";

//    CoachManageDocumentsFragment parentDocumentsListingFragment;
CoachUploadedDocumentsFragment coachUploadedDocumentsFragment;

    public CoachDocumentListingAdapter(Context context, ArrayList<DocumentBean> documentsList, /*CoachManageDocumentsFragment parentDocumentsListingFragment*/ CoachUploadedDocumentsFragment coachUploadedDocumentsFragment) {
        this.context = context;
        this.documentsList = documentsList;
        layoutInflater = LayoutInflater.from(context);
//        this.parentDocumentsListingFragment = parentDocumentsListingFragment;
        this.coachUploadedDocumentsFragment = coachUploadedDocumentsFragment;

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return documentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return documentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_document_item, null);

        RelativeLayout mainRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
        ImageView docIcon = (ImageView) convertView.findViewById(R.id.docIcon);
        TextView docTitle = (TextView) convertView.findViewById(R.id.docTitle);
        TextView comments = (TextView) convertView.findViewById(R.id.comments);
        ImageView edit = (ImageView) convertView.findViewById(R.id.edit);
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        ImageView download = (ImageView) convertView.findViewById(R.id.download);

        final DocumentBean documentBean = documentsList.get(position);

        docTitle.setText(documentBean.getTitle());
        comments.setText(documentBean.getComments());

        docTitle.setTypeface(linoType);
        comments.setTypeface(helvetica);

        if(position % 2 == 0) {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            docTitle.setTextColor(context.getResources().getColor(R.color.white));
            comments.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            docTitle.setTextColor(context.getResources().getColor(R.color.black));
            comments.setTextColor(context.getResources().getColor(R.color.black));
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 fileName  = documentBean.getFileURL();
                 documentId = documentBean.getDocumentId();
                new asyncDownload().execute();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadDocuments = new Intent(context, CoachUploadDocumentsScreen.class);
                uploadDocuments.putExtra("isEditMode", true);
                uploadDocuments.putExtra("documentToEdit", documentBean);
                context.startActivity(uploadDocuments);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_delete_document);

                TextView yes = (TextView) dialog.findViewById(R.id.yes);
                TextView no = (TextView) dialog.findViewById(R.id.no);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();


                        if(Utilities.isNetworkAvailable(context)) {

                            List<NameValuePair> nameValuePairList = new ArrayList<>();
                            nameValuePairList.add(new BasicNameValuePair("document_id", documentBean.getDocumentId()));

                            String webServiceUrl = Utilities.BASE_URL + "account/delete_document";

                            ArrayList<String> headers = new ArrayList<>();
                            headers.add("X-access-uid:"+loggedInUser.getId());
                            headers.add("X-access-token:"+loggedInUser.getToken());

                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, DELETE_DOCUMENT, CoachDocumentListingAdapter.this, headers);
                            postWebServiceAsync.execute(webServiceUrl);

                        } else {
                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        return convertView;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case DELETE_DOCUMENT:

                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

//                            parentDocumentsListingFragment.getDocumentsListing();
                            coachUploadedDocumentsFragment.getDocumentsListing();
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                break;
        }
    }

    //video download from url async task
    private class asyncDownload extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pDialog = Utilities.createProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... params) {
            String strResponse = null;
            try {
                File rootFile = new File("/sdcard/DCIM/");
                URL url = new URL(Utilities.BASE_URL+""+fileName+""+loggedInUser.getToken()+""+documentId);
                //System.out.println(""+url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                FileOutputStream f = new FileOutputStream(new File(rootFile,
                        fileName));
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    f.write(buffer, 0, len1);
                }
                f.close();
                strResponse = "success";
            } catch (Exception e) {
                e.printStackTrace();
                strResponse = "null";
            }
            return strResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("success")){
                pDialog.dismiss();
                String path = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+fileName;
                Toast.makeText(context, "Document saved location:"+path, Toast.LENGTH_SHORT).show();
            }else {
                pDialog.dismiss();
                Toast.makeText(context, "Something went wrong, please try after sometime", Toast.LENGTH_LONG ).show();
            }
        }
    }
}