package kr.co.kncom.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AuctionList {

	@Id
	private String ind;
	@Column(length = 64)
	private String address;
	private String gubun;
	private String appraisedvalue;
	private String lowestvalue;
	private String salevalue;
	private String landarea;
	private String buildingarea;
	private String saleday;
	private String openday;
	private String closeday;
	private String result;
	private int sidogus_ind;
	private int dongs_ind;
	private int bunji1;
	private int bunji2;
	private String x;
	private String y;

	public String getInd() {
		return ind;
	}

	public void setInd(String ind) {
		this.ind = ind;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public String getAppraisedvalue() {
		return appraisedvalue;
	}

	public void setAppraisedvalue(String appraisedvalue) {
		this.appraisedvalue = appraisedvalue;
	}

	public String getLowestvalue() {
		return lowestvalue;
	}

	public void setLowestvalue(String lowestvalue) {
		this.lowestvalue = lowestvalue;
	}

	public String getSalevalue() {
		return salevalue;
	}

	public void setSalevalue(String salevalue) {
		this.salevalue = salevalue;
	}

	public String getLandarea() {
		return landarea;
	}

	public void setLandarea(String landarea) {
		this.landarea = landarea;
	}

	public String getBuildingarea() {
		return buildingarea;
	}

	public void setBuildingarea(String buildingarea) {
		this.buildingarea = buildingarea;
	}

	public String getSaleday() {
		return saleday;
	}

	public void setSaleday(String saleday) {
		this.saleday = saleday;
	}

	public String getOpenday() {
		return openday;
	}

	public void setOpenday(String openday) {
		this.openday = openday;
	}

	public String getCloseday() {
		return closeday;
	}

	public void setCloseday(String closeday) {
		this.closeday = closeday;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getSidogus_ind() {
		return sidogus_ind;
	}

	public void setSidogus_ind(int sidogus_ind) {
		this.sidogus_ind = sidogus_ind;
	}

	public int getDongs_ind() {
		return dongs_ind;
	}

	public void setDongs_ind(int dongs_ind) {
		this.dongs_ind = dongs_ind;
	}

	public int getBunji1() {
		return bunji1;
	}

	public void setBunji1(int bunji1) {
		this.bunji1 = bunji1;
	}

	public int getBunji2() {
		return bunji2;
	}

	public void setBunji2(int bunji2) {
		this.bunji2 = bunji2;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

}
