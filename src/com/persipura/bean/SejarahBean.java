package com.persipura.bean;

public class SejarahBean {
	String desc = "";

	public String getdesc() {
		return desc;
	}

	public void setdesc(String desc) {
		if (desc.equals("null")) {
			desc = "";
		}
		this.desc = desc;
	}


}
