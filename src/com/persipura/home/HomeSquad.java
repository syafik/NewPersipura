package com.persipura.home;

public class HomeSquad {
	String id = "";
	String nama_lengkap = "";
	String posisi = "";
	String no_punggung = "";
	String warganegara = "";
	String age = "";
	String foto = "";
	String nama = "";
	String berat_badan = "";
	String tinggi_badan = "";
	String tanggal_lahir = "";
	String tahun_aktif = "";
	String tim_staf = "";
	String info = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id.equals("null")) {
			id = "";
		}
		this.id = id;
	}

	public String getNamaLengkap() {
		return nama_lengkap;
	}

	public void setNamaLengkap(String nama_lengkap) {
		if (nama_lengkap.equals("null")) {
			nama_lengkap = "";
		}
		this.nama_lengkap = nama_lengkap;
	}

	public String getposisi() {
		return posisi;
	}

	public void setposisi(String posisi) {
		if (posisi.equals("null")) {
			posisi = "";
		}
		this.posisi = posisi;
	}

	public String getno_punggung() {
		return no_punggung;
	}

	public void setno_punggung(String no_punggung) {
		if (no_punggung.equals("null")) {
			no_punggung = "";
		}
		this.no_punggung = no_punggung;
	}

	public String getwarganegara() {
		return warganegara;
	}

	public void setwarganegara(String warganegara) {
		if (warganegara.equals("null")) {
			warganegara = "";
		}
		this.warganegara = warganegara;
	}

	public String getage() {
		return age;
	}

	public void setage(String age) {
		if (age.equals("null")) {
			age = "";
		}
		this.age = age;
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

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		if (nama.equals("null")) {
			nama = "";
		}
		this.nama = nama;
	}

	public String getberat_badan() {
		return berat_badan;
	}

	public void setberat_badan(String berat_badan) {
		if (berat_badan.equals("null")) {
			berat_badan = "";
		}
		this.berat_badan = berat_badan;

	}

	public String gettinggi_badan() {
		return tinggi_badan;
	}

	public void settinggi_badan(String tinggi_badan) {
		if (tinggi_badan.equals("null")) {
			tinggi_badan = "";
		}
		this.tinggi_badan = tinggi_badan;

	}

	public String gettanggal_lahir() {
		return tanggal_lahir;
	}

	public void settanggal_lahir(String tanggal_lahir) {
		if (tanggal_lahir.equals("null")) {
			tanggal_lahir = "";
		}
		this.tanggal_lahir = tanggal_lahir;

	}

	public String gettahun_aktif() {
		return tahun_aktif;
	}

	public void settahun_aktif(String tahun_aktif) {
		if (tahun_aktif.equals("null")) {
			tahun_aktif = "";
		}
		this.tahun_aktif = tahun_aktif;

	}

	public String gettim_staf() {
		return tim_staf;
	}

	public void settim_staf(String tim_staf) {
		if (tim_staf.equals("null")) {
			tim_staf = "";
		}
		this.tim_staf = tim_staf;

	}

	public String getinfo() {
		return info;
	}

	public void setinfo(String info) {
		if (info.equals("null")) {
			info = "";
		}
		this.info = info;

	}

}
