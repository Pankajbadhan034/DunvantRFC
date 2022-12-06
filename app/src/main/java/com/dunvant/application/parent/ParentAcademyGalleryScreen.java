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
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachingAcademyBean;
import com.dunvant.application.beans.CoachingProgramGalleryBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ParentAcademyGalleryScreen extends AppCompatActivity {

    String url;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    ViewPager viewPager;
    ImageView left_nav;
    ImageView right_nav;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    CoachingAcademyBean clickedOnAcademy;
    ArrayList<CoachingProgramGalleryBean> galleryList;
    ImageView share;

    int imagePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_academy_gallery_screen);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        right_nav = (ImageView) findViewById(R.id.right_nav);
        left_nav = (ImageView) findViewById(R.id.left_nav);
        share = (ImageView) findViewById(R.id.share);

        title.setTypeface(linoType);

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentAcademyGalleryScreen.this));
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
        if(intent != null) {
            clickedOnAcademy = (CoachingAcademyBean) intent.getSerializableExtra("clickedOnAcademy");
            imagePosition = intent.getIntExtra("imagePosition", 0);

            galleryList = clickedOnAcademy.getGalleryList();

            title.setText("1 of "+galleryList.size());

            url = galleryList.get(0).getFilePath();

            PagerAdapter adapter = new CustomAdapter(ParentAcademyGalleryScreen.this);
            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int pos = position + 1;
                    //System.out.println("ViewPagerValue::'+pos");
                    title.setText(pos + " of " + galleryList.size());
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            right_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tab = viewPager.getCurrentItem();
                    tab++;
                    viewPager.setCurrentItem(tab);
                }
            });

            left_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tab = viewPager.getCurrentItem();
                    if (tab > 0) {
                        tab--;
                        viewPager.setCurrentItem(tab);
                    } else if (tab == 0) {
                        viewPager.setCurrentItem(tab);
                    }
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, ""+url);
                        startActivity(shareIntent);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(ParentAcademyGalleryScreen.this, "Share Intent not Supported.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        viewPager.setCurrentItem(imagePosition);

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

             url = galleryList.get(position).getFilePath();

            imageLoader.displayImage(galleryList.get(position).getFilePath(), imageView, options);

            ((ViewPager)container).addView(viewItem);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        mAttacher = new PhotoViewAttacher(imageView);
                        //System.out.println("onTouchEventActionDown");

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