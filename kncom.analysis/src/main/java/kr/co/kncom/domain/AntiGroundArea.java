package kr.co.kncom.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(RealtyMultiId.class)
public class AntiGroundArea {

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
	@Column(length = 255)
	private String address;

	private float antiGroundArea; // 대지면적
	private float landArea; // 토지대장면적
	private float eachOffcialPriceArea; // 개별공시지가면적
	private float antiGroundRatioDenominator; // 대지권비율분모
	private float antiGroundAreaReplaceVal; // 대지면적대체값
	
	private String result; // 결과값
	
	private int outsideParcel; // 외필지수
	
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public int getOutsideParcel() {
		return outsideParcel;
	}

	public void setOutsideParcel(int outsideParcel) {
		this.outsideParcel = outsideParcel;
	}
	
}
