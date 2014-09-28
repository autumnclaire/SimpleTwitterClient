package com.yahoo.autumnv.apps.simpletwitter;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yahoo.autumnv.apps.simpletwitter.fragments.UserTimelineFragment;
import com.yahoo.autumnv.apps.simpletwitter.models.User;

public class ProfileActivity extends FragmentActivity {

	private UserTimelineFragment userTimelineFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		Intent i = getIntent();
		User user = (User)i.getSerializableExtra("user");
		userTimelineFragment = (UserTimelineFragment) getFragmentManager().findFragmentById(R.id.fragmentUserTimeline);
		userTimelineFragment.setUser(user);
		loadProfileInfo(user);
	}

	private void loadProfileInfo(User user) {
		if (user == null) {
			TwitterApplication.getRestClient().getMyInfo(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					User u = User.fromJson(json);
					loadProfileResults(u);
				}

				
			});
		} else {
			loadProfileResults(user);
		}
	}
	
	private void loadProfileResults(User u) {
		getActionBar().setTitle( "@" + u.getScreenName());
		populateProfileHeader(u);
		populateProfileList(u);
	}

	private void populateProfileList(User u) {
		userTimelineFragment.populateTimeline();
	}

	private void populateProfileHeader(User u) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
		
		tvName.setText(u.getName());
		tvTagline.setText(u.getDescription());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Following");
		
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
		
	}
}
