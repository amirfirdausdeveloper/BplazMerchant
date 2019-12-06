package com.bplaz.merchant.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bplaz.merchant.Activity.ProfileActivity;
import com.bplaz.merchant.Class.TypeFaceClass;
import com.bplaz.merchant.Preferance.PreferenceManagerLogin;
import com.bplaz.merchant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageFragment extends Fragment {

    //HEADER
    TextView textView_header;

    //CONTENT
    TextView textView_profile,textView_staff;
    ImageView imageView_nextStaff,imageView_nextProfile;
    LinearLayout linear_profile,linear_staff;
    Button button_logout;

    //SESSION
    PreferenceManagerLogin session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_manage, container, false);

        session = new PreferenceManagerLogin(getContext());

        //DECLARE
        declare(v);

        //SET FONT
        setFont();

        //ON CLICK BUTTON
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.logoutUser();
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
        });

        imageView_nextProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getActivity(), ProfileActivity.class);
                startActivity(next);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        linear_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getActivity(), ProfileActivity.class);
                startActivity(next);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return v;
    }

    private void declare(View v){
        textView_header = v.findViewById(R.id.textView_header);
        textView_profile = v.findViewById(R.id.textView_profile);
        textView_staff = v.findViewById(R.id.textView_staff);
        imageView_nextStaff = v.findViewById(R.id.imageView_nextStaff);
        imageView_nextProfile = v.findViewById(R.id.imageView_nextProfile);
        linear_profile = v.findViewById(R.id.linear_profile);
        linear_staff = v.findViewById(R.id.linear_staff);
        button_logout = v.findViewById(R.id.button_logout);
    }

    private void setFont(){
        TypeFaceClass.setTypeFaceTextViewBold(textView_header,getContext());
        TypeFaceClass.setTypeFaceTextView(textView_profile,getContext());
        TypeFaceClass.setTypeFaceTextView(textView_staff,getContext());
        TypeFaceClass.setTypeFaceButtonBold(button_logout,getContext());
    }
}
