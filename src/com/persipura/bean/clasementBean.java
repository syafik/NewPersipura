package com.persipura.bean;

public class clasementBean {
	String team_name = "";
	String p = "";
	String w = "";
	String d = "";
	String l = "";
	String gd = "";
	String pts = "";
	String position = "";

	public String getTeamname() {
		return team_name;
	}

	public void setTeamname(String team_name) {
		if (team_name.equals("null")) {
			team_name = "";
		}
		this.team_name = team_name;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		if (p.equals("null")) {
			p = "";
		}
		this.p = p;
	}

	public String getW() {
		return w;
	}

	public void setW(String w) {
		if (w.equals("null")) {
			w = "";
		}
		this.w = w;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		if (d.equals("null")) {
			d = "";
		}
		this.d = d;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		if (l.equals("null")) {
			l = "";
		}
		this.l = l;
	}

	public String getGD() {
		return gd;
	}

	public void setGD(String gd) {
		if (gd.equals("null")) {
			gd = "";
		}
		this.gd = gd;
	}

	public String getPts() {
		return pts;
	}

	public void setPts(String pts) {
		if (pts.equals("null")) {
			pts = "";
		}
		this.pts = pts;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		if (position.equals("null")) {
			position = "";
		}
		this.position = position;
	}

}
