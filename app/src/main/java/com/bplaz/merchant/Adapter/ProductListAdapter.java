package com.bplaz.merchant.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bplaz.merchant.Class.ProductListClass;
import com.bplaz.merchant.Class.TypeFaceClass;
import com.bplaz.merchant.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private Context mCtx;
    public static List<ProductListClass> productListClassList;
    Activity activity;

    public ProductListAdapter(Context mCtx, List<ProductListClass> jobByMonthList, Activity activity) {
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
            Picasso.get().load(productListClass.getImageURL()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imageView_product);
        }
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


}