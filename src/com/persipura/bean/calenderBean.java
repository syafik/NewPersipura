package com.persipura.bean;

public class calenderBean {
	String id = "";
	String title = "";
	String type = "";
	String datetime = "";
	String league = "";
	String HTeam = "";
	String ATeam = "";
	String AGoal = "";
	String HGoal = "";
	
	public String getAGoal() {
		return AGoal;
	}

	public void setAGoal(String AGoal) {
		if (AGoal.equals("null")) {
			AGoal = "";
		}
		this.AGoal = AGoal;
	}
	
	public String getHGoal() {
		return HGoal;
	}

	public void setHGoal(String HGoal) {
		if (HGoal.equals("null")) {
			HGoal = "";
		}
		this.HGoal = HGoal;
	}

	
	public String getHTeam() {
		return HTeam;
	}

	public void setHTeam(String HTeam) {
		if (HTeam.equals("null")) {
			HTeam = "";
		}
		this.HTeam = HTeam;
	}
	
	public String getATeam() {
		return ATeam;
	}

	public void setATeam(String ATeam) {
		if (ATeam.equals("null")) {
			ATeam = "";
		}
		this.ATeam = ATeam;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id.equals("null")) {
			id = "";
		}
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title.equals("null")) {
			title = "";
		}
		this.title = title;
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

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		if (datetime.equals("null")) {
			datetime = "";
		}
		this.datetime = datetime;
	}
	
	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		if (league.equals("null")) {
			league = "";
		}
		this.league = league;
	}

}
