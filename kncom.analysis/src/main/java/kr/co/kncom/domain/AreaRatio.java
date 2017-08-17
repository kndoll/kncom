package kr.co.kncom.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(RealtyMultiId.class)
public class AreaRatio {

	@Id
	private String step;
	@Id
	private int si;
	@Id
	private int gu;
	@Id
	private int dong;
	@Id
	private int jibun1;
	@Id
	private int jibun2;

	private float arCalcTotalArea; // 용적률산정연면적
	private float totalArea; // 연면적
	private int underGroundFloor; // 지하층수
	private float groundEachFloorSum; // 지상층별개요합산
	private float groundPrivatePublicSum; // 지상전유공용합산
	private float areaRatio; // 용적률
	private int outsideParcel; // 외필지수
	private float antiGroundArea; // 대지면적
	private float landArea; // 토지대장면적
	private float eachOffcialPriceArea; // 개별공시지가면적
	private float antiGroundRatioDenominator; // 대지권비율분모
	private float areaRatioCalc; // 용적률 계산
	private float areaRatioCalcTotalAreaReplaceVal; // 용적률산정연면적대체값
	private float antiGroundAreaReplaceVal; // 대지면적대체값

	private String result;

	// 주소추가
	@Column(length = 255)
	private String address;
	@Column(length = 255)
	private String roadAddress;

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public int getSi() {
		return si;
	}

	public void setSi(int si) {
		this.si = si;
	}

	public int getGu() {
		return gu;
	}

	public void setGu(int gu) {
		this.gu = gu;
	}

	public int getDong() {
		return dong;
	}

	public void setDong(int dong) {
		this.dong = dong;
	}

	public int getJibun1() {
		return jibun1;
	}

	public void setJibun1(int jibun1) {
		this.jibun1 = jibun1;
	}

	public int getJibun2() {
		return jibun2;
	}

	public void setJibun2(int jibun2) {
		this.jibun2 = jibun2;
	}

	public float getArCalcTotalArea() {
		return arCalcTotalArea;
	}

	public void setArCalcTotalArea(float arCalcTotalArea) {
		this.arCalcTotalArea = arCalcTotalArea;
	}

	public float getTotalArea() {
		return totalArea;
	}

	public void setTotalArea(float totalArea) {
		this.totalArea = totalArea;
	}

	public int getUnderGroundFloor() {
		return underGroundFloor;
	}

	public void setUnderGroundFloor(int underGroundFloor) {
		this.underGroundFloor = underGroundFloor;
	}

	public float getGroundEachFloorSum() {
		return groundEachFloorSum;
	}

	public void setGroundEachFloorSum(float groundEachFloorSum) {
		this.groundEachFloorSum = groundEachFloorSum;
	}

	public float getGroundPrivatePublicSum() {
		return groundPrivatePublicSum;
	}

	public void setGroundPrivatePublicSum(float groundPrivatePublicSum) {
		this.groundPrivatePublicSum = groundPrivatePublicSum;
	}

	public float getAreaRatio() {
		return areaRatio;
	}

	public void setAreaRatio(float areaRatio) {
		this.areaRatio = areaRatio;
	}

	public int getOutsideParcel() {
		return outsideParcel;
	}

	public void setOutsideParcel(int outsideParcel) {
		this.outsideParcel = outsideParcel;
	}

	public float getAntiGroundArea() {
		return antiGroundArea;
	}

	public void setAntiGroundArea(float antiGroundArea) {
		this.antiGroundArea = antiGroundArea;
	}

	public float getLandArea() {
		return landArea;
	}

	public void setLandArea(float landArea) {
		this.landArea = landArea;
	}

	public float getEachOffcialPriceArea() {
		return eachOffcialPriceArea;
	}

	public void setEachOffcialPriceArea(float eachOffcialPriceArea) {
		this.eachOffcialPriceArea = eachOffcialPriceArea;
	}

	public float getAntiGroundRatioDenominator() {
		return antiGroundRatioDenominator;
	}

	public void setAntiGroundRatioDenominator(float antiGroundRatioDenominator) {
		this.antiGroundRatioDenominator = antiGroundRatioDenominator;
	}

	public float getAreaRatioCalc() {
		return areaRatioCalc;
	}

	public void setAreaRatioCalc(float areaRatioCalc) {
		this.areaRatioCalc = areaRatioCalc;
	}

	public float getAreaRatioCalcTotalAreaReplaceVal() {
		return areaRatioCalcTotalAreaReplaceVal;
	}

	public void setAreaRatioCalcTotalAreaReplaceVal(float areaRatioCalcTotalAreaReplaceVal) {
		this.areaRatioCalcTotalAreaReplaceVal = areaRatioCalcTotalAreaReplaceVal;
	}

	public float getAntiGroundAreaReplaceVal() {
		return antiGroundAreaReplaceVal;
	}

	public void setAntiGroundAreaReplaceVal(float antiGroundAreaReplaceVal) {
		this.antiGroundAreaReplaceVal = antiGroundAreaReplaceVal;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRoadAddress() {
		return roadAddress;
	}

	public void setRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}

}
