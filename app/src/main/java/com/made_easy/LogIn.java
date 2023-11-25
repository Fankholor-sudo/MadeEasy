package com.made_easy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends AppCompatActivity {

    private static Context mContext;
    private static TextView errorView;
    private AppCompatButton
            register,
            forgotUser,
            appSignin;
    private AppCompatEditText
            editTxtEmail,
            editTxtPassword;
    private static ProgressBar loading;
    private static String
            status_username,
            status_email,
            status_role;


    private static SharedPreferenceConfig
            preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = this;
        preferenceConfig = new SharedPreferenceConfig(mContext);

        loading = (ProgressBar)findViewById(R.id.login_progress);
        register = (AppCompatButton)findViewById(R.id.register);
        forgotUser = (AppCompatButton)findViewById(R.id.forgot_password);
        appSignin = (AppCompatButton)findViewById(R.id.login_button);

        errorView = (TextView) findViewById(R.id.errorView);

        editTxtEmail = (AppCompatEditText)findViewById(R.id.login_email);
        editTxtPassword = (AppCompatEditText)findViewById(R.id.login_password);

        loading.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);

        appSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);

                String user_email = editTxtEmail.getText().toString().trim();
                String user_password = editTxtPassword.getText().toString().trim();

                if(user_email.isEmpty() && user_password.isEmpty())
                {
                    loading.setVisibility(View.GONE);
                    editTxtEmail.setError("Email field should not be empty");
                    editTxtPassword.setError("Please enter your password");
                }
                else if(user_email.isEmpty())
                {
                    loading.setVisibility(View.GONE);
                    editTxtEmail.setError("Email field should not be empty");
                }
                else if(user_password.isEmpty())
                {
                    loading.setVisibility(View.GONE);
                    editTxtPassword.setError("Please enter your password");
                }
                else ServerLogin(mContext, user_email, user_password);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterPage();
            }
        });
        forgotUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotUser();
            }
        });
    }

    private static void ServerLogin(final Context c, final String email, String password)
    {
        ContentValues cv = new ContentValues();
        cv.put("email",email);
        cv.put("password",password);

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_login.php", cv) {
            @Override
            protected void onPreExecute(){
            };
            @Override
            protected void onPostExecute(String output) {
                try {

                    JSONArray users = new JSONArray(output);
                    JSONObject object_one = users.getJSONObject(0);

                    int error = object_one.getInt("login_status");
                    String status_message = object_one.getString("login_message");
                    if(error==1){errorView.setVisibility(View.GONE);}
                    else
                    {
                        errorView.setText(status_message);
                        errorView.setVisibility(View.VISIBLE);
                    }
                    status_email = object_one.getString("email");
                    status_username = object_one.getString("username");
                    status_role = object_one.getString("role");

                    preferenceConfig.writeLoginStatus(true);
                    preferenceConfig.writeLoginUserId(status_email);
                    preferenceConfig.writeLoginRole(status_role);
                    preferenceConfig.writeLoginUsername(status_username);

                    Toast.makeText(mContext,"welcome to MadeEasy "+status_username,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MainPage.class);
                    mContext.startActivity(intent);
                    ((LogIn)mContext).finish();
                }
                catch(JSONException e){
                    loading.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    public void openRegisterPage()
    {
        Intent intent = new Intent(mContext, Register.class);
        startActivity(intent);
        ((LogIn)mContext).finish();
    }

    public void openForgotUser()
    {
        Intent intent = new Intent(mContext, ForgotPassword.class);
        startActivity(intent);
        ((LogIn)mContext).finish();
    }
}
