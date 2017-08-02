package kr.co.kncom.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kncom.repository.JttypelistRepository;
import kr.co.kncom.util.FileUtil;
import kr.co.kncom.util.StringUtil;

@Service
public class AreaRatioService extends SimpleFileVisitor<Path>{
	
	private final String totalPyojebuFileName = "daejang_totalpyojebu.dat";
	private final String jygyAreaFileName = "daejang_jygyarea.dat";
	
	@Autowired
	private JttypelistRepository jttypelist;
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		
		if (file.getFileName().toString().equals("daejang_pyojebu.dat")) {
			//System.out.println("## file ==> " + file.toString());
		
			// 여기서 서비스 호출
			Map<String, String> params  = new HashMap<String, String>();
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

		// 용적률 데이터는 집합건물의 동 기준으로 처리
		List<String[]> pyojebuList = FileUtil.readFileToStringArrayList(filePath, "|");
		
		// 공통변수
		float areaRatio = 0f; // 용적률
		float arCalcTotalArea = 0f; // 용적률 산정 연면적
		float antiGroundArea = 0f; // 대지면적
		
		for (String[] _arr : pyojebuList) {
			// 재계산 대상 파일 여부 확인
			boolean isExcute = isDasedae(_arr);
			if (isExcute) {
				// 데이터 가공
				areaRatio = Float.parseFloat(_arr[30]);
				arCalcTotalArea = Float.parseFloat(_arr[29]);
				antiGroundArea = Float.parseFloat(_arr[25]);
				
				// 용적률 존재 여부
				if (areaRatio > 0) {
					if (arCalcTotalArea > 0 && antiGroundArea > 0) { // step 1-1
					} else if (arCalcTotalArea == 0 && antiGroundArea > 0) { // step 2-1,2
					} else if (arCalcTotalArea > 0 && antiGroundArea == 0) { // step 3-1,2
					} else if (arCalcTotalArea == 0 && antiGroundArea == 0) { // step 4-1,2,3
					}
				} else {
					// STEP 5-1 ~ STEP 5-3
				}
				
				// 데이터 DBMS에 저장
			}
		}

	}

	/**
	 * 용적률 재계산 대상 데이터 인지 확인
	 * 
	 * @return
	 */
	private boolean isDasedae(String[] data) {

		boolean rtnVal = false;

		// #STEP1. 주용도 코드가 "02000" 인지 확인
		if (data[34].equals("02000")) {

			// #STEP2. 해당 지번의 총괄표제부 파일이 존재하는지 확인
			// 파일 위치 계산
			StringBuilder jibunLocation = new StringBuilder();
			jibunLocation.append("F:\\201704lobig\\1\\");
			jibunLocation.append(data[8]);
			jibunLocation.append("\\");
			jibunLocation.append(data[9]);
			jibunLocation.append("\\");
			jibunLocation.append(Integer.parseInt(data[11]));
			jibunLocation.append("\\");
			jibunLocation.append(Integer.parseInt(data[12]));
			jibunLocation.append("\\");

			// 총괄표제부 파일 존재 확인
			if (FileUtil.isExistFile(jibunLocation + totalPyojebuFileName)) {
				// #STEP3. 해당지번의 전유공용면적 파일의 전유공용코드가 1이고,
				// 주용도코드가 2001인 데이터가 1개 이상 존재하는지 확인

				List<String[]> _jygyAreaList = FileUtil.readFileToStringArrayList(jibunLocation + jygyAreaFileName,
						"|");

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
	
	private boolean isDasedaeFromCityTypeHouse(String[] data, StringBuilder jibunLocation) {
		boolean rtnVal = false;
		
		if (isExistOneRoom(Integer.parseInt(data[8]), Integer.parseInt(data[9]), Integer.parseInt(data[11]),
				Integer.parseInt(data[12]))) {
			rtnVal = true;
		} else {
			rtnVal = calcDasedaeFromFloor(jibunLocation + jygyAreaFileName);
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
	 * @param filePath 전유공요면적 파일 위치
	 * @return
	 */
	private boolean calcDasedaeFromFloor(String filePath) {
		
		boolean rtnVal = false;
		
		List<String[]> _jygyAreaList = FileUtil.readFileToStringArrayList(filePath, "|");

		Set<String> floorSet = new HashSet<String>();
		
		for (String[] _arr : _jygyAreaList) {
			if (_arr[26].equals("1") && !_arr[24].equals("지하") && (_arr[34].equals("02002") || _arr[34].equals("02003"))) {
				floorSet.add(_arr[25]);
			}
		}
		
		if (floorSet.size() > 0 && floorSet.size() < 5) {
			rtnVal = true;
		}
		
		return rtnVal;
	}
	
}
