package com.yahoo.autumnv.apps.simpletwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yahoo.autumnv.apps.simpletwitter.models.User;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	private static final String STATUSES_UPDATE_JSON = "statuses/update.json";
	private static final String STATUSES_HOME_TIMELINE_JSON = "statuses/home_timeline.json";
	private static final String STATUSES_MENTIONS_TIMELINE_JSON = "statuses/mentions_timeline.json";
	private static final String STATUSES_USER_TIMELINE_JSON = "statuses/user_timeline.json";
	private static final String DEFAULT_COUNT = "25";
	private static final String COUNT = "count";
	private static final String MAX_ID = "max_id";
	private static final String STATUS = "status";
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "jMQAqAoKjbJ2Jayfbj3RNbQ7V";
	public static final String REST_CONSUMER_SECRET = "pqSJHsFBggdqJog5IPRbohSDMkP0lh0xYlDdlfSsQHwTq7RR5h";
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets";
	private static final String ACCOUNT_VERIFY_CREDENTIALS = "account/verify_credentials.json";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
	
	public void postNewTweet(AsyncHttpResponseHandler handler, String tweet) {
		String apiUrl = getApiUrl(STATUSES_UPDATE_JSON);
		RequestParams params = new RequestParams();
		params.put(STATUS, tweet);
		client.post(apiUrl,  params, handler);
	}
	
	public void getHomeTimeline(AsyncHttpResponseHandler handler, long lastId) {
		getTimeline(handler, lastId, STATUSES_HOME_TIMELINE_JSON);
	}

	public void getMentionsTimeline(
			AsyncHttpResponseHandler handler, long lastId) {
		getTimeline(handler, lastId, STATUSES_MENTIONS_TIMELINE_JSON);
	}
	
	public void getMyInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl(ACCOUNT_VERIFY_CREDENTIALS);
		client.get(apiUrl, null, handler);
	}
	
	public void getUserTimeline(AsyncHttpResponseHandler handler, User user) {
		String apiUrl = getApiUrl(STATUSES_USER_TIMELINE_JSON);
		RequestParams params = null;
		if (user != null) {
			params = new RequestParams();
			params.put("screen_name", user.getScreenName());
		}
		client.get(apiUrl, params, handler);
	}

	private void getTimeline(AsyncHttpResponseHandler handler, long lastId,
			String apiUrl) {
		RequestParams params = new RequestParams();
		if (lastId > 0) {
			params.put(MAX_ID, String.valueOf(lastId-1));

		}
		params.put(COUNT, DEFAULT_COUNT);
		client.get(getApiUrl(apiUrl), params, handler);
	}

}