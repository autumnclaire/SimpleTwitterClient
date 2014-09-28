package com.yahoo.autumnv.apps.simpletwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.yahoo.autumnv.apps.simpletwitter.fragments.HomeTimeLineFragment;
import com.yahoo.autumnv.apps.simpletwitter.fragments.MentionsTimeLineFragment;
import com.yahoo.autumnv.apps.simpletwitter.fragments.TweetsListFragment;
import com.yahoo.autumnv.apps.simpletwitter.listeners.SupportFragmentTabListener;


public class TimelineActivity extends ActionBarActivity {
	private TweetsListFragment tweetsListFragment = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
        setupTabs();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        getMenuInflater().inflate(R.menu.tweets, menu);
        return true;
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == ComposeActivity.REQUEST_CODE) {
			((HomeTimeLineFragment)getSupportFragmentManager().findFragmentByTag("HomeTimeLineFragment")).handleIntentData(data);
		}
	}

	public void onComposeAction(MenuItem item) {
		Intent i = new Intent(this, ComposeActivity.class);
		startActivityForResult(i, ComposeActivity.REQUEST_CODE);
	}
	
	public void onProfileView(MenuItem item) {
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
		
	}
	
	private void setupTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimeLineFragment")
			.setTabListener(
				new SupportFragmentTabListener<HomeTimeLineFragment>(R.id.flContainer, this, "home",
						HomeTimeLineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimeLineFragment")
			.setTabListener(
			    new SupportFragmentTabListener<MentionsTimeLineFragment>(R.id.flContainer, this, "mentions",
			    		MentionsTimeLineFragment.class));

		actionBar.addTab(tab2);
	}

}
