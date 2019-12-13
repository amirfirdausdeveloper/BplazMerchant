package com.bplaz.merchant.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bplaz.merchant.Adapter.ProductAdapter;
import com.bplaz.merchant.Adapter.SalesListAdapter;
import com.bplaz.merchant.Adapter.ServicesAdapter;
import com.bplaz.merchant.Adapter.ToAcceptAdapter;
import com.bplaz.merchant.Class.MyScrollView;
import com.bplaz.merchant.Class.ProductClass;
import com.bplaz.merchant.Class.SalesListClass;
import com.bplaz.merchant.Class.ServicesClass;
import com.bplaz.merchant.Class.StandardProgressDialog;
import com.bplaz.merchant.Class.ToAcceptClass;
import com.bplaz.merchant.Class.TypeFaceClass;
import com.bplaz.merchant.Preferance.PreferenceManagerLogin;
import com.bplaz.merchant.R;
import com.bplaz.merchant.URL.UrlClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.PUT;

public class SalesDetailsActivity extends AppCompatActivity {

    //ORDER ID
    TextView textView_sales_id,textView_title;
    LinearLayout textView_view;
    ImageView imageView_back;

    //LINEAR CUSTOMER
    TextView textView_customer_title;
    TextView textView_customer_name_title,textView_customer_phone_title,textView_customer_email_title;
    TextView textView_customer_name_value,textView_customer_phone_value,textView_customer_email_value;

    //LINEAR ADDRESS
    TextView textView_address_title;
    TextView textView_geo_location_title,textView_geo_location_customer_supply_title,textView_delivery_date_title;
    TextView textView_geo_location_value,textView_geo_location_customer_supply_value,textView_delivery_date_value;

    //LINEAR VEHICLE
    TextView textView_vehicle_title;
    TextView textView_model_title,textView_plate_number_title,textView_current_mileage_title,textView_payment_status_title,textView_customer_memo_title;
    TextView textView_model_value,textView_plate_number_value,textView_current_mileage_value,textView_payment_status_value,textView_customer_memo_value;

    //LINEAR RIDER
    TextView textView_rider_title;
    TextView textView_name_rider_title,textView_rider_phone_title,textView_rider_status_title;
    TextView textView_name_rider_value,textView_rider_phone_value,textView_rider_status_value;

    //LINEAR BUTTON
    LinearLayout linear_button;
    Button button_accept,button_reject;

    //PRODUCT
    ViewGroup viewGroup_product;
    View view_product;
    androidx.appcompat.app.AlertDialog.Builder builder_product;
    androidx.appcompat.app.AlertDialog dialog_product;

    //SCROLLVIEW
    ScrollView scrollView;

    String order_id,status,token;

    String customer_lan,customer_long;

    PreferenceManagerLogin session;
    StandardProgressDialog standardProgressDialog;

    JSONArray arr_product = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_details);

        //GET SESSION
        session = new PreferenceManagerLogin(getApplicationContext());
        standardProgressDialog = new StandardProgressDialog(this.getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        token = user.get(PreferenceManagerLogin.KEY_TOKEN);

        order_id = getIntent().getStringExtra("id");
        status = getIntent().getStringExtra("status");

        //DECLARE
        declare();

        //SET FONT
        setFont();

        //ON BACK
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                SalesDetailsActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

        });

        if(status.equals("accept")){
            scrollView.setOnTouchListener( new View.OnTouchListener( ) {
                @Override
                public boolean onTouch( View v, MotionEvent event ) {
                    switch ( event.getAction( ) ) {
                        case MotionEvent.ACTION_SCROLL:
                        case MotionEvent.ACTION_MOVE:
                            Log.e( "SCROLL", "ACTION_SCROLL" );
                            linear_button.setVisibility(View.GONE);
                            break;
                        case MotionEvent.ACTION_DOWN:
                            Log.e( "SCROLL", "ACTION_DOWN" );
                            linear_button.setVisibility(View.GONE);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            linear_button.setVisibility(View.VISIBLE);
                            break;
                    }
                    return false;
                }
            } );
        }else {
            linear_button.setVisibility(View.GONE);
        }

        //ACCEPT ORDER
        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SalesDetailsActivity.this)
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure want to accept?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                standardProgressDialog.show();
                                acceptJob();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        //REJECT ORDER
        button_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SalesDetailsActivity.this)
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure want to reject?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                standardProgressDialog.show();
                                rejectJob();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        //VIEW PRODUCT
        textView_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProduct();
            }
        });

    }

    private void getProduct(){
        viewGroup_product = findViewById(android.R.id.content);
        view_product = LayoutInflater.from(this).inflate(R.layout.dialog_view_product, viewGroup_product, false);
        builder_product = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder_product.setView(view_product);
        dialog_product = builder_product.create();
        dialog_product.show();

        double price = 0;

        RecyclerView recyclerView_p = view_product.findViewById(R.id.recyclerView_p);
        RecyclerView recyclerView_s = view_product.findViewById(R.id.recyclerView_s);
        TextView textView_price_value = view_product.findViewById(R.id.textView_price_value);
        ImageView imageView_cancel = view_product.findViewById(R.id.imageView_cancel);

        imageView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_product.dismiss();
            }
        });

        List<ProductClass> productClasses;
        ProductAdapter productAdapter;

        List<ServicesClass> servicesClasses;
        ServicesAdapter servicesAdapter;

        recyclerView_p.setHasFixedSize(false);
        productClasses = new ArrayList<>();
        productAdapter = new ProductAdapter(getApplicationContext(), productClasses,SalesDetailsActivity.this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_p.setLayoutManager(horizontalLayoutManager);
        recyclerView_p.setAdapter(productAdapter);


        recyclerView_s.setHasFixedSize(false);
        servicesClasses = new ArrayList<>();
        servicesAdapter = new ServicesAdapter(getApplicationContext(), servicesClasses,SalesDetailsActivity.this);
        LinearLayoutManager horizontalLayoutManagers = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_s.setLayoutManager(horizontalLayoutManagers);
        recyclerView_s.setAdapter(servicesAdapter);

        for (int i = 0; i<arr_product.length(); i++){
            try {
                JSONObject obj = arr_product.getJSONObject(i);
                if(obj.getString("type").equals("product")){
                    productClasses.add(new ProductClass(
                            "RM "+obj.getString("price"),
                            obj.getString("name"),
                            obj.getString("type")
                    ));

                    productAdapter = new ProductAdapter(getApplicationContext(), productClasses,SalesDetailsActivity.this);
                    recyclerView_p.setAdapter(productAdapter);
                }else{
                    servicesClasses.add(new ServicesClass(
                            "RM "+obj.getString("price"),
                            obj.getString("name"),
                            obj.getString("type")
                    ));

                    servicesAdapter = new ServicesAdapter(getApplicationContext(), servicesClasses,SalesDetailsActivity.this);
                    recyclerView_s.setAdapter(servicesAdapter);
                }

                price = price + Double.parseDouble(obj.getString("price"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        textView_price_value.setText(""+price);

    }

    private void declare() {
        scrollView = findViewById(R.id.scrollView);

        textView_title = findViewById(R.id.textView_title);
        imageView_back = findViewById(R.id.imageView_back);
        textView_sales_id = findViewById(R.id.textView_sales_id);
        textView_view = findViewById(R.id.textView_view);

        textView_customer_title = findViewById(R.id.textView_customer_title);
        textView_customer_name_title = findViewById(R.id.textView_customer_name_title);
        textView_customer_phone_title = findViewById(R.id.textView_customer_phone_title);
        textView_customer_email_title = findViewById(R.id.textView_customer_email_title);
        textView_customer_name_value = findViewById(R.id.textView_customer_name_value);
        textView_customer_phone_value = findViewById(R.id.textView_customer_phone_value);
        textView_customer_email_value = findViewById(R.id.textView_customer_email_value);

        textView_address_title = findViewById(R.id.textView_address_title);
        textView_geo_location_title = findViewById(R.id.textView_geo_location_title);
        textView_geo_location_customer_supply_title = findViewById(R.id.textView_geo_location_customer_supply_title);
        textView_delivery_date_title = findViewById(R.id.textView_delivery_date_title);
        textView_geo_location_value = findViewById(R.id.textView_geo_location_value);
        textView_geo_location_customer_supply_value = findViewById(R.id.textView_geo_location_customer_supply_value);
        textView_delivery_date_value = findViewById(R.id.textView_delivery_date_value);

        textView_vehicle_title = findViewById(R.id.textView_vehicle_title);
        textView_model_title = findViewById(R.id.textView_model_title);
        textView_plate_number_title = findViewById(R.id.textView_plate_number_title);
        textView_current_mileage_title = findViewById(R.id.textView_current_mileage_title);
        textView_payment_status_title = findViewById(R.id.textView_payment_status_title);
        textView_customer_memo_title = findViewById(R.id.textView_customer_memo_title);
        textView_model_value = findViewById(R.id.textView_model_value);
        textView_plate_number_value = findViewById(R.id.textView_plate_number_value);
        textView_current_mileage_value = findViewById(R.id.textView_current_mileage_value);
        textView_payment_status_value = findViewById(R.id.textView_payment_status_value);
        textView_customer_memo_value = findViewById(R.id.textView_customer_memo_value);

        textView_rider_title = findViewById(R.id.textView_rider_title);
        textView_name_rider_title = findViewById(R.id.textView_name_rider_title);
        textView_rider_phone_title = findViewById(R.id.textView_rider_phone_title);
        textView_rider_status_title = findViewById(R.id.textView_rider_status_title);
        textView_name_rider_value = findViewById(R.id.textView_name_rider_value);
        textView_rider_phone_value = findViewById(R.id.textView_rider_phone_value);
        textView_rider_status_value = findViewById(R.id.textView_rider_status_value);

        linear_button = findViewById(R.id.linear_button);
        button_accept = findViewById(R.id.button_accept);
        button_reject = findViewById(R.id.button_reject);
    }

    private void setFont() {
        TypeFaceClass.setTypeFaceTextViewBold(textView_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextViewBold(textView_sales_id,getApplicationContext());
        TypeFaceClass.setTypeFaceTextViewBold(textView_customer_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextViewBold(textView_address_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextViewBold(textView_vehicle_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextViewBold(textView_rider_title,getApplicationContext());
        TypeFaceClass.setTypeFaceButton(button_accept,getApplicationContext());
        TypeFaceClass.setTypeFaceButton(button_reject,getApplicationContext());

        TypeFaceClass.setTypeFaceTextView(textView_customer_name_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_customer_phone_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_customer_email_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_customer_name_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_customer_phone_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_customer_email_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_geo_location_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_geo_location_customer_supply_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_delivery_date_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_geo_location_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_geo_location_customer_supply_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_delivery_date_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_model_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_plate_number_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_current_mileage_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_payment_status_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_customer_memo_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_model_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_plate_number_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_current_mileage_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_payment_status_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_customer_memo_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_name_rider_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_rider_phone_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_rider_status_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_name_rider_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_rider_phone_value,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_rider_status_value,getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        standardProgressDialog.show();
        getDetails();
    }

    private void getDetails() {
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_sales_details_URL+order_id+".json",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject saleOBJ = new JSONObject(object.getString("sale"));

                            textView_sales_id.setText("#"+saleOBJ.getString("id"));

                            //GET CUSTOMER INFO
                            JSONObject customerOBJ = new JSONObject(saleOBJ.getString("customer"));
                            textView_customer_name_value.setText(customerOBJ.getString("name"));
                            textView_customer_phone_value.setText(customerOBJ.getString("telephone_number"));
                            textView_customer_email_value.setText(customerOBJ.getString("email"));

                            //GET ADDRESS INFO
                            if (saleOBJ.getString("sale").equals("null") || saleOBJ.getString("sale").equals(null)) {

                                JSONObject rider_job2 = new JSONObject(saleOBJ.getString("rider_job"));

                                textView_geo_location_value.setText(rider_job2.getString("geo_location"));
                                textView_geo_location_customer_supply_value.setText(rider_job2.getString("geo_location"));
                                if(rider_job2.getString("job_date").equals("null") || rider_job2.getString("job_date").equals(null)){
                                    textView_delivery_date_value.setText("-");
                                }else {
                                    textView_delivery_date_value.setText(rider_job2.getString("job_date"));
                                }
                                customer_lan = rider_job2.getString("latitude");
                                customer_long = rider_job2.getString("longitude");

                            }else {
                                JSONObject saleOBJS = new JSONObject(saleOBJ.getString("sale"));
                                textView_geo_location_value.setText(saleOBJS.getString("geo_location"));
                                textView_geo_location_customer_supply_value.setText(saleOBJS.getString("address"));
                                if(saleOBJS.getString("job_date").equals("null") || saleOBJS.getString("job_date").equals(null)){
                                    textView_delivery_date_value.setText("-");
                                }else {
                                    textView_delivery_date_value.setText(saleOBJS.getString("job_date"));
                                }
                                customer_lan = saleOBJS.getString("latitude");
                                customer_long = saleOBJS.getString("longitude");
                            }


                            //GET VEHICLE INFO
                            JSONObject vehicleOBJ = new JSONObject(saleOBJ.getString("vehicle"));
                            JSONObject manuFacOBJ = new JSONObject(vehicleOBJ.getString("manufacturer"));
                            textView_model_value.setText(manuFacOBJ.getString("name") +" "+vehicleOBJ.getString("name"));
                            textView_plate_number_value.setText(saleOBJ.getString("plate_number"));
                            textView_current_mileage_value.setText(saleOBJ.getString("current_mileage"));
                            if(saleOBJ.getString("payment_status").equals("0")){
                                textView_payment_status_value.setText("UNPAID");
                            }
                            textView_customer_memo_value.setText(saleOBJ.getString("memo"));

                            //GET RIDER INFO
                            JSONObject riderJobOBJ = new JSONObject(saleOBJ.getString("rider_job"));
                            JSONObject riderOBJ = new JSONObject(riderJobOBJ.getString("rider"));
                            textView_name_rider_value.setText(riderOBJ.getString("name"));
                            textView_rider_phone_value.setText(riderOBJ.getString("telephone_number"));
                            if(riderJobOBJ.getString("status").equals("1")){
                                textView_rider_status_value.setText("PENDING PARTNER ACCEPT");
                            }else if(riderJobOBJ.getString("status").equals("2")){
                                textView_rider_status_value.setText("PENDING RIDER ACCEPT");
                            }else if(riderJobOBJ.getString("status").equals("3")){
                                textView_rider_status_value.setText("RIDER ACCEPTED");
                            }else if(riderJobOBJ.getString("status").equals("6")){
                                textView_rider_status_value.setText("READY TO DISPATCH");
                            }else if(riderJobOBJ.getString("status").equals("8")){
                                textView_rider_status_value.setText("DISPATCHED");
                            }else if(riderJobOBJ.getString("status").equals("7")){
                                textView_rider_status_value.setText("IN PROGRESS");
                            }else if(riderJobOBJ.getString("status").equals("7")){
                                textView_rider_status_value.setText("IN PROGRESS");
                            }else if(riderJobOBJ.getString("status").equals("9")){
                                textView_rider_status_value.setText("COMPLETED");
                            }


                            //GET PRODUCT AND SERVICES
                            JSONArray sale_partner_items_arr = new JSONArray(saleOBJ.getString("sale_partner_items"));
                            for (int i =0; i < sale_partner_items_arr.length(); i++){
                                JSONObject obj = sale_partner_items_arr.getJSONObject(i);
                                JSONObject product_partner = new JSONObject(obj.getString("product_partner"));

                                JSONObject object_product = new JSONObject();
                                object_product.put("price",obj.getString("price_per_unit"));
                                object_product.put("name",product_partner.getString("product_name"));

                                if(product_partner.getString("service").equals("1")){
                                    object_product.put("type","service");
                                }else{
                                    object_product.put("type","product");
                                }

                                arr_product.put(object_product);
                            }


                            Log.d("yes",arr_product.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        standardProgressDialog.dismiss();
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void rejectJob() {
        StringRequest stringRequest = new StringRequest(PUT, UrlClass.reject_job_URL+order_id+".json",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject data = new JSONObject(object.getString("data"));

                            new AlertDialog.Builder(SalesDetailsActivity.this)
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage(data.getString("message"))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onBackPressed();
                                            SalesDetailsActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        }
                                    })
                                    .show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        standardProgressDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reject", "1");
                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void acceptJob() {
        StringRequest stringRequest = new StringRequest(PUT, UrlClass.accept_job_URL+order_id+".json",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject data = new JSONObject(object.getString("data"));

                            new AlertDialog.Builder(SalesDetailsActivity.this)
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage(data.getString("message"))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onBackPressed();
                                            SalesDetailsActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        }
                                    })
                                    .show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        standardProgressDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accept", "1");
                return params;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
