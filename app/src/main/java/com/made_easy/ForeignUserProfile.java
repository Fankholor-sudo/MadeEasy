package com.made_easy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForeignUserProfile extends AppCompatActivity {

    public static Context mContext;
    public static Activity mActivity;

    private static SharedPreferenceConfig preferenceConfig;
    public static ForeignUserComments foreignUserComments;

    public static ArrayList<ForeignUserComments> userCommentsAdapterList;

    public static ListView
            commentsListView;

    public static String
            com_username,
            com_comment,
            com_date,
            com_rating,
            com_pic,
            total_rating,
            high_rating,
            low_rating;

    public static ImageButton
            navigate_back,
            call_button;

    public static Button
                    rate_now_button;

    private static ImageView
            foreign_profile_image;

    private static TextView
            foreign_username,
            foreign_skills,
            foreign_city,
            foreign_email,
            foreign_phone_number,
            foreign_number_of_ratings,
            foreign_positive_rating,
            foreign_negavite_rating;

    private static RatingBar
            foreign_rating_stars;

    private static String
            profile_username,
            profile_email,
            profile_skill,
            profile_city,
            profile_image_url,
            profile_mobile_number,
            profile_number_rating,
            profile_rating,
            profile_pos_rating,
            profile_neg_rating,
            role,
            rating_message_comment,
            myUserId;

    public static float
                rating_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_user_profile);
        mContext = this;
        mActivity = this;
        preferenceConfig = new SharedPreferenceConfig(mContext);
        myUserId = preferenceConfig.readUserId();

        ///------USERS COMMENTS VIEWS----///
        commentsListView = findViewById(R.id.foreign_user_profile_customers_comments);
        navigate_back = findViewById(R.id.foreign_user_profile_back_button);
        call_button = findViewById(R.id.foreign_user_profile_call);
        rate_now_button = findViewById(R.id.foreign_user_profile_rate_now);

        rate_now_button.setVisibility(View.GONE);

        Intent intentExtra = getIntent();
        role = intentExtra.getStringExtra("ROLE");

        /////-------------------INITIALIZING VIEWS-----------------//////
        foreign_profile_image = findViewById(R.id.foreign_user_profile_picture);
        foreign_username = findViewById(R.id.foreign_user_profile_username);
        foreign_skills = findViewById(R.id.foreign_user_profile_skills);
        foreign_city = findViewById(R.id.foreign_user_profile_city);
        foreign_email = findViewById(R.id.foreign_user_profile_email);
        foreign_phone_number = findViewById(R.id.foreign_user_profile_phone_number);
        foreign_number_of_ratings = findViewById(R.id.foreign_user_profile_rating_number);
        foreign_rating_stars = findViewById(R.id.foreign_user_profile_showRatings);
        foreign_negavite_rating = findViewById(R.id.foreign_user_profile_low_rate);
        foreign_positive_rating = findViewById(R.id.foreign_user_profile_high_rate);

        if (role.equals("artisan")) {
            profile_username = intentExtra.getStringExtra("NAME");
            profile_email = intentExtra.getStringExtra("EMAIL");
            profile_skill = intentExtra.getStringExtra("SKILL");
            profile_city = intentExtra.getStringExtra("CITY");
            profile_image_url = intentExtra.getStringExtra("IMAGE");
            profile_mobile_number = intentExtra.getStringExtra("MOBILE");
            profile_number_rating = intentExtra.getStringExtra("NO_RATINGS");
            profile_rating = intentExtra.getStringExtra("STARS");
            profile_neg_rating = intentExtra.getStringExtra("LOW_STARS");
            profile_pos_rating = intentExtra.getStringExtra("HIGH_STARS");
            float star_number = Float.valueOf(profile_rating);

            String splited_kills = "";
            String[] splitedSkillArray = profile_skill.split(",");
            for (int i = 0; i < splitedSkillArray.length; ++i) {
                splited_kills += splitedSkillArray[i] + "\n";
            }
            splited_kills = splited_kills.trim();

            foreign_username.setText(profile_username);
            foreign_email.setText(profile_email);
            foreign_city.setText(profile_city);
            foreign_skills.setText(splited_kills);
            Glide.with(mContext).load(profile_image_url).into(foreign_profile_image);
            foreign_phone_number.setText(profile_mobile_number);
            foreign_number_of_ratings.setText(profile_number_rating);
            foreign_rating_stars.setRating(star_number);
            foreign_negavite_rating.setText("Negative rating: "+profile_neg_rating);
            foreign_positive_rating.setText("Positive rating: "+profile_pos_rating);

            CalculateUserRatings(mContext, profile_email);
        }

        else if (role.equals("customer")) {
            profile_username = intentExtra.getStringExtra("CNAME");
            profile_email = intentExtra.getStringExtra("CEMAIL");
            profile_city = intentExtra.getStringExtra("CCITY");
            profile_image_url = intentExtra.getStringExtra("CIMAGE");
            profile_mobile_number = intentExtra.getStringExtra("CMOBILE");
            profile_number_rating = intentExtra.getStringExtra("CNO_RATINGS");
            profile_rating = intentExtra.getStringExtra("CSTARS");
            profile_neg_rating = intentExtra.getStringExtra("CLOW_STARS");
            profile_pos_rating = intentExtra.getStringExtra("CHIGH_STARS");
            float star_number = Float.valueOf(profile_rating);

            foreign_username.setText(profile_username);
            foreign_email.setText(profile_email);
            foreign_city.setText(profile_city);
            foreign_skills.setText("");
            Glide.with(mContext).load(profile_image_url).into(foreign_profile_image);
            foreign_phone_number.setText(profile_mobile_number);
            foreign_number_of_ratings.setText(profile_number_rating);
            foreign_rating_stars.setRating(star_number);
            foreign_negavite_rating.setText("Negative rating: "+profile_neg_rating);
            foreign_positive_rating.setText("Positive rating: "+profile_pos_rating);

            CalculateUserRatings(mContext, profile_email);
        }

        /////////--------BACK TO MAIN PAGE BUTTON---------//////////
        navigate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.equals("customer")) {
                    MainPage.getUserCustomer(mContext);
                    MainPage.insertAllUsersIntoRatingTable(mContext);
                    ((ForeignUserProfile) mContext).finish();
                }
                else if(role.equals("artisan"))
                {
                    MainPage.getUserArtisan(mContext);
                    MainPage.insertAllUsersIntoRatingTable(mContext);
                    ((ForeignUserProfile) mContext).finish();
                }
            }
        });

        ////////---------TRIGGER TO MAKE A CALL BUTTON--------//////////
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate_now_button.setVisibility(View.VISIBLE);
                String mobile_call = "tel://" + profile_mobile_number.trim();

                ////refresh the the pages
                ForeignUserProfile.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainPage.list.deferNotifyDataSetChanged();
                    }
                });

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                intent.setData(Uri.parse(mobile_call));
                startActivity(intent);
            }
        });

        ////////------RATE USER BUTTON--------//////////
        rate_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = getLayoutInflater().inflate(R.layout.layout_rate_user,null);

                RatingBar mRatingBar = mView.findViewById(R.id.foreign_user_profile_rate_user);
                final EditText rating_message = mView.findViewById(R.id.foreign_user_profile_rate_user_message);
                final Button submit_rating = mView.findViewById(R.id.foreign_user_profile_rate_user_submit);

                submit_rating.setEnabled(false);

                mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        Toast.makeText(mContext , "You have Rated: "+rating +" ratings",Toast.LENGTH_SHORT).show();
                        rating_number = rating;
                        submit_rating.setEnabled(true);
                    }
                });

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                submit_rating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rating_message_comment = rating_message.getText().toString().trim();
                        RateUserLink(mContext, myUserId ,profile_email ,rating_message_comment ,rating_number);

                        Toast.makeText(mContext , "You have rated "+profile_username+" "+rating_number+"star rating" ,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        rate_now_button.setVisibility(View.GONE);

                        /////refresh the pages
                        ForeignUserProfile.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainPage.list.deferNotifyDataSetChanged();
                            }
                        });

                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(role.equals("customer")) {
            MainPage.getUserCustomer(mContext);
            MainPage.insertAllUsersIntoRatingTable(mContext);
            ((ForeignUserProfile) mContext).finish();
        }
        else if(role.equals("artisan"))
        {
            MainPage.getUserArtisan(mContext);
            MainPage.insertAllUsersIntoRatingTable(mContext);
            ((ForeignUserProfile) mContext).finish();
        }
    }

    //////////-------------RATE USER's SERICE -----------------///////////
    private static void RateUserLink(final Context c, String from_email , final String to_email , String rating_comment , float rating_num)
    {
        ContentValues cv = new ContentValues();
        cv.put("f_email",from_email);
        cv.put("t_email",to_email);
        cv.put("comment",rating_comment);
        cv.put("rating",rating_num);

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_rate_user.php", cv) {
            @Override
            protected void onPreExecute(){};
            @Override
            protected void onPostExecute(String output) {
                try {
                    JSONArray users = new JSONArray(output);
                    JSONObject object = users.getJSONObject(0);

                    CalculateUserRatings(c , to_email);
                }
                catch(JSONException e){
                    e.printStackTrace();
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

                    userCommentsAdapterList = new ArrayList<>();
                    JSONArray users = new JSONArray(output);

                    for(int i = 0; i < users.length()-1; i++)
                    {
                        JSONObject rate = users.getJSONObject(users.length()-1);

                        total_rating = rate.getString("total_rating");
                        low_rating = rate.getString("low_rating");
                        high_rating = rate.getString("high_rating");

                        JSONObject object = users.getJSONObject(i);
                        com_comment = object.getString("comment");
                        com_pic = object.getString("profile_pic");
                        com_date = object.getString("date");
                        com_username = object.getString("f_name");
                        com_rating = object.getString("rating");

                        foreignUserComments = new ForeignUserComments(com_username , com_comment ,com_pic , com_date , Float.valueOf(com_rating) );
                        userCommentsAdapterList.add(foreignUserComments);

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
        CommentUserArrayAdapter commentUserArrayAdapter = new CommentUserArrayAdapter(mActivity , userCommentsAdapterList);
        commentsListView.setAdapter(commentUserArrayAdapter);
        Utility.setListViewHeightBasedOnChildren(commentsListView);

        float star_number = Float.valueOf(total_rating);
        foreign_rating_stars.setRating(star_number);
        foreign_negavite_rating.setText("Negative rating: "+low_rating);
        foreign_positive_rating.setText("Positive rating: "+high_rating);
    }
}
