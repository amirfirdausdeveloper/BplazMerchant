package com.bplaz.merchant.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class ProfileActivity extends AppCompatActivity {

    //SESSION
    PreferenceManagerLogin session;
    StandardProgressDialog standardProgressDialog;
    String token,partner_id;

    //HEADER
    ImageView imageView_back;
    TextView textView_title;

    //USER INFO
    TextView textView_title_user_info;
    TextView textView_user_info_name,textView_user_info_email,textView_user_info_phone,textView_user_info_status;
    TextView textView_user_info_name_v,textView_user_info_email_v,textView_user_info_phone_v,textView_user_info_status_v;

    //COMPANY INFO
    TextView textView_title_company_info,textView_company_info_name_company_officer,textView_company_info_name_company_officer_v;
    TextView textView_company_info_name_company,textView_company_info_address_company,textView_company_info_state_company,textView_company_info_invoice_company;
    TextView textView_company_info_name_company_v,textView_company_info_address_company_v,textView_company_info_state_company_v,textView_company_info_invoice_company_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new PreferenceManagerLogin(getApplicationContext());
        standardProgressDialog = new StandardProgressDialog(this.getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        token = user.get(PreferenceManagerLogin.KEY_TOKEN);

        //DECLARE
        declare();

        //SET FONT
        setFont();

        standardProgressDialog.show();
        getDetails();

        //BACK
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                ProfileActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void declare(){
        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);

        textView_title_user_info = findViewById(R.id.textView_title_user_info);
        textView_user_info_name = findViewById(R.id.textView_user_info_name);
        textView_user_info_email = findViewById(R.id.textView_user_info_email);
        textView_user_info_phone = findViewById(R.id.textView_user_info_phone);
        textView_user_info_status = findViewById(R.id.textView_user_info_status);

        textView_user_info_name_v = findViewById(R.id.textView_user_info_name_v);
        textView_user_info_email_v = findViewById(R.id.textView_user_info_email_v);
        textView_user_info_phone_v = findViewById(R.id.textView_user_info_phone_v);
        textView_user_info_status_v = findViewById(R.id.textView_user_info_status_v);

        textView_title_company_info = findViewById(R.id.textView_title_company_info);
        textView_company_info_name_company = findViewById(R.id.textView_company_info_name_company);
        textView_company_info_address_company = findViewById(R.id.textView_company_info_address_company);
        textView_company_info_state_company = findViewById(R.id.textView_company_info_state_company);
        textView_company_info_invoice_company = findViewById(R.id.textView_company_info_invoice_company);

        textView_company_info_name_company_v = findViewById(R.id.textView_company_info_name_company_v);
        textView_company_info_address_company_v = findViewById(R.id.textView_company_info_address_company_v);
        textView_company_info_state_company_v = findViewById(R.id.textView_company_info_state_company_v);
        textView_company_info_invoice_company_v = findViewById(R.id.textView_company_info_invoice_company_v);
        textView_company_info_name_company_officer = findViewById(R.id.textView_company_info_name_company_officer);
        textView_company_info_name_company_officer_v = findViewById(R.id.textView_company_info_name_company_officer_v);
    }

    private void setFont(){
        TypeFaceClass.setTypeFaceTextViewBold(textView_title,getApplicationContext());
        TypeFaceClass.setTypeFaceTextViewBold(textView_title_user_info,getApplicationContext());
        TypeFaceClass.setTypeFaceTextViewBold(textView_title_company_info,getApplicationContext());

        TypeFaceClass.setTypeFaceTextView(textView_company_info_name_company_officer,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_company_info_name_company_officer_v,getApplicationContext());

        TypeFaceClass.setTypeFaceTextView(textView_user_info_name,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_user_info_email,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_user_info_phone,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_user_info_status,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_company_info_name_company,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_company_info_address_company,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_company_info_state_company,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_company_info_invoice_company,getApplicationContext());

        TypeFaceClass.setTypeFaceTextView(textView_user_info_name_v,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_user_info_email_v,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_user_info_phone_v,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_user_info_status_v,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_company_info_name_company_v,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_company_info_address_company_v,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_company_info_state_company_v,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_company_info_invoice_company_v,getApplicationContext());
    }

    private void getDetails(){

        StringRequest stringRequest = new StringRequest(GET, UrlClass.get_profile_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject partnerOBJ = new JSONObject(object.getString("partner"));

                            partner_id = partnerOBJ.getString("id");

                            //USER INFO
                            textView_company_info_name_company_officer_v.setText(partnerOBJ.getString("officer_name"));

                            //COMPANY INFO
                            textView_company_info_name_company_v.setText(partnerOBJ.getString("company_name"));
                            textView_company_info_address_company_v.setText(partnerOBJ.getString("address"));
                            textView_company_info_state_company_v.setText(partnerOBJ.getString("state_partner"));
                            textView_company_info_invoice_company_v.setText(partnerOBJ.getString("invoice_formula"));

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
