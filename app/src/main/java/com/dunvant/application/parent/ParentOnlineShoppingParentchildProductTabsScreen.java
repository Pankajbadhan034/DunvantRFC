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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ParentOnlineStoreChildSubCatBean;
import com.dunvant.application.beans.ParentOnlineStoreProductsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentOnlineShoppingChildSubCatAdapter;
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

public class ParentOnlineShoppingParentchildProductTabsScreen extends AppCompatActivity implements IWebServiceCallback {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageView backButton;
    TextView title;
    TextView products;
    TextView subCat;
    GridView gridView;
    GridView listView;

    Typeface helvetica;
    Typeface linoType;

    private final String ONLINE_CHILD_DATA = "ONLINE_CHILD_DATA";
    private final String ONLINE_PRODUCT_DATA = "ONLINE_PRODUCT_DATA";
    ArrayList<ParentOnlineStoreChildSubCatBean> parentOnlineStoreChildSubCatBeanArrayList = new ArrayList<>();
    ParentOnlineStoreChildSubCatBean parentOnlineStoreChildSubCatBean;

    ArrayList<ParentOnlineStoreProductsBean> parentOnlineStoreProductsBeanArrayList = new ArrayList<>();
    ParentOnlineStoreProductsBean parentOnlineStoreProductsBean;
    RelativeLayout relativeCart;
    String parentID;
    String categoryName;
    String userCount;
    TextView badgeText;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_online_shopping_parentchild_product_tabs_screen);
        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        products = findViewById(R.id.products);
        subCat = findViewById(R.id.subCat);
        gridView = findViewById(R.id.gridView);
        listView = findViewById(R.id.listView);
        relativeCart = findViewById(R.id.relativeCart);
        badgeText = findViewById(R.id.badgeText);
        linear = findViewById(R.id.linear);

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

        relativeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(ParentOnlineShoppingParentchildProductTabsScreen.this, ParentOnlineShoppingViewCartScreen.class);
                startActivity(obj);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent obj = new Intent(ParentOnlineShoppingParentchildProductTabsScreen.this, ParentOnlineShoppingSubCatProductsScreen.class);
                obj.putExtra("parentID", parentOnlineStoreChildSubCatBeanArrayList.get(i).getId());
                obj.putExtra("categoryName", parentOnlineStoreChildSubCatBeanArrayList.get(i).getCategoryName());
                startActivity(obj);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent obj = new Intent(ParentOnlineShoppingParentchildProductTabsScreen.this, ParentOnlineShoppingProductDetailScreen.class);
                obj.putExtra("AcademiesID", parentOnlineStoreProductsBeanArrayList.get(i).getAcademiesId());
                obj.putExtra("ProductID", parentOnlineStoreProductsBeanArrayList.get(i).getId());
                obj.putExtra("name", parentOnlineStoreProductsBeanArrayList.get(i).getName());
                startActivity(obj);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        subCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                subCat.setBackgroundColor(getResources().getColor(R.color.blue));
                subCat.setTextColor(getResources().getColor(R.color.white));
                products.setBackgroundColor(getResources().getColor(R.color.yellow));
                products.setTextColor(getResources().getColor(R.color.black));
                onlineChildData();
            }
        });

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                products.setBackgroundColor(getResources().getColor(R.color.blue));
                products.setTextColor(getResources().getColor(R.color.white));
                subCat.setBackgroundColor(getResources().getColor(R.color.yellow));
                subCat.setTextColor(getResources().getColor(R.color.black));
                onlineProductData();
            }
        });

        changeFonts();

    }

    private void changeFonts() {
        title.setTypeface(linoType);
        products.setTypeface(linoType);
        subCat.setTypeface(linoType);

    }
    @Override
    public void onResume() {
        super.onResume();
        onlineChildData();
    }
    private void onlineChildData() {
        if(Utilities.isNetworkAvailable(ParentOnlineShoppingParentchildProductTabsScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("parent_id", parentID));
            nameValuePairList.add(new BasicNameValuePair("userid", loggedInUser.getId()));

            String webServiceUrl = Utilities.BASE_URL + "product/child_category_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentOnlineShoppingParentchildProductTabsScreen.this, nameValuePairList, ONLINE_CHILD_DATA, ParentOnlineShoppingParentchildProductTabsScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        }else{
            Toast.makeText(ParentOnlineShoppingParentchildProductTabsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }

    private void onlineProductData() {
        if(Utilities.isNetworkAvailable(ParentOnlineShoppingParentchildProductTabsScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("category_id", parentID));
            nameValuePairList.add(new BasicNameValuePair("userid", loggedInUser.getId()));

            String webServiceUrl = Utilities.BASE_URL + "product/product_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentOnlineShoppingParentchildProductTabsScreen.this, nameValuePairList, ONLINE_PRODUCT_DATA, ParentOnlineShoppingParentchildProductTabsScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        }else{
            Toast.makeText(ParentOnlineShoppingParentchildProductTabsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case ONLINE_CHILD_DATA:

                parentOnlineStoreChildSubCatBeanArrayList.clear();
                listView.setAdapter(null);

                if (response == null) {
                    Toast.makeText(ParentOnlineShoppingParentchildProductTabsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        userCount = responseObject.getString("cartCount");
                        badgeText.setText(userCount);
                        if(status) {
                            JSONArray jsonArray = responseObject.getJSONArray("category");
                            for(int i=0; i<jsonArray.length(); i++){
                                parentOnlineStoreChildSubCatBean = new ParentOnlineStoreChildSubCatBean();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                parentOnlineStoreChildSubCatBean.setId(jsonObject.getString("id"));
                                parentOnlineStoreChildSubCatBean.setAcademiesId(jsonObject.getString("academies_id"));
                                parentOnlineStoreChildSubCatBean.setCategoryName(jsonObject.getString("category_name"));
                                parentOnlineStoreChildSubCatBean.setParentCategory(jsonObject.getString("parent_category"));
                                parentOnlineStoreChildSubCatBean.setDescription(jsonObject.getString("description"));
                                parentOnlineStoreChildSubCatBean.setImage(jsonObject.getString("image"));
                                parentOnlineStoreChildSubCatBean.setState(jsonObject.getString("state"));

                                parentOnlineStoreChildSubCatBeanArrayList.add(parentOnlineStoreChildSubCatBean);
                            }

                            ParentOnlineShoppingChildSubCatAdapter parentOnlineShoppingChildSubCatAdapter = new ParentOnlineShoppingChildSubCatAdapter(ParentOnlineShoppingParentchildProductTabsScreen.this, parentOnlineStoreChildSubCatBeanArrayList);
                            listView.setAdapter(parentOnlineShoppingChildSubCatAdapter);



                        } else {
                            if(parentOnlineStoreChildSubCatBeanArrayList.size()==0){
                                linear.setVisibility(View.GONE);
                                gridView.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                products.setBackgroundColor(getResources().getColor(R.color.blue));
                                products.setTextColor(getResources().getColor(R.color.white));
                                subCat.setBackgroundColor(getResources().getColor(R.color.yellow));
                                subCat.setTextColor(getResources().getColor(R.color.black));
                                onlineProductData();
                            }else{
                                Toast.makeText(ParentOnlineShoppingParentchildProductTabsScreen.this, message, Toast.LENGTH_SHORT).show();
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentOnlineShoppingParentchildProductTabsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case ONLINE_PRODUCT_DATA:

                parentOnlineStoreProductsBeanArrayList.clear();
                listView.setAdapter(null);

                if (response == null) {
                    Toast.makeText(ParentOnlineShoppingParentchildProductTabsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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

                            ParentOnlineShoppingProductStoreAdapter parentOnlineShoppingProductStoreAdapter = new ParentOnlineShoppingProductStoreAdapter(ParentOnlineShoppingParentchildProductTabsScreen.this, parentOnlineStoreProductsBeanArrayList);
                            gridView.setAdapter(parentOnlineShoppingProductStoreAdapter);

                        } else {
                            Toast.makeText(ParentOnlineShoppingParentchildProductTabsScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentOnlineShoppingParentchildProductTabsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }


}
