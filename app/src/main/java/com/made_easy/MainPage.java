package com.made_easy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MainPage extends AppCompatActivity {

    private static UserArtisan userArtisan;
    private static UserCustomer userCustomer;
    private static SharedPreferenceConfig
                    preferenceConfig;
    private static Context
                    mContext;
    public static Activity
                mActivity;
    public static ListView
            list;
    private static TextView
            appName;


    private static SearchView
            searchView;
    private static ImageButton
            close_icon,
            search_icon,
            menu_icon;

    private static String
            username,
            email,
            profile_pic,
            phone_number,
            skills,
            city,
            total_rating,
            low_rating,
            high_rating,
            myEmail;

    private static ArrayList<UserArtisan> userArtisansArray;
    private static ArrayList<UserCustomer> userCustomerArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mContext = this;
        mActivity = this;

        preferenceConfig = new SharedPreferenceConfig(mContext);
        myEmail = preferenceConfig.readUserId();

        searchView = findViewById(R.id.mainPage_search_new);
        search_icon = findViewById(R.id.mainPage_search_icon);
        close_icon = findViewById(R.id.mainPage_close_icon);
        menu_icon = findViewById(R.id.mainPage_menu_icon);

        list = findViewById(R.id.multi_ListView);
        appName = findViewById(R.id.main_app_name);

        ///---Get User Artisan-----///
        getUserArtisan(mContext);

        ////-----INSERT ALL USERS BOTH ARTISAN and CUSTOMER into Rating Table
        ////-----SO that they all have a default rating of 0
        insertAllUsersIntoRatingTable(mContext);

        /////Get User Customer When Menu TopRight(...) clicked

        ///////////------About Searching Users----------////////////////
        searchView.setVisibility(View.GONE);
        close_icon.setVisibility(View.GONE);

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setVisibility(View.VISIBLE);
                close_icon.setVisibility(View.VISIBLE);
                search_icon.setVisibility(View.GONE);
            }
        });

        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setVisibility(View.GONE);;
                close_icon.setVisibility(View.GONE);
                search_icon.setVisibility(View.VISIBLE);
            }
        });


        //////////-----------------Menu top right(more_vert)------------------///////////
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
    }

    //////////-----------------Menu top right(more_vert)---with List Customers------------///////////
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(mContext , v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_list_artisans:
                        getUserArtisan(mContext);
                        return true;
                    case R.id.menu_list_customers:
                        getUserCustomer(mContext);
                        return true;
                    case R.id.menu_Edit_profile:
                        Intent intent = new Intent(mContext , UserProfile.class);
                        mContext.startActivity(intent);
                        return true;
                    case R.id.menu_logout_item:
                        LogoutLink(mContext);
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.more_horizon_menu, popup.getMenu());
        popup.show();
    }

    //////////-------------END THE APPLICATION LOGIN SESSION-----------------///////////
    private static void LogoutLink(final Context c)
    {
        ContentValues cv = new ContentValues();

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_logout.php", cv) {
            @Override
            protected void onPreExecute(){};
            @Override
            protected void onPostExecute(String output) {
                try {
                    String status="",
                            status_message="";

                    JSONArray users = new JSONArray(output);
                    for (int i = 0; i < users.length(); i++)
                    {
                        JSONObject object = users.getJSONObject(i);
                        status += object.getString("logout_status");
                        status_message += object.getString("logout_message");
                    }
                    Toast.makeText(mContext , status_message , Toast.LENGTH_SHORT).show();

                    preferenceConfig.writeLoginStatus(false);
                    preferenceConfig.writeLoginUserId("");
                    preferenceConfig.writeLoginUsername("");
                    preferenceConfig.writeLoginRole("");

                    Intent intent = new Intent(mContext , LogIn.class);
                    mContext.startActivity(intent);
                    ((MainPage)mContext).finish();
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    //////////-----------------GET LIST OF ALL ARTISANS REGISTERED WITH US---------------///////////
    public static void getUserArtisan(final Context c)
    {
        ContentValues cv = new ContentValues();

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_get_user_artisan.php", cv) {
            @Override
            protected void onPreExecute(){};
            @Override
            protected void onPostExecute(String output) {
                try {

                    userArtisansArray = new ArrayList<>();
                    JSONArray users = new JSONArray(output);

                    for (int i = 0; i < users.length(); i++)
                    {
                        JSONObject object = users.getJSONObject(i);

                        email = object.getString("email");
                        if(email.equals(myEmail))continue;

                        username = object.getString("username");
                        profile_pic = object.getString("profile_pic");
                        phone_number = object.getString("phone_number");
                        skills = object.getString("skills");
                        city = object.getString("city");
                        total_rating = object.getString("total_rating");
                        low_rating = object.getString("low_rating");
                        high_rating = object.getString("high_rating");

                        userArtisan = new UserArtisan(username, email, phone_number, city, skills, profile_pic , Float.valueOf(total_rating), Integer.valueOf(low_rating),Integer.valueOf(high_rating));
                        userArtisansArray.add(userArtisan);
                    }
                    callArtisanAdapter();
                }
                catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(mContext , e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
    //////////-----------------ADD THE ARTISAN TO THE MAINPAGE LISTVIEW---------------///////////
    public static void callArtisanAdapter(){
        appName.setText("Made Easy | "+"Artisan");
        /////refresh the pages
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.deferNotifyDataSetChanged();
            }
        });

        final UsersArrayAdapter adapter = new UsersArrayAdapter(mActivity, userArtisansArray);
        list.setAdapter(adapter);
        Collections.sort(adapter.userArtisansArrayFilter);
        adapter.notifyDataSetChanged();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String img = adapter.userArtisansArrayFilter.get(position).getImgUrl();
                String image_url = ("https://lamp.ms.wits.ac.za/home/s1671848/uploads/" + img).trim();
                String rating = String.valueOf(adapter.userArtisansArrayFilter.get(position).getRating());
                String low_rating = String.valueOf(adapter.userArtisansArrayFilter.get(position).getLow_rating());
                String high_rating = String.valueOf(adapter.userArtisansArrayFilter.get(position).getHigh_rating());

                Intent intent = new Intent(mContext , ForeignUserProfile.class);

                intent.putExtra("ROLE", "artisan");
                intent.putExtra("NAME", adapter.userArtisansArrayFilter.get(position).getUsername());
                intent.putExtra("SKILL", adapter.userArtisansArrayFilter.get(position).getSkills());
                intent.putExtra("CITY",adapter.userArtisansArrayFilter.get(position).getCity());
                intent.putExtra("IMAGE", image_url);
                intent.putExtra("NO_RATINGS", rating + " Star Rated");
                intent.putExtra("STARS", rating);
                intent.putExtra("LOW_STARS", low_rating);
                intent.putExtra("HIGH_STARS", high_rating);
                intent.putExtra("EMAIL", adapter.userArtisansArrayFilter.get(position).getEmail());
                intent.putExtra("MOBILE", adapter.userArtisansArrayFilter.get(position).getPhoneNumber());
                mContext.startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

    }


    //////////-----------------GET LIST OF ALL CUSTOMERS REGISTERED WITH US---------------///////////
    public static void getUserCustomer(final Context c)
    {
        ContentValues cv = new ContentValues();

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_get_user_customer.php", cv) {
            @Override
            protected void onPreExecute(){};
            @Override
            protected void onPostExecute(String output) {
                try {

                    userCustomerArray = new ArrayList<>();
                    JSONArray users = new JSONArray(output);

                    for (int i = 0; i < users.length(); i++)
                    {
                        JSONObject object = users.getJSONObject(i);

                        email = object.getString("email");
                        if(email.equals(myEmail))continue;

                        username = object.getString("username");
                        profile_pic = object.getString("profile_pic");
                        phone_number = object.getString("phone_number");
                        city = object.getString("city");
                        total_rating = object.getString("total_rating");
                        low_rating = object.getString("low_rating");
                        high_rating = object.getString("high_rating");

                        userCustomer = new UserCustomer(username, email, phone_number, city, profile_pic , Float.valueOf(total_rating), Integer.valueOf(low_rating),Integer.valueOf(high_rating));
                        userCustomerArray.add(userCustomer);
                    }
                    callAdapterCustomer();
                }
                catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(mContext , e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
    //////////-----------------ADD THE CUSTOMERS TO THE MAINPAGE LISTVIEW---------------///////////
    public static void callAdapterCustomer(){

        appName.setText("Made Easy | "+"Customer");

        final CustomerUserArrayAdapter adapter = new CustomerUserArrayAdapter(mActivity, userCustomerArray);
        list.setAdapter(adapter);
        Collections.sort(adapter.userCustomerArrayFilter);
        adapter.notifyDataSetChanged();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String img = adapter.userCustomerArrayFilter.get(position).getImgUrl();

                /////refresh the pages
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.deferNotifyDataSetChanged();
                    }
                });

                String image_url = ("https://lamp.ms.wits.ac.za/home/s1671848/uploads/" + img).trim();
                String rating = String.valueOf(adapter.userCustomerArrayFilter.get(position).getRating());
                String low_rating = String.valueOf(adapter.userCustomerArrayFilter.get(position).getLow_rating());
                String high_rating = String.valueOf(adapter.userCustomerArrayFilter.get(position).getHigh_rating());

                Intent intent = new Intent(mContext, ForeignUserProfile.class);

                intent.putExtra("ROLE", "customer");
                intent.putExtra("CNAME", adapter.userCustomerArrayFilter.get(position).getUsername());
                intent.putExtra("CCITY",adapter.userCustomerArrayFilter.get(position).getCity());
                intent.putExtra("CIMAGE", image_url);
                intent.putExtra("CNO_RATINGS", rating + " Star Rated");
                intent.putExtra("CSTARS", rating);
                intent.putExtra("CLOW_STARS", low_rating);
                intent.putExtra("CHIGH_STARS", high_rating);
                intent.putExtra("CEMAIL", adapter.userCustomerArrayFilter.get(position).getEmail());
                intent.putExtra("CMOBILE", adapter.userCustomerArrayFilter.get(position).getPhoneNumber());

                mContext.startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    //////////-----------------INSERT ALL THE USERS INTO RATING TABLE---------------///////////
    public static void insertAllUsersIntoRatingTable(final Context c)
    {
        ContentValues cv = new ContentValues();

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s1671848/mc_app_insert_all_user_to_rating_table.php", cv) {
            @Override
            protected void onPreExecute(){};
            @Override
            protected void onPostExecute(String output) {
                try
                {
                    String status="";

                    JSONArray users = new JSONArray(output);
                    JSONObject object = users.getJSONObject(0);
                    status += object.getString("status");
                }
                catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(mContext,"error updating: "+e,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

}
