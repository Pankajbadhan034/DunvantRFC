package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.dunvant.application.beans.ProductOnlineShoppingAttributeDataBean;
import com.dunvant.application.beans.ProductOnlineShoppingViewCartBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentOnlineShoppingViewCartAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ParentOnlineShoppingViewCartScreen extends AppCompatActivity implements IWebServiceCallback {
    double totalItems = 0;
    String promoCheck = "";
    String gstStr;
    String delChargesStr;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    ImageView backButton;
    TextView title;
    ListView listView;
    ArrayList<ProductOnlineShoppingViewCartBean>productOnlineShoppingProductDetailsBeanArrayList = new ArrayList<>();
    ProductOnlineShoppingViewCartBean productOnlineShoppingViewCartBean;
    private final String CART_DATA = "CART_DATA";
    private final String APPLY_PROMO_CODE = "APPLY_PROMO_CODE";
    Button applyPromoCode;
    Button cancelPromoCode;
    EditText promoCodeEditText;
    String strPromoCode = "";
    double dblNetAmount = 0;
    double promoCodeDeductAmount = 0;
    TextView promoCodeDiscount;
    TextView netAmount;
    TextView totalAmount;
    DecimalFormat decimalFormat = new DecimalFormat("#.00");
    TextView makePayment;
    TextView gstTV;
    TextView delTV;
    TextView tAmountTV;
    RelativeLayout relativeCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_online_shopping_view_cart_screen);
        backButton = findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        listView = findViewById(R.id.listView);
        applyPromoCode = findViewById(R.id.applyPromoCode);
        cancelPromoCode = findViewById(R.id.cancelPromoCode);
        promoCodeEditText = findViewById(R.id.promoCodeEditText);
        promoCodeEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        promoCodeDiscount = findViewById(R.id.promoCodeDiscount);
        netAmount = findViewById(R.id.netAmount);
        totalAmount = findViewById(R.id.totalAmount);
        makePayment = findViewById(R.id.makePayment);
        gstTV = findViewById(R.id.gstTV);
        delTV = findViewById(R.id.delTV);
        tAmountTV = findViewById(R.id.tAmountTV);
        relativeCart = findViewById(R.id.relativeCart);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentOnlineShoppingViewCartScreen.this));
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

//        relativeCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent obj = new Intent(ParentOnlineShoppingViewCartScreen.this, ParentOnlineShoppingViewCartScreen.class);
//                startActivity(obj);
//            }
//        });

        applyPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPromoCode = promoCodeEditText.getText().toString().trim();

                dblNetAmount = dblNetAmount + promoCodeDeductAmount;
                promoCodeDeductAmount = 0;
                String academy_currency = sharedPreferences.getString("academy_currency", null);
                promoCodeDiscount.setText("0.00 "+academy_currency);
                if(dblNetAmount == 0) {
                    netAmount.setText("0.00 "+academy_currency);
                } else {
                    netAmount.setText(decimalFormat.format(dblNetAmount)+" "+academy_currency);
                }

                if(strPromoCode == null || strPromoCode.isEmpty()) {
                    Toast.makeText(ParentOnlineShoppingViewCartScreen.this, "Please enter Promo Code", Toast.LENGTH_SHORT).show();
                } else {
                    promoApply();
                }
            }
        });

        cancelPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoCheck = "";
                promoCodeEditText.setText("");
                strPromoCode = promoCodeEditText.getText().toString().trim();

                dblNetAmount = dblNetAmount + promoCodeDeductAmount;
                promoCodeDeductAmount = 0;

                String academy_currency = sharedPreferences.getString("academy_currency", null);
                promoCodeDiscount.setText("0.00 "+academy_currency);
                if(dblNetAmount == 0) {
                    netAmount.setText("0.00 "+academy_currency);
                } else {
//                    netAmount.setText(decimalFormat.format(dblNetAmount)+" "+academy_currency);
                    afterGstPrice(dblNetAmount);
                }
            }
        });

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONArray jsonArray = new JSONArray();

                for(int i=0; i<productOnlineShoppingProductDetailsBeanArrayList.size(); i++){

                    try{

                        if(Double.parseDouble(productOnlineShoppingProductDetailsBeanArrayList.get(i).getProductInfoQuantity()) < Double.parseDouble(productOnlineShoppingProductDetailsBeanArrayList.get(i).getAvailStock())){
                            // red product
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("product_id", productOnlineShoppingProductDetailsBeanArrayList.get(i).getProductId());
                            jsonObject.put("combination_id", productOnlineShoppingProductDetailsBeanArrayList.get(i).getCombinationId());
                            jsonObject.put("product_cart_id", productOnlineShoppingProductDetailsBeanArrayList.get(i).getCartId());
                            jsonObject.put("net_cost", productOnlineShoppingProductDetailsBeanArrayList.get(i).getProductInfoPrice());
                            double totalPriceInt =  Double.parseDouble(productOnlineShoppingProductDetailsBeanArrayList.get(i).getProductInfoPrice()) *  Double.parseDouble(productOnlineShoppingProductDetailsBeanArrayList.get(i).getAvailStock());
                            jsonObject.put("total_cost", ""+totalPriceInt);
                            jsonObject.put("quantity", productOnlineShoppingProductDetailsBeanArrayList.get(i).getAvailStock());
                            jsonArray.put(jsonObject);
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                System.out.println("PERSONARRAY::"+jsonArray.toString());
                Intent obj = new Intent(ParentOnlineShoppingViewCartScreen.this, ParentOnlineShoppingBillingShippingAddressScreen.class);
                obj.putExtra("amount", ""+totalItems);
                obj.putExtra("delivery_charges", delChargesStr);
                obj.putExtra("gst", gstStr);
                obj.putExtra("product_data", jsonArray.toString());
                if(promoCheck.equalsIgnoreCase("yes")){
                    obj.putExtra("module_type", "5");
                    obj.putExtra("promo_code", strPromoCode);
                }else{
                    obj.putExtra("module_type", "");
                    obj.putExtra("promo_code", "");
                }

                startActivity(obj);

            }
        });

        changeFonts();
        onlineCartData();
    }

    private void changeFonts() {
        title.setTypeface(linoType);
        netAmount.setTypeface(helvetica);
    }

    private void onlineCartData() {
        if (Utilities.isNetworkAvailable(ParentOnlineShoppingViewCartScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("userid", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));

            String webServiceUrl = Utilities.BASE_URL + "product/view_cart";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentOnlineShoppingViewCartScreen.this, nameValuePairList, CART_DATA, ParentOnlineShoppingViewCartScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentOnlineShoppingViewCartScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }

    private void promoApply (){
        if(Utilities.isNetworkAvailable(ParentOnlineShoppingViewCartScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("amount", dblNetAmount+""));
            nameValuePairList.add(new BasicNameValuePair("module_type", "5"));
            nameValuePairList.add(new BasicNameValuePair("promo_code", strPromoCode));

            String webServiceUrl = Utilities.BASE_URL + "product/apply_promo_code";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentOnlineShoppingViewCartScreen.this, nameValuePairList, APPLY_PROMO_CODE, ParentOnlineShoppingViewCartScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentOnlineShoppingViewCartScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case CART_DATA:

                productOnlineShoppingProductDetailsBeanArrayList.clear();

                if (response == null) {
                    Toast.makeText(ParentOnlineShoppingViewCartScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONObject settingObj = responseObject.getJSONObject("setting");
                            String tax_label = settingObj.getString("tax_label");
                            String gst = settingObj.getString("gst");
                            String delivery_charges = settingObj.getString("delivery_charges");
                            JSONArray jsonArray = responseObject.getJSONArray("cart");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                productOnlineShoppingViewCartBean = new ProductOnlineShoppingViewCartBean();
                                productOnlineShoppingViewCartBean.setTaxLabel(tax_label);
                                productOnlineShoppingViewCartBean.setGst(gst);
                                productOnlineShoppingViewCartBean.setDelCharges(delivery_charges);

                                productOnlineShoppingViewCartBean.setId(jsonObject.getString("id"));
                                productOnlineShoppingViewCartBean.setCartId(jsonObject.getString("cart_id"));
                                productOnlineShoppingViewCartBean.setProductId(jsonObject.getString("product_id"));
                                productOnlineShoppingViewCartBean.setCombinationId(jsonObject.getString("combination_id"));
                                productOnlineShoppingViewCartBean.setAvailStock(jsonObject.getString("stock"));
                                productOnlineShoppingViewCartBean.setState(jsonObject.getString("state"));
                                productOnlineShoppingViewCartBean.setCreatedAt(jsonObject.getString("created_at"));
                                productOnlineShoppingViewCartBean.setModifiedAt(jsonObject.getString("modified_at"));

                                JSONObject jsonObject1 = jsonObject.getJSONObject("product_info");
                                productOnlineShoppingViewCartBean.setProductInfoName(jsonObject1.getString("name"));
                                productOnlineShoppingViewCartBean.setProductInfoImage(jsonObject1.getString("image"));
                                productOnlineShoppingViewCartBean.setProductInfoPrice(jsonObject1.getString("price"));
                                productOnlineShoppingViewCartBean.setProductInfoSku(jsonObject1.getString("sku"));
                                productOnlineShoppingViewCartBean.setProductInfoQuantity(jsonObject1.getString("quantity"));

                                if(jsonObject1.getString("combination").equalsIgnoreCase("")){

                                }else{
                                    try{
                                        String combar = jsonObject1.getString("combination");
                                        JSONArray combArray = new JSONArray(combar);
                                      //  JSONArray combArray = jsonObject1.getJSONArray("combination");
                                        ArrayList<String> arrayList = new ArrayList<>();
                                        arrayList.clear();
                                        for(int j=0; j<combArray.length(); j++){
                                            String combination = combArray.getString(j);
                                            arrayList.add(combination);
                                        }
                                        productOnlineShoppingViewCartBean.setCombinationArrayList(arrayList);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                if(jsonObject1.has("attribute_data")){
                                    try{
                                        String att = jsonObject1.getString("attribute_data");
                                        JSONArray attArray = new JSONArray(att);
                                        ArrayList<ProductOnlineShoppingAttributeDataBean> productOnlineShoppingAttributeDataBeanArrayList = new ArrayList<>();
                                        productOnlineShoppingAttributeDataBeanArrayList.clear();
                                        for(int j=0; j<attArray.length(); j++){
                                            JSONObject jsonObject2 = attArray.getJSONObject(j);
                                            ProductOnlineShoppingAttributeDataBean productOnlineShoppingAttributeDataBean = new ProductOnlineShoppingAttributeDataBean();
                                            productOnlineShoppingAttributeDataBean.setName(jsonObject2.getString("name"));
                                            productOnlineShoppingAttributeDataBean.setValue(jsonObject2.getString("value"));
                                            productOnlineShoppingAttributeDataBeanArrayList.add(productOnlineShoppingAttributeDataBean);
                                        }
                                        productOnlineShoppingViewCartBean.setProductOnlineShoppingAttributeDataBeanArrayList(productOnlineShoppingAttributeDataBeanArrayList);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }else{
                                    ArrayList<ProductOnlineShoppingAttributeDataBean> productOnlineShoppingAttributeDataBeanArrayList = new ArrayList<>();
                                    productOnlineShoppingAttributeDataBeanArrayList.clear();
                                    ProductOnlineShoppingAttributeDataBean productOnlineShoppingAttributeDataBean = new ProductOnlineShoppingAttributeDataBean();
                                    productOnlineShoppingAttributeDataBean.setName("-1");
                                    productOnlineShoppingAttributeDataBean.setValue("-1");
                                    productOnlineShoppingAttributeDataBeanArrayList.add(productOnlineShoppingAttributeDataBean);
                                    productOnlineShoppingViewCartBean.setProductOnlineShoppingAttributeDataBeanArrayList(productOnlineShoppingAttributeDataBeanArrayList);

                                }
                                productOnlineShoppingProductDetailsBeanArrayList.add(productOnlineShoppingViewCartBean);
                            }

                            ParentOnlineShoppingViewCartAdapter parentOnlineShoppingViewCartAdapter = new ParentOnlineShoppingViewCartAdapter(this,productOnlineShoppingProductDetailsBeanArrayList, ParentOnlineShoppingViewCartScreen.this);
                            listView.setAdapter(parentOnlineShoppingViewCartAdapter);
                            Utilities.setListViewHeightBasedOnChildren(listView);
                            totalScoreUpdate();

                        } else {
                            Toast.makeText(ParentOnlineShoppingViewCartScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentOnlineShoppingViewCartScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case APPLY_PROMO_CODE:

                if (response == null) {
                    Toast.makeText(ParentOnlineShoppingViewCartScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentOnlineShoppingViewCartScreen.this, message, Toast.LENGTH_LONG).show();

                        if(status) {

                            String strDeductAmount = responseObject.getString("deduct_amt");
                            try{
                                promoCodeDeductAmount = Double.parseDouble(strDeductAmount);
                                System.out.println("promoCodeDeductAmount::"+promoCodeDeductAmount);

                                String academy_currency = sharedPreferences.getString("academy_currency", null);
                                promoCodeDiscount.setText(strDeductAmount+" "+academy_currency);

                                System.out.println("NET_AMOUNT::"+dblNetAmount+"::PROMO_CODE::"+promoCodeDeductAmount);


                                dblNetAmount = dblNetAmount - promoCodeDeductAmount;


                                promoCheck = "yes";

                                if(dblNetAmount == 0) {
                                    netAmount.setText("0.00 "+academy_currency);

                                } else {
//                                    netAmount.setText(decimalFormat.format(dblNetAmount)+" "+academy_currency);
                                    afterGstPrice(dblNetAmount);
                                    //     roundedOffNetAmount = Math.round(dblNetAmount);

                                    System.out.println("HereValue::"+dblNetAmount);

                                 //   netAmount.setText(dblNetAmount+" "+academy_currency);
                                }

                            } catch (NumberFormatException e){
                                Toast.makeText(ParentOnlineShoppingViewCartScreen.this, "Invalid discount", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentOnlineShoppingViewCartScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }

    public void hitAPItoSetListSizeView(){
        listView.setAdapter(null);
        onlineCartData();
    }


    public void totalScoreUpdate(){
        if(productOnlineShoppingProductDetailsBeanArrayList.size()==0){
            finish();
        }else{
             totalItems = 0;
            gstStr = productOnlineShoppingProductDetailsBeanArrayList.get(0).getGst();
            delChargesStr = productOnlineShoppingProductDetailsBeanArrayList.get(0).getDelCharges();
//        gstTV.setText(gstStr+"%");

            for(int i=0; i<productOnlineShoppingProductDetailsBeanArrayList.size(); i++){

                double totalPriceInt =  Double.parseDouble(productOnlineShoppingProductDetailsBeanArrayList.get(i).getProductInfoPrice()) *  Double.parseDouble(productOnlineShoppingProductDetailsBeanArrayList.get(i).getAvailStock());
                if(Double.parseDouble(productOnlineShoppingProductDetailsBeanArrayList.get(i).getProductInfoQuantity()) > Double.parseDouble(productOnlineShoppingProductDetailsBeanArrayList.get(i).getAvailStock())){
                    totalItems = totalItems +  totalPriceInt;
                }
            }
            String academy_currency = sharedPreferences.getString("academy_currency", null);
            totalAmount.setText(""+decimalFormat.format(totalItems)+" "+academy_currency);
            dblNetAmount = totalItems;

//        netAmount.setText(""+decimalFormat.format(dblNetAmount)+" "+academy_currency);
            promoCodeEditText.setText("");
            promoCodeDiscount.setText("0.00 "+academy_currency);
            strPromoCode = promoCodeEditText.getText().toString().trim();

            afterGstPrice(dblNetAmount);
        }


    }

    public void afterGstPrice(double dblNetAmount){
        String academy_currency = sharedPreferences.getString("academy_currency", null);
        tAmountTV.setText(""+decimalFormat.format(dblNetAmount)+" "+academy_currency);
        double gstPrice = (dblNetAmount * Double.parseDouble(gstStr)) / 100;
        dblNetAmount = dblNetAmount + gstPrice;
        dblNetAmount = dblNetAmount + Double.parseDouble(delChargesStr);
        gstTV.setText(decimalFormat.format(gstPrice)+" "+academy_currency +" ("+gstStr+"%)");
        delTV.setText(delChargesStr+" "+academy_currency);
        netAmount.setText(""+decimalFormat.format(dblNetAmount)+" "+academy_currency);
    }


}