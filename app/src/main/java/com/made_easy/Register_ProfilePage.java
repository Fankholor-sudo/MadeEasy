package com.made_easy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register_ProfilePage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static Context mContext;

    private ProgressDialog
            progressDialog;
    private CircleImageView
            register_profile_pic;
    private ImageButton
            register_profile_upload;
    private AppCompatEditText
            register_name,
            register_city,
            register_skills;
    private AppCompatButton
            register_save;
    private static String
            browser_link,
            user_email,
            role;
    private static SharedPreferenceConfig
            preferenceConfig;
    public static Spinner jobSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__profile_page);
        mContext = this;

        preferenceConfig = new SharedPreferenceConfig(mContext);
        jobSpinner = findViewById(R.id.JobsPinner);
        jobSpinner.setOnItemSelectedListener(this);

        Intent intentExtra = getIntent();
        user_email = intentExtra.getStringExtra("USER_EMAIL");
        role = intentExtra.getStringExtra("USER_ROLE");

        preferenceConfig.writeLoginUserId(user_email);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Uploading Image, Plaease Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        register_profile_pic = (CircleImageView)findViewById(R.id.register_profile_pic);
        register_profile_upload = (ImageButton)findViewById(R.id.register_profile_upload);

        register_name = (AppCompatEditText)findViewById(R.id.register_username);
        register_city = (AppCompatEditText)findViewById(R.id.register_city);
        register_skills = (AppCompatEditText)findViewById(R.id.register_skills);

        register_save = (AppCompatButton)findViewById(R.id.register_save);

        register_skills.setVisibility(View.GONE);
        register_skills.setKeyListener(null);
        register_skills.setText("");

        if(role.equals("artisan")) {
            browser_link = "https://lamp.ms.wits.ac.za/home/s1671848/mc_app_register_profile_artisan.php";
            register_skills.setVisibility(View.VISIBLE);
            jobSpinner.setVisibility(View.VISIBLE);
        }
        else if(role.equals("customer")){
            browser_link = "https://lamp.ms.wits.ac.za/home/s1671848/mc_app_register_profile_customer.php";
            register_skills.setVisibility(View.GONE);
            jobSpinner.setVisibility(View.GONE);
        }

        register_profile_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(Register_ProfilePage.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                CropImage.activity()
                                        .setGuidelines(CropImageView.Guidelines.ON)
                                        .start(Register_ProfilePage.this);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                if(permissionDeniedResponse.isPermanentlyDenied()){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    builder.setTitle("Permission Denied")
                                            .setMessage("Permission to access your device storage is required to pick profile image. Please go to settings to enable permission storage")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                    startActivityForResult(intent,51);
                                                }
                                            }).setNegativeButton("Cancel", null).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        register_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = register_name.getText().toString().trim();
                String city = register_city.getText().toString().trim();
                String skills = register_skills.getText().toString().trim();

                if(username.isEmpty() && city.isEmpty())
                {
                    register_name.setError("Please enter your name");
                    register_city.setError("Please enter your city");
                }
                else if(username.isEmpty())
                {
                    register_name.setError("Please enter your name");
                }
                else if(city.isEmpty())
                {
                    Toast.makeText(mContext,"expertise ::: "+ skills,Toast.LENGTH_SHORT).show();
                    register_city.setError("Please enter your city");
                }
                else if(role.equals("artisan") && skills.isEmpty())
                {
                    register_skills.setError("Please select your expertise in the list bellow");

                }
                else ServerLoginArtisan(mContext ,user_email, username, city, skills);

            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////
    ///----------------------Profile Photo (Insertion)-----------------------///
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                register_profile_pic.setImageURI(resultUri);

                progressDialog.show();

                File imageFile = new File(resultUri.getPath());
                AndroidNetworking.upload("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_register_update_profile_pic.php")
                        .addMultipartFile("image", imageFile)
                        .addMultipartParameter("email", user_email)
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {
                                float progress = (float)bytesUploaded / totalBytes * 100;
                                progressDialog.setProgress((int)progress);
                            }
                        }).getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            String message = jsonObject.getString("message");
                            if(status == 0){
                                Toast.makeText(mContext, "Unable to Upload profile image" , Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(mContext, message , Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                           e.printStackTrace();
                            Toast.makeText(mContext, "JSON Error retrieving result profile image" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(mContext, "Error Uploading profile image" , Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////
    ///----------------------user (Insertion)-----------------------///
    private static void ServerLoginArtisan(final Context c ,String user_email ,String username , String city, String skills)
    {
        ContentValues cv = new ContentValues();
        cv.put("email", user_email);
        cv.put("username", username);
        cv.put("city", city);
        cv.put("skills", skills);

        new ServerCommunicator(browser_link, cv) {
            @Override
            protected void onPreExecute(){ };
            @Override
            protected void onPostExecute(String output) {
                try {
                    String
                            register_status = "",
                            status_message = "";

                    JSONArray users = new JSONArray(output);
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject object = users.getJSONObject(i);
                        register_status += object.getString("status");
                        status_message += object.getString("update_user");
                    }

                    if (register_status.equals("1")) {

                        preferenceConfig.writeLoginStatus(true);
                        preferenceConfig.writeLoginRole(role);

                        Intent intent = new Intent(mContext, MainPage.class);
                        mContext.startActivity(intent);
                        ((Register_ProfilePage) mContext).finish();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Registration Error " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        String skill = parent.getSelectedItem().toString();
        register_skills.setText(skill);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
