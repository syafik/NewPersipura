package com.persipura.home;

public class HomeNextMatch {
	String id = "";
	String team1 = "";
	String team2 = "";
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id.equals("null")) {
			id = "";
		}
		this.id = id;
	}

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		if (team1.equals("null")) {
			team1 = "";
		}
		this.team1 = team1;
	}
	
	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		if (team2.equals("null")) {
			team2 = "";
		}
		this.team2 = team2;
	}

}
