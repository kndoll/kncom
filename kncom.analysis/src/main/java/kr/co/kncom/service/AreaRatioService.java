package kr.co.kncom.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kncom.domain.AreaRatio;
import kr.co.kncom.domain.RealtyMigError;
import kr.co.kncom.repository.RealtyMigErrorRepository;
import kr.co.kncom.repository.AreaRatioRepository;
import kr.co.kncom.util.FileUtil;
import kr.co.kncom.util.FormatUtil;

@Service
public class AreaRatioService extends SimpleFileVisitor<Path> {

	private final String[] pyojebuFileArr = { "X:\\201706lobig\\", "daejang_pyojebu.dat" }; // 표제부
	private final String[] totalPyojebuFileArr = { "X:\\201706lobig\\", "daejang_totalpyojebu.dat" }; // 총괄표제부
	private final String[] jygyAreaFileArr = { "X:\\201706lobig\\", "daejang_jygyarea.dat" }; // 전유공유
	private final String[] floorGaeyoFileArr = { "X:\\201706lobig\\", "daejang_floorgaeyo.dat" }; // 층별개요
	private final String[] djgiFileArr = { "J:\\Tjiy\\", "djgi.dat" }; // 대지권
	private final String[] tjFileArr = { "T:\\Tjiy\\", "tj.dat" }; // 토지임야
	private final String[] bsjibunArr = { "X:\\201706lobig\\", "daejang_bsjibun.dat" }; // 부속지번
	private final String[] eachOffcialPriceFileArr = { "E:\\Tjiy\\", "gb.dat" }; // 개별공시지가
	
	@Autowired
	private RealtyStandardData realtyStandardData;
	
	@Autowired
	private AreaRatioRepository areaRatioRepository;

	@Autowired
	private RealtyMigErrorRepository realtyMigErrorRepository;

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

		if (file.getFileName().toString().equals(pyojebuFileArr[1])) {
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

		try {
			String filePath = params.get("filePath");
			String totalPyojebu = filePath.replaceAll(pyojebuFileArr[1], totalPyojebuFileArr[1]);

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
			float calcResult2 = 0f;

			AreaRatio areaRatioVO;
			String step = null;
			String result = null;

			boolean isExcuteReCalc = false;
			boolean isExcute = false;

			// 해당 지번에 총괄표제부가 존재하는지 확인한다.
			if (FileUtil.isExistFile(totalPyojebu)) {

				// 총괄표제부 파일을 읽어 처리한다.
				String[] totalPyojebuData = FileUtil.readFileToString(totalPyojebu).split("\\|");

				// StringUtil.printIndexData(totalPyojebuData, "총괄 표제부");
				// 총괄표제부 데이터 표제부 데이터 순서로 매핑
				String[] pyojebuData = new String[40];

				pyojebuData[34] = totalPyojebuData[30]; // 주용도 코드
				pyojebuData[8] = totalPyojebuData[10]; // 시군구코드
				pyojebuData[9] = totalPyojebuData[11]; // 동코드
				pyojebuData[11] = totalPyojebuData[13]; // 지번1
				pyojebuData[12] = totalPyojebuData[14]; // 지번2

				// 재계산 대상 파일 여부 확인
				isExcute = realtyStandardData.isDasedae(pyojebuData);

				if (isExcute) {
					// 합산
					areaRatio = Float.parseFloat(totalPyojebuData[29]);
					arCalcTotalArea = Float.parseFloat(totalPyojebuData[28]);
					antiGroundArea = Float.parseFloat(totalPyojebuData[24]);
					totalArea = Float.parseFloat(totalPyojebuData[27]);

					// 아무값
					underGroundFloor = 0;
					outsideParcel = Integer.parseInt(totalPyojebuData[18]);

					gu = Integer.parseInt(totalPyojebuData[10]);
					dong = Integer.parseInt(totalPyojebuData[11]);
					jibun1 = Integer.parseInt(totalPyojebuData[13]);
					jibun2 = Integer.parseInt(totalPyojebuData[14]);

					address = totalPyojebuData[7];
					roadAddress = totalPyojebuData[8];

					isExcuteReCalc = true;
				}

			} else {

				// 용적률 데이터는 집합건물의 동 기준으로 처리
				List<String[]> pyojebuList = FileUtil.readFileToStringArrayList(filePath, "|");

				for (String[] _arr : pyojebuList) {

					// 재계산 대상 파일 여부 확인
					isExcute = realtyStandardData.isDasedae(_arr);

					if (isExcute) {
						// 데이터 가공
						// StringUtil.printIndexData(_arr, "표제부");

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

						isExcuteReCalc = true;
					} else {
						// System.out.println("## ERROR MSG ==> 재계산 대상이 아님");
					}
				}
			}

			System.out.println("## isExcuteReCalc ==> " + isExcuteReCalc);

			// 표제부 기반 데이터 수집은 한번만 실행
			if (isExcuteReCalc) {
				System.out.println("## areaRatio ==> " + areaRatio);
				System.out.println("## arCalcTotalArea ==> " + arCalcTotalArea);
				System.out.println("## antiGroundArea ==> " + antiGroundArea);
				System.out.println("## totalArea ==> " + totalArea);

				System.out.println("## underGroundFloor ==> " + underGroundFloor);
				System.out.println("## outsideParcel ==> " + outsideParcel);

				// 표제부의 모든 집합건물 데이터는 하나로 처리한다.
				Map<String, Object> pyojebuData = new HashMap<String, Object>();
				pyojebuData.put("areaRatio", FormatUtil.round(areaRatio));
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

				pyojebuData.put("areaRatioCalcTotalAreaReplaceVal", 0f);
				pyojebuData.put("antiGroundAreaReplaceVal", 0f);

				// 용적률 존재 여부
				areaRatioVO = new AreaRatio();
				areaRatio = (float) pyojebuData.get("areaRatio");
				arCalcTotalArea = (float) pyojebuData.get("arCalcTotalArea");

				if (areaRatio > 0) { // 용적률이 대장상에 존재하는 경우

					if (arCalcTotalArea > 0 && antiGroundArea > 0) { // step 1-1
						step = "Step1-1";
						calcResult = FormatUtil
								.round(Math.abs(1 - areaRatio / ((arCalcTotalArea / antiGroundArea) * 100)));
						calcResult2 = FormatUtil.round((arCalcTotalArea / antiGroundArea) * 100);
						if (calcResult <= 0.02 && calcResult2 >= 40.00) {
							result = "1";
						}

						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);

						if (areaRatioVO.getResult() == null) {
							step = "Step1-2";

							calcResult = (areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal()
									/ areaRatioVO.getAntiGroundAreaReplaceVal()) * 100;
							pyojebuData.put("areaRatioCalcTotalAreaReplaceVal",
									areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal());
							pyojebuData.put("antiGroundAreaReplaceVal", areaRatioVO.getAntiGroundAreaReplaceVal());

							if (calcResult > 400) {
								result = "계산오류1";
							}

							areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						}

						// 데이터 DBMS에 저장
						areaRatioRepository.save(areaRatioVO);

					} else if (arCalcTotalArea == 0 && antiGroundArea > 0) { // step

						step = "Step2-1";
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						// areaRatioRepository.save(areaRatioVO);

						if (areaRatioVO.getResult() == null) {
							step = "Step2-2";
							calcResult = FormatUtil.round(Math.abs(1 - areaRatio
									/ ((areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal() / antiGroundArea) * 100)));
							pyojebuData.put("areaRatioCalcTotalAreaReplaceVal",
									areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal());

							if (calcResult <= 0.01) {
								result = "2";
							} else {
								result = "계산오류2";
							}

							areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						}

						areaRatioRepository.save(areaRatioVO);

					} else if (arCalcTotalArea > 0 && antiGroundArea == 0) { // step

						step = "Step3-1";
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						// areaRatioRepository.save(areaRatioVO);

						if (areaRatioVO.getResult() == null) {
							step = "Step3-2";
							calcResult = FormatUtil.round(Math.abs(1 - areaRatio
									/ ((arCalcTotalArea / areaRatioVO.getAntiGroundAreaReplaceVal()) * 100)));
							pyojebuData.put("antiGroundAreaReplaceVal", areaRatioVO.getAntiGroundAreaReplaceVal());

							if (calcResult <= 0.01) {
								result = "3";
							} else {
								result = "계산오류3";
							}

							areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						}

						areaRatioRepository.save(areaRatioVO);

					} else if (arCalcTotalArea == 0 && antiGroundArea == 0) { // step
																				// 4-1,2,3

						step = "Step4-1";
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						// areaRatioRepository.save(areaRatioVO);

						if (areaRatioVO.getResult() == null) {
							step = "Step4-2";
							pyojebuData.put("areaRatioCalcTotalAreaReplaceVal",
									areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal());
							areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
							// areaRatioRepository.save(areaRatioVO);
						}

						if (areaRatioVO.getResult() == null) {
							step = "Step4-3";
							calcResult = FormatUtil
									.round(Math.abs(1 - areaRatio / ((areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal()
											/ areaRatioVO.getAntiGroundAreaReplaceVal()) * 100)));
							pyojebuData.put("antiGroundAreaReplaceVal", areaRatioVO.getAntiGroundAreaReplaceVal());

							if (calcResult <= 0.01) {
								result = "4";
							} else {
								result = "계산오류4";
							}

							// pyojebuData.put("arCalcTotalArea",
							// areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal());
							// pyojebuData.put("antiGroundArea",
							// areaRatioVO.getAntiGroundAreaReplaceVal());

							areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						}

						areaRatioRepository.save(areaRatioVO);
					}
				} else {
					// STEP 5-1 ~ STEP 5-3
					step = "Step5-1";
					areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
					// areaRatioRepository.save(areaRatioVO);

					if (areaRatioVO.getResult() == null) {
						step = "Step5-2";
						pyojebuData.put("areaRatioCalcTotalAreaReplaceVal",
								areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal());
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);
						// areaRatioRepository.save(areaRatioVO);
					}

					if (areaRatioVO.getResult() == null) {
						step = "Step5-3";

						calcResult = (areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal()
								/ areaRatioVO.getAntiGroundAreaReplaceVal()) * 100;
						pyojebuData.put("antiGroundAreaReplaceVal", areaRatioVO.getAntiGroundAreaReplaceVal());

						if (calcResult <= 400) {
							result = "5";
						} else {
							result = "계산오류5";
						}

						// pyojebuData.put("arCalcTotalArea",
						// areaRatioVO.getAreaRatioCalcTotalAreaReplaceVal());
						// pyojebuData.put("antiGroundArea",
						// areaRatioVO.getAntiGroundAreaReplaceVal());
						areaRatioVO = buildAreaRatioVO(result, step, pyojebuData);

					}

					areaRatioRepository.save(areaRatioVO);
				}

			}

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();

			RealtyMigError realtyMigError = new RealtyMigError();
			realtyMigError.setFilePath(params.get("filePath").toString());
			realtyMigError.setErrorMsg(exceptionAsString);
			realtyMigError.setKind("areaRatio");
			
			realtyMigErrorRepository.save(realtyMigError);

			e.printStackTrace();
		}

	}
	
	/**
	 * areaRatio VO BULID 한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	private AreaRatio buildAreaRatioVO(String result, String step, Map pyojebuData) throws Exception {

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
		areaRatio.setArCalcTotalArea((float) pyojebuData.get("arCalcTotalArea")); // 용적률산정연면적
		areaRatio.setTotalArea((float) pyojebuData.get("totalArea")); // 연면적
		areaRatio.setUnderGroundFloor((int) pyojebuData.get("underGroundFloor")); // 지하층수

		// 요청 메소드에서 areaRatioCalcTotalAreaReplaceVal, antiGroundAreaReplaceVal
		// 값이 넘어오면 해당 값을 set 한다.
		float areaRatioCalcTotalAreaReplaceVal = (float) pyojebuData.get("areaRatioCalcTotalAreaReplaceVal");
		float antiGroundAreaReplaceVal = (float) pyojebuData.get("antiGroundAreaReplaceVal");

		if (areaRatioCalcTotalAreaReplaceVal > 0) {
			areaRatio.setAreaRatioCalcTotalAreaReplaceVal(areaRatioCalcTotalAreaReplaceVal);
		}

		if (antiGroundAreaReplaceVal > 0) {
			areaRatio.setAntiGroundAreaReplaceVal(antiGroundAreaReplaceVal);
		}

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
				realtyStandardData.getGroundEachFloorSum(this.floorGaeyoFileArr[0] + filePath.toString() + this.floorGaeyoFileArr[1]));
		// 지상전유공용면적합산
		areaRatio.setGroundPrivatePublicSum(
				realtyStandardData.getGroundPrivatePublicSum(this.jygyAreaFileArr[0] + filePath.toString() + this.jygyAreaFileArr[1]));
		areaRatio.setAreaRatio((float) pyojebuData.get("areaRatio")); // 용적률
		// 외필지
		areaRatio.setOutsideParcel((int) pyojebuData.get("outsideParcel"));
		// 대지면적
		areaRatio.setAntiGroundArea((float) pyojebuData.get("antiGroundArea"));

		// 토지대장면적(합산)
		// 대표지번의 토지면적
		float landAreaSum = realtyStandardData.getLandArea(this.tjFileArr[0] + filePath.toString() + this.tjFileArr[1]); // 대표지번

		// 개별공시지가면적(합산)
		// 대표지번의 개별공시지가면적
		float eachOffcialPriceAreaSum = realtyStandardData.getEachOffcialPriceArea(
				this.eachOffcialPriceFileArr[0] + filePath.toString() + eachOffcialPriceFileArr[1]);

		// # 부속지번존재할 경우 토지대장면적, 개별공시지가 면적 재계산
		String bsjibunFileName = this.bsjibunArr[0] + filePath.toString() + this.bsjibunArr[1];
		if (FileUtil.isExistFile(bsjibunFileName)) {

			Set<String> _bsJibunArr = realtyStandardData.getBsJibunList(bsjibunFileName);

			for (String _str : _bsJibunArr) {
				landAreaSum += realtyStandardData.getLandArea(this.tjFileArr[0] + _str + this.tjFileArr[1]);
				eachOffcialPriceAreaSum += realtyStandardData.getEachOffcialPriceArea(
						this.eachOffcialPriceFileArr[0] + _str + this.eachOffcialPriceFileArr[1]);
			}
		}

		// 토지대장면적(합산)
		areaRatio.setLandArea(landAreaSum);
		// 개별공시지가면적(합산)
		areaRatio.setEachOffcialPriceArea(eachOffcialPriceAreaSum);

		// 대지권비율의 분모값
		areaRatio.setAntiGroundRatioDenominator(
				realtyStandardData.getAntiGroundRatioDenominator(this.djgiFileArr[0] + filePath.toString() + this.djgiFileArr[1]));

		// ------------------------------------- 용적률 계산 ----------------------------------//
		float areaRatioCalc = 0f;
		// 계산값 지정
		float calcArTotalArea = 0f; // 용적률 산정면적
		if (areaRatioCalcTotalAreaReplaceVal > 0) {
			calcArTotalArea = areaRatioCalcTotalAreaReplaceVal;
		} else {
			calcArTotalArea = areaRatio.getArCalcTotalArea();
		}

		float calcAntiGroundArea = 0f; // 대지면적
		if (antiGroundAreaReplaceVal > 0) {
			calcAntiGroundArea = antiGroundAreaReplaceVal;
		} else {
			calcAntiGroundArea = areaRatio.getAntiGroundArea();
		}

		if (calcAntiGroundArea > 0) {
			areaRatioCalc = (calcArTotalArea / calcAntiGroundArea) * 100;
		}

		areaRatio.setAreaRatioCalc(FormatUtil.round(areaRatioCalc));
		// -----------------------------------------------------------------------------------//

		// 용적률산정연면적대체값
		// 대지면적대체값
		float _areaRatioCalcTotalAreaReplaceVal = 0f;
		float _antiGroundAreaReplaceVal = 0f;

		switch (step) {
		case "Step1-1":

			if (result == null) {

				// 용적률 산정면적과 대지면적을 동시에 클린징 한다.
				_areaRatioCalcTotalAreaReplaceVal = realtyStandardData.getArCalcTotalAreaCleansing(areaRatio.getGroundEachFloorSum(),
						areaRatio.getArCalcTotalArea(), areaRatio.getGroundPrivatePublicSum());
				_antiGroundAreaReplaceVal = realtyStandardData.getAntiGroundAreaCleansing(areaRatio.getAntiGroundArea(),
						areaRatio.getLandArea(), areaRatio.getEachOffcialPriceArea(),
						areaRatio.getAntiGroundRatioDenominator());

				if (_areaRatioCalcTotalAreaReplaceVal != -1 && _antiGroundAreaReplaceVal != -1) {
					areaRatio.setAreaRatioCalcTotalAreaReplaceVal(_areaRatioCalcTotalAreaReplaceVal);
					areaRatio.setAntiGroundAreaReplaceVal(_antiGroundAreaReplaceVal);
				} else {
					areaRatio.setResult("용적율산정연면적 또는(외)대지면적 결측값 보완불가");
				}

			}

			break;

		case "Step2-1":
			_areaRatioCalcTotalAreaReplaceVal = realtyStandardData.getArCalcTotalAreaCleansing(areaRatio.getGroundEachFloorSum(),
					areaRatio.getArCalcTotalArea(), areaRatio.getGroundPrivatePublicSum());
			if (_areaRatioCalcTotalAreaReplaceVal != -1) {
				areaRatio.setAreaRatioCalcTotalAreaReplaceVal(_areaRatioCalcTotalAreaReplaceVal);
			} else {
				areaRatio.setResult("용적율산정연면적 결측값 보완불가");
			}
			break;

		case "Step3-1":
			_antiGroundAreaReplaceVal = realtyStandardData.getAntiGroundAreaCleansing(areaRatio.getAntiGroundArea(),
					areaRatio.getLandArea(), areaRatio.getEachOffcialPriceArea(),
					areaRatio.getAntiGroundRatioDenominator());
			if (_antiGroundAreaReplaceVal != -1) {
				areaRatio.setAntiGroundAreaReplaceVal(_antiGroundAreaReplaceVal);
			} else {
				areaRatio.setResult("(외)대지면적 결측값 보완불가");
			}
			break;

		case "Step4-1":
			_areaRatioCalcTotalAreaReplaceVal = realtyStandardData.getArCalcTotalAreaCleansing(areaRatio.getGroundEachFloorSum(),
					areaRatio.getArCalcTotalArea(), areaRatio.getGroundPrivatePublicSum());
			if (_areaRatioCalcTotalAreaReplaceVal != -1) {
				areaRatio.setAreaRatioCalcTotalAreaReplaceVal(_areaRatioCalcTotalAreaReplaceVal);
			} else {
				areaRatio.setResult("용적율산정연면적 결측값 보완불가");
			}
			break;

		case "Step4-2":
			_antiGroundAreaReplaceVal = realtyStandardData.getAntiGroundAreaCleansing(areaRatio.getAntiGroundArea(),
					areaRatio.getLandArea(), areaRatio.getEachOffcialPriceArea(),
					areaRatio.getAntiGroundRatioDenominator());
			if (_antiGroundAreaReplaceVal != -1) {
				areaRatio.setAntiGroundAreaReplaceVal(_antiGroundAreaReplaceVal);
			} else {
				areaRatio.setResult("(외)대지면적 결측값 보완불가");
			}
			break;

		case "Step5-1":
			_areaRatioCalcTotalAreaReplaceVal = realtyStandardData.getArCalcTotalAreaCleansing(areaRatio.getGroundEachFloorSum(),
					areaRatio.getArCalcTotalArea(), areaRatio.getGroundPrivatePublicSum());
			if (_areaRatioCalcTotalAreaReplaceVal != -1) {
				areaRatio.setAreaRatioCalcTotalAreaReplaceVal(_areaRatioCalcTotalAreaReplaceVal);
			} else {
				areaRatio.setResult("용적율산정연면적 클린징실패");
			}
			break;

		case "Step5-2":
			_antiGroundAreaReplaceVal = realtyStandardData.getAntiGroundAreaCleansing(areaRatio.getAntiGroundArea(),
					areaRatio.getLandArea(), areaRatio.getEachOffcialPriceArea(),
					areaRatio.getAntiGroundRatioDenominator());
			if (_antiGroundAreaReplaceVal != -1) {
				areaRatio.setAntiGroundAreaReplaceVal(_antiGroundAreaReplaceVal);
			} else {
				areaRatio.setResult("(외)대지면적 클린징실패");
			}
			break;
		}

		return areaRatio;
	}
	
}
