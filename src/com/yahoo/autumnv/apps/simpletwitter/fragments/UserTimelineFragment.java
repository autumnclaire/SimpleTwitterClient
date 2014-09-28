package com.yahoo.autumnv.apps.simpletwitter.fragments;

public class UserTimelineFragment extends TweetsListFragment {

	@Override
	protected void setLastItemId(long uid) {
		// TODO Auto-generated method stub

	}

	@Override
	protected long getLastItemId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void getTimeline() {
		getClient().getUserTimeline(getResponseHandler(getLastItemId(), this));		
	}

}
