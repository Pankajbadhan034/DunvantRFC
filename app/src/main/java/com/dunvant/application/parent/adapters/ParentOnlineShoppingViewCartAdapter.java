package com.dunvant.application.parent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.dunvant.application.parent.ParentOnlineShoppingViewCartScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class ParentOnlineShoppingViewCartAdapter extends BaseAdapter implements IWebServiceCallback {
    private final String DELETE_CART_VALUE = "DELETE_CART_VALUE";
    Context context;
    ArrayList<ProductOnlineShoppingViewCartBean> productOnlineShoppingViewCartBeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    ParentOnlineShoppingViewCartScreen parentOnlineShoppingViewCartScreen;

    public ParentOnlineShoppingViewCartAdapter(Context context, ArrayList<ProductOnlineShoppingViewCartBean> productOnlineShoppingViewCartBeanArrayList, ParentOnlineShoppingViewCartScreen parentOnlineShoppingViewCartScreen){
        this.context = context;
        this.productOnlineShoppingViewCartBeanArrayList = productOnlineShoppingViewCartBeanArrayList;
        this.parentOnlineShoppingViewCartScreen = parentOnlineShoppingViewCartScreen;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

    }

    @Override
    public int getCount() {
        return productOnlineShoppingViewCartBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return productOnlineShoppingViewCartBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_online_shopping_view_cart_item, null);

        RelativeLayout mainRelativeLayout = convertView.findViewById(R.id.mainRelativeLayout);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView name = convertView.findViewById(R.id.name);
        TextView variations = convertView.findViewById(R.id.variations);
        TextView perItemPrice = convertView.findViewById(R.id.perItemPrice);
        TextView sku = convertView.findViewById(R.id.sku);
        TextView availStock = convertView.findViewById(R.id.availStock);
        TextView quantityStock = convertView.findViewById(R.id.quantityStock);
        TextView totalPrice = convertView.findViewById(R.id.totalPrice);
        ImageView delete = convertView.findViewById(R.id.delete);
        LinearLayout linearEdit = convertView.findViewById(R.id.linearEdit);
        name.setTypeface(linoType);

        final ProductOnlineShoppingViewCartBean productOnlineShoppingViewCartBean = productOnlineShoppingViewCartBeanArrayList.get(position);
        name.setText(""+productOnlineShoppingViewCartBean.getProductInfoName());
        imageLoader.displayImage(productOnlineShoppingViewCartBean.getProductInfoImage(), image, options);


            String variationsStr="";
            for (ProductOnlineShoppingAttributeDataBean productOnlineShoppingAttributeDataBean : productOnlineShoppingViewCartBean.getProductOnlineShoppingAttributeDataBeanArrayList()) {
                if(productOnlineShoppingAttributeDataBean.getName().equalsIgnoreCase("-1")) {
                    variationsStr = variationsStr + productOnlineShoppingAttributeDataBean.getName()+" : "+productOnlineShoppingAttributeDataBean.getValue()+", ";
                    variations.setVisibility(View.GONE);
                }else{
                    variationsStr = variationsStr+productOnlineShoppingAttributeDataBean.getName()+" : "+productOnlineShoppingAttributeDataBean.getValue()+", ";
                    variations.setVisibility(View.VISIBLE);
            }

        }
            try{
                variationsStr = variationsStr.substring(0, variationsStr.length() - 2);
                variations.setText(variationsStr);
            }catch (Exception e){
                e.printStackTrace();
            }


//        if(productOnlineShoppingViewCartBean.getProductOnlineShoppingAttributeDataBeanArrayList().size()==0){
//            variations.setText("PRODUCT VARIATIONS - ");
//            variations.setVisibility(View.GONE);
//        }else{
//            String combString="";
//            for(int i=0; i<productOnlineShoppingViewCartBean.getProductOnlineShoppingAttributeDataBeanArrayList().size(); i++){
//                ProductOnlineShoppingAttributeDataBean productOnlineShoppingAttributeDataBean = new ProductOnlineShoppingAttributeDataBean();
//
//                combString = combString + productOnlineShoppingViewCartBean.getCombinationArrayList().get(i)+", ";
//            }
//            variations.setText("PRODUCT VARIATIONS - "+combString);
//            variations.setVisibility(View.GONE);
//        }


        perItemPrice.setText("Price Per Unit: "+productOnlineShoppingViewCartBean.getProductInfoPrice());
        sku.setText("SKU : "+productOnlineShoppingViewCartBean.getProductInfoSku());
        availStock.setText("Available Stock : "+productOnlineShoppingViewCartBean.getProductInfoQuantity());
        quantityStock.setText("Quantity : "+productOnlineShoppingViewCartBean.getAvailStock());


        try{
            String academy_currency = sharedPreferences.getString("academy_currency", null);
            double totalPriceInt =  Double.parseDouble(productOnlineShoppingViewCartBean.getProductInfoPrice()) *  Double.parseDouble(productOnlineShoppingViewCartBean.getAvailStock());
            totalPrice.setText(""+totalPriceInt+" "+academy_currency);
        }catch (Exception e){
            e.printStackTrace();
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCart(productOnlineShoppingViewCartBeanArrayList.get(position).getId(), productOnlineShoppingViewCartBeanArrayList.get(position).getCartId());
                        productOnlineShoppingViewCartBeanArrayList.remove(position);
                        notifyDataSetChanged();
                        parentOnlineShoppingViewCartScreen.totalScoreUpdate();
                        parentOnlineShoppingViewCartScreen.hitAPItoSetListSizeView();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        linearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_online_shop_edit_quantity_dialog);

                final EditText quantityEdit = (EditText) dialog.findViewById(R.id.quantityEdit);
                Button add = (Button) dialog.findViewById(R.id.add);

                quantityEdit.setText(productOnlineShoppingViewCartBean.getAvailStock());
                quantityEdit.setSelection(quantityEdit.getText().length());

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quantityEditStr = quantityEdit.getText().toString().trim();
                        productOnlineShoppingViewCartBeanArrayList.get(position).setAvailStock(quantityEditStr);
                        notifyDataSetChanged();
                        parentOnlineShoppingViewCartScreen.totalScoreUpdate();
                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });

        if(Integer.parseInt(productOnlineShoppingViewCartBean.getProductInfoQuantity()) < Integer.parseInt(productOnlineShoppingViewCartBean.getAvailStock())){
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.red_trans));
           // name.setTextColor(context.getResources().getColor(R.color.red));
            //variations.setTextColor(context.getResources().getColor(R.color.red));
            //perItemPrice.setTextColor(context.getResources().getColor(R.color.red));
            //sku.setTextColor(context.getResources().getColor(R.color.white));
            //totalPrice.setTextColor(context.getResources().getColor(R.color.red));
            quantityStock.setTextColor(context.getResources().getColor(R.color.red));
            availStock.setTextColor(context.getResources().getColor(R.color.red));
            availStock.setVisibility(View.VISIBLE);
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            name.setTextColor(context.getResources().getColor(R.color.black));
            variations.setTextColor(context.getResources().getColor(R.color.black));
            perItemPrice.setTextColor(context.getResources().getColor(R.color.black));
            sku.setTextColor(context.getResources().getColor(R.color.black));
            totalPrice.setTextColor(context.getResources().getColor(R.color.black));
            availStock.setVisibility(View.GONE);
        }


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 60) / 100;
        float textWidthForTitle = name.getPaint().measureText(name.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        name.setLines(numberOfLines);


        return convertView;
    }

    private void deleteCart(String id, String cartId) {
        if (Utilities.isNetworkAvailable(context)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("id", id));
            nameValuePairList.add(new BasicNameValuePair("cart_id", cartId));

            String webServiceUrl = Utilities.BASE_URL + "product/remove_cart_product";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, DELETE_CART_VALUE, ParentOnlineShoppingViewCartAdapter.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {

            case DELETE_CART_VALUE:

                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;


        }
    }
}