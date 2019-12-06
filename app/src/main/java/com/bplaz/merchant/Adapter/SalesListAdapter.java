package com.bplaz.merchant.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bplaz.merchant.Class.SalesListClass;
import com.bplaz.merchant.Class.TypeFaceClass;
import com.bplaz.merchant.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SalesListAdapter extends RecyclerView.Adapter<SalesListAdapter.ProductViewHolder> {

    private Context mCtx;
    public static List<SalesListClass> productListClassList;
    Activity activity;

    public SalesListAdapter(Context mCtx, List<SalesListClass> jobByMonthList, Activity activity) {
        this.mCtx = mCtx;
        this.productListClassList = jobByMonthList;
        this.activity = activity;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_sales_list, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final SalesListClass productListClass = productListClassList.get(position);

        holder.textView_status.setText(productListClass.getStatus_job());
        holder.textView_date.setText(dateFormat(productListClass.getAssign_date()).toUpperCase());
        holder.textView_name.setText(productListClass.getName_customer());
        holder.textView_adress.setText(productListClass.getGeo_location_customer());
        holder.textView_product.setText(productListClass.getProduct_name());
    }

    private String dateFormat(String date_input) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String date_string = null;
        try {
            date = inputFormat.parse(date_input.replace("T"," "));
            date_string = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date_string;
    }

    @Override
    public int getItemCount() {
        return productListClassList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textView_status,textView_date,textView_name,textView_adress,textView_product;
        public ProductViewHolder(View itemView) {
            super(itemView);

            textView_status = itemView.findViewById(R.id.textView_status);
            textView_date = itemView.findViewById(R.id.textView_date);
            textView_name = itemView.findViewById(R.id.textView_name);
            textView_adress = itemView.findViewById(R.id.textView_adress);
            textView_product = itemView.findViewById(R.id.textView_product);

            TypeFaceClass.setTypeFaceTextViewBold(textView_status,mCtx);
            TypeFaceClass.setTypeFaceTextViewBold(textView_date,mCtx);
            TypeFaceClass.setTypeFaceTextViewBold(textView_name,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_adress,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_product,mCtx);

        }
    }


}