package com.yahoo.autumnv.apps.simpletwitter.fragments;

import android.os.Bundle;


public class MentionsTimeLineFragment extends TweetsListFragment {
	private long lastItemId;

	@Override
	protected void setLastItemId(long uid) {
		this.lastItemId = uid;
	}

	@Override
	protected long getLastItemId() {
		return lastItemId;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lastItemId = 0;
		populateTimeline();		
	}
	
	@Override
	protected void getTimeline() {
		getClient().getMentionsTimeline(getResponseHandler(getLastItemId(), this), getLastItemId());		
	}

}
