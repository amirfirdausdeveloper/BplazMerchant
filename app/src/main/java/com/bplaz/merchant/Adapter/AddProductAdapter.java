package com.bplaz.merchant.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bplaz.merchant.Activity.CreateSalesActivity;
import com.bplaz.merchant.Class.AddProductClass;
import com.bplaz.merchant.Class.SalesListClass;
import com.bplaz.merchant.Class.TypeFaceClass;
import com.bplaz.merchant.R;
import com.bplaz.merchant.URL.UrlClass;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class AddProductAdapter extends RecyclerView.Adapter<AddProductAdapter.ProductViewHolder> {

    ViewGroup viewGroup_product;
    View view_product;
    AlertDialog.Builder builder_product;
    AlertDialog dialog_product;
    SearchableSpinner spinner_product;
    String spinner_product_id;
    EditText editText_quantity,editText_price,editText_discount,editText_total;
    Button button_add_product;
    private Context mCtx;
    public static List<AddProductClass> productListClassList;
    Activity activity;

    public AddProductAdapter(Context mCtx, List<AddProductClass> jobByMonthList, Activity activity) {
        this.mCtx = mCtx;
        this.productListClassList = jobByMonthList;
        this.activity = activity;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_add_product_list, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final AddProductClass productListClass = productListClassList.get(position);


        holder.textView_product_name_v.setText(productListClass.getProductNAME());
        holder.textView_product_quantity_v.setText(productListClass.getQuantity());
        holder.textView_product_price_v.setText(productListClass.getPrice());
        holder.textView_product_discount_v.setText(productListClass.getDiscount());
        holder.textView_product_total_v.setText(productListClass.getTotal());

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productListClassList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,productListClassList.size());
            }
        });

        holder.button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declareDialogProduct(
                        position,
                        productListClass.getProductID(),
                        productListClass.getProductNAME(),
                        productListClass.getQuantity(),
                        productListClass.getPrice(),
                        productListClass.getDiscount(),
                        productListClass.getTotal()
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return productListClassList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textView_product_name_v,textView_product_quantity_v,textView_product_price_v,textView_product_discount_v,textView_product_total_v;
        Button button_delete,button_add;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textView_product_name_v = itemView.findViewById(R.id.textView_product_name_v);
            textView_product_quantity_v = itemView.findViewById(R.id.textView_product_quantity_v);
            textView_product_price_v = itemView.findViewById(R.id.textView_product_price_v);
            textView_product_discount_v = itemView.findViewById(R.id.textView_product_discount_v);
            textView_product_total_v = itemView.findViewById(R.id.textView_product_total_v);

            button_delete = itemView.findViewById(R.id.button_delete);
            button_add = itemView.findViewById(R.id.button_add);

        }
    }

    //DECLARE DIALOG PRODUCT
    private void declareDialogProduct(final int position, String productID, String productNAME, String quantity, String price, String discount, final String total){
        viewGroup_product = activity.findViewById(android.R.id.content);
        view_product = LayoutInflater.from(activity).inflate(R.layout.dialog_add_product, viewGroup_product, false);
        builder_product = new AlertDialog.Builder(activity);
        builder_product.setView(view_product);
        dialog_product = builder_product.create();
        dialog_product.show();

        spinner_product = view_product.findViewById(R.id.spinner_product);
        editText_quantity = view_product.findViewById(R.id.editText_quantity);
        editText_price = view_product.findViewById(R.id.editText_price);
        editText_discount = view_product.findViewById(R.id.editText_discount);
        editText_total = view_product.findViewById(R.id.editText_total);
        button_add_product = view_product.findViewById(R.id.button_add_product);
        button_add_product.setText("EDIT");
        //GET PRODUCT
        getProduct(productNAME);

        editText_quantity.setText(quantity);
        editText_price.setText(price);
        editText_discount.setText(discount);
        editText_total.setText(total);


        //BUTTON ADD PRODUCT
        button_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddProductClass productListClass = productListClassList.get(position);
                productListClass.setProductID(spinner_product_id);
                productListClass.setProductNAME(spinner_product.getSelectedItem().toString());
                productListClass.setDiscount(editText_discount.getText().toString());
                productListClass.setQuantity(editText_quantity.getText().toString());
                productListClass.setPrice(editText_price.getText().toString());
                productListClass.setTotal(editText_total.getText().toString());

                productListClassList.set(position, productListClass);
                notifyItemChanged(position);
                dialog_product.dismiss();

            }
        });
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }


    //GET PRODUCT
    private void getProduct(final String productNAME){
        final ArrayList<String> spinnerManuArray = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_list_product_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, spinnerManuArray);
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

                            spinner_product.setSelection(getIndex(spinner_product, productNAME));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer "+CreateSalesActivity.token);
                return headers;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getProductCheckId(final String ss){
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_list_product_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer "+CreateSalesActivity.token);
                return headers;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        requestQueue.add(stringRequest);
    }


}