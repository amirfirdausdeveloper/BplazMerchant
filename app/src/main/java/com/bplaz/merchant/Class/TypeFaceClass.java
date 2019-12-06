package com.bplaz.merchant.Class;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class TypeFaceClass {

    public static void setTypeFaceEditText(EditText editText, Context context){
        editText.setTypeface(Typeface.createFromAsset(context.getAssets(), "Avenir-Medium-09.ttf"));
    }

    public static void setTypeFaceTextView(TextView textView, Context context){
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "Avenir-Medium-09.ttf"));
    }
    public static void setTypeFaceButton(Button button, Context context){
        button.setTypeface(Typeface.createFromAsset(context.getAssets(), "Avenir-Medium-09.ttf"));
    }

    public static void setTypeFaceTextInputEditText(TextInputEditText textInputEditText, Context context){
        textInputEditText.setTypeface(Typeface.createFromAsset(context.getAssets(), "Avenir-Medium-09.ttf"));
    }

    public static void setTypeFaceCheckBox(CheckBox checkBox, Context context){
        checkBox.setTypeface(Typeface.createFromAsset(context.getAssets(), "Avenir-Medium-09.ttf"));
    }
    public static void setTypeFaceTextViewBold(TextView textView, Context context){
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "Avenir-Heavy-05.ttf"));
    }
    public static void setTypeFaceButtonBold(Button button, Context context){
        button.setTypeface(Typeface.createFromAsset(context.getAssets(), "Avenir-Heavy-05.ttf"));
    }
}
