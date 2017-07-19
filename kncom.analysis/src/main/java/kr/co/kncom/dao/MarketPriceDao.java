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
	
	private Gson gson = new Gson();
	
	public List<MarketPriceVO> getMarketPriceList(Map params) {
		
		//StringUtil.printParamter(params);
		
		String index = params.get("index").toString();
		
		String marketPriceDirPath = marketPriceDirPrefix;
		String areaDirPath = areaDirPathPrefix;
		
		String[] _dirPathArr = null;
		
		// 파일 경로를 찾는다.
		// 파일 경로가 없으면 null를 리턴한다.
		if (index != null) {
			_dirPathArr = index.split("_");
			
			for (String str : _dirPathArr) {
				marketPriceDirPath += str + "\\";
				areaDirPath += str + "\\";
			}
		}
		
		List<MarketPriceVO> rtnMarketPriceList = new ArrayList<MarketPriceVO>();
		List<String> fileList = FileUtil.getXmlFileNameList(marketPriceDirPath);
		
		// 조건에 맞는 파일만 필터링을 한다.
		// 조건 : 검색 년월에 12개이전 데이터
		if (fileList != null) {
			// 파일을 읽어 vo 에 setting
			String _tmpFileStr;
			MarketPriceVO _marketPriceVO;
			
			// bidDate
			String bidDate = "20"+params.get("bidDate").toString();
			String[] _bidDateArr = bidDate.split("\\.");
			String targetDate = bidDate = _bidDateArr[0] + _bidDateArr[1];
			String calcDate = StringUtil.getDateFromTargetDate(-12, targetDate);
			
			String[] _fileNameArr = null;
			String _fileName = null;
			boolean isCorrectFile = false;
			String compareStr = null;
			
			int fileDate = 0;
			
			for (String fileName : fileList) {
					_tmpFileStr = null;
					_marketPriceVO = null;
					
					System.out.println("######################################");
					System.out.println("## fileName ==> " + fileName);
					System.out.println("## T A R G E T - D A T E => " + targetDate);
					System.out.println("## C A L C - D A T E => " + calcDate);
					System.out.println("## P V  => " + params.get("suffix").toString());
					compareStr = fileName.split("\\.")[0];
					
					isCorrectFile = compareStr.endsWith(params.get("suffix").toString());
					
					if (!isCorrectFile) {
						System.out.println("## SKIP FILE - SUFFIX NOT MATCHING!!! ");
						continue;
					} else {
						
						_fileNameArr = fileName.split("_");
						_fileName = _fileNameArr[0];
						
						fileDate = Integer.parseInt(_fileName);
						
						boolean isTargetDate = (fileDate <= Integer.parseInt(targetDate)); 
						boolean isCalcDate = (fileDate >= Integer.parseInt(calcDate)); 
						
						if (!(isTargetDate && isCalcDate)) {
							System.out.println("#### SKIP FILE - DATE NOT MATCHING!!!");
							continue;
						} 
					}
					
					
					try {
						_tmpFileStr = FileUtil.readFileToString(marketPriceDirPath+fileName);
						
//						System.out.println("### " + _tmpFileStr);
						
						JSONObject _jsonObj = XML.toJSONObject(_tmpFileStr, true);
						
						// JSON TO MAP
						Map<String, Object> map = new HashMap<String, Object>();
						map = (Map<String, Object>) gson.fromJson(_jsonObj.toString(), map.getClass());
						
						Map<String, Object> dong = (Map<String, Object>) map.get("PREDICTVALUE");
						//_marketPriceVO = gson.fromJson(_jsonObj.toString(), MarketPriceVO.class);
						//System.out.println("## dong ==> " + dong);
						
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
								_marketPriceVO.setJygyArea(getCommonArea(areaDirPath, dongKey.split("_")[1], hoKey.split("_")[1], Float.parseFloat(params.get("buildingArea").toString())));
								
								// ** 대지면적 ** //
								
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
	private float getCommonArea(String areaDirPath, String dong, String ho, float buildingArea) {
		
		float rtnVal = 0;
		
		List<String[]> jyRawList = FileUtil.readFileToStringArrayList(areaDirPath+"daejang_jygyarea.dat");
		
		System.out.println("## CNT ==> " + jyRawList.size());
		
		float _jygy = 0;
		System.out.println("## B U I D I N G - A R E A ==> " + buildingArea);
		
		for (String[] _strArr : jyRawList) {
			
			//System.out.println("## TRY MATCH ==> " + _strArr[21] + " : " + dong);
			
			if (_strArr[21].equals(dong+"동") && _strArr[22].equals(ho+"호") && _strArr[27].equals("전유")) {
				System.out.println("## DONG MATCH ==> " + _strArr[21] + " : " + dong);
				System.out.println("## HO MATCH ==> " + _strArr[22] + " : " + ho);
				
				_jygy = Float.parseFloat(_strArr[37]);
				
				// 
				//System.out.println("## JYGY ==> " + _jygy);
			}
		}
		
		rtnVal = _jygy;
		System.out.println("##################################################");
		
		return rtnVal;
	}
	
	
	
}
