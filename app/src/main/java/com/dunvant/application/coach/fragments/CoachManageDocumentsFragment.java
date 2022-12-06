package com.dunvant.application.coach.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachUploadDocumentsScreen;
import com.dunvant.application.utils.Utilities;

public class CoachManageDocumentsFragment extends Fragment /*implements IWebServiceCallback*/{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    /*ListView documentsListView;
    TextView addNewDocument;

    private final String GET_DOCUMENTS = "GET_DOCUMENTS";

    ArrayList<DocumentBean> documentsList = new ArrayList<>();
    CoachDocumentListingAdapter parentDocumentListingAdapter;*/

    TextView uploadedDocuments;
    TextView receivedDocuments;

    TextView addNewDocument;

    CoachUploadedDocumentsFragment coachUploadedDocumentsFragment;
    CoachReceivedDocumentsFragment coachReceivedDocumentsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_fragment_manage_documents, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        /*documentsListView = (ListView) view.findViewById(R.id.documentsListView);
        addNewDocument = (TextView) view.findViewById(R.id.addNewDocument);

        addNewDocument.setTypeface(linoType);

        parentDocumentListingAdapter = new CoachDocumentListingAdapter(getActivity(), documentsList, CoachManageDocumentsFragment.this);
        documentsListView.setAdapter(parentDocumentListingAdapter);

        documentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DocumentBean clickedOnDocument = documentsList.get(position);
                Intent previewDocument = new Intent(getActivity(), ParentPreviewDocumentScreen.class);
                previewDocument.putExtra("clickedOnDocument", clickedOnDocument);
                startActivity(previewDocument);
            }
        });*/

        uploadedDocuments = (TextView) view.findViewById(R.id.uploadedDocuments);
        receivedDocuments = (TextView) view.findViewById(R.id.receivedDocuments);

        addNewDocument = (TextView) view.findViewById(R.id.addNewDocument);

        uploadedDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadedDocuments.setBackgroundColor(getResources().getColor(R.color.black));
                uploadedDocuments.setTextColor(getResources().getColor(R.color.white));

                receivedDocuments.setBackgroundColor(getResources().getColor(R.color.yellow));
                receivedDocuments.setTextColor(getResources().getColor(R.color.black));

                coachUploadedDocumentsFragment = new CoachUploadedDocumentsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, coachUploadedDocumentsFragment)
                        .commit();
            }
        });

        receivedDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receivedDocuments.setBackgroundColor(getResources().getColor(R.color.black));
                receivedDocuments.setTextColor(getResources().getColor(R.color.white));

                uploadedDocuments.setBackgroundColor(getResources().getColor(R.color.yellow));
                uploadedDocuments.setTextColor(getResources().getColor(R.color.black));

                coachReceivedDocumentsFragment = new CoachReceivedDocumentsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, coachReceivedDocumentsFragment)
                        .commit();
            }
        });

        addNewDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadDocuments = new Intent(getActivity(), CoachUploadDocumentsScreen.class);
                uploadDocuments.putExtra("isEditMode", false);
                startActivity(uploadDocuments);
            }
        });

        uploadedDocuments.setTypeface(linoType);
        receivedDocuments.setTypeface(linoType);
        addNewDocument.setTypeface(linoType);

        uploadedDocuments.performClick();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (coachUploadedDocumentsFragment != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(coachUploadedDocumentsFragment).commit();
            }
            if (coachReceivedDocumentsFragment != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(coachReceivedDocumentsFragment).commit();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onResume() {
        super.onResume();
        getDocumentsListing();
    }

    public void getDocumentsListing() {
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("users_id", loggedInUser.getId()));

            String webServiceUrl = Utilities.BASE_URL + "account/get_documents";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_DOCUMENTS, CoachManageDocumentsFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_DOCUMENTS:

                documentsList.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            DocumentBean documentBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject documentObject = dataArray.getJSONObject(i);
                                documentBean = new DocumentBean();

                                documentBean.setDocumentId(documentObject.getString("id"));
                                documentBean.setType(documentObject.getString("type"));
                                documentBean.setTitle(documentObject.getString("title"));
                                documentBean.setFilePath(documentObject.getString("file_path"));
                                documentBean.setComments(documentObject.getString("comments"));
                                documentBean.setState(documentObject.getString("state"));
                                documentBean.setCreatedAt(documentObject.getString("created_at"));
                                documentBean.setUserId(documentObject.getString("users_id"));
//                                documentBean.setFileURL(documentObject.getString("file_url"));
                                documentBean.setFileName(documentObject.getString("file_name"));

                                documentsList.add(documentBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentDocumentListingAdapter.notifyDataSetChanged();

                break;
        }
    }*/
}
