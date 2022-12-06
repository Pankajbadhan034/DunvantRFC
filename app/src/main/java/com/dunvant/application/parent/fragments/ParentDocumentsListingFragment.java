package com.dunvant.application.parent.fragments;

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
import com.dunvant.application.parent.ParentUploadDocumentsScreen;
import com.dunvant.application.utils.Utilities;

public class ParentDocumentsListingFragment extends Fragment{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView uploadedDocuments;
    TextView receivedDocuments;

    TextView addNewDocument;

    ParentUploadedDocumentsFragment parentUploadedDocumentsFragment;
    ParentReceivedDocumentsFragment parentReceivedDocumentsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_documents_listing, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

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

                parentUploadedDocumentsFragment = new ParentUploadedDocumentsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, parentUploadedDocumentsFragment)
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

                parentReceivedDocumentsFragment = new ParentReceivedDocumentsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, parentReceivedDocumentsFragment)
                        .commit();
            }
        });

        addNewDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadDocuments = new Intent(getActivity(), ParentUploadDocumentsScreen.class);
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
            if (parentUploadedDocumentsFragment != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(parentUploadedDocumentsFragment).commit();
            }
            if (parentReceivedDocumentsFragment != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(parentReceivedDocumentsFragment).commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}