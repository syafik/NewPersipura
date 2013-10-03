package com.persipura.bean;

public class SearchBean {
	String id = "";
	String node_title = "";
	String type = "";
	String timeago = "";
	String timestamp = "";
	String foto = "";

	public String getid() {
		return id;
	}

	public void setid(String id) {
		if (id.equals("null")) {
			id = "";
		}
		this.id = id;
	}

	public String getnode_title() {
		return node_title;
	}

	public void setnode_title(String node_title) {
		if (node_title.equals("null")) {
			node_title = "";
		}
		this.node_title = node_title;
	}

	public String gettype() {
		return type;
	}

	public void settype(String type) {
		if (type.equals("null")) {
			type = "";
		}
		this.type = type;
	}

	public String gettimeago() {
		return timeago;
	}

	public void settimeago(String timeago) {
		if (timeago.equals("null")) {
			timeago = "";
		}
		this.timeago = timeago;
	}

	public String gettimestamp() {
		return timestamp;
	}

	public void settimestamp(String timestamp) {
		if (timestamp.equals("null")) {
			timestamp = "";
		}
		this.timestamp = timestamp;
	}

	public String getfoto() {
		return foto;
	}

	public void setfoto(String foto) {
		if (foto.equals("null")) {
			foto = "";
		}
		this.foto = foto;
	}
}
