package com.dunvant.application.child;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.child.fragments.ChildCreatePostFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ChildBlankActivityForCreatePost extends AppCompatActivity {
    // Run time permission
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_blank_activity_for_create_post);

        backButton = findViewById(R.id.backButton);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, new ChildCreatePostFragment())
                .commit();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions()) {
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Permissions already granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Initially permission not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(ChildBlankActivityForCreatePost.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
                    // no permissions granted.
                    Toast.makeText(ChildBlankActivityForCreatePost.this, "This feature cannot work without Permissions. \nTry Again and allow permissions or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }
}