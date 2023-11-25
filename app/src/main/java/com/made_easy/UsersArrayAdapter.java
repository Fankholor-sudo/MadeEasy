package com.made_easy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class UsersArrayAdapter extends ArrayAdapter<UserArtisan> implements Filterable{

    private Activity mContext;

    public List<UserArtisan>userArtisansArray;
    public List<UserArtisan>userArtisansArrayFilter;

    public UsersArrayAdapter(Activity context, List<UserArtisan>userArtisansArray ) {
        super(context, R.layout.activity_main_users, userArtisansArray);

        this.mContext = context;
        this.userArtisansArrayFilter = userArtisansArray;
        this.userArtisansArray = userArtisansArray;
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;

        if (r == null) {
            LayoutInflater layoutInflater =  mContext.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.activity_main_users, null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }

        else{
            viewHolder = (ViewHolder) r.getTag();
        }

        String img = userArtisansArrayFilter.get(position).getImgUrl();
        String image_url = ("https://lamp.ms.wits.ac.za/home/s1671848/uploads/"+img).trim();

        viewHolder.username.setText(userArtisansArrayFilter.get(position).getUsername());
        viewHolder.userRoleArray.setText(userArtisansArrayFilter.get(position).getSkills());
        Glide.with(mContext).load(image_url).into(viewHolder.profile_image);
        viewHolder.user_ratings.setText("Positive rating: "+userArtisansArrayFilter.get(position).getHigh_rating()+"\t\tNegative rating: "+userArtisansArrayFilter.get(position).getLow_rating());
        viewHolder.ratingStars.setRating(userArtisansArrayFilter.get(position).getRating());

        return r;
    }

    @Override
    public int getCount(){
        return userArtisansArrayFilter.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if(constraint == null || constraint.length()==0)
                {
                    filterResults.count = userArtisansArray.size(); //pass your array
                    filterResults.values = userArtisansArray; //pass your array
                }
                else{
                    String searchString = constraint.toString().toLowerCase().trim();
                    List<UserArtisan> resultData = new ArrayList<>(); //use datatype of your array

                    for (UserArtisan userArtisan : userArtisansArray)//loop through your array
                    {
                        if(userArtisan.getSkills().toLowerCase().trim().contains(searchString))
                        {
                            resultData.add(userArtisan);
                            Collections.sort(resultData, new Comparator<UserArtisan>() {
                                @Override
                                public int compare(UserArtisan first, UserArtisan second) {
                                    return (int)(second.getRating() - first.getRating());
                                }
                            });
                        }
                        filterResults.count = resultData.size();
                        filterResults.values = resultData;
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userArtisansArrayFilter = (List<UserArtisan>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    class ViewHolder
    {
        ImageView profile_image;
        TextView username,
                userRoleArray,
                user_ratings;
        RatingBar ratingStars;

        ViewHolder(View v)
        {
            username = v.findViewById(R.id.user_name);
            userRoleArray = v.findViewById(R.id.user_role);
            profile_image = v.findViewById(R.id.user_profile_pic);
            user_ratings = v.findViewById(R.id.user_ratings);
            ratingStars = v.findViewById(R.id.user_rating_stars);
        }
    }
}


