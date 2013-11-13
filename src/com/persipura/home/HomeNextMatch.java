package com.persipura.home;

public class HomeNextMatch {
	String id = "";
	String team1 = "";
	String team2 = "";
	String team1Logo = "";
	String team2Logo = "";
	String date = "";
	String time = "";
	String league = "";
	
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
	
	
	public String getdate() {
		return date;
	}

	public void setdate(String date) {
		if (date.equals("null")) {
			date = "";
		}
		this.date = date;
	}
	
	public String gettime() {
		return time;
	}

	public void settime(String time) {
		if (time.equals("null")) {
			time = "";
		}
		this.time = time;
	}

	public String getleague() {
		return league;
	}

	public void setleague(String league) {
		if (league.equals("null")) {
			league = "";
		}
		this.league = league;
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
	
	public String getTeam1Logo() {
		return team1Logo;
	}

	public void setTeam1Logo(String team1Logo) {
		if (team1Logo.equals("null")) {
			team1Logo = "";
		}
		this.team1Logo = team1Logo;
	}
	
	public String getTeam2Logo() {
		return team2Logo;
	}

	public void setTeam2Logo(String team2Logo) {
		if (team2Logo.equals("null")) {
			team2Logo = "";
		}
		this.team2Logo = team2Logo;
	}

	
	
}
