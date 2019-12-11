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
import com.bplaz.merchant.Class.ServicesClass;
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

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ProductViewHolder> {

    private Context mCtx;
    public static List<ServicesClass> productListClassList;
    Activity activity;

    public ServicesAdapter(Context mCtx, List<ServicesClass> jobByMonthList, Activity activity) {
        this.mCtx = mCtx;
        this.productListClassList = jobByMonthList;
        this.activity = activity;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_services_adapter_view, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final ServicesClass productListClass = productListClassList.get(position);


        holder.textView_name.setText(productListClass.getName());
        holder.textView_price.setText(productListClass.getPrice());


    }

    @Override
    public int getItemCount() {
        return productListClassList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textView_name,textView_price;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.textView_name);
            textView_price = itemView.findViewById(R.id.textView_price);

        }
    }






}