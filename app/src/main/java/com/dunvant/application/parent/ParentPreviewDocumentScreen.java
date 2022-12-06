package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.DocumentBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ParentPreviewDocumentScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    ImageView shareDocument;
    WebView webView;
    ImageView imageView;
    PhotoViewAttacher mAttacher;

    DocumentBean documentBean;
    String documentUrl;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_preview_document_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        webView = (WebView) findViewById(R.id.webView);
        imageView = (ImageView) findViewById(R.id.imageView);
        shareDocument = (ImageView) findViewById(R.id.shareDocument);

        title.setTypeface(linoType);

//        mAttacher.update();

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentPreviewDocumentScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mAttacher = new PhotoViewAttacher(imageView);

                    return true;
                }
                return false;
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            documentBean = (DocumentBean) intent.getSerializableExtra("clickedOnDocument");

            documentUrl = Utilities.BASE_URL + "account/download_document/" + loggedInUser.getToken() + "/" + documentBean.getDocumentId();
            //System.out.println("Document URL :: " + documentUrl);

            switch (documentBean.getType()) {
                case ".jpeg":
                case ".jpg":
                case ".png":

                    webView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                    imageLoader.displayImage(documentUrl, imageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            bitmap = loadedImage;
                        }
                    });

                    break;
                default:
                    imageView.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);

                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + documentUrl);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            //System.out.println("Should override " + url);
                            view.loadUrl(url);
                            return true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            // TODO Auto-generated method stub
                            //System.out.println("page finished  " + url);
                            super.onPageFinished(view, url);
                        }
                    });
            }

        }

        shareDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (documentBean.getType()) {
                    case ".jpeg":
                    case ".jpg":
                    case ".png":

                        if(bitmap == null){
                            Toast.makeText(ParentPreviewDocumentScreen.this, "Downloading document, please wait", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, "Document");
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", null);
                            Uri screenshotUri = Uri.parse(path);
                            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                            intent.setType("image/*");
                            startActivity(Intent.createChooser(intent, "Share document via..."));
                        }

                        break;
                    default:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Document");
                        intent.putExtra(Intent.EXTRA_TEXT, documentUrl);
                        startActivity(Intent.createChooser(intent, "Share document via..."));
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}