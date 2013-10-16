package com.persipura.bean;

public class calenderBean {
	String id = "";
	String title = "";
	String type = "";
	String datetime = "";

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

}
