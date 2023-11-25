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

import java.util.List;

public class CommentUserArrayAdapter extends ArrayAdapter<ForeignUserComments> {

    private Activity mContext;

    public List<ForeignUserComments> userCommentsArray;

    public CommentUserArrayAdapter(Activity context, List<ForeignUserComments>userCommentsArray ) {
        super(context, R.layout.activity_user_profile_comments , userCommentsArray);

        this.mContext = context;

        this.userCommentsArray = userCommentsArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CommentUserArrayAdapter.ViewHolder viewHolder = null;

        if (r == null) {
            LayoutInflater layoutInflater =  mContext.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.activity_user_profile_comments, null,true);
            viewHolder = new CommentUserArrayAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (CommentUserArrayAdapter.ViewHolder) r.getTag();
        }

        String img = userCommentsArray.get(position).getImgUrl();
        String image_url = ("https://lamp.ms.wits.ac.za/home/s1671848/uploads/"+img).trim();
        Glide.with(mContext).load(image_url).into(viewHolder.profile_image);
        viewHolder.username.setText(userCommentsArray.get(position).getName());
        viewHolder.date.setText(userCommentsArray.get(position).getDate());
        viewHolder.comment.setText(userCommentsArray.get(position).getComment());
        viewHolder.ratings.setRating(userCommentsArray.get(position).getRating());


        return r;
    }

    class ViewHolder
    {
        TextView username;
        TextView date;
        ImageView profile_image;
        TextView comment;
        RatingBar ratings;

        ViewHolder(View v)
        {
            username = (TextView)v.findViewById(R.id.user_profile_comment_name);
            date = (TextView)v.findViewById(R.id.user_profile_comment_date);
            profile_image = (ImageView)v.findViewById(R.id.user_profile_comment_picture);
            comment = (TextView)v.findViewById(R.id.user_profile_comment_message);
            ratings = v.findViewById(R.id.user_profile_rating_stars);
        }
    }

}
