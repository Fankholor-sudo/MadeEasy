package com.made_easy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.bumptech.glide.Glide;

import java.io.File;

public class ForgotPassword extends AppCompatActivity {

    private static Context mContext;
    private AppCompatButton request,
                    navTo_login,
                    navTo_register;
    private AppCompatEditText email,
                    phone_number;

    ImageView image;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mContext = this;

        request = (AppCompatButton)findViewById(R.id.forgotUser_button);
        navTo_login = (AppCompatButton)findViewById(R.id.forgotUser_login);
        navTo_register = (AppCompatButton)findViewById(R.id.forgotUser_register);

        email = (AppCompatEditText)findViewById(R.id.forgotUser_email);
        phone_number = (AppCompatEditText)findViewById(R.id.forgotUser_phone);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        navTo_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LogIn.class);
                startActivity(intent);
                ((ForgotPassword)mContext).finish();
            }
        });

        navTo_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Register.class);
                startActivity(intent);
                ((ForgotPassword)mContext).finish();
            }
        });
    }
}
