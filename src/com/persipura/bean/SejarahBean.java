package com.persipura.bean;

public class SejarahBean {
	String desc = "";
	String logo = "";
	String ketua = "";
	String stadion = "";
	String berdiri = "";
	String alamat = "";
	String julukan = "";
	String manager = "";
	String telepon = "";
	String pelatih = "";
	String nama = "";

	public String getdesc() {
		return desc;
	}

	public void setdesc(String desc) {
		if (desc.equals("null")) {
			desc = "";
		}
		this.desc = desc;
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
	public String getmanager() {
		return manager;
	}

	public void setmanager(String manager) {
		if (manager.equals("null")) {
			manager = "";
		}
		this.manager = manager;
	}
	
	public String getstadion() {
		return stadion;
	}

	public void setstadion(String stadion) {
		if (stadion.equals("null")) {
			stadion = "";
		}
		this.stadion = stadion;
	}
	
	public String getalamat() {
		return alamat;
	}

	public void setalamat(String alamat) {
		if (alamat.equals("null")) {
			alamat = "";
		}
		this.alamat = alamat;
	}
	
	public String getberdiri() {
		return berdiri;
	}

	public void setberdiri(String berdiri) {
		if (berdiri.equals("null")) {
			berdiri = "";
		}
		this.berdiri = berdiri;
	}
	
	public String getjulukan() {
		return julukan;
	}

	public void setjulukan(String julukan) {
		if (julukan.equals("null")) {
			julukan = "";
		}
		this.julukan = julukan;
	}
	
	public String getketua() {
		return ketua;
	}

	public void setketua(String ketua) {
		if (ketua.equals("null")) {
			ketua = "";
		}
		this.ketua = ketua;
	}
	
	public String gettelepon() {
		return telepon;
	}

	public void settelepon(String telepon) {
		if (telepon.equals("null")) {
			telepon = "";
		}
		this.telepon = telepon;
	}
	
	public String getpelatih() {
		return pelatih;
	}

	public void setpelatih(String pelatih) {
		if (pelatih.equals("null")) {
			pelatih = "";
		}
		this.pelatih = pelatih;
	}
	
	public String getnama() {
		return nama;
	}

	public void setnama(String nama) {
		if (nama.equals("null")) {
			nama = "";
		}
		this.nama = nama;
	}




}
