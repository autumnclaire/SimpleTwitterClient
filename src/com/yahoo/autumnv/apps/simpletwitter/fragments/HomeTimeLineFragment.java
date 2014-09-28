package com.yahoo.autumnv.apps.simpletwitter.fragments;

import android.os.Bundle;

public class HomeTimeLineFragment extends TweetsListFragment {

	private long lastItemId;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lastItemId = 0;
		populateTimeline();		
	}
	
	@Override
	protected void getTimeline() {
		getClient().getHomeTimeline(getResponseHandler(getLastItemId(), this), getLastItemId());		
	}



	@Override
	public long getLastItemId() {
		return lastItemId;
	}

	@Override
	public void setLastItemId(long lastItemId) {
		this.lastItemId = lastItemId;
	}





}