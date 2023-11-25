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

import java.util.ArrayList;
import java.util.List;

public class CustomerUserArrayAdapter extends ArrayAdapter<UserCustomer> implements Filterable {


    private Activity mContext;

    public List<UserCustomer> userCustomerArray;
    public List<UserCustomer> userCustomerArrayFilter;

    public CustomerUserArrayAdapter(Activity context, List<UserCustomer> userCustomerArray ) {
        super(context, R.layout.activity_main_users , userCustomerArray);

        this.mContext = context;
        this.userCustomerArrayFilter = userCustomerArray;
        this.userCustomerArray = userCustomerArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomerUserArrayAdapter.ViewHolder viewHolder = null;

        if (r == null) {
            LayoutInflater layoutInflater =  mContext.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.activity_main_users, null,true);
            viewHolder = new CustomerUserArrayAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        }

        else{
            viewHolder = (CustomerUserArrayAdapter.ViewHolder) r.getTag();
        }

        String img = userCustomerArrayFilter.get(position).getImgUrl();
        String image_url = ("https://lamp.ms.wits.ac.za/home/s1671848/uploads/"+img).trim();

        viewHolder.username.setText(userCustomerArrayFilter.get(position).getUsername());
        viewHolder.userRoleArray.setText(userCustomerArrayFilter.get(position).getEmail());
        Glide.with(mContext).load(image_url).into(viewHolder.profile_image);
        viewHolder.user_ratings.setText("Positive rating: "+userCustomerArrayFilter.get(position).getHigh_rating()+"\t\tNegative rating: "+userCustomerArrayFilter.get(position).getLow_rating());
        viewHolder.ratingStars.setRating(userCustomerArrayFilter.get(position).getRating());

        return r;
    }

    @Override
    public int getCount(){
        return userCustomerArrayFilter.size();
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
                    filterResults.count = userCustomerArray.size();
                    filterResults.values = userCustomerArray;
                }
                else{
                    String searchString = constraint.toString().toLowerCase().trim();
                    List<UserCustomer> resultData = new ArrayList<>();

                    for (UserCustomer userCustomer : userCustomerArray)
                    {
                        if(userCustomer.getUsername().toLowerCase().trim().contains(searchString) || userCustomer.getEmail().toLowerCase().trim().contains(searchString))
                        {
                            resultData.add(userCustomer);
                        }
                        filterResults.count = resultData.size();
                        filterResults.values = resultData;
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userCustomerArrayFilter = (List<UserCustomer>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }



    class ViewHolder
    {
        TextView username;
        TextView userRoleArray;
        ImageView profile_image;
        TextView user_ratings;
        RatingBar ratingStars;

        ViewHolder(View v)
        {
            username = (TextView)v.findViewById(R.id.user_name);
            userRoleArray = (TextView)v.findViewById(R.id.user_role);
            profile_image = (ImageView)v.findViewById(R.id.user_profile_pic);
            user_ratings = (TextView)v.findViewById(R.id.user_ratings);
            ratingStars = (RatingBar)v.findViewById(R.id.user_rating_stars);
        }
    }
}


