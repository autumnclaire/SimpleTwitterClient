package com.yahoo.autumnv.apps.simpletwitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.autumnv.apps.simpletwitter.models.Tweet;

public class TimelineActivity extends Activity {
	private static final int REQUEST_CODE = 0;
	private TwitterClient client;
	private List<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private long lastItemId;
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		lastItemId = 0;
		populateTimeline();
		lvTweets = (ListView)findViewById(R.id.lvTweets);
		tweets = new ArrayList<>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline();
			}
		});
	}


	public void populateTimeline() {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONArray json) {
				aTweets.addAll(Tweet.fromJson(json));
				lastItemId = aTweets.getItem(aTweets.getCount()-1).getUid();
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		}, lastItemId);
	}
	
	public void onComposeAction(MenuItem item) {
//    	Toast.makeText(this, "Settings icon clicked", Toast.LENGTH_SHORT).show();
    	//Create Intent
    	Intent i = new Intent(this, ComposeActivity.class);
		//pass any arguments
    	
    	//execute Intent startActivityForResult
    	startActivityForResult(i, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	     // Extract name value from result extras
	     Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
	     if (tweet != null) {
	    	 aTweets.insert(tweet, 0);
	     }
	  }
	}
	
}
