package com.bplaz.merchant.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bplaz.merchant.BuildConfig;
import com.bplaz.merchant.Class.TypeFaceClass;
import com.bplaz.merchant.Fragment.DashboardFragment;
import com.bplaz.merchant.Fragment.ManageFragment;
import com.bplaz.merchant.Fragment.PricingFragment;
import com.bplaz.merchant.Fragment.ProductFragment;
import com.bplaz.merchant.Fragment.SalesFragment;
import com.bplaz.merchant.Preferance.PreferenceManagerLogin;
import com.bplaz.merchant.R;
import com.google.android.gms.maps.model.Dash;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.internal.BaselineLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView navigation;

    PreferenceManagerLogin session;

    String token;

//    public static String token_gps;
//
    public static Fragment fragment1 = null;
    public static Fragment fragment2 = null;
    public static Fragment fragment3= null;
    public static Fragment fragment4 = null;
    public static Fragment fragment5= null;
    public static FragmentManager fm;
    public static Fragment active= null;
//
    private static long back_pressed;
    public static Activity activity;
//    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm= getSupportFragmentManager();
        activity = this;

        fragment1 = new DashboardFragment();
        fragment2 = new SalesFragment();
        fragment3 = new ProductFragment();
        fragment5 = new ManageFragment();
        active = fragment1;

        fm.beginTransaction().add(R.id.container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.container,fragment1, "1").commit();

        //FOR RESTRICT HTTPS
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        session = new PreferenceManagerLogin(getApplicationContext());

        if(session.checkLogin()){
            finish();
        }else{
            HashMap<String, String> user = session.getUserDetails();
            token = user.get(PreferenceManagerLogin.KEY_TOKEN);
//            token_gps = token;
            navigation = findViewById(R.id.navigationView);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            BottomNavigationHelper.removeShiftMode(navigation);
            navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
            setNavigationTypeface();
        }



    }


    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Version");
        String versionName = BuildConfig.VERSION_NAME;

        mDatabase.orderByChild("version").equalTo(versionName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                        }else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setMessage("Please Update the apps")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String url = "https://drive.google.com/drive/u/1/folders/1RPm8Wrxnk0uArS5NBdhD7EDyxWNtOG17";
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                        }
                                    })
                                    .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


    }

    public void getLocation(){
//        gpsTracker = new GPSTracker(MainActivity.this);
//        if(gpsTracker.canGetLocation()){
//            double latitude = gpsTracker.getLatitude();
//            double longitude = gpsTracker.getLongitude();
//            String lan = String.valueOf(latitude);
//            String logt = String.valueOf(longitude);
//            if(lan != null && logt != null){
//                if(lan.equals("0.0") && logt.equals("0.0")){
//                    getLocation();
//                }
//            }
//        }else{
//            gpsTracker.showSettingsAlert();
//        }
    }

    //SET FONT FOR NAVIGATION
    public void setNavigationTypeface() {
        ViewGroup navigationGroup = (ViewGroup) navigation.getChildAt(0);
        for (int i = 0; i < navigationGroup.getChildCount(); i++) {
            ViewGroup navUnit = (ViewGroup) navigationGroup.getChildAt(i);
            for (int j = 0; j < navUnit.getChildCount(); j++) {
                View navUnitChild = navUnit.getChildAt(j);
                if (navUnitChild instanceof BaselineLayout) {
                    BaselineLayout baselineLayout = (BaselineLayout) navUnitChild;
                    for (int k = 0; k < baselineLayout.getChildCount(); k++) {
                        View baselineChild = baselineLayout.getChildAt(k);
                        if (baselineChild instanceof TextView) {
                            TextView textView = (TextView) baselineChild;
                            TypeFaceClass.setTypeFaceTextViewBold(textView,getApplicationContext());
                            textView.setTextSize(10);
                        }
                    }
                }
            }
        }
    }

    //FOR NAVIGATION UNABLE ANIMATION
    private static final class BottomNavigationHelper {
        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                item.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
                item.setChecked(item.getItemData().isChecked());
            }
        }
    }

    //FOR CLICK NAVIGATION
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.dashboard:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.sales:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.product:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;

                case R.id.profile:
                    fm.beginTransaction().hide(active).show(fragment5).commit();
                    active = fragment5;
                    return true;

            }

            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())  moveTaskToBack(true);
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    //FOR LOAD FRAGMENT
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


//    public static void dialog(String Message){
//        final MediaPlayer mp = MediaPlayer.create(activity, R.raw.notification);
//        AlertDialog.Builder mBuilderExpressCheckout = new AlertDialog.Builder(activity, R.style.CustomDialog);
//        final View mView = active.getLayoutInflater().inflate(R.layout.custom_dialog_new_incoming_job, null);
//        mBuilderExpressCheckout.setView(mView);
//        final AlertDialog mDialogExpressCheckout = mBuilderExpressCheckout.create();
//        mDialogExpressCheckout.show();
//        mDialogExpressCheckout.setCancelable(false);
//        mp.start();
//        Button button_decline = mView.findViewById(R.id.button_decline);
//        Button button_accept = mView.findViewById(R.id.button_accept);
//
//        button_decline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialogExpressCheckout.dismiss();
//                fragment1 = new Dashboard();
//                fragment2 = new ApprovalFragment();
//                fragment3 = new ToDeliverFragment();
//                fragment4 = new CompletedFragment();
//                fragment5 = new ProfileFragment();
//                active = fragment1;
//
//                fm.beginTransaction().add(R.id.container, fragment5, "5").hide(fragment5).commit();
//                fm.beginTransaction().add(R.id.container, fragment4, "4").hide(fragment4).commit();
//                fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
//                fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
//                fm.beginTransaction().add(R.id.container,fragment1, "1").commit();
//
//            }
//        });
//
//        button_accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialogExpressCheckout.dismiss();
//
//                fragment1 = new Dashboard();
//                fragment2 = new ApprovalFragment();
//                fragment3 = new ToDeliverFragment();
//                fragment4 = new CompletedFragment();
//                fragment5 = new ProfileFragment();
//                active = fragment2;
//
//                fm.beginTransaction().add(R.id.container, fragment5, "5").hide(fragment5).commit();
//                fm.beginTransaction().add(R.id.container, fragment4, "4").hide(fragment4).commit();
//                fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
//                fm.beginTransaction().add(R.id.container, fragment2, "2").commit();
//                fm.beginTransaction().add(R.id.container,fragment1, "1").hide(fragment1).commit();
//                MainActivity.navigation.setSelectedItemId(R.id.approval);
//            }
//        });
//    }


}
