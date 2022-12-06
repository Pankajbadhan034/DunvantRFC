package com.dunvant.application.child;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.dunvant.application.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ChildViewProfileGalleryImageViewScreen extends AppCompatActivity {
    ImageView backButton;
    ImageView image;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_view_profile_gallery_image_view_screen);
        image = (ImageView) findViewById(R.id.image);
        backButton = (ImageView) findViewById(R.id.backButton);

        String intent = getIntent().getStringExtra("type");
        //System.out.println("Image::"+intent);

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildViewProfileGalleryImageViewScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        imageLoader.displayImage(intent, image, options);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
