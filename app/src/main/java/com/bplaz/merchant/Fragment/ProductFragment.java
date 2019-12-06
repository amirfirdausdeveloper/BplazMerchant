package com.bplaz.merchant.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bplaz.merchant.Adapter.ProductListAdapter;
import com.bplaz.merchant.Class.ProductListClass;
import com.bplaz.merchant.Class.StandardProgressDialog;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    PreferenceManagerLogin session;
    StandardProgressDialog standardProgressDialog;
    String token;
    List<ProductListClass> productListClasses;
    private ProductListAdapter productListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        //GET SESSION
        session = new PreferenceManagerLogin(getActivity());
        standardProgressDialog = new StandardProgressDialog(getActivity().getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        token = user.get(PreferenceManagerLogin.KEY_TOKEN);

        mSwipeRefreshLayout = v.findViewById(R.id.swipeToRefresh);
        recyclerView = v.findViewById(R.id.recyclerView);

        //SWIPE REFRESH
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProduct();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        standardProgressDialog.show();
        getProduct();
    }

    private void getProduct(){
        recyclerView.setHasFixedSize(false);
        productListClasses = new ArrayList<>();
        productListAdapter = new ProductListAdapter(getContext(), productListClasses,getActivity());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(productListAdapter);

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

                                productListClasses.add(new ProductListClass(
                                        proOBJ.getString("id"),
                                        proOBJ.getString("product_name"),
                                        proOBJ.getString("brand"),
                                        proOBJ.getString("manufacturer"),
                                        proOBJ.getString("category"),
                                        proOBJ.getString("service"),
                                        proOBJ.getString("availability"),
                                        proOBJ.getString("image_product")
                                ));

                                productListAdapter = new ProductListAdapter(getContext(), productListClasses,getActivity());
                                recyclerView.setAdapter(productListAdapter);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
