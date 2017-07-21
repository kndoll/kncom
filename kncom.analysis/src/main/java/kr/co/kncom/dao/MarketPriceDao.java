package kr.co.kncom.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.google.gson.Gson;

import kr.co.kncom.util.FileUtil;
import kr.co.kncom.util.StringUtil;
import kr.co.kncom.vo.MarketPriceVO;

/**
 * 
 * @author kndoll
 *
 */
public class MarketPriceDao {

	// 아래 데이터는 properties로 뽑아내야 함.
	private final String marketPriceDirPrefix = "P:\\newvalues\\1\\";
	private final String areaDirPathPrefix = "F:\\201704lobig\\1\\";
	private final String djgiDirPrefix = "J:\\Tjiy\\1\\";
	
	private final String bjibunFileName = "daejang_bsjibun.dat";
	private final String djgiFileName = "djgi.dat";
	
	private Gson gson = new Gson();
	
	public List<MarketPriceVO> getMarketPriceList(Map params) {

		String index = params.get("index").toString();

		String dirParh = "";

		String[] _dirPathArr = null;

		// 파일 경로를 찾는다.
		// 파일 경로가 없으면 null를 리턴한다.
		if (index != null) {
			_dirPathArr = index.split("_");

			for (String str : _dirPathArr) {
				dirParh += str + "\\";
			}
		}

		List<MarketPriceVO> rtnMarketPriceList = new ArrayList<MarketPriceVO>();
		List<String> fileList = FileUtil.getXmlFileNameList(marketPriceDirPrefix + dirParh);

		// 조건에 맞는 파일만 필터링을 한다.
		// 조건 : 검색 년월에 12개이전 데이터
		if (fileList != null) {
			// 파일을 읽어 vo 에 setting
			String _tmpFileStr;
			MarketPriceVO _marketPriceVO;

			// bidDate
			String bidDate = "20" + params.get("bidDate").toString();
			String[] _bidDateArr = bidDate.split("\\.");
			String targetDate = _bidDateArr[0] + _bidDateArr[1];
			String calcDate = null;

			String[] _fileNameArr = null;
			String _fileName = null;
			boolean isCorrectFile = false;
			String compareStr = null;

			int fileDate = 0;
			
			String type = params.get("type").toString();
			
			for (String fileName : fileList) {
				_tmpFileStr = null;
				_marketPriceVO = null;

				compareStr = fileName.split("\\.")[0];

				isCorrectFile = compareStr.endsWith(params.get("suffix").toString());

				// sunffix와 날짜 범위에 맞지 않으면 skip 한다.
				if (!isCorrectFile) {
					// System.out.println("## SKIP FILE - SUFFIX NOT MATCHING!!!
					// ");
					continue;
				} else {
					
					_fileNameArr = fileName.split("_");
					_fileName = _fileNameArr[0];

					fileDate = Integer.parseInt(_fileName);
					
					if (type.equals("12")) {
						// 12개월 이전 데이터
						boolean isTargetDate = (fileDate <= Integer.parseInt(targetDate));
						boolean isCalcDate = false;
						
						calcDate = StringUtil.getDateFromTargetDate(-12, targetDate);
						isCalcDate = (fileDate >= Integer.parseInt(calcDate));
						
						if (!(isTargetDate && isCalcDate)) {
							// System.out.println("#### SKIP FILE - DATE NOT
							// MATCHING!!!");
							continue;
						}
					} else if(type.equals("6")){ // 6개월 데이터
						calcDate = StringUtil.getDateFromTargetDate(-6, targetDate);
						
						//System.out.println("_fileName ==> " + _fileName + " calcDate ==> " + calcDate + " targetDate ==> " + targetDate);
						if (!_fileName.equals(calcDate)) {
							continue;
						}
					}
					
				}

				try {

					System.out.println("######################################");
					System.out.println("## fileName ==> " + fileName);
					System.out.println("## T A R G E T - D A T E => " + targetDate);
					System.out.println("## C A L C - D A T E => " + calcDate);
					System.out.println("## P V  => " + params.get("suffix").toString());

					_tmpFileStr = FileUtil.readFileToString(marketPriceDirPrefix + dirParh + fileName);

					// System.out.println("### " + _tmpFileStr);

					JSONObject _jsonObj = XML.toJSONObject(_tmpFileStr, true);

					// JSON TO MAP
					Map<String, Object> map = new HashMap<String, Object>();
					map = (Map<String, Object>) gson.fromJson(_jsonObj.toString(), map.getClass());

					Map<String, Object> dong = (Map<String, Object>) map.get("PREDICTVALUE");
					// _marketPriceVO = gson.fromJson(_jsonObj.toString(),
					// MarketPriceVO.class);
					// System.out.println("## dong ==> " + dong);

					Set<String> dongKeys = dong.keySet();

					Map<String, Object> ho = new HashMap<String, Object>();

					for (String dongKey : dongKeys) {
						
						// System.out.println("## dongKey ==> " + dongKey);

						ho = (Map<String, Object>) dong.get(dongKey);

						Set<String> hoKeys = ho.keySet();

						Map<String, Object> data = new HashMap<String, Object>();
						for (String hoKey : hoKeys) {
							_marketPriceVO = new MarketPriceVO();

							// System.out.println("## hoKeys ==> " + hoKey);

							data = (Map<String, Object>) ho.get(hoKey);

							_marketPriceVO.setBidName(params.get("bidName").toString());
							_marketPriceVO.setDate(fileName.split("_")[0]);

							_marketPriceVO.setSidogus_ind(_dirPathArr[0]);
							_marketPriceVO.setDongs_ind(_dirPathArr[1]);
							_marketPriceVO.setBunji1(_dirPathArr[2]);
							_marketPriceVO.setBunji2(_dirPathArr[3]);

							_marketPriceVO.setDong(dongKey.split("_")[1]);
							_marketPriceVO.setHo(hoKey.split("_")[1]);

							_marketPriceVO.setCenterValue(data.get("CENTERVALUE").toString());
							_marketPriceVO.setSang(data.get("SANG").toString());
							_marketPriceVO.setHa(data.get("HA").toString());
							_marketPriceVO.setArea(data.get("AREA").toString());
							_marketPriceVO.setScount(data.get("SCOUNT").toString());

							// 전유공용면적
							_marketPriceVO.setJygyArea(getJygyAreaCalc(areaDirPathPrefix + dirParh,
									dongKey.split("_")[1], hoKey.split("_")[1],
									Float.parseFloat(params.get("buildingArea").toString())));

							// 대지면적
							_marketPriceVO.setDigiArea(getDjgiAreaCalc(dirParh, dongKey.split("_")[1],
									hoKey.split("_")[1], Float.parseFloat(params.get("landArea").toString())));

							rtnMarketPriceList.add(_marketPriceVO);
						}

					}

				} catch (IOException | JSONException e) {
					e.printStackTrace();
				}
			}
		} else {
			rtnMarketPriceList = null;
		}

		return rtnMarketPriceList;
	}

	/**
	 * 전유공용면적을 가져온다.
	 * 
	 * @param areaDirPath
	 */
	private float getJygyAreaCalc(String areaDirPath, String dong, String ho, float buildingArea) {

		float rtnVal = 0;

		List<String[]> jyRawList = FileUtil.readFileToStringArrayList(areaDirPath + "daejang_jygyarea.dat", "|");

		float _jygy = 0;
		float jygy_fix = 0;

		for (String[] _strArr : jyRawList) {

			// System.out.println("## TRY MATCH ==> " + _strArr[21] + " : " +
			// dong);
			// 동 여부 체크
			if (dong.equals("NONE")) { // 동이 없는 경우
				if (_strArr[22].equals(ho + "호") && _strArr[27].equals("전유")) {
					// System.out.println("## DONG MATCH ==> " + _strArr[21] + "
					// : " + dong);
					System.out.println("## HO MATCH ==> " + _strArr[22] + " : " + ho);

					_jygy = Float.parseFloat(_strArr[37]);

				}
			} else { // 동이 존재하는 경우
				if (_strArr[21].equals(dong + "호") && _strArr[22].equals(ho + "호") && _strArr[27].equals("전유")) {
					System.out.println("## DONG MATCH ==> " + _strArr[21] + " : " + dong);
					System.out.println("## HO MATCH ==> " + _strArr[22] + " : " + ho);

					_jygy = Float.parseFloat(_strArr[37]);
				}
			}

			if (_jygy > 0) {
				System.out.println("##################################");
				System.out.println("## buildingArea : " + buildingArea);
				System.out.println("## _jygy : " + _jygy);

				// 오차범위 체크 +-1
				if (!(buildingArea + 1 >= _jygy && buildingArea - 1 <= _jygy)) {
					System.out.println("## NOT MATCH 전유공용면적!!!");
					_jygy = 0;
				} else {
					System.out.println("## MATCH 전유공용면적!!!");
					// 건물면적과 가장 근사한 값을 최종값으로 결정
					if (jygy_fix > 0) {
						jygy_fix = _jygy < jygy_fix ? _jygy : jygy_fix;
					} else {
						jygy_fix = _jygy;
					}
				}

				System.out.println("##################################");
			}
		}

		rtnVal = jygy_fix;

		return rtnVal;
	}

	/**
	 * 대지면적을 계산한다.
	 * 
	 * @param areaDirPath
	 * @param dong
	 * @param ho
	 * @param landArea
	 * @return
	 */
	protected float getDjgiAreaCalc(String dirParh, String dong, String ho, float landArea) {

		float rtnVal = 0;

		System.out.println("## LAND AREA ==> " + landArea);

		// #1. 경로에 부속지번 데이터가 존재하는지 확인
		List<String> fileList = FileUtil.getXmlFileNameList(areaDirPathPrefix + dirParh);
		
		// 부속지번 파일이 존재하는지 체크
		boolean isExistBsjibunFile = false;
		
		if (fileList != null) {
			for (String _fileName : fileList) {
				if (_fileName.equals(bjibunFileName)) {
					isExistBsjibunFile = true;
					break;
				}
			}
		}
		
		// #.1 주지번의 대지정보를 가져온다.
		rtnVal = getDjgiAreaFromDjgi(dirParh, dong, ho, landArea);
	
		// 부속지번이 존재하는 경우
		if (isExistBsjibunFile) {
			// #.2 부속지번 파일을 읽는다.
			List<String[]> bsjibunList = FileUtil.readFileToStringArrayList(areaDirPathPrefix+dirParh+"daejang_bsjibun.dat", "|");
			
			StringBuilder _bsJibunFilePath;
			
			for (String[] _arr : bsjibunList) {
				_bsJibunFilePath = new StringBuilder();
				_bsJibunFilePath.append("//");
				_bsJibunFilePath.append(_arr[23]);
				_bsJibunFilePath.append("//");
				_bsJibunFilePath.append(_arr[24]);
				_bsJibunFilePath.append("//");
				_bsJibunFilePath.append(String.valueOf(Integer.parseInt(_arr[26])));
				_bsJibunFilePath.append("//");
				_bsJibunFilePath.append(String.valueOf(Integer.parseInt(_arr[27])));
				_bsJibunFilePath.append("//");
				
				// rtnVal += getDajiAreaFromPyojenu(_bsJibunFilePath.toString(), dong, landArea);
				rtnVal += getDjgiAreaFromDjgi(_bsJibunFilePath.toString(), dong, ho, landArea);
			}
		}
		
		return rtnVal;
	}
	
	/**
	 * 표제부에서 대지면적을 가져온다.
	 * 
	 * @param dirParh
	 * @param dong
	 * @param landArea
	 * @param _dajiArea
	 * @return
	 */
	private float getDajiAreaFromPyojenu(String dirParh, String dong, float landArea) {
		
		float rtnVal = 0;
		float _dajiArea = 0;
		
		// 표제부에서 대지면적을 가져온다.
		List<String[]> pojebuList = FileUtil.readFileToStringArrayList(areaDirPathPrefix+dirParh+"daejang_pyojebu.dat", "|");
		
		String _dong = null;
		
		boolean isExistDong = pojebuList.size() > 1 ? true : false;
		
		for(String[] _arr : pojebuList) {
			// 동 존재 여부
			if (isExistDong) {
				_dong = _arr[22];
				if(_dong.equals(dong+"동")) {
					System.out.println("!! DONG-MATCH!! ==> " + dong);
					_dajiArea = Float.parseFloat(_arr[25]);
				}
			} else {
				_dajiArea = Float.parseFloat(_arr[25]);
			}
			
			// 오차 범위 체크
			if (landArea+5 > _dajiArea && landArea-5 < _dajiArea) {
				rtnVal = _dajiArea;
			} else {
				System.out.println("## 오차범위 벗어남 ==> " + landArea + " : " + _dajiArea);
			}
		}
		
		return rtnVal;
	}
	
	/**
	 * 대지권에서 대지면적을 가져온다.
	 * 
	 * @param dirParh
	 * @param dong
	 * @param ho
	 * @param landArea
	 * @return
	 */
	private float getDjgiAreaFromDjgi(String dirParh, String dong, String ho, float landArea) {
		
		float rtnVal = 0;
		float _dajiArea = 0;
		
		// 대지권등록정보를 가져온다.
		List<String[]> dajiList = FileUtil.readFileToStringArrayList(djgiDirPrefix+dirParh+djgiFileName, ",");
		
		boolean isExistDong = false;
		for (String[] _arr : dajiList) {

			isExistDong = _arr[8].equals("0000") ? false : true;
			
			// 동이 존재하는 경우
			if (isExistDong) {
				if(_arr[8].equals(dong) && _arr[10].equals(ho)) {
					_dajiArea = Float.parseFloat(_arr[12].split("\\/")[0]);
				}
			} else {
				if (_arr[10].equals(ho)) {
					_dajiArea = Float.parseFloat(_arr[12].split("\\/")[0]);
				}
			}
			
			// 오차 범위 체크
			if (_dajiArea > 0) {
				if (landArea+5 > _dajiArea && landArea-5 < _dajiArea) {
					rtnVal = _dajiArea;
				} else {
					System.out.println("## 오차범위 벗어남 ==> " + landArea + " : " + _dajiArea);
				}
			}
		}
		
		return rtnVal;
	}
}
