package kr.co.kncom.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kncom.repository.JttypelistRepository;
import kr.co.kncom.util.FileUtil;
import kr.co.kncom.util.FormatUtil;

@Component
public class RealtyStandardData {
	
	// properties로 추출해야 함.
	private final String[] totalPyojebuFileArr = { "X:\\201706lobig\\", "daejang_totalpyojebu.dat" }; // 총괄표제부
	private final String[] jygyAreaFileArr = { "X:\\201706lobig\\", "daejang_jygyarea.dat" }; // 전유공유
	
	@Autowired
	private JttypelistRepository jttypelist;
	
	/**
	 * 용적률 재계산 대상 데이터 인지 확인
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean isDasedae(String[] data) throws Exception {

		boolean rtnVal = false;

		// #STEP1. 주용도 코드가 "02000" 인지 확인
		if (data[34].equals("02000")) {

			// #STEP2. 해당 지번의 총괄표제부 파일이 존재하는지 확인
			// 파일 위치 계산
			StringBuilder jibunLocation = new StringBuilder();
			// jibunLocation.append("F:\\201704lobig\\1\\");
			jibunLocation.append("1");
			jibunLocation.append("\\");
			jibunLocation.append(data[8]);
			jibunLocation.append("\\");
			jibunLocation.append(data[9]);
			jibunLocation.append("\\");
			jibunLocation.append(Integer.parseInt(data[11]));
			jibunLocation.append("\\");
			jibunLocation.append(Integer.parseInt(data[12]));
			jibunLocation.append("\\");

			// 총괄표제부 파일 존재 확인
			if (FileUtil.isExistFile(
					this.totalPyojebuFileArr[0] + jibunLocation.toString() + this.totalPyojebuFileArr[1])) {
				// #STEP3. 해당지번의 전유공용면적 파일의 전유공용코드가 1이고,
				// 주용도코드가 2001인 데이터가 1개 이상 존재하는지 확인

				List<String[]> _jygyAreaList = FileUtil.readFileToStringArrayList(
						this.jygyAreaFileArr[0] + jibunLocation.toString() + this.jygyAreaFileArr[1], "|");

				boolean isApt = false;

				for (String[] _arr : _jygyAreaList) {
					if (_arr[26].equals("1") && _arr[34].equals("02001")) {

						isApt = true;
						break;
					}
				}

				if (!isApt) { // 아파트가 아닐경우
					// jttypelist 테이블(도시형생활주택)에서 존재여부 확인
					rtnVal = isDasedaeFromCityTypeHouse(data, jibunLocation);
				}

			} else {
				rtnVal = isDasedaeFromCityTypeHouse(data, jibunLocation);
			}

		} else {
			rtnVal = false;
		}

		return rtnVal;
	}

	private boolean isDasedaeFromCityTypeHouse(String[] data, StringBuilder jibunLocation) throws IOException {

		boolean rtnVal = false;

		boolean isExistOneRoom = isExistOneRoom(Integer.parseInt(data[8]), Integer.parseInt(data[9]),
				Integer.parseInt(data[11]), Integer.parseInt(data[12]));
		String jygyFilePath = this.jygyAreaFileArr[0] + jibunLocation.toString() + jygyAreaFileArr[1];

		rtnVal = calcDasedaeFromFloor(jygyFilePath, isExistOneRoom);

		return rtnVal;
	}

	/**
	 * 해당 지번이 도시형 생활주택인지 체크
	 * 
	 * @param jibunArr
	 *            지번
	 * @return
	 */
	private boolean isExistOneRoom(int sidogus_ind, int dongs_ind, int bunji1, int bunji2) {

		boolean rtnVal = false;

		int count = jttypelist.countByJttypelist(sidogus_ind, dongs_ind, bunji1, bunji2);

		if (count > 0) {
			rtnVal = true;
		}

		return rtnVal;
	}

	/**
	 * 전유공용면적파일에서 주용도코드와 층수로 다세대 건물인지 확인한다.
	 * 
	 * @param filePath
	 *            전유공용면적 파일 위치
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean calcDasedaeFromFloor(String filePath, boolean isExistOneRoom) throws IOException {

		boolean rtnVal = false;

		List<String[]> _jygyAreaList = FileUtil.readFileToStringArrayList(filePath, "|");

		Set<String> floorSet = new HashSet<String>();

		for (String[] _arr : _jygyAreaList) {
			if (_arr[26].equals("1") && !_arr[24].equals("지하")
					&& (_arr[34].equals("02002") || _arr[34].equals("02003"))) {
				floorSet.add(_arr[25]);
			}
		}

		if (isExistOneRoom) {
			if (floorSet.size() > 0) {
				rtnVal = true;
			}
		} else {
			if (floorSet.size() > 0 && floorSet.size() < 5) {
				rtnVal = true;
			}
		}

		return rtnVal;
	}

	

	/**
	 * 층별 개요 합산을 리턴한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public float getGroundEachFloorSum(String filePath) {

		float rtnAntiGroundArea = 0f;

		List<String[]> floorArrList;
		try {
			floorArrList = FileUtil.readFileToStringArrayList(filePath, "|");
			int floor = 0;

			for (String[] _arr : floorArrList) {
				floor = Integer.parseInt(_arr[18]);
				if (floor >= 20 && floor < 30) {
					if (!_arr[27].contains("제외") || !_arr[27].contains("차")) {
						rtnAntiGroundArea += Float.parseFloat(_arr[28]);
					}
				}
			}
		} catch (IOException | NumberFormatException e) {
		}

		return FormatUtil.round(rtnAntiGroundArea);
	}

	/**
	 * 지상전유공용면적 합산을 리턴한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public float getGroundPrivatePublicSum(String filePath) {

		float rtnAntiGroundArea = 0f;

		List<String[]> jygyArrList;
		try {
			jygyArrList = FileUtil.readFileToStringArrayList(filePath, "|");
		
			int floor = 0;
			
			for (String[] _arr : jygyArrList) {
				// StringUtil.printIndexData(_arr, "전유공용구분");
				if (_arr[23].length() > 0) {
					floor = Integer.parseInt(_arr[23]);
				}
				
				if ((floor >= 20 && floor < 30) && !_arr[36].contains("차")) {
					rtnAntiGroundArea += Float.parseFloat(_arr[37]);
				}
			}
		} catch (IOException | NumberFormatException e) {
		}


		return FormatUtil.round(rtnAntiGroundArea);
	}

	/**
	 * 대지권비율분모
	 * 
	 * @return
	 * @throws IOException
	 */
	public float getAntiGroundRatioDenominator(String filePath) {

		float rtnDenominator = 0f;

		List<String[]> djgiArrList;
		try {
			djgiArrList = FileUtil.readFileToStringArrayList(filePath, ",");
			for (String[] _arr : djgiArrList) {
				// StringUtil.printIndexData(_arr, "대지권등록정보");
				if (_arr[5].equals("1")) {
					if (_arr[12].length() > 0) {
						
						try {
							rtnDenominator = Float.parseFloat(_arr[12].split("\\/")[1]);
							break;
						} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
							rtnDenominator = 0f;
						}
					}
				}
			}
		} catch (IOException | NumberFormatException e) {
		}

		return rtnDenominator;
	}

	/**
	 * 토지대장면적을 계산하여 리턴한다.
	 * 
	 * @return
	 * @throws IOException
	 */
	public float getLandArea(String filePath) {

		float rtnLandAreaSum = 0f;

		List<String[]> tjArrList;
		try {
			tjArrList = FileUtil.readFileToStringArrayList(filePath, ",");
			for (String[] _arr : tjArrList) {
				// StringUtil.printIndexData(_arr, "토지임야");
				// 대장구분 코드가 1인 값만 합산 (구분코드 1:토지대장 2:임야대장)
				if (_arr[4].equals("1")) {
					rtnLandAreaSum += Float.parseFloat(_arr[8]);
				}
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}

		return rtnLandAreaSum;
	}

	/**
	 * 부속지번을 가져온다.
	 * 
	 * @return
	 * @throws IOException
	 */
	public Set<String> getBsJibunList(String filePath) {

		Set<String> rtnList = new HashSet<String>();

		// 부속지번이 존재하는 경우
		List<String[]> bsJibunList;
		try {
			bsJibunList = FileUtil.readFileToStringArrayList(filePath, "|");
			String _filePath = null;
			
			for (String[] _arr : bsJibunList) {
				// 대장 구분코드가 "2" 이고 부속 대장 구분 코드가 "0"인 부속지번
				if (_arr[1].equals("2") && _arr[25].equals("0")) {
					// StringUtil.printIndexData(_arr, "부속지번");
					_filePath = 1 + "\\";
					_filePath += Integer.parseInt(_arr[23]) + "\\";
					_filePath += Integer.parseInt(_arr[24]) + "\\";
					_filePath += Integer.parseInt(_arr[26]) + "\\";
					_filePath += Integer.parseInt(_arr[27]) + "\\";
					
					rtnList.add(_filePath);
				}
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}

		return rtnList;
	}

	/**
	 * 개별 공시지가 면적 계산
	 * 
	 * @param string
	 * @return
	 * @throws IOException
	 */
	public float getEachOffcialPriceArea(String filePath) {

		float eachOffcialPriceArea = 0f;

		List<String[]> eachOfficeArrList;
		try {
			eachOfficeArrList = FileUtil.readFileToStringArrayList(filePath, ",");
			for (String[] _arr : eachOfficeArrList) {
				// StringUtil.printIndexData(_arr, "개별공시지가");
				if (_arr[3].equals("1")) {
					eachOffcialPriceArea += Float.parseFloat(_arr[13]);
				}
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}

		return eachOffcialPriceArea;
	}

	/**
	 * 용적률산정연면적 클린징 로직
	 * 
	 * @param groundEachFloorSum
	 *            지상층별개요합산
	 * @param arCalcTotalArea
	 *            용적률산정연면적
	 * @param groundPrivatePublicSum
	 *            지상전유고용합산
	 * @return
	 * @throws Exception
	 */
	public float getArCalcTotalAreaCleansing(float groundEachFloorSum, float arCalcTotalArea,
			float groundPrivatePublicSum) {

		float areaRatioCalcTotalAreaReplaceVal = 0f;
		float calcFloorDivideAreaRatio = 1f;
		float calcGroundPrivatePublicDivideAreaRatio = 1f;
		float calcFloorDivideGroundPrivatePublic = 1f;

		if (groundEachFloorSum > 0 && arCalcTotalArea > 0) {
			calcFloorDivideAreaRatio = FormatUtil.round(Math.abs(1 - (groundEachFloorSum / arCalcTotalArea)));
		}

		if (groundPrivatePublicSum > 0 && arCalcTotalArea > 0) {
			calcGroundPrivatePublicDivideAreaRatio = FormatUtil
					.round(Math.abs(1 - (groundPrivatePublicSum / arCalcTotalArea)));
		}

		if (groundEachFloorSum > 0 && groundPrivatePublicSum > 0) {
			calcFloorDivideGroundPrivatePublic = FormatUtil
					.round(Math.abs(1 - (groundEachFloorSum / groundPrivatePublicSum)));
		}

		if (calcFloorDivideAreaRatio <= 0.02 || calcGroundPrivatePublicDivideAreaRatio <= 0.02) {
			areaRatioCalcTotalAreaReplaceVal = arCalcTotalArea;
		} else if (calcFloorDivideGroundPrivatePublic <= 0.02) {
			areaRatioCalcTotalAreaReplaceVal = groundEachFloorSum;
		} else {
			// 결측값보완불가
			areaRatioCalcTotalAreaReplaceVal = -1;
		}

		return areaRatioCalcTotalAreaReplaceVal;
	}

	/**
	 * 
	 * @param antiGroundArea
	 *            대지면적
	 * @param landArea
	 *            토지대장면적
	 * @param eachOffcialPriceArea
	 *            개별공시지가
	 * @param antiGroundRatioDenominator
	 *            // 대지권비율분모
	 * @return
	 * @throws Exception
	 */
	public float getAntiGroundAreaCleansing(float antiGroundArea, float landArea, float eachOffcialPriceArea,
			float antiGroundRatioDenominator) {

		float antiGroundAreaReplaceVal = 0f;
		float calcAntiGroundDivideLandArea = 1f;
		float calcAntiGroundDivideEachOffice = 1f;
		float calcAntiGroundDivideAntiGroundRatioDenominator = 1f;
		float calcEachOfficeDivideLandArea = 1f;
		float calcAntiGroundRatioDenominatorDivideEachOffice = 1f;
		float calcAntiGroundRatioDenominatorDivideLandArea = 1f;

		if (antiGroundArea > 0 && landArea > 0) {
			calcAntiGroundDivideLandArea = FormatUtil.round(Math.abs(1 - (antiGroundArea / landArea)));
		}

		if (antiGroundArea > 0 && eachOffcialPriceArea > 0) {
			calcAntiGroundDivideEachOffice = FormatUtil.round(Math.abs(1 - (antiGroundArea / eachOffcialPriceArea)));
		}

		if (antiGroundArea > 0 && antiGroundRatioDenominator > 0) {
			calcAntiGroundDivideAntiGroundRatioDenominator = FormatUtil
					.round(Math.abs(1 - (antiGroundArea / antiGroundRatioDenominator)));
		}

		if (eachOffcialPriceArea > 0 && landArea > 0) {
			calcEachOfficeDivideLandArea = FormatUtil.round(Math.abs(1 - (eachOffcialPriceArea / landArea)));
		}

		if (antiGroundRatioDenominator > 0 && eachOffcialPriceArea > 0) {
			calcAntiGroundRatioDenominatorDivideEachOffice = FormatUtil
					.round(Math.abs(1 - (antiGroundRatioDenominator / eachOffcialPriceArea)));
		}

		if (antiGroundRatioDenominator > 0 && landArea > 0) {
			calcAntiGroundRatioDenominatorDivideLandArea = FormatUtil
					.round(Math.abs(1 - (antiGroundRatioDenominator / landArea)));
		}

		if (calcAntiGroundDivideLandArea <= 0.05 || calcAntiGroundDivideEachOffice <= 0.05
				|| calcAntiGroundDivideAntiGroundRatioDenominator <= 0.05) {
			antiGroundAreaReplaceVal = antiGroundArea;
		} else if (calcEachOfficeDivideLandArea <= 0.05) {
			antiGroundAreaReplaceVal = landArea;
		} else if (calcAntiGroundRatioDenominatorDivideEachOffice <= 0.05
				|| calcAntiGroundRatioDenominatorDivideLandArea <= 0.05) {
			antiGroundAreaReplaceVal = antiGroundRatioDenominator;
		} else {
			antiGroundAreaReplaceVal = -1;
		}

		return antiGroundAreaReplaceVal;
	}
	
	
}
