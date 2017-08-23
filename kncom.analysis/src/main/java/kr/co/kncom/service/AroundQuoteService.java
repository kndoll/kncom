package kr.co.kncom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.kncom.domain.AroundQuote;
import kr.co.kncom.domain.Sidos;
import kr.co.kncom.repository.AroundQuoteRepository;
import kr.co.kncom.repository.SidogusRepository;
import kr.co.kncom.repository.SidosRepository;
import kr.co.kncom.util.FileUtil;
import kr.co.kncom.util.StringUtil;

@Service
public class AroundQuoteService {
	
	@Autowired
	private SidosRepository sidosRepository;
	
	@Autowired
	private SidogusRepository sidogusRepository;
	
	@Autowired
	private AroundQuoteRepository aroundQuoteRepository;
	
	// 수집 월
	@Value("${month}")
	private String month;
	
	// 대장 루트 경로
	@Value("${daejangRootDir}")
	private String daejangRootDir; 
	
	// 시세 루트 경로
	@Value("${newLobigsisepath}")
	private String newLobigsisepath;
	private String averSuffix = "_aver.dat";
	
	// 층별 세대 데이터 파일 이름
	@Value("${floorDataFileName}")
	private String floorDataFileName;
	
	// 파일 저장 경로
	@Value("${writeFilePath}")
	private String writeFilePath;
	
	/**
	 * 
	 * 시동구별 주변 시세 정보 입력
	 * 
	 * 
	 * 
	 */
	public void insertAroundQuote() {
		
		Map<String, String> params = new HashMap<>();
		List<AroundQuote> aroundQuoteList = new ArrayList<AroundQuote>();
		
		String key = null;
		String type = null;

		//------------------------------------------------------------//
		// #1. 시 데이터 
		// 서울 지역만 가져옴
		List<Sidos> sidos = sidosRepository.findByInd(1);
		//List<Sidos> sidos = sidosRepository.findAll();
		
		
		
		for (Sidos _sidos : sidos) {
			params.put("key", "1");
			params.put("type", "1");
			params.put("si", String.valueOf(_sidos.getInd())); // 서울만
			aroundQuoteList.add(buildAroundQuote(params));
		}
		//------------------------------------------------------------//
		
		//------------------------------------------------------------//
		// #2. 구 데이터
		List<String> sidogus = sidogusRepository.findSidogusFilePath(1);
		String[] sidogusArr = null;
		
		for (String _data : sidogus) {
			
			sidogusArr = _data.split("\\\\");
			
			params.put("key", sidogusArr[0]+sidogusArr[1]);
			params.put("type", "2");
			params.put("si", "1"); // 서울만
			params.put("sreg", sidogusArr[1]);
			
			aroundQuoteList.add(buildAroundQuote(params));
		}
		//------------------------------------------------------------//
		
		//------------------------------------------------------------//
		// #3. 동 데이터
		List<String> newDongs = sidogusRepository.findDongFilePath(1);
		String[] dongsArr = null;
		
		for (String _data : newDongs) {
			
			dongsArr = _data.split("\\\\");
			
			params.put("key", dongsArr[0]+dongsArr[1]+dongsArr[2]);
			params.put("type", "3");
			params.put("si", "1"); // 서울만
			params.put("sreg", dongsArr[1]);
			params.put("seug", dongsArr[2]);
			
			aroundQuoteList.add(buildAroundQuote(params));
		}
		//------------------------------------------------------------//
		
		// data insert
		aroundQuoteRepository.save(aroundQuoteList);
		
		// 파일 출력
		
		// DB 셀렉트
		List<AroundQuote> aroundQuoteResult = aroundQuoteRepository.findAll();
		
		List<String> contentList = new ArrayList<String>();
		StringBuilder content = null; 
		
		for (AroundQuote _data : aroundQuoteResult) {
			content = new StringBuilder();
			
			// 스키마의 길이만큼 전문을 생성한다.
			content.append(String.format("%18s", _data.getKey()));
			content.append(String.format("%1s", _data.getType()));
			content.append(String.format("%1s", _data.getSi()));
			content.append(String.format("%5s", _data.getSreg()));
			content.append(String.format("%5s", _data.getSeug()));
			content.append(String.format("%6s", _data.getMonth()));
			content.append(String.format("%6s", _data.getSedecount()));
			content.append(String.format("%32s", _data.getMemeChange()));
			content.append(String.format("%32s", _data.getMemCValue()));
			
			contentList.add(content.toString());
		}
		
		// 파일 생성
		if (contentList.size() > 0) {
			FileUtil.writeFileFromArr(writeFilePath, contentList);
		}
	}
	
	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	private AroundQuote buildAroundQuote(Map<String, String> params) {
		
		AroundQuote rtnAroundQuote = new AroundQuote();
		String filePath = null;
		String si = params.get("si");
		String sreg = params.get("sreg");
		String seug = params.get("seug");
		
		// 기본데이터 setting
		rtnAroundQuote.setKey(params.get("key"));
		rtnAroundQuote.setType(params.get("type"));
		rtnAroundQuote.setSi(si);
		rtnAroundQuote.setMonth(month);
		
		filePath = si + "\\";
		if (sreg != null && sreg.length() > 0) {
			rtnAroundQuote.setSreg(sreg);
			filePath += sreg + "\\";
		}
		
		if (seug != null && seug.length() > 0) {
			rtnAroundQuote.setSeug(seug);
			filePath += seug + "\\";
		}
		
		// 세대수를 가져온다.
		String sedeCount = String.valueOf(getSedaesu(daejangRootDir + filePath + floorDataFileName));
		rtnAroundQuote.setSedecount(sedeCount);
		
		// 매매변동액
		String memeChange = getTradingChange(filePath);
		rtnAroundQuote.setMemeChange(memeChange);
		
		// 매매평가시세
		String memeCValue = getTradingValuation(newLobigsisepath + filePath + month + averSuffix);
		rtnAroundQuote.setMemCValue(memeCValue);
		
		return rtnAroundQuote;
		
	}
	
	/**
	 * 세대수를 가져온다.
	 * 
	 * @param filePath
	 * @return
	 */
	private String getSedaesu(String filePath) {
		
		String rtnSedaeSu = null;
		
		if (FileUtil.isExistFile(filePath)) {
			
			Map<String, Object> floorInfo = FileUtil.getMapFromXml(filePath);
			rtnSedaeSu = floorInfo.get("TOTALHOUSE").toString();
		}
		
		return rtnSedaeSu;
	}
	
	/**
	 * 매매변동액을 가져온다.
	 * 
	 * @return
	 */
	private String getTradingChange(String fileKeyPath) {
		String rtnMemeChange = null;
						
		String prevMonth = StringUtil.getDateFromTargetDate(-6, month);
		String prevAverFilePath = newLobigsisepath + fileKeyPath + prevMonth + averSuffix;
		String averFilePath = newLobigsisepath + fileKeyPath + month + averSuffix;
		
		if (FileUtil.isExistFile(prevAverFilePath) && FileUtil.isExistFile(averFilePath)) {
		
			String[] prevAverArr = FileUtil.readFileToString(prevAverFilePath).split("\\|");
			Double prevAver = Double.parseDouble(prevAverArr[(prevAverArr.length)-1]);
			
			Double aver = Double.parseDouble(getTradingValuation(averFilePath));
			
			System.out.println("## prevAver ==> " + prevAver);
			System.out.println("## aver ==> " + aver);
			
			if (Double.compare(prevAver, 0) > 0) {
				rtnMemeChange = String.valueOf(Math.round(aver - prevAver));
			}
		}
		
		return rtnMemeChange;
	}
	
	/**
	 * 매매평가액을 가져온다.
	 * 
	 * @return
	 */
	private String getTradingValuation(String filePath) {
		
		String rtnMemeCValue = null;
		
		if (FileUtil.isExistFile(filePath)) {
			
			String[] averArr = FileUtil.readFileToString(filePath).split("\\|");
			rtnMemeCValue = averArr[(averArr.length)-1];
		}
		
		return rtnMemeCValue;
	}
}
