package com.made_easy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
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
import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    public static Context mContext;
    public static Activity mActivity;

    public static ForeignUserComments foreignUserComments;

    private ProgressDialog
            progressDialog;
    private SharedPreferenceConfig
            sharedPreferenceConfig;
    public static UsersRatings
            usersRatings;

    public static ArrayList<ForeignUserComments> userCommentsList;

    public static String
            com_username,
            com_comment,
            com_date,
            com_rating,
            com_pic;

    public static ListView commentsListView;

    public static String myName,myEmail,myRole;

    public static ImageButton
            change_profile_img,
            navigate_back;

    public static ImageView
            profile_edit_btn,
            profile_picture;

    public static TextView
                myNametxt,
                myPhonetxt,
                myCitytxt;

    public static TextView
                    myUsername,
                    myEmailtxt,
                    mRatingNum,
                    mLowRateTv,
                    mHighRateTv;
    public static RatingBar mRating;

    private static String
            status_username,
            status_email,
            status_mobile,
            status_city,
            status_imgUrl,
            status_role,
            status_low_rating,
            status_high_rating,
            status_skills;
    private static String
            change_username,
            change_city,
            change_mobile;

    private static Float status_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mContext = this;
        mActivity = this;
        sharedPreferenceConfig = new SharedPreferenceConfig(mContext);

        myName = sharedPreferenceConfig.readUsername();
        myEmail = sharedPreferenceConfig.readUserId();
        myRole = sharedPreferenceConfig.readRole();

        myNametxt = findViewById(R.id.user_profile_display_name);
        myEmailtxt = findViewById(R.id.user_profile_display_email);
        myPhonetxt = findViewById(R.id.user_profile_display_mobile);
        myCitytxt = findViewById(R.id.user_profile_display_city);

        commentsListView = findViewById(R.id.customers_comments);
        mRatingNum = findViewById(R.id.user_profile_rating_number);
        mRating = findViewById(R.id.user_profile_showRatings);
        mLowRateTv = findViewById(R.id.my_profile_low_rate);
        mHighRateTv = findViewById(R.id.my_profile_high_rate);
        myUsername = findViewById(R.id.user_profile_username_stacked);

        navigate_back = findViewById(R.id.back_button_profile);
        profile_picture =  findViewById(R.id.my_profile_picture);
        profile_edit_btn = findViewById(R.id.user_profile_edit_details);

        CalculateUserRatings(mContext, myEmail, myRole);
        CalculateUserRatings(mContext, myEmail);

        navigate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPage.insertAllUsersIntoRatingTable(mContext);
                ((UserProfile) mContext).finish();

            }
        });

        change_profile_img = findViewById(R.id.change_profile_picture);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Uploading Image, Plaease Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        change_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(mActivity)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                CropImage.activity()
                                        .setGuidelines(CropImageView.Guidelines.ON)
                                        .start(mActivity);
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

            profile_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(mContext);
                View mView = getLayoutInflater().inflate(R.layout.layout_change_user_details,null);

                final EditText edit_name = mView.findViewById(R.id.user_profile_edit_name);
                final EditText edit_city = mView.findViewById(R.id.user_profile_edit_city);
                final EditText edit_mobile = mView.findViewById(R.id.user_profile_edit_mobile);
                Button save = mView.findViewById(R.id.user_profile_edit_save);
                edit_name.setText(status_username);
                edit_city.setText(status_city);
                edit_mobile.setText(status_mobile);

                mBuilder.setView(mView);
                final android.app.AlertDialog dialog = mBuilder.create();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change_username = edit_name.getText().toString();
                        change_city  = edit_city.getText().toString();
                        change_mobile = edit_mobile.getText().toString();
                        Toast.makeText(mContext,"update was successfull." ,Toast.LENGTH_LONG).show();

                        EditUserDetails(mContext, status_email, change_username, change_city, change_mobile, status_role);
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
                profile_picture.setImageURI(resultUri);

                progressDialog.show();

                File imageFile = new File(resultUri.getPath());
                AndroidNetworking.upload("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_register_update_profile_pic.php")
                        .addMultipartFile("image", imageFile)
                        .addMultipartParameter("email", myEmail)
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

    ////////////-----CHANGE USER DETAILS-------////////
    public static void EditUserDetails(final Context c, String email, String username, String city ,String mobile,String role)
    {
        ContentValues cv = new ContentValues();
        cv.put("email",email);
        cv.put("username",username);
        cv.put("city",city);
        cv.put("mobile",mobile);
        cv.put("role",role);

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_user_edit_details.php", cv) {
            @Override
            protected void onPreExecute(){};
            @Override
            protected void onPostExecute(String output) {
                try {
                    JSONArray users = new JSONArray(output);
                    JSONObject object = users.getJSONObject(0);

                    ((UserProfile)mContext).finish();
                    Intent intent = new Intent(mContext, UserProfile.class);
                    mContext.startActivity(intent);
                }
                catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(mContext,"error updating: "+e,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////-------------------CALCULATE THE CURRENT LOGGEDIN USER RATINGS-----------------////
    public static void CalculateUserRatings(final Context c, String to_email, String role)
    {
        ContentValues cv = new ContentValues();
        cv.put("email",to_email);
        cv.put("role",role);

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_user_profile_get_rating.php", cv) {
            @Override
            protected void onPreExecute(){};
            @Override
            protected void onPostExecute(String output) {
                try {
                    String status="";

                    JSONArray users = new JSONArray(output);
                    JSONObject object = users.getJSONObject(0);

                    status_email = object.getString("email");
                    status_username = object.getString("username");
                    status_mobile = object.getString("mobile");
                    status_city = object.getString("city");
                    status_imgUrl = object.getString("profile_pic");
                    status_role = object.getString("role");
                    status_rating = Float.valueOf(object.getString("rating"));
                    status_high_rating = object.getString("high_rating");
                    status_low_rating = object.getString("low_rating");

                    if(status_role.equals("artisan"))
                    {
                        status_skills = object.getString("skills");
                    }

                    String myImgUrl = "https://lamp.ms.wits.ac.za/home/s1671848/uploads/"+status_imgUrl;
                    myNametxt.setText(status_username);
                    myUsername.setText(status_username);
                    myEmailtxt.setText(status_email);
                    myPhonetxt.setText(status_mobile);
                    myCitytxt.setText(status_city);
                    mRating.setRating(status_rating);
                    mRatingNum.setText(status_rating+" Star Rated");
                    mLowRateTv.setText("Negative rating: "+status_low_rating);
                    mHighRateTv.setText("\t\tPositive rating: "+status_high_rating);
                    Glide.with(mContext).load(myImgUrl).into(profile_picture);

                }
                catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(mContext,"error updating: "+e,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public static void CalculateUserRatings(final Context c, String to_email)
    {
        ContentValues cv = new ContentValues();
        cv.put("t_email",to_email);

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_user_rating_calculation.php", cv) {
            @Override
            protected void onPreExecute(){};
            @Override
            protected void onPostExecute(String output) {
                try {

                    userCommentsList = new ArrayList<>();
                    JSONArray users = new JSONArray(output);

                    for(int i = 0; i < users.length()-1; i++)
                    {
                        JSONObject object = users.getJSONObject(i);
                        com_comment = object.getString("comment");
                        com_pic = object.getString("profile_pic");
                        com_date = object.getString("date");
                        com_username = object.getString("f_name");
                        com_rating = object.getString("rating");
                        foreignUserComments = new ForeignUserComments(com_username , com_comment ,com_pic , com_date , Float.valueOf(com_rating) );
                        userCommentsList.add(foreignUserComments);
                        callCommentAdapter();
                    }

                }
                catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(mContext,"COMMENTS ERROR "+e,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
    public static void callCommentAdapter()
    {
        CommentUserArrayAdapter commentUserArrayAdapter = new CommentUserArrayAdapter(mActivity , userCommentsList);
        commentsListView.setAdapter(commentUserArrayAdapter);
        Utility.setListViewHeightBasedOnChildren(commentsListView);
    }
}
