package com.persipura.bean;

public class AdsBean {
	String clickable = "";
	String image = "";
	String link = "";
	String ad_rank = "";
	String clickcounter = "";
	

	public String getad_rank() {
		return ad_rank;
	}

	public void setad_rank(String ad_rank) {
		if (ad_rank.equals("null")) {
			ad_rank = "";
		}
		this.ad_rank = ad_rank;
	}

	public String getclickcounter() {
		return clickcounter;
	}

	public void setclickcounter(String clickcounter) {
		if (clickcounter.equals("null")) {
			clickcounter = "";
		}
		this.clickcounter = clickcounter;
	}

	
	public String getclickable() {
		return clickable;
	}

	public void setclickable(String clickable) {
		if (clickable.equals("null")) {
			clickable = "";
		}
		this.clickable = clickable;
	}

	public String getimage() {
		return image;
	}

	public void setimage(String image) {
		if (image.equals("null")) {
			image = "";
		}
		this.image = image;
	}

	public String getlink() {
		return link;
	}

	public void setlink(String link) {
		if (link.equals("null")) {
			link = "";
		}
		this.link = link;
	}

}
