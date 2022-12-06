package com.dunvant.application.parent.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.dunvant.application.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ParentPaymentDetailFragment extends Fragment{

    Typeface helvetica;
    Typeface linoType;

    TextInputLayout cardTIL;
    EditText cardNumber;
    TextInputLayout nameTIL;
    EditText nameOnCard;
    TextInputLayout expiryTIL;
    EditText expiryDate;
    TextInputLayout cvvTIL;
    EditText cvv;
    Button pay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_payment_detail, container, false);

        cardTIL = (TextInputLayout) view.findViewById(R.id.cardTIL);
        cardNumber = (EditText) view.findViewById(R.id.cardNumber);
        nameTIL = (TextInputLayout) view.findViewById(R.id.nameTIL);
        nameOnCard = (EditText) view.findViewById(R.id.nameOnCard);
        expiryTIL = (TextInputLayout) view.findViewById(R.id.expiryTIL);
        expiryDate = (EditText) view.findViewById(R.id.expiryDate);
        cvvTIL = (TextInputLayout) view.findViewById(R.id.cvvTIL);
        cvv = (EditText) view.findViewById(R.id.cvv);
        pay = (Button) view.findViewById(R.id.pay);

        changeFonts();

        return view;
    }

    private void changeFonts() {
        cardTIL.setTypeface(helvetica);
        cardNumber.setTypeface(helvetica);
        nameTIL.setTypeface(helvetica);
        nameOnCard.setTypeface(helvetica);
        expiryTIL.setTypeface(helvetica);
        expiryDate.setTypeface(helvetica);
        cvvTIL.setTypeface(helvetica);
        cvv.setTypeface(helvetica);
        pay.setTypeface(linoType);
    }

}