package kr.co.kncom.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

@Data
@Entity
@IdClass(AreaRatioMultiId.class)
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

	private float arCalcTotalArea; // 용적율산정연면적
	private float totalArea; // 연면적
	private int underGroundFloor;
	private float groundEachFloorSum; // 지상층별개요합산
	private float groundPrivatePublicSum; // 지상전유공용합산
	private float areaRatio;
	private float outsideParcel; // 외필지
	private float antiGroundArea; // 대지면적
	private float landArea; // 토지대장면적
	private Long eachOffcialPrice; // 개별공시지가
	private float antiGroundRatioDenominator; // 대지권비율분모
	private float areaRatioCalc; // 용적율 계산
	private float areaRatioCalcTotalAreaReplaceVal; // 용적율산정연면적대체값
	private float antiGroundAreaReplaceVal; // 대지면적대체값

	private String result;
}
