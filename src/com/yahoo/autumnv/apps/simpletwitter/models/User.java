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
	/**
	 * 
	 */
	private static final long serialVersionUID = 2585053043423912775L;
	
    @Column(name = "uid")//, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
    
    @Column(name = "name")
	private String name;
    
    @Column(name = "screen_name")
	private String screenName;
    
    @Column(name = "profile_image_url")
	private String profileImageUrl;
	
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
			u.name = jsonObject.getString("name");
			u.uid = jsonObject.getLong("id");
			u.screenName = jsonObject.getString("screen_name");
			u.profileImageUrl = jsonObject.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		u.save();
		return u;
	}
	
//	public static List<User> getAll(long uid) {
//        // This is how you execute a query
//        return new Select()
//          .from(User.class)
//          .where("uid = ?", uid)
//          .orderBy("name ASC")
//          .execute();
//    }

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

}
