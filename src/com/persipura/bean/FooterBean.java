package com.persipura.bean;

public class FooterBean {
	String clickable = "";
	String footer_logo = "";
	String link = "";
	

	public String getclickable() {
		return clickable;
	}

	public void setclickable(String clickable) {
		if (clickable.equals("null")) {
			clickable = "";
		}
		this.clickable = clickable;
	}

	public String getfooter_logo() {
		return footer_logo;
	}

	public void setfooter_logo(String footer_logo) {
		if (footer_logo.equals("null")) {
			footer_logo = "";
		}
		this.footer_logo = footer_logo;
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
