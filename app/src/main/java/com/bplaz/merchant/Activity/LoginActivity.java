package com.bplaz.merchant.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import static com.android.volley.Request.Method.POST;

public class LoginActivity extends AppCompatActivity {

    StandardProgressDialog standardProgressDialog;

    TextView textView_welcome;

    EditText editText_email, editText_password;

    Button button_login;

    private static long back_pressed;

    PreferenceManagerLogin session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        //FOR RESTRICT HTTPS
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        standardProgressDialog = new StandardProgressDialog(this.getWindow().getContext());
        session = new PreferenceManagerLogin(getApplicationContext());

        declareAndFont();

        //HARCODE EMAIL AND PASSWORD

        editText_password.setText("12345");
        editText_email.setText("amirfirdaussamsudin93@gmail.com");

        //BUTTON LOGIN CLICK FUNCTION
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_email.getText().toString().equals("")) {
                    editText_email.setError("Empty");
                } else if (editText_password.getText().toString().equals("")) {
                    editText_password.setError("Empty");
                } else {
                    standardProgressDialog.show();
                    login();
                }
            }
        });

    }

    //ON BACK PRESS FUNCTION
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) moveTaskToBack(true);
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    private void declareAndFont() {
        textView_welcome = findViewById(R.id.textView_welcome);
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        button_login = findViewById(R.id.button_login);

        TypeFaceClass.setTypeFaceTextViewBold(textView_welcome, getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_email, getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_password, getApplicationContext());
        TypeFaceClass.setTypeFaceButtonBold(button_login, getApplicationContext());
    }

    //FUNCTION CONNECT WITH LOGIN API
    private void login() {
        StringRequest stringRequest = new StringRequest(POST, UrlClass.login_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        standardProgressDialog.dismiss();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            if (responseObj.getString("success").equals("true")) {
                                JSONObject data = new JSONObject(responseObj.getString("data"));
                                session.createLoginSession(
                                        data.getString("token"),
                                        editText_email.getText().toString()
                                );

                                Intent next = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(next);
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
                        parseVolleyError(error);
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", editText_email.getText().toString());
                params.put("password", editText_password.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            dialog(data.getString("message"));

        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }

    private void dialog(String Message) {
        new AlertDialog.Builder(LoginActivity.this)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(Message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}