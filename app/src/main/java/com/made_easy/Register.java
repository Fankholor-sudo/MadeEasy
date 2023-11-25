package com.made_easy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private static SharedPreferenceConfig preferenceConfig;
    private static Context mContext;
    private AppCompatEditText
            register_email,
            register_email_comfirm,
            register_phone_number,
            register_password,
            register_password_comfirm;

    private AppCompatButton
            register_btn,
            registered_login;

    private CheckBox
            register_customer,
            register_artisan;

    private static ProgressBar
                    reg_loading;
    private static String
                    role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        preferenceConfig = new SharedPreferenceConfig(mContext);

        reg_loading = (ProgressBar)findViewById(R.id.register_progress);

        register_email = (AppCompatEditText)findViewById(R.id.register_email);
        register_email_comfirm = (AppCompatEditText)findViewById(R.id.register_email_verify);
        register_phone_number = (AppCompatEditText)findViewById(R.id.register_phone);
        register_password = (AppCompatEditText)findViewById(R.id.register_password);
        register_password_comfirm = (AppCompatEditText)findViewById(R.id.register_retype_password);

        register_btn = (AppCompatButton)findViewById(R.id.register_button);
        registered_login = (AppCompatButton)findViewById(R.id.register_login);

        register_customer = (CheckBox)findViewById(R.id.customer);
        register_artisan = (CheckBox)findViewById(R.id.artisan);

        reg_loading.setVisibility(View.GONE);

        register_customer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                register_artisan.setChecked(false);
                role = "customer";
            }
        });

        register_artisan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                register_customer.setChecked(false);
                role = "artisan";
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reg_loading.setVisibility(View.VISIBLE);
                String email = register_email.getText().toString().trim();
                String email_verify = register_email_comfirm.getText().toString().trim();
                String phone = register_phone_number.getText().toString().trim();
                String password = register_password.getText().toString().trim();
                String password_verify = register_password_comfirm.getText().toString().trim();

                if(email.isEmpty() && phone.isEmpty() && password.isEmpty())
                {
                    reg_loading.setVisibility(View.GONE);
                    register_email.setError("Email should not be empty");
                    register_password.setError("Please enter your password");
                    register_phone_number.setError("Please enter your phone number");
                    Toast.makeText(mContext , "make sure all the required fields are not empty!", Toast.LENGTH_LONG).show();
                }
                else if(email.isEmpty() || email_verify.isEmpty()){
                    reg_loading.setVisibility(View.GONE);
                    register_email.setError("Please enter email and email verify");
                }
                else if(!email.equals(email_verify))
                {
                    reg_loading.setVisibility(View.GONE);
                    register_email_comfirm.setError("This email does not match the above email");
                    Toast.makeText(mContext , "The email does not match, make sure your email verify is correct", Toast.LENGTH_LONG).show();
                }
                else if(!password_verify.equals(password))
                {
                    reg_loading.setVisibility(View.GONE);
                    register_password_comfirm.setError("This password does not match the above password");
                    Toast.makeText(mContext , "The password does not match, make sure your password verify is correct", Toast.LENGTH_LONG).show();
                }
                else if(password.isEmpty() || password_verify.isEmpty())
                {
                    reg_loading.setVisibility(View.GONE);
                    register_password.setError("Please enter password and password verify");
                }
                else if(phone.isEmpty()){
                    reg_loading.setVisibility(View.GONE);
                    register_phone_number.setError("Please enter your phone number");
                }
                else if(!register_customer.isChecked() && !register_artisan.isChecked())
                {
                    reg_loading.setVisibility(View.GONE);
                    Toast.makeText(mContext , "Please make sure you tick your role, either Customer or Artisan",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    reg_loading.setVisibility(View.GONE);
                    ServerLogin(mContext, email, phone, password, role);
                }
            }
        });

        registered_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LogIn.class);
                mContext.startActivity(intent);
                ((Register)mContext).finish();
            }
        });
    }

    private static void ServerLogin(final Context c ,String email , String phone, String password, String role)
    {
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("phone_number", phone);
        cv.put("password", password);
        cv.put("role", role);

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_register.php", cv) {
            @Override
            protected void onPreExecute(){};
            @Override
            protected void onPostExecute(String output) {
                try {
                    String
                            register_status = "",
                            status_message = "",
                            user_email = "",
                            user_role = "";
                    String
                            name="",
                            mobile,
                            city,
                            skills,
                            img;

                    JSONArray users = new JSONArray(output);
                    JSONObject ErrorObject = users.getJSONObject(0);

                    register_status += ErrorObject.getString("register_status");
                    status_message += ErrorObject.getString("status_message");
                    Toast.makeText(mContext , status_message , Toast.LENGTH_LONG).show();

                    for (int i = 0; i < users.length(); i++)
                    {
                        JSONObject object = users.getJSONObject(i);
                        user_email += object.getString("email");
                        user_role += object.getString("role");

                    }

                    if(register_status.equals("1"))
                    {
                        Toast.makeText(mContext , status_message , Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(mContext, Register_ProfilePage.class);
                        intent.putExtra("USER_ROLE", user_role);
                        intent.putExtra("USER_EMAIL", user_email);
                        mContext.startActivity(intent);
	                    ((Register)mContext).finish();
                    }
                }
                catch(JSONException e){
                    reg_loading.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
