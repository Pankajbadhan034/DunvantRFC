package com.dunvant.application.parent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.dunvant.application.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ParentViewImageInFullScreen extends AppCompatActivity {

    ImageView backButton;
    ImageView image;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_view_image_in_full_screen);

        backButton = (ImageView) findViewById(R.id.backButton);
        image = (ImageView) findViewById(R.id.image);

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);

                    return true;
                }
                return false;
            }
        });

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentViewImageInFullScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        Intent intent = getIntent();
        if(intent != null){
            imageUrl = intent.getStringExtra("imageUrl");

            imageLoader.displayImage(imageUrl, image, options);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}