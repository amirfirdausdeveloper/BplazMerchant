package com.bplaz.merchant.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bplaz.merchant.Activity.CreateProductActivity;
import com.bplaz.merchant.Class.ProductListClass;
import com.bplaz.merchant.Class.StandardProgressDialog;
import com.bplaz.merchant.Class.TypeFaceClass;
import com.bplaz.merchant.Fragment.ProductFragment;
import com.bplaz.merchant.Preferance.PreferenceManagerLogin;
import com.bplaz.merchant.R;
import com.bplaz.merchant.URL.UrlClass;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;
import static com.bplaz.merchant.Fragment.ProductFragment.getProduct;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private Context mCtx;
    public static List<ProductListClass> productListClassList;
    Activity activity;
    PreferenceManagerLogin session;
    StandardProgressDialog standardProgressDialog;
    String token;

    public ProductListAdapter(Context mCtx, List<ProductListClass> jobByMonthList, Activity activity) {
        //GET SESSION
        session = new PreferenceManagerLogin(mCtx.getApplicationContext());
        standardProgressDialog = new StandardProgressDialog(activity.getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        token = user.get(PreferenceManagerLogin.KEY_TOKEN);

        this.mCtx = mCtx;
        this.productListClassList = jobByMonthList;
        this.activity = activity;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_product_list, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final ProductListClass productListClass = productListClassList.get(position);

        holder.textView_product_name.setText(productListClass.getProductNAME()); //PRODUCT NAME

        if(productListClass.getServiceTYPE().equals("13")){
            holder.textView_product_service_type.setText("Accessories");
            holder.linear_manu.setVisibility(View.GONE);
        }else if(productListClass.getServiceTYPE().equals("16")){
            holder.textView_product_service_type.setText("Else");
            holder.linear_manu.setVisibility(View.GONE);
        }else if(productListClass.getServiceTYPE().equals("15")){
            holder.textView_product_service_type.setText("Lock Smith");
            holder.linear_manu.setVisibility(View.GONE);
        }else if(productListClass.getServiceTYPE().equals("5")){
            holder.textView_product_service_type.setText("Battery");
            holder.textView_product_brand.setText(productListClass.getProductBRAND()); //PRODUCT BRAND
            holder.textView_product_manu.setText(productListClass.getProductMANUFACTURE()); //PRODUCT MANU
            holder.linear_manu.setVisibility(View.VISIBLE);
        }else if(productListClass.getServiceTYPE().equals("14")){
            holder.textView_product_service_type.setText("Petrol");
            holder.linear_manu.setVisibility(View.GONE);
        }else if(productListClass.getServiceTYPE().equals("12")){
            holder.textView_product_service_type.setText("Repair");
            holder.linear_manu.setVisibility(View.GONE);
        }else if(productListClass.getServiceTYPE().equals("11")){
            holder.textView_product_service_type.setText("Services");
            holder.linear_manu.setVisibility(View.GONE);
        }else if(productListClass.getServiceTYPE().equals("9")){
            holder.textView_product_service_type.setText("Spare Part");
            holder.linear_manu.setVisibility(View.GONE);
        }else if(productListClass.getServiceTYPE().equals("7")){
            holder.textView_product_service_type.setText("Towing");
            holder.linear_manu.setVisibility(View.GONE);
        }else if(productListClass.getServiceTYPE().equals("6")){
            holder.textView_product_service_type.setText("Tyre");
            holder.linear_manu.setVisibility(View.GONE);
        }

        if(productListClass.getSevice().equals("0")){
            holder.textView_product_service.setText("-");
        }else if(productListClass.getSevice().equals("1")){
            holder.textView_product_service.setText("Available");
        }

        if(productListClass.getAvailable().equals("0")){
            holder.textView_product_available.setText("-");
        }else if(productListClass.getAvailable().equals("1")){
            holder.textView_product_available.setText("Available");
        }else if(productListClass.getAvailable().equals("2")){
            holder.textView_product_available.setText("-");
        }

        if(productListClass.getImageURL().equals(null) || productListClass.getImageURL().equals("null") || productListClass.getImageURL().equals("")){

        }else {
            Picasso.get().load("https://dev.merchant.bplaz.com/"+productListClass.getImageURL()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imageView_product);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Choose edit or delete product?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                new AlertDialog.Builder(activity)
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setMessage("Are you sure want to delete "+productListClass.getProductNAME()+" ?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                delete(productListClass.getProductID());


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
                        })
                        .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent next = new Intent(activity,CreateProductActivity.class);
                                next.putExtra("id",productListClass.getProductID());
                                next.putExtra("product_name",productListClass.getProductNAME());
                                next.putExtra("product_brand",productListClass.getProductBRAND());
                                next.putExtra("product_manu",productListClass.getProductMANUFACTURE());
                                next.putExtra("image",productListClass.getImageURL());
                                next.putExtra("service",productListClass.getSevice());
                                next.putExtra("service_type",productListClass.getServiceTYPE());
                                next.putExtra("availability",productListClass.getAvailable());
                                next.putExtra("base_price",productListClass.getBase_price());
                                next.putExtra("retail_price",productListClass.getRetail_price());
                                mCtx.startActivity(next);
                                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        })
                        .show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return productListClassList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textView_product_name,textView_product_brand,textView_product_manu,textView_product_service_type,textView_product_service,textView_product_available;
        TextView textView_product_name_v,textView_product_brand_v,textView_product_manu_v,textView_product_service_type_v,textView_product_service_v,textView_product_available_v;
        ImageView imageView_product;
        LinearLayout linear_manu;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textView_product_name = itemView.findViewById(R.id.textView_product_name);
            textView_product_brand = itemView.findViewById(R.id.textView_product_brand);
            textView_product_manu = itemView.findViewById(R.id.textView_product_manu);
            textView_product_service_type = itemView.findViewById(R.id.textView_product_service_type);
            textView_product_service = itemView.findViewById(R.id.textView_product_service);
            textView_product_available = itemView.findViewById(R.id.textView_product_available);

            textView_product_name_v = itemView.findViewById(R.id.textView_product_name_v);
            textView_product_brand_v = itemView.findViewById(R.id.textView_product_brand_v);
            textView_product_manu_v = itemView.findViewById(R.id.textView_product_manu_v);
            textView_product_service_type_v = itemView.findViewById(R.id.textView_product_service_type_v);
            textView_product_service_v = itemView.findViewById(R.id.textView_product_service_v);
            textView_product_available_v = itemView.findViewById(R.id.textView_product_available_v);

            imageView_product = itemView.findViewById(R.id.imageView_product);
            linear_manu = itemView.findViewById(R.id.linear_manu);

            TypeFaceClass.setTypeFaceTextView(textView_product_name,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_brand,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_manu,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_service_type,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_service,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_available,itemView.getContext());

            TypeFaceClass.setTypeFaceTextView(textView_product_name_v,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_brand_v,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_manu_v,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_service_type_v,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_service_v,itemView.getContext());
            TypeFaceClass.setTypeFaceTextView(textView_product_available_v,itemView.getContext());

        }
    }

    public void delete(String delete_id){
        StringRequest stringRequest = new StringRequest(DELETE, UrlClass.delete_product_URL+delete_id+".json",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject data = new JSONObject(object.getString("data"));

                            new AlertDialog.Builder(activity)
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage(data.getString("message"))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getProduct();
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

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        requestQueue.add(stringRequest);
    }

}