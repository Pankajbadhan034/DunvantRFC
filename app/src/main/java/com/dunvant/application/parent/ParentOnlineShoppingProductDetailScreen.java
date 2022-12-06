package com.dunvant.application.parent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.AttributesValuesBean;
import com.dunvant.application.beans.AttributesValuesDataBean;
import com.dunvant.application.beans.ProductCombBean;
import com.dunvant.application.beans.ProductOnlineShoppingProductDetailsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentDialogBoxShowAdapter;
import com.dunvant.application.parent.adapters.ParentOnlineShoppingProductDetailAdapter;
import com.dunvant.application.parent.adapters.ParentOnlineShoppingSelectOptionsCartAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class ParentOnlineShoppingProductDetailScreen extends AppCompatActivity implements IWebServiceCallback {
    Timer timer;
    RelativeLayout relativeCart;
    ViewPager mViewPager;
    SpringDotsIndicator springDotsIndicator;
    String combinationId="0";
    String nameProduct;
    ArrayList<String>stringArrayList = new ArrayList<>();
    ParentOnlineShoppingSelectOptionsCartAdapter parentOnlineShoppingSelectOptionsCartAdapter;
    ArrayList<AttributesValuesBean>attributesValuesBeanArrayList;
    ArrayList<ProductCombBean> productCombBeanArrayList;
    AttributesValuesBean attributesValuesBean;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageView backButton;
    TextView title;
//    ImageView image;
//    HorizontalListView horList;
    TextView name;
    TextView price;
    Button plusScore;
    TextView scoreEdit;
    Button minusScore;
    TextView addToCart;
    TextView availStock;
    TextView desc;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    int scoreValue = 1;

    Typeface helvetica;
    Typeface linoType;
    TextView desTV;

    private final String ONLINE_PRODUCT_DATA = "ONLINE_PRODUCT_DATA";
    private final String ADD_TO_CART = "ADD_TO_CART";
    private final String VARIABLE_PRODUCT_DATA = "VARIABLE_PRODUCT_DATA";

    ArrayList<ProductOnlineShoppingProductDetailsBean> productOnlineShoppingProductDetailsBeanArrayList = new ArrayList<>();
    ProductOnlineShoppingProductDetailsBean productOnlineShoppingProductDetailsBean;

    String productID, AcademiesID, nameStr;
   // Button contShopping;
   // Button viewCart;
    //LinearLayout linear2;
    GridView gridView;
    String userCount;
    TextView badgeText;
    RelativeLayout relativeAdToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_online_shopping_product_detail_screen);
        backButton = findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
//        image = findViewById(R.id.image);
//        horList = findViewById(R.id.horList);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        plusScore = findViewById(R.id.plusScore);
        scoreEdit = findViewById(R.id.scoreEdit);
        minusScore = findViewById(R.id.minusScore);
        addToCart = findViewById(R.id.addToCart);
        availStock = findViewById(R.id.availStock);
        desc = findViewById(R.id.desc);
       // contShopping = findViewById(R.id.contShopping);
       // viewCart = findViewById(R.id.viewCart);
       // linear2= findViewById(R.id.linear2);
        gridView = findViewById(R.id.gridView);
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);
        springDotsIndicator = (SpringDotsIndicator) findViewById(R.id.spring_dots_indicator);
        relativeCart = findViewById(R.id.relativeCart);
        badgeText = findViewById(R.id.badgeText);
        relativeAdToCart = findViewById(R.id.relativeAdToCart);
        desTV = findViewById(R.id.desTV);

        productID = getIntent().getStringExtra("ProductID");
        AcademiesID = getIntent().getStringExtra("AcademiesID");
        nameStr = getIntent().getStringExtra("name");

        title.setText(nameStr);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentOnlineShoppingProductDetailScreen.this));
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
            public void onClick(View view) {
                finish();
            }
        });

        relativeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(ParentOnlineShoppingProductDetailScreen.this, ParentOnlineShoppingViewCartScreen.class);
                startActivity(obj);
            }
        });

        plusScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    addToCart.setText("ADD TO CART");
                    String strValue = scoreEdit.getText().toString();
                    if(strValue.isEmpty()){
                        strValue = "1";
                    }
                    scoreValue = Integer.parseInt(strValue);
                        scoreValue++;
                        scoreEdit.setText("" + scoreValue);

                } catch(NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });

        minusScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    addToCart.setText("ADD TO CART");
                    String strValue = scoreEdit.getText().toString();
                    if(strValue.isEmpty()){
                        strValue = "1";
                    }
                    scoreValue = Integer.parseInt(strValue);
                    if (scoreValue > 1) {
                        scoreValue--;
                    }
                    scoreEdit.setText("" + scoreValue);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });

//        horList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String path = productOnlineShoppingProductDetailsBeanArrayList.get(0).getSlider().get(i);
//                imageLoader.displayImage(path, image, options);
//            }
//        });

        relativeAdToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                for (AttributesValuesBean attributesValuesBean : productOnlineShoppingProductDetailsBeanArrayList.get(0).getAttributesValuesBeanArrayList()){
//
//                }


                if(productOnlineShoppingProductDetailsBeanArrayList.get(0).getProductType().equalsIgnoreCase("2")) {
                    String valueSelected = "";
                    for(AttributesValuesBean AttributesValuesBean : attributesValuesBeanArrayList){
                        for(AttributesValuesDataBean attributesValuesDataBean : AttributesValuesBean.getAttributesValuesDataBeanArrayList()){
                            if(attributesValuesDataBean.getClickedBool().equalsIgnoreCase("true")){
                                if(attributesValuesDataBean.getAttribute_value_id().contains("-1")){
                                    valueSelected = attributesValuesDataBean.getAttribute_value_name();
                                    Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, ""+valueSelected, Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            }

                        }
                    }


                    if(addToCart.getText().toString().trim().equalsIgnoreCase("Go To Cart")){
                        Intent obj = new Intent(ParentOnlineShoppingProductDetailScreen.this, ParentOnlineShoppingViewCartScreen.class);
                        startActivity(obj);
                    }else{
                        addTOCart();
                    }

                }else{
                    if(addToCart.getText().toString().trim().equalsIgnoreCase("Go To Cart")){
                        Intent obj = new Intent(ParentOnlineShoppingProductDetailScreen.this, ParentOnlineShoppingViewCartScreen.class);
                        startActivity(obj);
                    }else{
                        addTOCart();
                    }
                }





//                if(productOnlineShoppingProductDetailsBeanArrayList.get(0).getProductType().equalsIgnoreCase("2")) {
//                    String valueSelected = "";
//                    for (AttributesValuesBean attributesValuesBean : productOnlineShoppingProductDetailsBeanArrayList.get(0).getAttributesValuesBeanArrayList()) {
//                        System.out.println("IDHERE::"+attributesValuesBean.getAttributeId());
//
//                        for(AttributesValuesDataBean attributesValuesDataBean : attributesValuesBean.getAttributesValuesDataBeanArrayList()){
//                            if(attributesValuesDataBean.getAttribute_value_id().equalsIgnoreCase("-1")){
//                                valueSelected = attributesValuesDataBean.getAttribute_name();
//                                break;
//                            }
//                        }
//
//                        if(!valueSelected.equalsIgnoreCase("")){
//                            break;
//                        }
//
//                    }
//
//
//                    if(valueSelected.equalsIgnoreCase("")){
//                        Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, valueSelected, Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, valueSelected, Toast.LENGTH_SHORT).show();
//                        valueSelected="";
//                    }
//
//
//                }else{
//                    Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, "else product 1", Toast.LENGTH_SHORT).show();
//                }

//                    for(int i=0 ; i<productOnlineShoppingProductDetailsBeanArrayList.get(0).getAttributesValuesBeanArrayList().size(); i++){
//
//                        if(productOnlineShoppingProductDetailsBeanArrayList.get(0).getAttributesValuesBeanArrayList().get(i).getAttributeId().equalsIgnoreCase("-1")){
//                            Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, "Please select all fields", Toast.LENGTH_SHORT).show();
//                            break;
//                        }
//
//                    }
//                }else{
//                    Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, "all selected", Toast.LENGTH_SHORT).show();
//
//                }
                
//                if(stringArrayList.size()==0 || stringArrayList.contains("-1")){
//                    Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, "Please select all fields", Toast.LENGTH_SHORT).show();
//
//                }else{
//                    if(addToCart.getText().toString().trim().equalsIgnoreCase("Go To Cart")){
//                        Intent obj = new Intent(ParentOnlineShoppingProductDetailScreen.this, ParentOnlineShoppingViewCartScreen.class);
//                        startActivity(obj);
//                    }else{
//                        addTOCart();
//                    }
//                }
//
                

//                if(scoreValue==0){
//                //    Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, "", Toast.LENGTH_SHORT).show();
//                }else{
//
//                }
                
               
            }
        });

//        contShopping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//
//        viewCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent obj = new Intent(ParentOnlineShoppingProductDetailScreen.this, ParentOnlineShoppingViewCartScreen.class);
//                startActivity(obj);
//            }
//        });


        changeFonts();
        //onlineProductData();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final AttributesValuesBean attributesValuesBean = attributesValuesBeanArrayList.get(position);
                final Dialog dialog = new Dialog(ParentOnlineShoppingProductDetailScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_box_show);

                ListView listView = (ListView) dialog.findViewById(R.id.listView);

                ParentDialogBoxShowAdapter parentDialogBoxShowAdapter = new ParentDialogBoxShowAdapter(ParentOnlineShoppingProductDetailScreen.this, attributesValuesBean.getAttributesValuesDataBeanArrayList());
                listView.setAdapter(parentDialogBoxShowAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        stringArrayList.clear();

                        funcUpdateBool(position, i);
                        dialog.dismiss();




                        for(int m=0; m<attributesValuesBeanArrayList.size(); m++){
                            for(int s=0; s<attributesValuesBeanArrayList.get(m).getAttributesValuesDataBeanArrayList().size(); s++){

                                if(attributesValuesBeanArrayList.get(m).getAttributesValuesDataBeanArrayList().get(s).getClickedBool().equalsIgnoreCase("true")){
                                    if(attributesValuesBeanArrayList.get(m).getAttributesValuesDataBeanArrayList().get(s).getAttribute_value_id().equalsIgnoreCase("-1")){
                                        stringArrayList.add("-1");
                                    }else{
                                        stringArrayList.add(attributesValuesBeanArrayList.get(m).getAttributesValuesDataBeanArrayList().get(s).getAttribute_value_id());
                                    }
                                }

                            }

                        }

                        if(stringArrayList.contains("-1")){
                            name.setText("");
                            price.setText("");
                            availStock.setText("");
                        }else{
                            String arr = "[";
                            for (String element: stringArrayList) {
                                System.out.println(element);

                                arr = arr + "\""+element+"\",";
                            }
                            arr = arr.substring(0, arr.length() -1);
                            arr = arr + "]";
                            // HIT API HERE
                            variableProductData(arr);

                        }


                    }
                });

                dialog.show();



            }
        });

    }

    private void changeFonts() {
        title.setTypeface(linoType);
        name.setTypeface(linoType);
        price.setTypeface(linoType);
        desc.setTypeface(helvetica);
        desTV.setTypeface(linoType);

    }

    @Override
    public void onResume() {
        super.onResume();
        onlineProductData();
        addToCart.setText("ADD TO CART");
    }

    private void onlineProductData() {
        if (Utilities.isNetworkAvailable(ParentOnlineShoppingProductDetailScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("product_id", productID));
            nameValuePairList.add(new BasicNameValuePair("userid", loggedInUser.getId()));

            String webServiceUrl = Utilities.BASE_URL + "product/product_detail";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentOnlineShoppingProductDetailScreen.this, nameValuePairList, ONLINE_PRODUCT_DATA, ParentOnlineShoppingProductDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }

    private void variableProductData(String arr) {
        if (Utilities.isNetworkAvailable(ParentOnlineShoppingProductDetailScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("product_id", productID));
            nameValuePairList.add(new BasicNameValuePair("attribute_val", arr));

            String webServiceUrl = Utilities.BASE_URL + "product/variable_product";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentOnlineShoppingProductDetailScreen.this, nameValuePairList, VARIABLE_PRODUCT_DATA, ParentOnlineShoppingProductDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }

    private void addTOCart() {
        System.out.println("combinationId:: "+combinationId);
        if (Utilities.isNetworkAvailable(ParentOnlineShoppingProductDetailScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("product_id", productID));
            nameValuePairList.add(new BasicNameValuePair("stock", ""+scoreValue));
            nameValuePairList.add(new BasicNameValuePair("combination_id", combinationId));

            String webServiceUrl = Utilities.BASE_URL + "product/add_to_cart";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentOnlineShoppingProductDetailScreen.this, nameValuePairList, ADD_TO_CART, ParentOnlineShoppingProductDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case ONLINE_PRODUCT_DATA:

                productOnlineShoppingProductDetailsBeanArrayList.clear();

                if (response == null) {
                    Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            userCount = responseObject.getString("cartCount");
                            badgeText.setText(userCount);
                            JSONObject jsonObject = responseObject.getJSONObject("product");
                                productOnlineShoppingProductDetailsBean = new ProductOnlineShoppingProductDetailsBean();
                                productOnlineShoppingProductDetailsBean.setId(jsonObject.getString("id"));
                                productOnlineShoppingProductDetailsBean.setName(jsonObject.getString("name"));
                                productOnlineShoppingProductDetailsBean.setCategory(jsonObject.getString("category"));
                                productOnlineShoppingProductDetailsBean.setDescription(jsonObject.getString("description"));
                                productOnlineShoppingProductDetailsBean.setImage(jsonObject.getString("image"));
                                productOnlineShoppingProductDetailsBean.setProductType(jsonObject.getString("product_type"));
                                productOnlineShoppingProductDetailsBean.setCombination(jsonObject.getString("combination"));
                                productOnlineShoppingProductDetailsBean.setSku(jsonObject.getString("sku"));
                                productOnlineShoppingProductDetailsBean.setPrice(jsonObject.getString("price"));
                                productOnlineShoppingProductDetailsBean.setQuantity(jsonObject.getString("quantity"));
                                productOnlineShoppingProductDetailsBean.setState(jsonObject.getString("state"));
                                productOnlineShoppingProductDetailsBean.setPathImg(jsonObject.getString("path_image"));
                                productOnlineShoppingProductDetailsBean.setFiles(jsonObject.getString("files"));
                                productOnlineShoppingProductDetailsBean.setFileName(jsonObject.getString("file_name"));




                               // productOnlineShoppingProductDetailsBean.setProductCombBeanArrayList(jsonObject.getString("product_combination_data"));




                                //productOnlineShoppingProductDetailsBean.setGalCount(jsonObject.getString("gal_count"));

                                try{
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("slider");
                                    ArrayList<String> sliderArrayList = new ArrayList<>();
                                    for(int k=0; k<jsonArray1.length(); k++){
                                        sliderArrayList.add(jsonArray1.getString(k));
                                    }
                                    productOnlineShoppingProductDetailsBean.setSlider(sliderArrayList);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }


                                if(jsonObject.getString("product_type").equalsIgnoreCase("2")){
                                    productCombBeanArrayList = new ArrayList<>();
                                    productCombBeanArrayList.clear();

                                    JSONArray jsonArr = new JSONArray(jsonObject.getString("product_combination_data"));
                                    ProductCombBean productCombBean = null;
                                    for(int j=0; j<jsonArr.length(); j++){
                                        JSONObject jsonObject1 = jsonArr.getJSONObject(j);
                                        productCombBean = new ProductCombBean();
                                        productCombBean.setSku(jsonObject1.getString("sku"));
                                        productCombBean.setPrice(jsonObject1.getString("price"));
                                        productCombBean.setQuantity(jsonObject1.getString("quantity"));

                                        JSONArray jsonArray1 = new JSONArray(jsonObject1.getString("combination"));
                                        ArrayList<String>stringArrayList1 = new ArrayList<>();
                                        for(int l=0; l<jsonArray1.length(); l++){
                                            stringArrayList.add(jsonArray1.getString(l));
                                        }
                                        productCombBean.setCombIdArray(stringArrayList1);

                                        productCombBeanArrayList.add(productCombBean);

                                    }
                                    productOnlineShoppingProductDetailsBean.setProductCombBeanArrayList(productCombBeanArrayList);


                                    JSONArray attribute_valuesArray = jsonObject.getJSONArray("attribute_values");
                                    attributesValuesBeanArrayList = new ArrayList<>();
                                    attributesValuesBeanArrayList.clear();
                                    for(int j=0; j<attribute_valuesArray.length(); j++){
                                         attributesValuesBean = new AttributesValuesBean();
                                        JSONObject jsonObject1 = attribute_valuesArray.getJSONObject(j);
                                        attributesValuesBean.setAttributeId(jsonObject1.getString("attribute_id"));
                                        attributesValuesBean.setAttributeName(jsonObject1.getString("attribute_name"));
//                                        attributesValuesBean.setClickedBool("false");

                                        JSONArray jsonArray1 = jsonObject1.getJSONArray("attribute_value_data");
                                        JSONArray jsonArray = jsonArray1.getJSONArray(0);
                                        ArrayList<AttributesValuesDataBean>attributesValuesDataBeanArrayList = new ArrayList<>();

                                        AttributesValuesDataBean attributesValuesDataBeanFirstEntryStatic = new AttributesValuesDataBean();
                                        attributesValuesDataBeanFirstEntryStatic.setAttribute_value_id("-1");
                                        attributesValuesDataBeanFirstEntryStatic.setAttribute_value_name("Please Select "+jsonObject1.getString("attribute_name"));
                                        attributesValuesDataBeanFirstEntryStatic.setClickedBool("true");
                                        attributesValuesDataBeanFirstEntryStatic.setAttribute_name(jsonObject1.getString("attribute_name"));
                                        attributesValuesDataBeanArrayList.add(attributesValuesDataBeanFirstEntryStatic);

                                        for(int k=0; k<jsonArray.length(); k++){
                                            AttributesValuesDataBean attributesValuesDataBean = new AttributesValuesDataBean();
                                            JSONObject jsonObject2 = jsonArray.getJSONObject(k);
                                            attributesValuesDataBean.setAttribute_value_id(jsonObject2.getString("attribute_value_id"));
                                            attributesValuesDataBean.setAttribute_value_name(jsonObject2.getString("attribute_value_name"));
                                            attributesValuesDataBean.setAttribute_name(jsonObject1.getString("attribute_name"));
                                            attributesValuesDataBean.setClickedBool("false");
                                            attributesValuesDataBeanArrayList.add(attributesValuesDataBean);
                                        }
                                        attributesValuesBean.setAttributesValuesDataBeanArrayList(attributesValuesDataBeanArrayList);
                                        attributesValuesBeanArrayList.add(attributesValuesBean);
                                    }

                                    productOnlineShoppingProductDetailsBean.setAttributesValuesBeanArrayList(attributesValuesBeanArrayList);

                                }
                                
                                productOnlineShoppingProductDetailsBeanArrayList.add(productOnlineShoppingProductDetailsBean);

                            String academy_currency = sharedPreferences.getString("academy_currency", null);

                            for(int i=0; i<productOnlineShoppingProductDetailsBeanArrayList.size(); i++){
                                nameProduct = productOnlineShoppingProductDetailsBeanArrayList.get(i).getName();
                                
                                if(productOnlineShoppingProductDetailsBeanArrayList.get(i).getProductType().equalsIgnoreCase("2")){

                                    ParentOnlineShoppingProductDetailAdapter parentOnlineShoppingProductDetailAdapter = new ParentOnlineShoppingProductDetailAdapter(this,productOnlineShoppingProductDetailsBeanArrayList.get(i).getSlider());
                                    mViewPager.setAdapter(parentOnlineShoppingProductDetailAdapter);
                                    springDotsIndicator.setViewPager(mViewPager);

                                    if(productOnlineShoppingProductDetailsBeanArrayList.get(i).getSlider().size()==1){
                                        springDotsIndicator.setVisibility(View.GONE);
                                    }else{
                                        springDotsIndicator.setVisibility(View.VISIBLE);
                                    }

                                    desc.setText((Html.fromHtml(productOnlineShoppingProductDetailsBeanArrayList.get(i).getDescription())));
                                   // imageLoader.displayImage(productOnlineShoppingProductDetailsBeanArrayList.get(i).getPathImg(), image, options);
                                    gridView.setVisibility(View.VISIBLE);

                                    parentOnlineShoppingSelectOptionsCartAdapter = new ParentOnlineShoppingSelectOptionsCartAdapter(this,productOnlineShoppingProductDetailsBeanArrayList.get(i).getAttributesValuesBeanArrayList(), ParentOnlineShoppingProductDetailScreen.this);
                                    gridView.setAdapter(parentOnlineShoppingSelectOptionsCartAdapter);
                                    Utilities.setGridViewHeightBasedOnChildren(gridView, 2);

                                    name.setText(nameProduct);
                                    price.setText(productOnlineShoppingProductDetailsBeanArrayList.get(i).getPrice()+" "+academy_currency+"");

//
//                                    text.setText(""+nameProduct+"\n"
//                                            +""+productOnlineShoppingProductDetailsBeanArrayList.get(i).getPrice()+" "+academy_currency+"");



                                }else{
                                    ParentOnlineShoppingProductDetailAdapter parentOnlineShoppingProductDetailAdapter = new ParentOnlineShoppingProductDetailAdapter(this,productOnlineShoppingProductDetailsBeanArrayList.get(i).getSlider());
                                    mViewPager.setAdapter(parentOnlineShoppingProductDetailAdapter);
                                    springDotsIndicator.setViewPager(mViewPager);

                                    if(productOnlineShoppingProductDetailsBeanArrayList.get(i).getSlider().size()==1){
                                        springDotsIndicator.setVisibility(View.GONE);
                                    }else{
                                        springDotsIndicator.setVisibility(View.VISIBLE);
                                    }

                                    desc.setText((Html.fromHtml(productOnlineShoppingProductDetailsBeanArrayList.get(i).getDescription())));
                                    //imageLoader.displayImage(productOnlineShoppingProductDetailsBeanArrayList.get(i).getPathImg(), image, options);
                                    gridView.setVisibility(View.VISIBLE);

                                    name.setText(nameProduct);
                                    price.setText(productOnlineShoppingProductDetailsBeanArrayList.get(i).getPrice()+" "+academy_currency+"");


//                                    text.setText(""+nameProduct+"\n"
//                                            +""+productOnlineShoppingProductDetailsBeanArrayList.get(i).getPrice()+" "+academy_currency+"");

                                    availStock.setText("Available Stock : "+productOnlineShoppingProductDetailsBeanArrayList.get(i).getQuantity());



                                }

                            }

//                            try{
//                                TimerTask timerTask = new TimerTask() {
//                                    @Override
//                                    public void run() {
//                                        mViewPager.post(new Runnable(){
//
//                                            @Override
//                                            public void run() {
//                                                mViewPager.setCurrentItem((mViewPager.getCurrentItem()+1)%productOnlineShoppingProductDetailsBeanArrayList.get(0).getSlider().size());
//                                            }
//                                        });
//                                    }
//                                };
//                                timer = new Timer();
//                                timer.schedule(timerTask, 3000, 3000);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }

                        } else {
                            Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case ADD_TO_CART:

                if (response == null) {
                    Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        //    linear2.setVisibility(View.VISIBLE);
                            addToCart.setText("GO TO CART");
                        } else {
                            Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case VARIABLE_PRODUCT_DATA:

                if (response == null) {
                    Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                           JSONObject jsonObject = responseObject.getJSONObject("data");

                           combinationId = jsonObject.getString("id");
                           String product_id = jsonObject.getString("product_id");
                           String combination = jsonObject.getString("combination");
                           String sku = jsonObject.getString("sku");
                           String pricestr = jsonObject.getString("price");
                           String quantity = jsonObject.getString("quantity");
                           String inital_quantity = jsonObject.getString("inital_quantity");
                           String created = jsonObject.getString("created");
                           String modified = jsonObject.getString("modified");

                            String academy_currency = sharedPreferences.getString("academy_currency", null);

//                            text.setText("NAME : "+nameProduct+"\n"
//                                   +"PRICE : "+pricestr+" "+academy_currency+"\n");
                            name.setText(nameProduct);
                            price.setText(pricestr+" "+academy_currency);

                           availStock.setText("Available Stock : "+quantity);



                        } else {
                            Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentOnlineShoppingProductDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;


        }
    }

//    public void changeTimeSlotInAllPitches(AttributesValuesBean attributesValuesBean) {
//        for (AttributesValuesBean currentTimeSlot : attributesValuesBeanArrayList.get(0).isClickedBool()) {
//            if (currentTimeSlot.isClickedBool()==attributesValuesBean.isClickedBool()) {
//                currentTimeSlot.setClickedBool(attributesValuesBean.isClickedBool());
//            }
//        }
//    }

    public void funcUpdateBool (int position, int iAdapter){
        for(int i=0; i<attributesValuesBeanArrayList.get(position).getAttributesValuesDataBeanArrayList().size(); i++){
            if(i == iAdapter){
                attributesValuesBeanArrayList.get(position).getAttributesValuesDataBeanArrayList().get(i).setClickedBool("true");
                System.out.println("HERE:::HERE::");
            }else{
                attributesValuesBeanArrayList.get(position).getAttributesValuesDataBeanArrayList().get(i).setClickedBool("false");
//                attributesValuesBeanArrayList.get(i).setClickedBool("false");
            }

        }

        parentOnlineShoppingSelectOptionsCartAdapter.notifyDataSetChanged();
    }
}
