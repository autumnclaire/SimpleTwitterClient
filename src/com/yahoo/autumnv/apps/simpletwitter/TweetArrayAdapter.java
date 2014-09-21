package com.yahoo.autumnv.apps.simpletwitter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yahoo.autumnv.apps.simpletwitter.models.Tweet;
import com.yahoo.autumnv.apps.simpletwitter.models.User;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context,
			List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		View v;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.tweet_item, parent, false);
		} else {
			v = convertView;
		}
		
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		TextView tvUserName = (TextView) v.findViewById(R.id.tvScreenName);
		TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
		TextView tvTimestamp = (TextView) v.findViewById(R.id.tvTimestamp);
		
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		User user = tweet.getUser();
		String profileImageUrl = user.getProfileImageUrl();
		imageLoader.displayImage(profileImageUrl, ivProfileImage);
		
		tvUserName.setText(user.getScreenName());
		tvBody.setText(tweet.getBody());
		tvTimestamp.setText(tweet.getCreatedAt());
		
		return v;
	}

}