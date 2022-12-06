package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ParentOnlineStoreProductsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentOnlineShoppingProductStoreAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ParentOnlineShoppingSubCatProductsScreen extends AppCompatActivity implements IWebServiceCallback {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageView backButton;
    TextView title;
    TextView products;
    TextView subCat;
    GridView gridView;

    Typeface helvetica;
    Typeface linoType;
    RelativeLayout relativeCart;

    private final String ONLINE_PRODUCT_DATA = "ONLINE_PRODUCT_DATA";

    ArrayList<ParentOnlineStoreProductsBean> parentOnlineStoreProductsBeanArrayList = new ArrayList<>();
    ParentOnlineStoreProductsBean parentOnlineStoreProductsBean;
    String userCount;
    TextView badgeText;
    String parentID;
    String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_online_shopping_sub_cat_products_screen);

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        products = findViewById(R.id.products);
        subCat = findViewById(R.id.subCat);
        gridView = findViewById(R.id.gridView);
        relativeCart = findViewById(R.id.relativeCart);
        badgeText = findViewById(R.id.badgeText);

        parentID = getIntent().getStringExtra("parentID");
        categoryName = getIntent().getStringExtra("categoryName");
        title.setText(categoryName);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        relativeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(ParentOnlineShoppingSubCatProductsScreen.this, ParentOnlineShoppingViewCartScreen.class);
                startActivity(obj);
            }
        });



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent obj = new Intent(ParentOnlineShoppingSubCatProductsScreen.this, ParentOnlineShoppingProductDetailScreen.class);
                obj.putExtra("AcademiesID", parentOnlineStoreProductsBeanArrayList.get(i).getAcademiesId());
                obj.putExtra("ProductID", parentOnlineStoreProductsBeanArrayList.get(i).getId());
                obj.putExtra("name", parentOnlineStoreProductsBeanArrayList.get(i).getName());
                startActivity(obj);

            }
        });

        changeFonts();

    }

    private void changeFonts() {
        title.setTypeface(linoType);

    }

    @Override
    public void onResume() {
        super.onResume();
        onlineProductData();
    }

    private void onlineProductData() {
        if(Utilities.isNetworkAvailable(ParentOnlineShoppingSubCatProductsScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("category_id", parentID));
            nameValuePairList.add(new BasicNameValuePair("userid", loggedInUser.getId()));

            String webServiceUrl = Utilities.BASE_URL + "product/product_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentOnlineShoppingSubCatProductsScreen.this, nameValuePairList, ONLINE_PRODUCT_DATA, ParentOnlineShoppingSubCatProductsScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        }else{
            Toast.makeText(ParentOnlineShoppingSubCatProductsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case ONLINE_PRODUCT_DATA:

                parentOnlineStoreProductsBeanArrayList.clear();

                if (response == null) {
                    Toast.makeText(ParentOnlineShoppingSubCatProductsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        userCount = responseObject.getString("cartCount");
                        badgeText.setText(userCount);

                        if(status) {
                            JSONArray jsonArray = responseObject.getJSONArray("product");

                            for(int i=0; i<jsonArray.length(); i++){
                                parentOnlineStoreProductsBean = new ParentOnlineStoreProductsBean();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                parentOnlineStoreProductsBean.setId(jsonObject.getString("id"));
                                parentOnlineStoreProductsBean.setAcademiesId(jsonObject.getString("academies_id"));
                                parentOnlineStoreProductsBean.setName(jsonObject.getString("name"));
                                parentOnlineStoreProductsBean.setCategory(jsonObject.getString("category"));
                                parentOnlineStoreProductsBean.setDescription(jsonObject.getString("description"));
                                parentOnlineStoreProductsBean.setImage(jsonObject.getString("image"));
                                parentOnlineStoreProductsBean.setProductType(jsonObject.getString("product_type"));
                                parentOnlineStoreProductsBean.setState(jsonObject.getString("state"));

                                if(jsonObject.has("MinPrice")){
                                    parentOnlineStoreProductsBean.setMinPrice(jsonObject.getString("MinPrice"));
                                }else{
                                    parentOnlineStoreProductsBean.setMinPrice("No Min Price");
                                }

                                if(jsonObject.has("MaxPrice")){
                                    parentOnlineStoreProductsBean.setMaxPrice(jsonObject.getString("MaxPrice"));
                                }else{
                                    parentOnlineStoreProductsBean.setMaxPrice("No Max Price");
                                }

                                if(jsonObject.has("price")){
                                    parentOnlineStoreProductsBean.setPrice(jsonObject.getString("price"));
                                }else{
                                    parentOnlineStoreProductsBean.setPrice("No Price");
                                }

                                parentOnlineStoreProductsBeanArrayList.add(parentOnlineStoreProductsBean);
                            }

                            ParentOnlineShoppingProductStoreAdapter parentOnlineShoppingProductStoreAdapter = new ParentOnlineShoppingProductStoreAdapter(ParentOnlineShoppingSubCatProductsScreen.this, parentOnlineStoreProductsBeanArrayList);
                            gridView.setAdapter(parentOnlineShoppingProductStoreAdapter);

                        } else {
                            Toast.makeText(ParentOnlineShoppingSubCatProductsScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentOnlineShoppingSubCatProductsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

}