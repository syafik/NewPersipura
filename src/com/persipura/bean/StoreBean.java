package com.persipura.bean;

public class StoreBean {
	String name = "";
	String address = "";
	String logo = "";
	String phone = "";
	
	public String getname() {
		return name;
		
	}

	public void setname(String name) {
		if (name.equals("null")) {
			name = "";
		}
		this.name = name;
	}

	public String getaddress() {
		return address;
		
	}

	public void setaddress(String address) {
		if (address.equals("null")) {
			address = "";
		}
		this.address = address;
	}
	
	public String getlogo() {
		return logo;
		
	}

	public void setlogo(String logo) {
		if (logo.equals("null")) {
			logo = "";
		}
		this.logo = logo;
	}
	
	public String getphone() {
		return phone;
		
	}

	public void setphone(String phone) {
		if (phone.equals("null")) {
			phone = "";
		}
		this.phone = phone;
	}



}
