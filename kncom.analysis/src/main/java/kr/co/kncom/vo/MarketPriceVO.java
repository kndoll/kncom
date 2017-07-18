package kr.co.kncom.vo;

public class MarketPriceVO {

	private String bidName;
	private String date;

	// key
	private String sidogus_ind;
	private String dongs_ind;
	private String bunji1;
	private String bunji2;

	private String dong;
	private String ho;
	private String centerValue;
	private String sang;
	private String ha;
	private String area;
	private String scount;
	
	
	
	public String getBidName() {
		return bidName;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSidogus_ind() {
		return sidogus_ind;
	}

	public void setSidogus_ind(String sidogus_ind) {
		this.sidogus_ind = sidogus_ind;
	}

	public String getDongs_ind() {
		return dongs_ind;
	}

	public void setDongs_ind(String dongs_ind) {
		this.dongs_ind = dongs_ind;
	}

	public String getBunji1() {
		return bunji1;
	}

	public void setBunji1(String bunji1) {
		this.bunji1 = bunji1;
	}

	public String getBunji2() {
		return bunji2;
	}

	public void setBunji2(String bunji2) {
		this.bunji2 = bunji2;
	}

	public String getDong() {
		return dong;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}

	public String getHo() {
		return ho;
	}

	public void setHo(String ho) {
		this.ho = ho;
	}

	public String getCenterValue() {
		return centerValue;
	}

	public void setCenterValue(String centerValue) {
		this.centerValue = centerValue;
	}

	public String getSang() {
		return sang;
	}

	public void setSang(String sang) {
		this.sang = sang;
	}

	public String getHa() {
		return ha;
	}

	public void setHa(String ha) {
		this.ha = ha;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getScount() {
		return scount;
	}

	public void setScount(String scount) {
		this.scount = scount;
	}

	@Override
	public String toString() {

		String rtnVal = "dong : " + this.getDong() + "\r\n";
		rtnVal += " ho : " + this.getHo() + "\r\n";
		rtnVal += " centerValue : " + this.getCenterValue() + "\r\n";
		rtnVal += " sang : " + this.getSang() + "\r\n";
		rtnVal += " ha : " + this.getHa() + "\r\n";
		rtnVal += " area : " + this.getArea() + "\r\n";
		rtnVal += " scount : " + this.getScount();

		return rtnVal;
	}
}
