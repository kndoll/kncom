package kr.co.kncom.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kncom.domain.AreaRatio;
import kr.co.kncom.domain.AreaRatioError;
import kr.co.kncom.repository.AreaRatioErrorRepository;
import kr.co.kncom.repository.AreaRatioRepository;
import kr.co.kncom.repository.JttypelistRepository;
import kr.co.kncom.util.FileUtil;
import kr.co.kncom.util.FormatUtil;
import kr.co.kncom.util.StringUtil;

@Service
public class AreaRatioService extends SimpleFileVisitor<Path> {

	private final String[] totalPyojebuFileArr = { "X:\\201706lobig\\", "daejang_totalpyojebu.dat" };
	private final String[] jygyAreaFileArr = { "X:\\201706lobig\\", "daejang_jygyarea.dat" };
	private final String[] floorGaeyoFileArr = { "X:\\201706lobig\\", "daejang_floorgaeyo.dat" };
	private final String[] djgiFileArr = { "Q:\\Tjiy\\", "djgi.dat" }; // 대지권
	private final String[] tjFileArr = { "Q:\\Tjiy\\", "tj.dat" };
	private final String[] bsjibunArr = { "X:\\201706lobig\\", "daejang_bsjibun.dat" }; // 부속지번
	private final String[] eachOffcialPriceFileArr = { "E:\\Tjiy\\", "gb.dat" };

	@Autowired
	private JttypelistRepository jttypelist;

	@Autowired
	private AreaRatioRepository areaRatioRepository;

	@Autowired
	private AreaRatioErrorRepository areaRatioErrorRepository;

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

		if (file.getFileName().toString().equals("daejang_pyojebu.dat")) {
			System.out.println("## file ==> " + file.toString());

			// 여기서 서비스 호출
			Map<String, String> params = new HashMap<String, String>();
			params.put("filePath", file.toString());

			insertAreaRatio(params);
		}

		return super.visitFile(file, attrs);
	}

	/**
	 * 표제부 파일 경로를 입력받아 용적률을 재계산한다.
	 * 
	 * @param params
	 */
	public void insertAreaRatio(Map<String, String> params) {

		String filePath = params.get("filePath");

		try {
			// 용적률 데이터는 집합건물의 동 기준으로 처리
			List<String[]> pyojebuList = FileUtil.readFileToStringArrayList(filePath, "|");

			// 공통변수 (표제부에서 합산해야 함)
			float areaRatio = 0f; // 용적률 (합산)
			float arCalcTotalArea = 0f; // 용적률 산정 연면적 (합산)
			float antiGroundArea = 0f; // 대지면적 (합산)
			int underGroundFloor = 0; // (아무값)
			float totalArea = 0f; // 연면적 (합산)
			int outsideParcel = 0; // 외필지수 (아무값)
			
			// 지번
			int gu = 0;
			int dong = 0;
			int jibun1 = 0;
			int jibun2 = 0;
			
			// 주소
			String address = null;
			String roadAddress = null;
			
			float calcResult = 0f;
			
			AreaRatio areaRatioVO;
			String step = null;
			String result = null;
			
			boolean isExcuteReCalc = false;

			for (String[] _arr : pyojebuList) {

				// 재계산 대상 파일 여부 확인
				boolean isExcute = isDasedae(_arr);
				if (isExcute) {
					// 데이터 가공
					//StringUtil.printIndexData(_arr, "표제부");

					// 합산
					areaRatio += Float.parseFloat(_arr[30]);
					arCalcTotalArea += Float.parseFloat(_arr[29]);
					antiGroundArea += Float.parseFloat(_arr[25]);
					totalArea += Float.parseFloat(_arr[28]);

					// 아무값
					underGroundFloor = Integer.parseInt(_arr[44]);
					outsideParcel = Integer.parseInt(_arr[16]);
					
					gu = Integer.parseInt(_arr[8]);
					dong = Integer.parseInt(_arr[9]);
					jibun1 = Integer.parseInt(_arr[11]);
					jibun2 = Integer.parseInt(_arr[12]);
					
					address = _arr[5];
					roadAddress = _arr[6];
					
					System.out.println("## areaRatio ==> " + areaRatio);
					System.out.println("## arCalcTotalArea ==> " + arCalcTotalArea);
					System.out.println("## antiGroundArea ==> " + antiGroundArea);
					System.out.println("## totalArea ==> " + totalArea);

					System.out.println("## underGroundFloor ==> " + underGroundFloor);
					System.out.println("## outsideParcel ==> " + outsideParcel);

					isExcuteReCalc = true;
				} else {
					//System.out.println("## ERROR MSG ==> 재계산 대상이 아님");
				}
			}

			// 표제부의 모든 집합건물 데이터는 하나로 처리한다.
			Map<String, Object> pyojebuData = new HashMap<String, Object>();
			pyojebuData.put("areaRatio", areaRatio);
			pyojebuData.put("arCalcTotalArea", arCalcTotalArea);
			pyojebuData.put("antiGroundArea", antiGroundArea);
			pyojebuData.put("totalArea", totalArea);
			pyojebuData.put("underGroundFloor", underGroundFloor);
			pyojebuData.put("outsideParcel", outsideParcel);
			
			pyojebuData.put("gu", gu);
			pyojebuData.put("dong", dong);
			pyojebuData.put("jibun1", jibun1);
			pyojebuData.put("jibun2", jibun2);
			pyojebuData.put("address", address);
			pyojebuData.put("roadAddress", roadAddress);
			
			
			// 표제부 기반 데이터 수집은 한번만 실행
			if (isExcuteReCalc) {

				// 용적률 존재 여부
				areaRatioVO = new AreaRatio();
				areaRatio = (float) pyojebuData.get("areaRatio");
				arCalcTotalArea = (float) pyojebuData.get("arCalcTotalArea");
				
				calcResult = FormatUtil.round(Math.abs(1 - areaRatio / ((arCalcTotalArea / antiGroundArea) * 100)));

				if (areaRatio > 0) { // 용적률이 대장상에 존재하는 경우
					result = "";
					
					if (arCalcTotalArea > 0 && antiGroundArea > 0) { // step 1-1
						step = "Step1-1";

						if (calcResult <= 0.01) {
							result = "1";
						} else {
							result = "계산오류";
						}

						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						// 데이터 DBMS에 저장
						areaRatioRepository.save(areaRatioVO);
					} else if (arCalcTotalArea == 0 && antiGroundArea > 0) { // step

						step = "Step2-1";
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						areaRatioRepository.save(areaRatioVO);
						
						if (areaRatioVO.getResult().equals("")) {
							step = "Step2-2";
							if (calcResult <= 0.01) {
								result = "2";
							} else {
								result = "계산오류";
							}
							
							areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
							areaRatioRepository.save(areaRatioVO);
						}
						
					} else if (arCalcTotalArea > 0 && antiGroundArea == 0) { // step

						step = "Step3-1";
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						areaRatioRepository.save(areaRatioVO);
						
						System.out.println("##result-length ==> " + areaRatioVO.getResult().length());
						
						if (areaRatioVO.getResult().equals("")) {
							step = "Step3-2";
							if (calcResult <= 0.01) {
								result = "3";
							} else {
								result = "계산오류3";
							}
							areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
							areaRatioRepository.save(areaRatioVO);
						}

					} else if (arCalcTotalArea == 0 && antiGroundArea == 0) { // step
																				// 4-1,2,3

						step = "Step4-1";
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						areaRatioRepository.save(areaRatioVO);
						
						if (areaRatioVO.getResult().equals("")) {
							step = "Step4-2";
							areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
							areaRatioRepository.save(areaRatioVO);
						}
						
						if (areaRatioVO.getResult().equals("")) {
							step = "Step4-3";
							if (calcResult <= 0.01) {
								result = "4";
							} else {
								result = "계산오류4";
							}
							areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
							areaRatioRepository.save(areaRatioVO);
						}

					}
				} else {
					// STEP 5-1 ~ STEP 5-3
					step = "Step5-1";
					areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
					areaRatioRepository.save(areaRatioVO);
					
					if (areaRatioVO.getResult().equals("")) {
						step = "Step5-2";
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						areaRatioRepository.save(areaRatioVO);
					}
					
					if (areaRatioVO.getResult().equals("")) {
						step = "Step5-3";
						calcResult = (arCalcTotalArea / antiGroundArea) * 100;
						if (calcResult >= 400) {
							result = "5";
						} else {
							result = "계산오류5";
						}
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						areaRatioRepository.save(areaRatioVO);
					}
				}

			}

		} catch (Exception e) {
			AreaRatioError areaRatioError = new AreaRatioError();
			areaRatioError.setFilePath(params.get("filePath").toString());
			areaRatioError.setErrorMsg(e.getMessage());
			areaRatioErrorRepository.save(areaRatioError);
			
			//e.printStackTrace();
		}

	}

	/**
	 * 용적률 재계산 대상 데이터 인지 확인
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean isDasedae(String[] data) throws Exception {

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

		if (isExistOneRoom(Integer.parseInt(data[8]), Integer.parseInt(data[9]), Integer.parseInt(data[11]),
				Integer.parseInt(data[12]))) {
			rtnVal = true;
		} else {
			rtnVal = calcDasedaeFromFloor(this.jygyAreaFileArr[0] + jibunLocation.toString() + jygyAreaFileArr[1]);
		}

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
	private boolean calcDasedaeFromFloor(String filePath) throws IOException {

		boolean rtnVal = false;

		List<String[]> _jygyAreaList = FileUtil.readFileToStringArrayList(filePath, "|");

		Set<String> floorSet = new HashSet<String>();

		for (String[] _arr : _jygyAreaList) {
			if (_arr[26].equals("1") && !_arr[24].equals("지하")
					&& (_arr[34].equals("02002") || _arr[34].equals("02003"))) {
				floorSet.add(_arr[25]);
			}
		}

		if (floorSet.size() > 0 && floorSet.size() < 5) {
			rtnVal = true;
		}

		return rtnVal;
	}

	/**
	 * areaRatio VO BULID 한다.
	 * 
	 * @return
	 * @throws IOException
	 */
	private AreaRatio buildAreaRatioVO(String result, String step, Map pyojebuData) throws IOException {

		AreaRatio areaRatio = new AreaRatio();

		int gu = (int) pyojebuData.get("gu");
		int dong = (int) pyojebuData.get("dong");
		int jibun1 = (int) pyojebuData.get("jibun1");
		int jibun2 = (int) pyojebuData.get("jibun2");

		// 결과값
		areaRatio.setStep(step);
		areaRatio.setResult(result);
		areaRatio.setSi(1);
		areaRatio.setGu(gu);
		areaRatio.setDong(dong);
		areaRatio.setJibun1(jibun1);
		areaRatio.setJibun2(jibun2);
		areaRatio.setArCalcTotalArea((float)pyojebuData.get("arCalcTotalArea")); // 용적률산정연면적
		areaRatio.setTotalArea((float)pyojebuData.get("totalArea")); // 연면적
		areaRatio.setUnderGroundFloor((int)pyojebuData.get("underGroundFloor")); // 지하층수
		
		// 주소
		areaRatio.setAddress(pyojebuData.get("address").toString());
		areaRatio.setRoadAddress(pyojebuData.get("roadAddress").toString());
		
		StringBuilder filePath = new StringBuilder();

		filePath.append("1");
		filePath.append("\\");
		filePath.append(gu);
		filePath.append("\\");
		filePath.append(dong);
		filePath.append("\\");
		filePath.append(jibun1);
		filePath.append("\\");
		filePath.append(jibun2);
		filePath.append("\\");

		// 층별개요합산
		areaRatio.setGroundEachFloorSum(
				getGroundEachFloorSum(this.floorGaeyoFileArr[0] + filePath.toString() + this.floorGaeyoFileArr[1]));
		// 지상전유공용면적합산
		areaRatio.setGroundPrivatePublicSum(
				getGroundPrivatePublicSum(this.jygyAreaFileArr[0] + filePath.toString() + this.jygyAreaFileArr[1]));
		areaRatio.setAreaRatio((float)pyojebuData.get("areaRatio")); // 용적률
		// 외필지
		areaRatio.setOutsideParcel((int)pyojebuData.get("outsideParcel"));
		// 대지면적
		areaRatio.setAntiGroundArea((float)pyojebuData.get("antiGroundArea"));

		// 토지대장면적(합산)
		// 대표지번의 토지면적
		float landAreaSum = getLandArea(this.tjFileArr[0] + filePath.toString() + this.tjFileArr[1]); // 대표지번

		// 개별공시지가면적(합산)
		// 대표지번의 개별공시지가면적
		float eachOffcialPriceAreaSum = getEachOffcialPriceArea(
				this.eachOffcialPriceFileArr[0] + filePath.toString() + eachOffcialPriceFileArr[1]);

		// # 부속지번존재할 경우 토지대장면적, 개별공시지가 면적 재계산
		String bsjibunFileName = this.bsjibunArr[0] + filePath.toString() + this.bsjibunArr[1];
		if (FileUtil.isExistFile(bsjibunFileName)) {

			List<String> _bsJibunArr = getBsJibunList(bsjibunFileName);

			for (String _str : _bsJibunArr) {
				landAreaSum += getLandArea(this.tjFileArr[0] + _str + this.tjFileArr[1]);
				eachOffcialPriceAreaSum += getEachOffcialPriceArea(
						this.eachOffcialPriceFileArr[0] + _str + this.eachOffcialPriceFileArr[1]);
			}
		}

		// 토지대장면적(합산)
		areaRatio.setLandArea(landAreaSum);
		// 개별공시지가면적(합산)
		areaRatio.setEachOffcialPriceArea(eachOffcialPriceAreaSum);

		// 대지권비율의 분모값
		areaRatio.setAntiGroundRatioDenominator(
				getAntiGroundRatioDenominator(this.djgiFileArr[0] + filePath.toString() + this.djgiFileArr[1]));
		// 용적률 계산
		float areaRatioCalc = 0f;
		if (areaRatio.getAntiGroundArea() > 0) {
			areaRatioCalc = (areaRatio.getArCalcTotalArea() / areaRatio.getAntiGroundArea()) * 100;
		}
		areaRatio.setAreaRatioCalc(FormatUtil.round(areaRatioCalc));

		// 용적률산정연면적대체값
		// 대지면적대체값
		float areaRatioCalcTotalAreaReplaceVal = 0f;
		float antiGroundAreaReplaceVal = 0f;
		switch (step) {
		case "Step2-1":
			areaRatioCalcTotalAreaReplaceVal = getArCalcTotalAreaCleansing(areaRatio.getGroundEachFloorSum(),
					areaRatio.getArCalcTotalArea(), areaRatio.getGroundPrivatePublicSum());
			if (areaRatioCalcTotalAreaReplaceVal != -1) {
				areaRatio.setAreaRatioCalcTotalAreaReplaceVal(areaRatioCalcTotalAreaReplaceVal);
			} else {
				areaRatio.setResult("용적율산정연면적 결측값 보완불가");
			}
			break;

		case "Step3-1":
			antiGroundAreaReplaceVal = getAntiGroundAreaCleansing(areaRatio.getAntiGroundArea(),
					areaRatio.getLandArea(), areaRatio.getEachOffcialPriceArea(),
					areaRatio.getAntiGroundRatioDenominator());
			if (antiGroundAreaReplaceVal != -1) {
				areaRatio.setAntiGroundAreaReplaceVal(antiGroundAreaReplaceVal);
			} else {
				areaRatio.setResult("(외)대지면적 결측값 보완불가");
			}
			break;

		case "Step4-1":
			areaRatioCalcTotalAreaReplaceVal = getArCalcTotalAreaCleansing(areaRatio.getGroundEachFloorSum(),
					areaRatio.getArCalcTotalArea(), areaRatio.getGroundPrivatePublicSum());
			if (areaRatioCalcTotalAreaReplaceVal != -1) {
				areaRatio.setAreaRatioCalcTotalAreaReplaceVal(areaRatioCalcTotalAreaReplaceVal);
			} else {
				areaRatio.setResult("용적율산정연면적 결측값 보완불가");
			}
			break;

		case "Step4-2":
			antiGroundAreaReplaceVal = getAntiGroundAreaCleansing(areaRatio.getAntiGroundArea(),
					areaRatio.getLandArea(), areaRatio.getEachOffcialPriceArea(),
					areaRatio.getAntiGroundRatioDenominator());
			if (antiGroundAreaReplaceVal != -1) {
				areaRatio.setAntiGroundAreaReplaceVal(antiGroundAreaReplaceVal);
			} else {
				areaRatio.setResult("(외)대지면적 결측값 보완불가");
			}
			break;

		case "Step5-1":
			areaRatioCalcTotalAreaReplaceVal = getArCalcTotalAreaCleansing(areaRatio.getGroundEachFloorSum(),
					areaRatio.getArCalcTotalArea(), areaRatio.getGroundPrivatePublicSum());
			if (areaRatioCalcTotalAreaReplaceVal != -1) {
				areaRatio.setAreaRatioCalcTotalAreaReplaceVal(areaRatioCalcTotalAreaReplaceVal);
			} else {
				areaRatio.setResult("용적율산정연면적 클린징실패");
			}
			break;

		case "Step5-2":
			antiGroundAreaReplaceVal = getAntiGroundAreaCleansing(areaRatio.getAntiGroundArea(),
					areaRatio.getLandArea(), areaRatio.getEachOffcialPriceArea(),
					areaRatio.getAntiGroundRatioDenominator());
			if (antiGroundAreaReplaceVal != -1) {
				areaRatio.setAntiGroundAreaReplaceVal(antiGroundAreaReplaceVal);
			} else {
				areaRatio.setResult("(외)대지면적 클린징실패");
			}
			break;
		}

		return areaRatio;
	}

	/**
	 * 층별 개요 합산을 리턴한다.
	 * 
	 * @return
	 * @throws IOException
	 */
	private float getGroundEachFloorSum(String filePath) throws IOException {

		float rtnAntiGroundArea = 0f;

		List<String[]> floorArrList = FileUtil.readFileToStringArrayList(filePath, "|");

		int floor = 0;

		for (String[] _arr : floorArrList) {
			floor = Integer.parseInt(_arr[18]);
			if (floor >= 20 && floor < 30) {
				if (!_arr[27].equals("제외") || !_arr[27].equals("차")) {
					rtnAntiGroundArea += Float.parseFloat(_arr[28]);
				}
			}
		}

		return FormatUtil.round(rtnAntiGroundArea);
	}

	/**
	 * 지상전유공용면적 합산을 리턴한다.
	 * 
	 * @return
	 * @throws IOException
	 */
	private float getGroundPrivatePublicSum(String filePath) throws IOException {

		float rtnAntiGroundArea = 0f;

		List<String[]> jygyArrList = FileUtil.readFileToStringArrayList(filePath, "|");

		int floor = 0;

		for (String[] _arr : jygyArrList) {
			//StringUtil.printIndexData(_arr, "전유공용구분");
			floor = Integer.parseInt(_arr[23]);

			if ((floor >= 20 && floor < 30) && !_arr[36].equals("차")) {
				rtnAntiGroundArea += Float.parseFloat(_arr[37]);
			}
		}

		return FormatUtil.round(rtnAntiGroundArea);
	}

	/**
	 * 대지권비율분모
	 * 
	 * @return
	 * @throws IOException
	 */
	private float getAntiGroundRatioDenominator(String filePath) throws IOException {

		float rtnDenominator = 0f;

		List<String[]> djgiArrList = FileUtil.readFileToStringArrayList(filePath, ",");
		for (String[] _arr : djgiArrList) {
			// StringUtil.printIndexData(_arr, "대지권등록정보");
			if (_arr[12].length() > 0) {

				rtnDenominator = Float.parseFloat(_arr[12].split("\\/")[1]);
				break;
			}
		}

		return rtnDenominator;
	}

	/**
	 * 토지대장면적을 계산하여 리턴한다.
	 * 
	 * @return
	 * @throws IOException
	 */
	private float getLandArea(String filePath) throws IOException {

		float rtnLandAreaSum = 0f;

		List<String[]> tjArrList = FileUtil.readFileToStringArrayList(filePath, ",");

		for (String[] _arr : tjArrList) {
			// StringUtil.printIndexData(_arr, "토지임야");
			rtnLandAreaSum += Float.parseFloat(_arr[8]);
		}

		return rtnLandAreaSum;
	}

	/**
	 * 부속지번을 가져온다.
	 * 
	 * @return
	 * @throws IOException
	 */
	private List<String> getBsJibunList(String filePath) throws IOException {

		List<String> rtnList = new ArrayList<String>();

		// 부속지번이 존재하는 경우
		List<String[]> bsJibunList = FileUtil.readFileToStringArrayList(filePath, "|");
		String _filePath = null;
		for (String[] _arr : bsJibunList) {
			// StringUtil.printIndexData(_arr, "부속지번");
			_filePath = 1 + "\\";
			_filePath += Integer.parseInt(_arr[23]) + "\\";
			_filePath += Integer.parseInt(_arr[24]) + "\\";
			_filePath += Integer.parseInt(_arr[26]) + "\\";
			_filePath += Integer.parseInt(_arr[27]) + "\\";

			rtnList.add(_filePath);
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
	private float getEachOffcialPriceArea(String filePath) throws IOException {

		float eachOffcialPriceArea = 0f;

		List<String[]> eachOfficeArrList = FileUtil.readFileToStringArrayList(filePath, ",");

		for (String[] _arr : eachOfficeArrList) {
			// StringUtil.printIndexData(_arr, "개별공시지가");
			eachOffcialPriceArea += Float.parseFloat(_arr[13]);
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
	 */
	private float getArCalcTotalAreaCleansing(float groundEachFloorSum, float arCalcTotalArea,
			float groundPrivatePublicSum) {

		float areaRatioCalcTotalAreaReplaceVal = 0f;

		float calcFloorDivideAreaRatio = FormatUtil.round(Math.abs(1 - (groundEachFloorSum / arCalcTotalArea)));
		float calcGroundPrivatePublicDivideAreaRatio = FormatUtil
				.round(Math.abs(1 - (groundPrivatePublicSum / arCalcTotalArea)));
		float calcFloorDivideGroundPrivatePublic = FormatUtil
				.round(Math.abs(1 - (groundEachFloorSum / groundPrivatePublicSum)));

		if (calcFloorDivideAreaRatio <= 0.01 || calcGroundPrivatePublicDivideAreaRatio <= 0.01) {
			areaRatioCalcTotalAreaReplaceVal = arCalcTotalArea;
		} else if (calcFloorDivideGroundPrivatePublic <= 0.01) {
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
	 */
	private float getAntiGroundAreaCleansing(float antiGroundArea, float landArea, float eachOffcialPriceArea,
			float antiGroundRatioDenominator) {

		float antiGroundAreaReplaceVal = 0f;

		float calcAntiGroundDivideLandArea = FormatUtil.round(Math.abs(1 - (antiGroundArea / landArea)));
		float calcAntiGroundDivideEachOffice = FormatUtil.round(Math.abs(1 - (antiGroundArea / eachOffcialPriceArea)));
		float calcAntiGroundDivideAntiGroundRatioDenominator = FormatUtil
				.round(Math.abs(1 - (antiGroundArea / antiGroundRatioDenominator)));
		float calcEachOfficeDivideLandArea = FormatUtil.round(Math.abs(1 - (eachOffcialPriceArea / landArea)));
		float calcAntiGroundRatioDenominatorDivideEachOffice = FormatUtil
				.round(Math.abs(1 - (antiGroundRatioDenominator / eachOffcialPriceArea)));
		float calcAntiGroundRatioDenominatorDivideLandArea = FormatUtil
				.round(Math.abs(1 - (antiGroundRatioDenominator / landArea)));

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
