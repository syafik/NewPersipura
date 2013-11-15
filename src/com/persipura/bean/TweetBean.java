package com.persipura.bean;

public class TweetBean {
	String tweet = "";
	String time = "";
	String img = "";
	String name = "";
	String username = "";
	
	public String getUsername() {
		return username;
		
	}

	public void setUsername(String username) {
		if (username.equals("null")) {
			username = "";
		}
		this.username = username;
	}

	public String getName() {
		return name;
		
	}

	public void setName(String name) {
		if (name.equals("null")) {
			name = "";
		}
		this.name = name;
	}

	
	public String getUserImg() {
		return img;
		
	}

	public void setUserImg(String img) {
		if (img.equals("null")) {
			img = "";
		}
		this.img = img;
	}
	
	public String getTime() {
		return time;
		
	}

	public void setTime(String time) {
		if (time.equals("null")) {
			time = "";
		}
		this.time = time;
	}
	
	public String getTweet() {
		return tweet;
		
	}

	public void setTweet(String tweet) {
		if (tweet.equals("null")) {
			tweet = "";
		}
		this.tweet = tweet;
	}

}
