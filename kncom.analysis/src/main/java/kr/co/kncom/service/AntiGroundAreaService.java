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

import kr.co.kncom.domain.AntiGroundArea;
import kr.co.kncom.domain.RealtyMigError;
import kr.co.kncom.repository.AntiGroundAreaRepository;
import kr.co.kncom.repository.RealtyMigErrorRepository;
import kr.co.kncom.util.FileUtil;

@Service
public class AntiGroundAreaService extends SimpleFileVisitor<Path> {

	private final String[] pyojebuFileArr = { "X:\\201706lobig\\", "daejang_pyojebu.dat" }; // 표제부
	private final String[] totalPyojebuFileArr = { "X:\\201706lobig\\", "daejang_totalpyojebu.dat" }; // 총괄표제부
	private final String[] djgiFileArr = { "J:\\Tjiy\\", "djgi.dat" }; // 대지권
	private final String[] tjFileArr = { "T:\\Tjiy\\", "tj.dat" }; // 토지임야
	private final String[] bsjibunArr = { "X:\\201706lobig\\", "daejang_bsjibun.dat" }; // 부속지번
	private final String[] eachOffcialPriceFileArr = { "E:\\Tjiy\\", "gb.dat" }; // 개별공시지가

	@Autowired
	private RealtyStandardData realtyStandardData;

	@Autowired
	private AntiGroundAreaRepository antiGroundAreaRepository;

	@Autowired
	private RealtyMigErrorRepository realtyMigErrorRepository;

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

		if (file.getFileName().toString().equals(pyojebuFileArr[1])) {
			System.out.println("## file ==> " + file.toString());

			// 여기서 서비스 호출
			Map<String, String> params = new HashMap<String, String>();
			params.put("filePath", file.toString());

			insertAntiGroundArea(params);
		}

		return super.visitFile(file, attrs);
	}

	public void insertAntiGroundArea(Map<String, String> params) {

		try {
			String filePath = params.get("filePath");
			String totalPyojebu = filePath.replaceAll(pyojebuFileArr[1], totalPyojebuFileArr[1]);

			// 공통변수 (표제부에서 합산해야 함)
			float antiGroundArea = 0f; // 대지면적 (합산)
			float landArea = 0f; // 토지대장 면적
			float eachOffcialPriceArea = 0f; // 개별공시지가
			float antiGroundRatioDenominator = 0f; // 대지권비율분모

			// 지번
			int gu = 0;
			int dong = 0;
			int jibun1 = 0;
			int jibun2 = 0;

			// 주소
			String address = null;

			float calcResult = 0f;
			float calcResult2 = 0f;

			AntiGroundArea antiGroundAreaVO;

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
					antiGroundArea = Float.parseFloat(totalPyojebuData[24]);

					gu = Integer.parseInt(totalPyojebuData[10]);
					dong = Integer.parseInt(totalPyojebuData[11]);
					jibun1 = Integer.parseInt(totalPyojebuData[13]);
					jibun2 = Integer.parseInt(totalPyojebuData[14]);

					address = totalPyojebuData[7];

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
						antiGroundArea += Float.parseFloat(_arr[25]);

						gu = Integer.parseInt(_arr[8]);
						dong = Integer.parseInt(_arr[9]);
						jibun1 = Integer.parseInt(_arr[11]);
						jibun2 = Integer.parseInt(_arr[12]);

						address = _arr[5];

						isExcuteReCalc = true;
					} else {
						// System.out.println("## ERROR MSG ==> 재계산 대상이 아님");
					}
				}
			}

			System.out.println("## isExcuteReCalc ==> " + isExcuteReCalc);

			// 표제부 기반 데이터 수집은 한번만 실행
			if (isExcuteReCalc) {
				System.out.println("## antiGroundArea ==> " + antiGroundArea);

				// 표제부의 모든 집합건물 데이터는 하나로 처리한다.
				Map<String, Object> pyojebuData = new HashMap<String, Object>();
				pyojebuData.put("antiGroundArea", antiGroundArea);

				pyojebuData.put("gu", gu);
				pyojebuData.put("dong", dong);
				pyojebuData.put("jibun1", jibun1);
				pyojebuData.put("jibun2", jibun2);
				pyojebuData.put("address", address);

				pyojebuData.put("areaRatioCalcTotalAreaReplaceVal", 0f);
				pyojebuData.put("antiGroundAreaReplaceVal", 0f);

				// 대지면적 존재 여부
				antiGroundAreaVO = new AntiGroundArea();

				antiGroundArea = (float) pyojebuData.get("antiGroundArea");

				antiGroundAreaVO = buildAntiGroundAreaVO(result, step, pyojebuData);

				// 데이터 DBMS에 저장
				antiGroundAreaRepository.save(antiGroundAreaVO);
			}

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();

			RealtyMigError realtyMigError = new RealtyMigError();
			realtyMigError.setFilePath(params.get("filePath").toString());
			realtyMigError.setErrorMsg(exceptionAsString);
			realtyMigError.setKind("대지면적클린징");

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
	private AntiGroundArea buildAntiGroundAreaVO(String result, String step, Map pyojebuData) throws Exception {

		AntiGroundArea antiGroundArea = new AntiGroundArea();

		int gu = (int) pyojebuData.get("gu");
		int dong = (int) pyojebuData.get("dong");
		int jibun1 = (int) pyojebuData.get("jibun1");
		int jibun2 = (int) pyojebuData.get("jibun2");

		// 결과값
		antiGroundArea.setStep(step);
		antiGroundArea.setResult(result);
		antiGroundArea.setSi(1);
		antiGroundArea.setGu(gu);
		antiGroundArea.setDong(dong);
		antiGroundArea.setJibun1(jibun1);
		antiGroundArea.setJibun2(jibun2);

		// 주소
		antiGroundArea.setAddress(pyojebuData.get("address").toString());

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

		// 대지면적
		antiGroundArea.setAntiGroundArea((float) pyojebuData.get("antiGroundArea"));

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
		antiGroundArea.setLandArea(landAreaSum);
		// 개별공시지가면적(합산)
		antiGroundArea.setEachOffcialPriceArea(eachOffcialPriceAreaSum);

		// 대지권비율의 분모값
		antiGroundArea.setAntiGroundRatioDenominator(realtyStandardData
				.getAntiGroundRatioDenominator(this.djgiFileArr[0] + filePath.toString() + this.djgiFileArr[1]));

		// ------------------------------------- 대지면적 클린징
		// ----------------------------------//

		// 대지면적대체값
		float _antiGroundAreaReplaceVal = 0f;
		float _antiGroundArea = antiGroundArea.getAntiGroundArea();
		float _landArea = antiGroundArea.getLandArea();
		float _eachOffcialPriceArea = antiGroundArea.getEachOffcialPriceArea();
		float _antiGroundRatioDenominator = antiGroundArea.getAntiGroundRatioDenominator();

		// 클린징 실행
		_antiGroundAreaReplaceVal = realtyStandardData.getAntiGroundAreaCleansing(_antiGroundArea, _landArea,
				_eachOffcialPriceArea, _antiGroundRatioDenominator);
		
		antiGroundArea.setAntiGroundAreaReplaceVal(_antiGroundAreaReplaceVal);
		
		// 결과에 다른 step 및 result 설정
		String _step = null;
		String _result = null;
		
		if (_antiGroundArea > 0) {
			_step = "1-1";
			if (_antiGroundAreaReplaceVal == -1) {
				_result = "클린징 실패";
			} else if (_antiGroundAreaReplaceVal == _antiGroundArea) {
				_result = "1";
			} else if (_antiGroundAreaReplaceVal != _antiGroundArea) {
				_step = "1-2";
				_result = "클린징 성공";
			}
		} else {
			_step = "2-1";
			if (_antiGroundAreaReplaceVal == -1) {
				_result = "클린징 실패";
			} else {
				_result = "클린징 성공";
			}
		}

		antiGroundArea.setStep(_step);
		antiGroundArea.setResult(_result);
		
		// -----------------------------------------------------------------------------------//

		return antiGroundArea;
	}

}
