package com.dunvant.application.parent;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.parent.fragments.ParentManageChildrenFragment;
import com.dunvant.application.utils.Utilities;

public class ParentShowManageChildren extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ImageView backButton;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_show_manage_children);

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        title.setText(verbiage_singular.toUpperCase()+" INFO");

        ParentManageChildrenFragment parentManageChildrenFragment = new ParentManageChildrenFragment();
        ParentShowManageChildren.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, parentManageChildrenFragment)
                .commit();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}