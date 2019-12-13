package com.bplaz.merchant.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bplaz.merchant.Adapter.ProductListAdapter;
import com.bplaz.merchant.Class.ProductListClass;
import com.bplaz.merchant.Class.StandardProgressDialog;
import com.bplaz.merchant.Class.TypeFaceClass;
import com.bplaz.merchant.Preferance.PreferenceManagerLogin;
import com.bplaz.merchant.R;
import com.bplaz.merchant.URL.UrlClass;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class CreateProductActivity extends AppCompatActivity {

    CheckBox checkBox,checkBox_stock_available;
    LinearLayout linear_product,linear_services;

    TextView textView_title,textView_brand,textView_product_name,textView_manufacture,textView_sku,textView_service_name,textView_service_type,
            textView_image_title,textView_image_click;
    TextView textView_retails,textView_base;
    EditText editText_retail_price,editText_base_price;
    EditText editText_brand,editText_product_name,editText_manufacture,editText_sku,editText_service_name;
    SearchableSpinner spinner_service_type;
    PreferenceManagerLogin session;
    StandardProgressDialog standardProgressDialog;
    String token,service_type_id;
    ImageView imageView_photo;
    Bitmap selected_image = null;
    Button button_submit;
    ImageView imageView_back;
    String intent_id;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //GET SESSION
        session = new PreferenceManagerLogin(getApplicationContext());
        standardProgressDialog = new StandardProgressDialog(this.getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        token = user.get(PreferenceManagerLogin.KEY_TOKEN);


        declare();

        setfont();

        imageView_back = findViewById(R.id.imageView_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                CreateProductActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        linear_product.setVisibility(View.VISIBLE);
        linear_services.setVisibility(View.GONE);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    linear_product.setVisibility(View.GONE);
                    linear_services.setVisibility(View.VISIBLE);
                } else {
                    linear_product.setVisibility(View.VISIBLE);
                    linear_services.setVisibility(View.GONE);
                }
            }

        });



        getSpinnerType();


        //TAKE IMAGE
        textView_image_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
        imageView_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        //BUTTON SUBMIT
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){ //SERVICES
                    if(editText_service_name.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please insert service name",Toast.LENGTH_LONG).show();
                    }else if(spinner_service_type.getSelectedItem().toString().equals("Please choose service type")){
                        Toast.makeText(getApplicationContext(),"Please select service type", Toast.LENGTH_LONG).show();
                    }else if(editText_base_price.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please insert base price", Toast.LENGTH_LONG).show();
                    }else if(editText_retail_price.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please insert retail price", Toast.LENGTH_LONG).show();
                    }else{
                        if(selected_image == null){
                            standardProgressDialog.show();
                            if(status.equals("add")){
                                addServiceWithoutImage();
                            }else{
                                editServiceWithoutImage();
                            }

                        }else{
                            addServiceWithoutImage();
                        }
                    }
                }else { //PRODUCT
                    if(editText_brand.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please insert product brand",Toast.LENGTH_LONG).show();
                    }else if(spinner_service_type.getSelectedItem().toString().equals("Please choose service type")){
                        Toast.makeText(getApplicationContext(),"Please select service type", Toast.LENGTH_LONG).show();
                    }else if(editText_product_name.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please insert product name", Toast.LENGTH_LONG).show();
                    }else if(editText_manufacture.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please insert manufacture", Toast.LENGTH_LONG).show();
                    }else if(editText_base_price.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please insert base price", Toast.LENGTH_LONG).show();
                    }else if(editText_retail_price.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please insert retail price", Toast.LENGTH_LONG).show();
                    }else{
                        if(selected_image == null){
                            standardProgressDialog.show();
                            if(status.equals("add")){
                                addProductWithoutImage();
                            }else{
                                editProductWithoutImage();
                            }

                        }else{
                            addProductWithoutImage();
                        }
                    }
                }
            }
        });

        //IF EDIT OR ADD
        if(getIntent().hasExtra("id")){
            editText_service_name.setEnabled(false);
            spinner_service_type.setEnabled(false);
            editText_brand.setEnabled(false);
            editText_manufacture.setEnabled(false);
            editText_product_name.setEnabled(false);
            checkBox.setVisibility(View.GONE);

            standardProgressDialog.show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    intent_id = getIntent().getStringExtra("id");
                    textView_title.setText("EDIT PRODUCT");
                    status = "edit";

                    if(getIntent().getStringExtra("service").equals("1")){
                        checkBox.setChecked(true);
                        editText_service_name.setText(getIntent().getStringExtra("product_name"));
                        if(getIntent().getStringExtra("service_type").equals("13")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Accessories"));
                        }else if(getIntent().getStringExtra("service_type").equals("5")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Battery"));
                        }else if(getIntent().getStringExtra("service_type").equals("16")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Else"));
                        }else if(getIntent().getStringExtra("service_type").equals("15")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Lock Smith"));
                        }else if(getIntent().getStringExtra("service_type").equals("14")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Petrol"));
                        }else if(getIntent().getStringExtra("service_type").equals("12")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Repair"));
                        }else if(getIntent().getStringExtra("service_type").equals("11")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Services"));
                        }else if(getIntent().getStringExtra("service_type").equals("9")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Spare Part"));
                        }else if(getIntent().getStringExtra("service_type").equals("7")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Towing"));
                        }else if(getIntent().getStringExtra("service_type").equals("6")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Tyre"));
                        }

                    }else{
                        checkBox.setChecked(false);
                        editText_brand.setText(getIntent().getStringExtra("product_brand"));
                        editText_product_name.setText(getIntent().getStringExtra("product_name"));
                        editText_manufacture.setText(getIntent().getStringExtra("product_manu"));

                        if(getIntent().getStringExtra("service_type").equals("13")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Accessories"));
                        }else if(getIntent().getStringExtra("service_type").equals("5")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Battery"));
                        }else if(getIntent().getStringExtra("service_type").equals("16")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Else"));
                        }else if(getIntent().getStringExtra("service_type").equals("15")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Lock Smith"));
                        }else if(getIntent().getStringExtra("service_type").equals("14")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Petrol"));
                        }else if(getIntent().getStringExtra("service_type").equals("12")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Repair"));
                        }else if(getIntent().getStringExtra("service_type").equals("11")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Services"));
                        }else if(getIntent().getStringExtra("service_type").equals("9")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Spare Part"));
                        }else if(getIntent().getStringExtra("service_type").equals("7")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Towing"));
                        }else if(getIntent().getStringExtra("service_type").equals("6")){
                            spinner_service_type.setSelection(getIndex(spinner_service_type, "Tyre"));
                        }
                    }

                    if(getIntent().getStringExtra("availability").equals("1")){
                        checkBox_stock_available.setChecked(true);
                    }else{
                        checkBox_stock_available.setChecked(false);
                    }

                    editText_base_price.setText(getIntent().getStringExtra("base_price"));
                    editText_retail_price.setText(getIntent().getStringExtra("retail_price"));
                }
            }, 1000);
        }else {
            status = "add";
        }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    private void editProductWithoutImage(){
        StringRequest stringRequest = new StringRequest(PUT, UrlClass.edit_product_URL+intent_id+".json",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject data = new JSONObject(object.getString("data"));

                            new AlertDialog.Builder(CreateProductActivity.this)
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage(data.getString("message"))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onBackPressed();
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
                params.put("service","0");
                params.put("brand", editText_brand.getText().toString());
                params.put("product_name", editText_product_name.getText().toString());
                params.put("manufacturer", editText_manufacture.getText().toString());
                params.put("sku", editText_sku.getText().toString());

                if(checkBox_stock_available.isChecked()){
                    params.put("availability", "1");
                }else{
                    params.put("availability", "0");
                }
                params.put("pricing[rsp_price]", editText_retail_price.getText().toString());
                params.put("pricing[base_price]", editText_base_price.getText().toString());

                if(spinner_service_type.getSelectedItem().toString().equals("Accessories")){
                    params.put("category", "13");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Battery")){
                    params.put("category", "5");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Else")){
                    params.put("category", "16");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Lock Smith")){
                    params.put("category", "15");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Petrol")){
                    params.put("category", "14");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Repair")){
                    params.put("category", "12");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Services")){
                    params.put("category", "11");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Spare Part")){
                    params.put("category", "9");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Towing")){
                    params.put("category", "7");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Tyre")){
                    params.put("category", "6");
                }



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

    private void addProductWithoutImage() {
        StringRequest stringRequest = new StringRequest(POST, UrlClass.add_product_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject data = new JSONObject(object.getString("data"));

                            new AlertDialog.Builder(CreateProductActivity.this)
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage(data.getString("message"))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onBackPressed();
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
                params.put("service","0");
                params.put("brand", editText_brand.getText().toString());
                params.put("product_name", editText_product_name.getText().toString());
                params.put("manufacturer", editText_manufacture.getText().toString());
                params.put("sku", editText_sku.getText().toString());

                if(checkBox_stock_available.isChecked()){
                    params.put("availability", "1");
                }else{
                    params.put("availability", "0");
                }
                params.put("pricing[rsp_price]", editText_retail_price.getText().toString());
                params.put("pricing[base_price]", editText_base_price.getText().toString());

                if(spinner_service_type.getSelectedItem().toString().equals("Accessories")){
                    params.put("category", "13");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Battery")){
                    params.put("category", "5");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Else")){
                    params.put("category", "16");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Lock Smith")){
                    params.put("category", "15");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Petrol")){
                    params.put("category", "14");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Repair")){
                    params.put("category", "12");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Services")){
                    params.put("category", "11");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Spare Part")){
                    params.put("category", "9");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Towing")){
                    params.put("category", "7");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Tyre")){
                    params.put("category", "6");
                }


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

    private void editServiceWithoutImage(){
        StringRequest stringRequest = new StringRequest(PUT, UrlClass.edit_product_URL+intent_id+".json",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject data = new JSONObject(object.getString("data"));

                            new AlertDialog.Builder(CreateProductActivity.this)
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage(data.getString("message"))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onBackPressed();
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
                params.put("service","1");
                params.put("product_name", editText_service_name.getText().toString());
                if(checkBox_stock_available.isChecked()){
                    params.put("availability", "1");
                }else{
                    params.put("availability", "0");
                }
                params.put("pricing[rsp_price]", editText_retail_price.getText().toString());
                params.put("pricing[base_price]", editText_base_price.getText().toString());

                if(spinner_service_type.getSelectedItem().toString().equals("Accessories")){
                    params.put("category", "13");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Battery")){
                    params.put("category", "5");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Else")){
                    params.put("category", "16");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Lock Smith")){
                    params.put("category", "15");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Petrol")){
                    params.put("category", "14");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Repair")){
                    params.put("category", "12");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Services")){
                    params.put("category", "11");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Spare Part")){
                    params.put("category", "9");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Towing")){
                    params.put("category", "7");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Tyre")){
                    params.put("category", "6");
                }


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

    private void addServiceWithoutImage() {
        StringRequest stringRequest = new StringRequest(POST, UrlClass.add_product_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject data = new JSONObject(object.getString("data"));

                            new AlertDialog.Builder(CreateProductActivity.this)
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage(data.getString("message"))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onBackPressed();
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
                params.put("service","1");
                params.put("product_name", editText_service_name.getText().toString());
                if(checkBox_stock_available.isChecked()){
                    params.put("availability", "1");
                }else{
                    params.put("availability", "0");
                }
                params.put("pricing[rsp_price]", editText_retail_price.getText().toString());
                params.put("pricing[base_price]", editText_base_price.getText().toString());

                if(spinner_service_type.getSelectedItem().toString().equals("Accessories")){
                    params.put("category", "13");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Battery")){
                    params.put("category", "5");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Else")){
                    params.put("category", "16");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Lock Smith")){
                    params.put("category", "15");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Petrol")){
                    params.put("category", "14");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Repair")){
                    params.put("category", "12");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Services")){
                    params.put("category", "11");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Spare Part")){
                    params.put("category", "9");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Towing")){
                    params.put("category", "7");
                }
                if(spinner_service_type.getSelectedItem().toString().equals("Tyre")){
                    params.put("category", "6");
                }


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

    private void setfont() {
        TypeFaceClass.setTypeFaceTextViewBold(textView_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_brand,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_product_name,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_manufacture,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_sku,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_service_name,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_service_type,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_image_title,getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_brand,getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_product_name,getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_manufacture,getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_sku,getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_service_name,getApplicationContext());

        TypeFaceClass.setTypeFaceTextView(textView_retails,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_base,getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_retail_price,getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_base_price,getApplicationContext());

    }

    private void declare() {
        button_submit = findViewById(R.id.button_submit);
        imageView_photo= findViewById(R.id.imageView_photo);
        textView_image_click = findViewById(R.id.textView_image_click);
        checkBox = findViewById(R.id.checkBox);
        checkBox_stock_available = findViewById(R.id.checkBox_stock_available);
        linear_product = findViewById(R.id.linear_product);
        linear_services = findViewById(R.id.linear_services);
        textView_title = findViewById(R.id.textView_title);
        textView_brand = findViewById(R.id.textView_brand);
        textView_product_name = findViewById(R.id.textView_product_name);
        textView_manufacture = findViewById(R.id.textView_manufacture);
        textView_sku = findViewById(R.id.textView_sku);
        textView_service_name = findViewById(R.id.textView_service_name);
        textView_service_type = findViewById(R.id.textView_service_type);
        textView_image_title = findViewById(R.id.textView_image_title);
        editText_brand = findViewById(R.id.editText_brand);
        editText_product_name = findViewById(R.id.editText_product_name);
        editText_manufacture = findViewById(R.id.editText_manufacture);
        editText_sku = findViewById(R.id.editText_sku);
        editText_service_name = findViewById(R.id.editText_service_name);
        spinner_service_type = findViewById(R.id.spinner_service_type);

        textView_retails = findViewById(R.id.textView_retails);
        textView_base = findViewById(R.id.textView_base);
        editText_retail_price = findViewById(R.id.editText_retail_price);
        editText_base_price = findViewById(R.id.editText_base_price);

    }

    private void getSpinnerType() {
        final ArrayList<String> spinnerManuArray = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_list_product_categories_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);

                            JSONObject proOBJ = new JSONObject(object.getString("categories"));
                            spinnerManuArray.add("Please choose service type");

                            spinnerManuArray.add(proOBJ.getString("13"));
                            spinnerManuArray.add(proOBJ.getString("5"));
                            spinnerManuArray.add(proOBJ.getString("16"));
                            spinnerManuArray.add(proOBJ.getString("15"));
                            spinnerManuArray.add(proOBJ.getString("14"));
                            spinnerManuArray.add(proOBJ.getString("12"));
                            spinnerManuArray.add(proOBJ.getString("11"));
                            spinnerManuArray.add(proOBJ.getString("9"));
                            spinnerManuArray.add(proOBJ.getString("7"));
                            spinnerManuArray.add(proOBJ.getString("6"));

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateProductActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerManuArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_service_type.setAdapter(adapter);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                selected_image = Bitmap.createScaledBitmap(thumbnail, 720, 576, true);
                imageView_photo.setImageBitmap(selected_image);
            }

        }
    }
}
