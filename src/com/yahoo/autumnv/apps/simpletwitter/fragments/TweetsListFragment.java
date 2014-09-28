package com.yahoo.autumnv.apps.simpletwitter.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.autumnv.apps.simpletwitter.EndlessScrollListener;
import com.yahoo.autumnv.apps.simpletwitter.R;
import com.yahoo.autumnv.apps.simpletwitter.TweetArrayAdapter;
import com.yahoo.autumnv.apps.simpletwitter.TwitterApplication;
import com.yahoo.autumnv.apps.simpletwitter.TwitterClient;
import com.yahoo.autumnv.apps.simpletwitter.models.Tweet;

public abstract class TweetsListFragment extends Fragment {

	private List<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private ListView lvTweets;
	private SwipeRefreshLayout swipeContainer;

	private TwitterClient client;

	protected abstract void setLastItemId(long uid);
	protected abstract long getLastItemId();
	protected abstract void getTimeline();

	public void populateTimeline() {
		if (!isNetworkAvailable()) {
			if (getLastItemId() == 0) {
				populateTimelineOffline();
			}
			return;
		}
		getTimeline();
	}


	private void populateTimelineOffline() {
		List<Tweet> retrievedTweets = Tweet.getAll();
		addAll(retrievedTweets);
		updateLastItemId();
		finishedRefreshing();
		Toast.makeText(getActivity(), "Retrieved offline data from DB", Toast.LENGTH_SHORT).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//inflate our layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		//assign our view references
		lvTweets = (ListView)v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);		
		setupEndlessScroll();
		setupPullToRefresh(v);
		setLastItemId(0);
		populateTimeline();
		//return the layout view
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);
		setClient(TwitterApplication.getRestClient());

	}
	
	

	public void addAll(List<Tweet> tweets) {
		aTweets.addAll(tweets);
	}

	protected void clearTweets() {
		aTweets.clear();
	}


	public void insertAtFront(Tweet tweet) {
		aTweets.insert(tweet, 0);		
	}
	
	protected Tweet getLastItem() {
		return aTweets.getItem(aTweets.getCount()-1);
	}

	
	public void finishedRefreshing() {
		if (swipeContainer != null) {
			swipeContainer.setRefreshing(false);
		}
	}
	
	protected void loadFromDb() {
		this.tweets = Tweet.getAll();
	}
	
	protected void setupEndlessScroll() {
		lvTweets.setOnScrollListener(getScrollListener());
	}

	
	protected void setupPullToRefresh(View v) {
		swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(getRefreshListener());
		// Configure the refreshing colors
		swipeContainer.setColorScheme(android.R.color.holo_blue_bright, 
				android.R.color.holo_green_light, 
				android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);
	}
	
	
	
	public Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	public void handleIntentData(Intent data) {
		Tweet tweet = (Tweet) data.getSerializableExtra(Tweet.TWEET_KEY);
		if (tweet != null) {
			insertAtFront(tweet);
		}
	}

	public TwitterClient getClient() {
		return client;
	}

	public void setClient(TwitterClient client) {
		this.client = client;
	}
	
	protected JsonHttpResponseHandler getResponseHandler(final long lastItemId,
			final TweetsListFragment parentContext) {
		return new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray json) {
				if (lastItemId == 0) {
					clearTweets();
				}
				List<Tweet> retrievedTweets = Tweet.fromJson(json);
				addAll(retrievedTweets);
				updateLastItemId();
				finishedRefreshing();
				Tweet.saveTweets(retrievedTweets);
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(parentContext.getActivity(), "Failed to get data from Twitter, loading from DB", Toast.LENGTH_SHORT).show();
				loadFromDb();
				finishedRefreshing();
			}
		};
	}
	
	protected OnRefreshListener getRefreshListener() {
		return new OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateTimeline();
			} 
		};
	}
	

	protected EndlessScrollListener getScrollListener() {
		return new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline();
			}
		};
	}
	
	protected void updateLastItemId() {
		Tweet lastItem = getLastItem();
		if (lastItem != null) {
			setLastItemId(lastItem.getUid());
		}
	}


}
