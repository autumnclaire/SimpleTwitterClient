package com.yahoo.autumnv.apps.simpletwitter.fragments;

import com.yahoo.autumnv.apps.simpletwitter.models.User;

public class UserTimelineFragment extends TweetsListFragment {

	private User user;

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
		getClient().getUserTimeline(getResponseHandler(getLastItemId(), this), user);		
	}

	public void setUser(User u) {
		this.user = u;
	}

}
