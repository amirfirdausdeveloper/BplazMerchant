package com.bplaz.merchant.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bplaz.merchant.Activity.CreateSalesActivity;
import com.bplaz.merchant.Adapter.SalesListAdapter;
import com.bplaz.merchant.Class.SalesListClass;
import com.bplaz.merchant.Class.StandardProgressDialog;
import com.bplaz.merchant.Preferance.PreferenceManagerLogin;
import com.bplaz.merchant.R;
import com.bplaz.merchant.URL.UrlClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
public class SalesFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    PreferenceManagerLogin session;
    StandardProgressDialog standardProgressDialog;
    String token;
    List<SalesListClass> salesListClasses;
    private SalesListAdapter salesListAdapter;
    FloatingActionButton floatingActionButton;
    String totalCOunt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sales, container, false);

        //GET SESSION
        session = new PreferenceManagerLogin(getActivity());
        standardProgressDialog = new StandardProgressDialog(getActivity().getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        token = user.get(PreferenceManagerLogin.KEY_TOKEN);

        mSwipeRefreshLayout = v.findViewById(R.id.swipeToRefresh);
        floatingActionButton = v.findViewById(R.id.floatingActionButton);
        recyclerView = v.findViewById(R.id.recyclerView);

        //SWIPE REFRESH
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTotal();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //ADD SALES
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getActivity(), CreateSalesActivity.class);
                startActivity(next);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        getTotal();

        //SEARCHING
        androidx.appcompat.widget.SearchView simpleSearchView = v.findViewById(R.id.searchView);
        simpleSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SUBMIT",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                salesListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return v;
    }

    private void getList(String totalCOunt){
        recyclerView.setHasFixedSize(false);
        salesListClasses = new ArrayList<>();
        salesListAdapter = new SalesListAdapter(getContext(), salesListClasses,getActivity());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(salesListAdapter);

        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_sales_URL+"?limit="+totalCOunt,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        String customer_name = "",product_name = "",status_words ="";
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray sales = new JSONArray(object.getString("sales"));



                            JSONObject sale_status = new JSONObject(object.getString("sale_status"));



                            for (int i = 0; i < sales.length(); i++){
                                JSONObject salesOBJ = sales.getJSONObject(i);

                                //CUSTOMER INFO
                                JSONObject customerOBJ = new JSONObject(salesOBJ.getString("customer"));
                                customer_name = customerOBJ.getString("name");

                                //GET PRODUCT NAME
                                JSONArray sale_partner_items = new JSONArray(salesOBJ.getString("sale_partner_items"));
                                for (int ii = 0; ii < sale_partner_items.length(); ii++){
                                    JSONObject sale_partner_item_obj = sale_partner_items.getJSONObject(ii);
                                    JSONObject product_partnerOBJ = new JSONObject(sale_partner_item_obj.getString("product_partner"));
                                    //PRODUCT NAME
                                    product_name = product_partnerOBJ.getString("product_name");
                                }

                                if(salesOBJ.getString("status").equals("0")){
                                    status_words = sale_status.getString("0");
                                }
                                if(salesOBJ.getString("status").equals("1")){
                                    status_words = sale_status.getString("1");
                                }
                                if(salesOBJ.getString("status").equals("2")){
                                    status_words = sale_status.getString("2");
                                }
                                if(salesOBJ.getString("status").equals("3")){
                                    status_words = sale_status.getString("3");
                                }
                                if(salesOBJ.getString("status").equals("4")){
                                    status_words = sale_status.getString("4");
                                }
                                if(salesOBJ.getString("status").equals("5")){
                                    status_words = sale_status.getString("5");
                                }
                                if(salesOBJ.getString("status").equals("6")){
                                    status_words = sale_status.getString("6");
                                }
                                if(salesOBJ.getString("status").equals("7")){
                                    status_words = sale_status.getString("7");
                                }
                                if(salesOBJ.getString("status").equals("8")){
                                    status_words = sale_status.getString("8");
                                }
                                if(salesOBJ.getString("status").equals("9")){
                                    status_words = sale_status.getString("9");
                                }
                                if(salesOBJ.getString("status").equals("10")){
                                    status_words = sale_status.getString("10");
                                }
                                if(salesOBJ.getString("status").equals("11")){
                                    status_words = sale_status.getString("11");
                                }
                                if(salesOBJ.getString("status").equals("12")){
                                    status_words = sale_status.getString("12");
                                }
                                if(salesOBJ.getString("status").equals("13")){
                                    status_words = sale_status.getString("13");
                                }
                                if(salesOBJ.getString("status").equals("14")){
                                    status_words = sale_status.getString("14");
                                }
                                if(salesOBJ.getString("status").equals("15")){
                                    status_words = "SALES CREATED BY PARTNER";
                                }



                                salesListClasses.add(new SalesListClass(
                                        salesOBJ.getString("id"),
                                        status_words.toUpperCase(),
                                        salesOBJ.getString("assign_date"),
                                        salesOBJ.getString("geo_location"),
                                        customer_name,
                                        product_name
                                ));

                                salesListAdapter = new SalesListAdapter(getContext(), salesListClasses,getActivity());
                                recyclerView.setAdapter(salesListAdapter);
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

    private void getTotal(){
        recyclerView.setHasFixedSize(false);
        salesListClasses = new ArrayList<>();
        salesListAdapter = new SalesListAdapter(getContext(), salesListClasses,getActivity());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(salesListAdapter);

        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_sales_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject sales = new JSONObject(object.getString("paging"));
                            totalCOunt = sales.getString("count");
                            getList(totalCOunt);
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
