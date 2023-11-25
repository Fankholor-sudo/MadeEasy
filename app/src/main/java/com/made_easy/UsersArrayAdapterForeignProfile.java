package com.made_easy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class UsersArrayAdapterForeignProfile extends ArrayAdapter<UserArtisan> {
    private Activity mContext;

    public UserArtisan[] userArtisansForeignArray;

    public UsersArrayAdapterForeignProfile(Activity context, UserArtisan[] userArtisansForeignArray ) {
        super(context, R.layout.activity_foreign_user_profile, userArtisansForeignArray);

        this.mContext = context;

        this.userArtisansForeignArray = userArtisansForeignArray;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        UsersArrayAdapterForeignProfile.ViewHolder viewHolder = null;

        if (r == null) {
            LayoutInflater layoutInflater = mContext.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.activity_foreign_user_profile, null, true);
            viewHolder = new UsersArrayAdapterForeignProfile.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (UsersArrayAdapterForeignProfile.ViewHolder) r.getTag();
        }

        String img = userArtisansForeignArray[position].getImgUrl();
        String image_url = ("https://lamp.ms.wits.ac.za/home/s1671848/uploads/" + img).trim();

        viewHolder.foreign_username.setText(userArtisansForeignArray[position].getUsername());
        viewHolder.foreign_skills.setText(userArtisansForeignArray[position].getSkills());
        Glide.with(mContext).load(image_url).into(viewHolder.foreign_profile_image);
        viewHolder.foreign_number_of_ratings.setText(userArtisansForeignArray[position].getRating() + " Star Rated");
        viewHolder.foreign_rating_stars.setRating(userArtisansForeignArray[position].getRating());
        viewHolder.foreign_email.setText(userArtisansForeignArray[position].getEmail());
        viewHolder.foreign_phone_number.setText(userArtisansForeignArray[position].getPhoneNumber());

        return r;
    }
    class ViewHolder
    {
        ImageView foreign_profile_image;
        TextView foreign_username,
                foreign_skills,
                foreign_email,
                foreign_phone_number,
                foreign_number_of_ratings;
        RatingBar foreign_rating_stars;

        ViewHolder(View v)
        {
            foreign_profile_image = v.findViewById(R.id.foreign_user_profile_picture);
            foreign_username = v.findViewById(R.id.foreign_user_profile_username);
            foreign_skills = v.findViewById(R.id.foreign_user_profile_skills);
            foreign_email = v.findViewById(R.id.foreign_user_profile_email);
            foreign_phone_number = v.findViewById(R.id.foreign_user_profile_phone_number);
            foreign_number_of_ratings = v.findViewById(R.id.foreign_user_profile_rating_number);
            foreign_rating_stars = v.findViewById(R.id.foreign_user_profile_showRatings);
        }
    }
}
