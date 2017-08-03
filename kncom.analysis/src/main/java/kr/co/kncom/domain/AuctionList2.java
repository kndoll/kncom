package kr.co.kncom.domain;

import javax.persistence.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

@Entity
public class AuctionList2 {

	@Id
	private String ind;
	
	@Column
	private String address;
	
	@Column(length=100)
	private String gubun;
	
	@Column
	private Long appraisedValue;
	
	@Column
	private Long lowestValue;
	
	@Column
	private Long saleValue;
	
	@Column
	private float landArea;
	
	private float landRate;
	
	@Column
	private float buildingArea;
	
	private float buildingRate;
	
	private float buildingOther;
	
	private Date saleDay;
	
	private Date openDay;
	
	private Date closeDay;
	
	private Date allotDay;
	
	@Column(length=8)
	private String result;
	
	@Column(columnDefinition = "TEXT")
	private String attention;
	
	@Column
	private int si_ind;
	
	@Column
	private int sidogus_ind;
	
	@Column
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

	public Long getAppraisedValue() {
		return appraisedValue;
	}

	public void setAppraisedValue(Long appraisedValue) {
		this.appraisedValue = appraisedValue;
	}

	public Long getLowestValue() {
		return lowestValue;
	}

	public void setLowestValue(Long lowestValue) {
		this.lowestValue = lowestValue;
	}

	public Long getSaleValue() {
		return saleValue;
	}

	public void setSaleValue(Long saleValue) {
		this.saleValue = saleValue;
	}

	public float getLandArea() {
		return landArea;
	}

	public void setLandArea(float landArea) {
		this.landArea = landArea;
	}

	public float getLandRate() {
		return landRate;
	}

	public void setLandRate(float landRate) {
		this.landRate = landRate;
	}

	public float getBuildingArea() {
		return buildingArea;
	}

	public void setBuildingArea(float buildingArea) {
		this.buildingArea = buildingArea;
	}

	public float getBuildingRate() {
		return buildingRate;
	}

	public void setBuildingRate(float buildingRate) {
		this.buildingRate = buildingRate;
	}

	public float getBuildingOther() {
		return buildingOther;
	}

	public void setBuildingOther(float buildingOther) {
		this.buildingOther = buildingOther;
	}

	public Date getSaleDay() {
		return saleDay;
	}

	public void setSaleDay(Date saleDay) {
		this.saleDay = saleDay;
	}

	public Date getOpenDay() {
		return openDay;
	}

	public void setOpenDay(Date openDay) {
		this.openDay = openDay;
	}

	public Date getCloseDay() {
		return closeDay;
	}

	public void setCloseDay(Date closeDay) {
		this.closeDay = closeDay;
	}

	public Date getAllotDay() {
		return allotDay;
	}

	public void setAllotDay(Date allotDay) {
		this.allotDay = allotDay;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public int getSi_ind() {
		return si_ind;
	}

	public void setSi_ind(int si_ind) {
		this.si_ind = si_ind;
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
