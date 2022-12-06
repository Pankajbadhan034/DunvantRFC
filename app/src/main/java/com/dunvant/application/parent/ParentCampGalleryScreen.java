
package com.dunvant.application.parent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampBean;
import com.dunvant.application.beans.CampGalleryBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ParentCampGalleryScreen extends AppCompatActivity {

    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    ViewPager viewPager;

    CampBean campBean;
    ArrayList<CampGalleryBean> galleryList;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_camp_gallery_screen);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        title.setTypeface(linoType);

        Intent intent = getIntent();
        if(intent != null) {
            campBean = (CampBean) intent.getSerializableExtra("clickedOnCamp");

            galleryList = campBean.getGalleryList();

            PagerAdapter adapter = new CustomAdapter(ParentCampGalleryScreen.this);
            viewPager.setAdapter(adapter);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentCampGalleryScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class CustomAdapter extends PagerAdapter{

        Context context;
        PhotoViewAttacher mAttacher;

        public CustomAdapter(Context context){
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            View viewItem = inflater.inflate(R.layout.parent_adapter_camp_gallery_item, container, false);
            final ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);

            imageLoader.displayImage(galleryList.get(position).getFilePath(), imageView, options);

            ((ViewPager)container).addView(viewItem);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        mAttacher = new PhotoViewAttacher(imageView);

                        return true;
                    }
                    return false;
                }
            });

            return viewItem;
        }

        @Override
        public int getCount() {
            return galleryList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View)object);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

    }
}
