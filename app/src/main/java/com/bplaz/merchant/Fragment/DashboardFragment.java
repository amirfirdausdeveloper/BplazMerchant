package com.bplaz.merchant.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bplaz.merchant.Activity.CreateSalesActivity;
import com.bplaz.merchant.Activity.ToAcceptActivity;
import com.bplaz.merchant.Class.StandardProgressDialog;
import com.bplaz.merchant.Class.TypeFaceClass;
import com.bplaz.merchant.Preferance.PreferenceManagerLogin;
import com.bplaz.merchant.R;
import com.bplaz.merchant.URL.UrlClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    //SESSION
    PreferenceManagerLogin session;

    //DIALOG
    StandardProgressDialog standardProgressDialog;

    //DASHBOARD
    TextView textView_header,textView_rating_value,textView_rating;

    //linear_accept_cancel
    TextView textView_accept_percentage,textView_accept_percentage_words,textView_cancel_percentage,textView_cancel_percentage_words;

    //linear_to_accept_to_completed
    TextView textView_to_accept,textView_to_accept_words,textView_to_completed,textView_to_completed_words;
    LinearLayout linear_to_accept,linear_to_completed;

    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //FOR RESTRICT HTTPS
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //GET SESSION
        session = new PreferenceManagerLogin(getActivity());
        standardProgressDialog = new StandardProgressDialog(getActivity().getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        token = user.get(PreferenceManagerLogin.KEY_TOKEN);

        //DECLARE
        declare(v);

        //SET FONT TYPE
        setFont();


        linear_to_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getActivity(), ToAcceptActivity.class);
                startActivity(next);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        standardProgressDialog.show();
        getDetailsDashboard();
    }

    private void declare(View v){
        textView_header = v.findViewById(R.id.textView_header);
        textView_rating_value = v.findViewById(R.id.textView_rating_value);
        textView_rating = v.findViewById(R.id.textView_rating);
        textView_accept_percentage = v.findViewById(R.id.textView_accept_percentage);
        textView_accept_percentage_words = v.findViewById(R.id.textView_accept_percentage_words);
        textView_cancel_percentage = v.findViewById(R.id.textView_cancel_percentage);
        textView_cancel_percentage_words = v.findViewById(R.id.textView_cancel_percentage_words);
        textView_to_accept = v.findViewById(R.id.textView_to_accept);
        textView_to_accept_words = v.findViewById(R.id.textView_to_accept_words);
        textView_to_completed = v.findViewById(R.id.textView_to_completed);
        textView_to_completed_words = v.findViewById(R.id.textView_to_completed_words);

        linear_to_accept = v.findViewById(R.id.linear_to_accept);
        linear_to_accept = v.findViewById(R.id.linear_to_accept);
    }

    private void setFont(){
        TypeFaceClass.setTypeFaceTextViewBold(textView_header,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_accept_percentage,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_rating,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_rating_value,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_accept_percentage_words,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_cancel_percentage,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_cancel_percentage_words,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_to_accept,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_to_accept_words,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_to_completed,getActivity());
        TypeFaceClass.setTypeFaceTextViewBold(textView_to_completed_words,getActivity());
    }

    private void getDetailsDashboard(){
        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_dashboard_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject salesOBJ = new JSONObject(object.getString("sales"));

                            textView_rating_value.setText(salesOBJ.getString("rating"));
                            if(salesOBJ.getString("acceptance").equals("0%")){
                                textView_accept_percentage.setText(salesOBJ.getString("acceptance"));
                            }else{
                                textView_accept_percentage.setText(salesOBJ.getString("acceptance")+"%");
                            }

                            if(salesOBJ.getString("cancel").equals("0%")){
                                textView_cancel_percentage.setText(salesOBJ.getString("cancel"));
                            }else{
                                textView_cancel_percentage.setText(salesOBJ.getString("cancel")+"%");
                            }


                            textView_to_accept.setText(salesOBJ.getString("to_accept"));
                            textView_to_completed.setText(salesOBJ.getString("complete"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        standardProgressDialog.dismiss();
                        parseVolleyError(error);
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

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            dialogError(data.getString("message"));

        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }

    private void dialogError(String Message){
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(Message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session.logoutUser();
                    }
                })
                .show();
    }

}
