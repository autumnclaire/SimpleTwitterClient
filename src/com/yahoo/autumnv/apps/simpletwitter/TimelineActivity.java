package com.yahoo.autumnv.apps.simpletwitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.autumnv.apps.simpletwitter.models.Tweet;


public class TimelineActivity extends Activity {
	private static final int REQUEST_CODE = 0;
	private TwitterClient client;
	private List<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private long lastItemId;
	private SwipeRefreshLayout swipeContainer;

	
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
		lvTweets = (ListView)findViewById(R.id.lvTweets);
		tweets = new ArrayList<>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);		
		setupEndlessScroll();
		setupPullToRefresh();
		populateTimeline(0);

	}

	private void loadFromDb() {
		this.tweets = Tweet.getAll();
	}

	private void setupEndlessScroll() {
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline(lastItemId);
			}
		});
	}

	private void setupPullToRefresh() {
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            	
                populateTimeline(0);
            } 
        });
        // Configure the refreshing colors
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright, 
                android.R.color.holo_green_light, 
                android.R.color.holo_orange_light, 
                android.R.color.holo_red_light);
	}


	public void populateTimeline(final long itemId) {
		if (!isNetworkAvailable()) {
			if (itemId == 0) {
				populateTimelineOffline();
			}
			return;
		}
		final TimelineActivity parentContext = this;
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONArray json) {
				if (itemId == 0) {
					aTweets.clear();
				}
				List<Tweet> retrievedTweets = Tweet.fromJson(json);
				aTweets.addAll(retrievedTweets);
				lastItemId = aTweets.getItem(aTweets.getCount()-1).getUid();
                swipeContainer.setRefreshing(false);
                Tweet.saveTweets(retrievedTweets);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(parentContext, "Failed to get data, loading from DB", Toast.LENGTH_SHORT).show();
				loadFromDb();
			}
		}, itemId);
	}
	
	private void populateTimelineOffline() {
		List<Tweet> retrievedTweets = Tweet.getAll();
		aTweets.addAll(retrievedTweets);
		lastItemId = tweets.get(tweets.size()-1).getUid();
		swipeContainer.setRefreshing(false);
		Toast.makeText(this, "got offline data", Toast.LENGTH_SHORT).show();
	}

	public void onComposeAction(MenuItem item) {
    	Intent i = new Intent(this, ComposeActivity.class);
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
	
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
	
}
