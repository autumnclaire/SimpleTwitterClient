package com.yahoo.autumnv.apps.simpletwitter.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "users")
public class User extends Model implements Serializable{
	private static final String DESCRIPTION2 = "description";
	private static final String FOLLOWERS_COUNT = "followers_count";
	private static final String FRIENDS_COUNT = "friends_count";
	private static final String PROFILE_IMAGE_URL = "profile_image_url";
	private static final String SCREEN_NAME = "screen_name";
	private static final String ID = "id";
	private static final String NAME = "name";

	/**
	 * 
	 */
	private static final long serialVersionUID = 2585053043423912775L;
	
	//we want this to be unique, but I'm getting wierd errors without it, so leaving it this way so that the tweets show up.
    @Column(name = "uid")//, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
    
    @Column(name = NAME)
	private String name;
    
    @Column(name = SCREEN_NAME)
	private String screenName;
    
    @Column(name = PROFILE_IMAGE_URL)
	private String profileImageUrl;
    
    @Column(name = FOLLOWERS_COUNT)
    private int followersCount;

    @Column(name = FRIENDS_COUNT)
    private int friendsCount;
    
    @Column(name = DESCRIPTION2)
    private String description;

    
	public User() {
		super();
	}
	
	// Used to return items from another table based on the foreign key
    public List<User> items() {
        return getMany(User.class, "user");
    }
	
	public static User fromJson(JSONObject jsonObject) {
		User u = new User();
		try {
			u.name = jsonObject.getString(NAME);
			u.uid = jsonObject.getLong(ID);
			u.screenName = jsonObject.getString(SCREEN_NAME);
			u.profileImageUrl = jsonObject.getString(PROFILE_IMAGE_URL);
			u.followersCount = jsonObject.getInt(FOLLOWERS_COUNT);
			u.friendsCount = jsonObject.getInt(FRIENDS_COUNT);
			u.description = jsonObject.getString(DESCRIPTION2);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		u.save();
		return u;
	}
	

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public int getFollowersCount() {
		return followersCount;
	}
	

	public int getFriendsCount() {
		return friendsCount;
	}
	
	public String getDescription() {
		return description;
	}

	

}
