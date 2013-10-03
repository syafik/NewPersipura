package com.persipura.bean;

public class mediaBean {
	String id = "";
	String created = "";
	String title = "";
	String total_view = "";
	String video_image = "";
	String video_uri = "";
	String category = "";
	String description = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String gettitle() {
		return title;
	}

	public void settitle(String title) {
		if (title.equals("null")) {
			title = "";
		}
		this.title = title;
	}

	public String getvideo_image() {
		return video_image;
	}

	public void setvideo_image(String video_image) {
		if (video_image.equals("null")) {
			video_image = "";
		}
		this.video_image = video_image;
	}

	public String gettotal_view() {
		return total_view;
	}

	public void settotal_view(String total_view) {
		if (total_view.equals("null")) {
			total_view = "";
		}
		this.total_view = total_view;
	}

	public String getvideo_uri() {
		return video_uri;
	}

	public void setvideo_uri(String video_uri) {
		if (video_uri.equals("null")) {
			video_uri = "";
		}
		this.video_uri = video_uri;
	}

	public String getdescription() {
		return description;
	}

	public void setdescription(String description) {
		if (description.equals("null")) {
			description = "";
		}
		this.description = description;
	}

}
