package com.persipura.bean;

public class imageBean {
	String id = "";
	String created = "";
	String total_views = "";
	String title = "";
	String picture_url = "";
	String picture_title = "";
	String category = "";

	public String getId() {
		return id;
	}

	public void setNid(String id) {
		if (id.equals("null")) {
			id = "";
		}
		this.id = id;
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
	
	public String getTotalViews() {
		return total_views;
	}

	public void setTotalViews(String total_views) {
		if (total_views.equals("null")) {
			total_views = "";
		}
		this.total_views = total_views;
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

	public String getpictureUrl() {
		return picture_url;
	}

	public void setimg_uri(String picture_url) {
		if (picture_url.equals("null")) {
			picture_url = "";
		}
		this.picture_url = picture_url;
	}

	public String getPictureTitle() {
		return picture_title;
	}

	public void setteaser(String picture_title) {
		if (picture_title.equals("null")) {
			picture_title = "";
		}
		this.picture_title = picture_title;
	}
}
