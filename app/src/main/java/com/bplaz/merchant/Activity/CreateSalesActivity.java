package com.bplaz.merchant.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bplaz.merchant.Adapter.AddProductAdapter;
import com.bplaz.merchant.Adapter.ProductListAdapter;
import com.bplaz.merchant.Adapter.SalesListAdapter;
import com.bplaz.merchant.Class.AddProductClass;
import com.bplaz.merchant.Class.ProductListClass;
import com.bplaz.merchant.Class.SalesListClass;
import com.bplaz.merchant.Class.StandardProgressDialog;
import com.bplaz.merchant.Preferance.PreferenceManagerLogin;
import com.bplaz.merchant.R;
import com.bplaz.merchant.URL.UrlClass;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class CreateSalesActivity extends AppCompatActivity {

    //SESSION
    PreferenceManagerLogin session;
    StandardProgressDialog standardProgressDialog;

    //SPINNER
    SearchableSpinner spinner_vehicle_manufacture,spinner_vehicle_model;
    String spinner_vehicle_manufacture_id_selected,spinner_vehicle_model_id_selected;
    boolean status_spinner_vehicle;

    //DIALOG ADD PRODUCT
    ViewGroup viewGroup_product;
    View view_product;
    AlertDialog.Builder builder_product;
    AlertDialog dialog_product;
    SearchableSpinner spinner_product;
    String spinner_product_id;
    EditText editText_quantity,editText_price,editText_discount,editText_total;
    Button button_add_product;
    RecyclerView recyclerView;
    List<AddProductClass> addProductClasses;
    private AddProductAdapter addProductAdapter;

    //BUTTON ADD PRODUCT SERVICES
    LinearLayout button_addProductService;

    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sales);

        //GET SESSION
        session = new PreferenceManagerLogin(getApplicationContext());
        standardProgressDialog = new StandardProgressDialog(this.getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        token = user.get(PreferenceManagerLogin.KEY_TOKEN);

        spinner_vehicle_manufacture = findViewById(R.id.spinner_vehicle_manufacture);
        spinner_vehicle_model = findViewById(R.id.spinner_vehicle_model);
        button_addProductService = findViewById(R.id.button_addProductService);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);

        getVehicleManufacture();
        declareDialogProduct();

        button_addProductService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_product.show();
            }
        });


        //SET ADAPTER
        recyclerView.setHasFixedSize(false);
        addProductClasses = new ArrayList<>();
        addProductAdapter = new AddProductAdapter(getApplicationContext(), addProductClasses,CreateSalesActivity.this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(addProductAdapter);
    }

    //GET VEHICLE MANUFACTURE
    private void getVehicleManufacture(){
        final ArrayList<String> spinnerManuArray = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_list_vehicle_manufacture,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray manuFactureARR = new JSONArray(object.getString("manufacturers"));
                            spinnerManuArray.add("Please choose Vehicle Manufacture");
                            for (int i =0; i < manuFactureARR.length(); i++){
                                JSONObject manuOBJ = manuFactureARR.getJSONObject(i);
                                spinnerManuArray.add(manuOBJ.getString("name"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateSalesActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerManuArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_vehicle_manufacture.setAdapter(adapter);

                            //SPINNER VEHICLE MANUFCATURE ON SELECT
                            spinner_vehicle_manufacture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position == 0){

                                    }else{
                                        getVehicleManufactureCheckId(spinner_vehicle_manufacture.getSelectedItem().toString());
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


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

    private void getVehicleManufactureCheckId(final String spinner_manufacture_value){
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_list_vehicle_manufacture,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray manuFactureARR = new JSONArray(object.getString("manufacturers"));
                            for (int i =0; i < manuFactureARR.length(); i++){
                                JSONObject manuOBJ = manuFactureARR.getJSONObject(i);

                                if(spinner_manufacture_value.equals(manuOBJ.getString("name"))){
                                    spinner_vehicle_manufacture_id_selected = manuOBJ.getString("id");
                                    getVehicleModel(spinner_vehicle_manufacture_id_selected);
                                }

                            }


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

    //GET VEHICLE MODEL
    private void getVehicleModel(final String spinner_select){
        final ArrayList<String> spinnerManuArray = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_list_vehicle_model+"/"+spinner_select+".json",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray manuFactureARR = new JSONArray(object.getString("vehicle"));
                            spinnerManuArray.add("Please choose Vehicle Model");
                            for (int i =0; i < manuFactureARR.length(); i++){
                                JSONObject manuOBJ = manuFactureARR.getJSONObject(i);
                                spinnerManuArray.add(manuOBJ.getString("name"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateSalesActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerManuArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_vehicle_model.setAdapter(adapter);

                            //SPINNER VEHICLE MODEL ON SELECT
                            spinner_vehicle_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position == 0){

                                    }else{
                                        getVehicleModelCheckId(spinner_select,spinner_vehicle_model.getSelectedItem().toString());
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
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

    private void getVehicleModelCheckId(String spinner_select, final String spiner_name){
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_list_vehicle_model+"/"+spinner_select+".json",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray manuFactureARR = new JSONArray(object.getString("vehicle"));
                            for (int i =0; i < manuFactureARR.length(); i++){
                                JSONObject manuOBJ = manuFactureARR.getJSONObject(i);

                                if(spiner_name.equals(manuOBJ.getString("name"))){
                                    spinner_vehicle_model_id_selected = manuOBJ.getString("id");
                                }
                            }
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

    //DECLARE DIALOG PRODUCT
    private void declareDialogProduct(){
        viewGroup_product = findViewById(android.R.id.content);
        view_product = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, viewGroup_product, false);
        builder_product = new AlertDialog.Builder(this);
        builder_product.setView(view_product);
        dialog_product = builder_product.create();

        spinner_product = view_product.findViewById(R.id.spinner_product);
        editText_quantity = view_product.findViewById(R.id.editText_quantity);
        editText_price = view_product.findViewById(R.id.editText_price);
        editText_discount = view_product.findViewById(R.id.editText_discount);
        editText_total = view_product.findViewById(R.id.editText_total);
        button_add_product = view_product.findViewById(R.id.button_add_product);

        //GET PRODUCT
        getProduct();

        //BUTTON ADD PRODUCT
        button_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                addProductClasses.add(new AddProductClass(
                        spinner_product_id,
                        spinner_product.getSelectedItem().toString(),
                        editText_quantity.getText().toString(),
                        editText_price.getText().toString(),
                        editText_discount.getText().toString(),
                        editText_total.getText().toString()
                ));

                addProductAdapter = new AddProductAdapter(getApplicationContext(), addProductClasses,CreateSalesActivity.this);
                recyclerView.setAdapter(addProductAdapter);
                dialog_product.dismiss();
            }
        });
    }

    //GET PRODUCT
    private void getProduct(){
        final ArrayList<String> spinnerManuArray = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_list_product_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray arr = new JSONArray(object.getString("products"));

                            spinnerManuArray.add("Please choose Product / Service");

                            for (int i =0; i < arr.length(); i++){
                                JSONObject proOBJ = arr.getJSONObject(i);
                                if (proOBJ.getString("brand").equals("null") || proOBJ.getString("brand").equals(null)) {
                                    spinnerManuArray.add(proOBJ.getString("product_name"));
                                }else{
                                    spinnerManuArray.add(proOBJ.getString("brand")+" - "+proOBJ.getString("product_name"));
                                }


                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateSalesActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerManuArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_product.setAdapter(adapter);


                            //SPINNER MODEL
                            spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(position == 0){

                                    }else{
                                        getProductCheckId(spinner_product.getSelectedItem().toString());
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

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

    private void getProductCheckId(final String ss){
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_list_product_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray arr = new JSONArray(object.getString("products"));

                            for (int i =0; i < arr.length(); i++){
                                JSONObject proOBJ = arr.getJSONObject(i);
                                if (proOBJ.getString("brand").equals("null") || proOBJ.getString("brand").equals(null)) {
                                    if(ss.equals(proOBJ.getString("product_name"))){
                                        spinner_product_id = proOBJ.getString("id");
                                        editText_price.setText(proOBJ.getString("price"));
                                    }
                                }else{
                                    if(ss.equals(proOBJ.getString("brand")+" - "+proOBJ.getString("product_name"))){
                                        spinner_product_id = proOBJ.getString("id");
                                        editText_price.setText(proOBJ.getString("price"));
                                    }
                                }
                            }


                            //INSERT QUANTITY
                            editText_quantity.addTextChangedListener(new TextWatcher() {
                                public void afterTextChanged(Editable s) {
                                    if(s.length() == 0){
                                        editText_total.setText("0");
                                    }else{
                                        double total = (Double.parseDouble(editText_price.getText().toString()) * Double.parseDouble(s.toString()))
                                                - ((Double.parseDouble(editText_price.getText().toString()) * Double.parseDouble(s.toString()))
                                                * Double.parseDouble(editText_discount.getText().toString()));
                                        editText_total.setText(String.valueOf(total));
                                    }
                                }
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                            });

                            editText_discount.addTextChangedListener(new TextWatcher() {
                                public void afterTextChanged(Editable s) {
                                    if(s.length() == 0){
                                        double total = (Double.parseDouble(editText_price.getText().toString()) * Double.parseDouble(editText_quantity.getText().toString()));
                                        editText_total.setText(String.valueOf(total));
                                    }else{
                                        double total = (Double.parseDouble(editText_price.getText().toString()) * Double.parseDouble(editText_quantity.getText().toString()))
                                                - ((Double.parseDouble(editText_price.getText().toString()) * Double.parseDouble(editText_quantity.getText().toString()))
                                                * Double.parseDouble(editText_discount.getText().toString()));
                                        editText_total.setText(String.valueOf(total));
                                    }
                                }
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                            });



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


}
