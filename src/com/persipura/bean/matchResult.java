package com.persipura.bean;

public class matchResult {
	String id = "";
	String minute = "";
	String player = "";
	String team = "";
	String type = "";
	

	public String getId() {
		return id;
	}

	public void setNid(String id) {
		if (id.equals("null")) {
			id = "";
		}
		this.id = id;
	}

	public String getMinute() {
		return minute;
	}
	
	public void setMinute(String minute) {
		if (minute.equals("null")) {
			minute = "";
		}
		this.minute = minute;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public void setPlayer(String player) {
		if (player.equals("null")) {
			player = "";
		}
		this.player = player;
	}
	
	public String getTeam() {
		return team;
	}
	
	public void setTeam(String team) {
		if (team.equals("null")) {
			team = "";
		}
		this.team = team;
	}
	
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		if (type.equals("null")) {
			type = "";
		}
		this.type = type;
	}
	
}
