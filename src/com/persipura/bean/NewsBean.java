package com.persipura.bean;

public class NewsBean {
	String nid = "";
	String created = "";
	String category = "";
	String title = "";
	String img_uri = "";
	String teaser = "";
	
	
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		if (nid.equals("null")) {
			nid = "";
		}
		this.nid = nid;
	}
	
	public String getcreated() {
		return created;
	}

	public void setcreated(String created) {
		if (created.equals("null")) {
			created = "";
		}
		this.created = created;
	}
	
	public String getcategory() {
		return category;
	}

	public void setcategory(String category) {
		if (category.equals("null")) {
			category = "";
		}
		this.category = category;
	}
	
	public String gettitle() {
		return title;
	}

	public void settitle(String title) {
		if (title.equals("null")) {
			title = "";
		}
		this.title = title;
	}
	
	public String getimg_uri() {
		return img_uri;
	}

	public void setimg_uri(String img_uri) {
		if (img_uri.equals("null")) {
			img_uri = "";
		}
		this.img_uri = img_uri;
	}
	
	public String getteaser() {
		return teaser;
	}

	public void setteaser(String teaser) {
		if (teaser.equals("null")) {
			teaser = "";
		}
		this.teaser = teaser;
	}
	
}
