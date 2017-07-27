package kr.co.kncom.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import kr.co.kncom.config.DaoConfig;
import kr.co.kncom.dao.MarketPriceDao;
import kr.co.kncom.domain.AuctionList;
import kr.co.kncom.domain.AuctionList2;
import kr.co.kncom.repository.AuctionListRepository;
import kr.co.kncom.repository.AuctionListRepository2;
import kr.co.kncom.repository.SidogusRepository;
import kr.co.kncom.util.FileUtil;
import kr.co.kncom.util.StringUtil;
import kr.co.kncom.vo.MarketPriceVO;

@Service
public class AuctionListService {

	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoConfig.class);
	private MarketPriceDao marketPriceDao = context.getBean(MarketPriceDao.class);

	@Autowired
	AuctionListRepository auctionListRepository;
	@Autowired
	AuctionListRepository2 auctionListRepository2;
	@Autowired
	private SidogusRepository sidogusRepository;
	
	public List<AuctionList> getAuctionListData(String bidDate) throws UnsupportedEncodingException {

		AuctionList auctionListVO = null;
		List<AuctionList> rtnAuctionList = new ArrayList<AuctionList>();

		List<AuctionList> auctionData = auctionListRepository.findBySaledayStartingWithOrderBySaledayAsc(bidDate);

		for (AuctionList tmpData : auctionData) {
			auctionListVO = new AuctionList();

			auctionListVO = tmpData;

			auctionListVO.setInd(new String(tmpData.getInd().getBytes("iso-8859-1"), "euc-kr"));
			auctionListVO.setAddress(new String(tmpData.getAddress().getBytes("iso-8859-1"), "euc-kr"));
			auctionListVO.setGubun(new String(tmpData.getGubun().getBytes("iso-8859-1"), "euc-kr"));
			auctionListVO.setResult(new String(tmpData.getResult().getBytes("iso-8859-1"), "euc-kr"));

			if (tmpData.getAppraisedvalue().length() > 0 && StringUtil.isStringDouble(tmpData.getAppraisedvalue())) {
				auctionListVO
						.setAppraisedvalue(String.format("%,d", Long.parseLong(tmpData.getAppraisedvalue()) / 10000));
			} else {
				continue;
			}

			if (tmpData.getLowestvalue().length() > 0 && StringUtil.isStringDouble(tmpData.getLowestvalue())) {
				auctionListVO.setLowestvalue(String.format("%,d", Long.parseLong(tmpData.getLowestvalue()) / 10000));
			} else {
				continue;
			}

			if (tmpData.getSalevalue().length() > 0 && StringUtil.isStringDouble(tmpData.getSalevalue())) {
				auctionListVO.setSalevalue(String.format("%,d", Long.parseLong(tmpData.getSalevalue()) / 10000));
			} else {
				continue;
			}

			rtnAuctionList.add(auctionListVO);
		}

		return rtnAuctionList;
	}

	public String getMarketPriceList(Map<String, String> params) {

		List<MarketPriceVO> marketPriceList = new ArrayList<MarketPriceVO>();
		Gson gson = new Gson();

		// obj -> json 변환
		marketPriceList = marketPriceDao.getMarketPriceList(params);
		String jsonStr = gson.toJson(marketPriceList);

		return jsonStr;
	}

	/**
	 * 경매 데이터를 입력한다.
	 * 
	 * @param auctionList
	 */
	public void insertAuctionList() {

		//final String fileDirPrefix = "P:\\경매가경기도\\dat\\";
		final String fileDirPrefix = "P:\\error\\";
		final String backDirPrefix = "P:\\error2nd\\";
		AuctionList2 auctionList = new AuctionList2();
		
		List<String> fileList = FileUtil.getXmlFileNameList(fileDirPrefix);

		String _tmpFileStr = null;
		Document _doc = null;
		for (String _str : fileList) {

			System.out.println("## F I L E ==> " + _str);

			try {
				_tmpFileStr = FileUtil.readFileToString(fileDirPrefix + _str);
				_doc = Jsoup.parseBodyFragment(_tmpFileStr);

				parseAuctionHtml(_doc, _str.split("\\.")[0]);

			} catch (IOException e) {
				
				e.printStackTrace();
				
				// 오류 파일 복사
				FileInputStream inputStream;
				try {
					inputStream = new FileInputStream(fileDirPrefix+_str);
					
					FileOutputStream outputStream = new FileOutputStream(backDirPrefix+_str);
					
					FileChannel fcin =  inputStream.getChannel();
					FileChannel fcout = outputStream.getChannel();
					
					long size = fcin.size();
					fcin.transferTo(0, size, fcout);
					
					fcout.close();
					fcin.close();
					
					outputStream.close();
					inputStream.close();
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}        
			}

		}

		// 데이터 입력
		// auctionListRepository2.save(auctionList);

	}

	private AuctionList2 parseAuctionHtml(Document doc, String index) throws IOException {

		AuctionList2 auctionList2 = new AuctionList2();

		Element element = null;
		
		//try {

			// element = doc.select("title").first();
			// 인덱스
			auctionList2.setInd(index);
			System.out.println("## index ==> " + index);
	
			// 주소
			element = doc.select(".td_1").get(0);
			// 도로명 주소 버리기
			String[] _doroArr = element.text().split("\\(");
			String address = null;
			
			if (_doroArr.length > 1) {
				address = _doroArr[0].replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
			} else {
				address = element.text();
			}
			System.out.println("## address ==> " + address);
			
			auctionList2.setAddress(address);
			
			String sidogu 	= null;
			String dong 	= null;
			int bunji1		= 0;
			int bunji2		= 0;
			
			int si_ind		= 1;
			String[] _arrBunji;
		
			
			if (element.text() != null) {
				
				// 주소파싱
				String[] _address = element.text().split(" ");
				
				System.out.println("## 주소 파싱 ==> " + _address.length);
				
				if (_address[0].contains("서울")) {
					si_ind = 1;
					sidogu = _address[0] + "특별시" + " " + _address[1];
					sidogu = sidogu.replaceAll("[★]", "");
					dong = _address[2];
					
					_arrBunji = _address[3].split("-");
					
				} else { // 경기도
					si_ind = 2;
					// split 결과가 4이면 구 포함 경기도 고양시 덕양구 고양동
					if (_address[2].endsWith("동")) {
						sidogu = _address[0] + " " + _address[1];
						dong = _address[2];
						
						_arrBunji = _address[3].split("-");
					} else if(_address[2].endsWith("읍") || _address[2].endsWith("면")) { // 읍이 있는 경우
						sidogu = _address[0] + " " + _address[1];
						dong = _address[2] + " " + _address[3];
						
						_arrBunji = _address[4].split("-");
					} else {
						sidogu = _address[0] + " " + _address[1] + " " + _address[2];
						dong = _address[3];
						
						_arrBunji = _address[4].split("-");
	 				}
	 				
				}
				
				if (_arrBunji.length > 1) {
					if (!StringUtil.isStringDouble(_arrBunji[0])) {
						System.out.println("### 번지가 숫자타입이 아님!!!!");
						return null;
					} else {
						bunji1 = Integer.parseInt(_arrBunji[0].replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
						bunji2 = Integer.parseInt(_arrBunji[1].replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
					}
					
				} else {
					if (StringUtil.isStringDouble(_arrBunji[0])) {
						bunji1 = Integer.parseInt(_arrBunji[0].replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
					} else {
						System.out.println("### 번지가 숫자타입이 아님!!!!");
						return null;
					}
				}
				
				sidogu = sidogu.replaceAll("경기", "경기도");
				
				System.out.println("## sidogu ==> " + sidogu);
				System.out.println("## dong ==> " + dong);
				System.out.println("## bunji1 ==> " + bunji1);
				System.out.println("## bunji2 ==> " + bunji2);
			}
		
		
			// 구분
			element = doc.select(".td_1").get(3);
			System.out.println("## gubun ==> " + element.text());
			auctionList2.setGubun(element.text());

			// 매각일
			element = doc.select(".td_1").get(5);
			System.out.println("### 매각정보 ===> " + element.text());
			
			// 매각일 존재 여부 체크
			String[] _saleDayArr = element.text().split("\\.");
			if (_saleDayArr.length > 1) {
				// substr로 날짜만 우선 가져온다
				auctionList2.setSaleDay(StringUtil.convertStrToDate(element.text().replaceAll("[★]", "").substring(0, 8)));
				System.out.println("## saleday ==> " + StringUtil.convertStrToDate(element.text().replaceAll("[★]", "").substring(0, 8)));
			} else if (element.text().contains("집행정지")) {
				// 결과
				auctionList2.setResult(element.text());
				System.out.println("## result ==> " + element.text());
			}
			
			// 매각가 - 취하- 기각
			System.out.println("## saleValue ==> " + convertPriceDayToPriceOrder(element.text()));
			auctionList2.setSaleValue(convertPriceDayToPriceOrder(element.text()));

			// 감정가
			element = doc.select(".td_1").get(6);
			System.out.println("## source data ==> " + element.text());
			auctionList2.setAppraisedValue(convertPricePriceToOtherOrder(element.text()));
			System.out.println("## appraisedValue ==> " + convertPricePriceToOtherOrder(element.text()));
			
			//종국일자
			element = doc.select(".td_1").get(8);
			
			System.out.println("## element ==> " + element.text());
			if (element.text().length() > 1) {
				if (element.text().contains("미정")){
					// 결과
					auctionList2.setResult(element.text());
					System.out.println("## result ==> " + element.text());
				} else if (element.text().startsWith("..")) {
					// 결과
					auctionList2.setResult(element.text().substring(3));
					System.out.println("## result ==> " + element.text().substring(3));
				} else {
					auctionList2.setCloseDay(StringUtil.convertStrToDate(element.text().replaceAll("[★]", "").substring(0, 8)));
					System.out.println("## closeDay ==> " + StringUtil.convertStrToDate(element.text().replaceAll("[★]", "").substring(0, 8)));
					
					// 결과
					if(element.text().substring(9).contains("원")) {
						auctionList2.setResult("진행중");
						System.out.println("## result ==> " + "진행중");	
					} else {
						auctionList2.setResult(element.text().substring(9));
						System.out.println("## result ==> " + element.text().substring(9));
					}
				}
			}
			
			// 최저가
			element = doc.select(".td_1").get(9);
			auctionList2.setLowestValue(convertPricePriceToOtherOrder(element.text()));
			System.out.println("## lowestValue ==> " + convertPricePriceToOtherOrder(element.text()));

			// 토지면적
			element = doc.select(".td_1").get(10);
			auctionList2.setLandArea(extractArea(element.text()));
			System.out.println("## landArea ==> " + extractArea(element.text()));
			
			// 토지지분비율
			auctionList2.setLandRate(calcJibunRate(element.text()));
			System.out.println("## landAreaRate ==> " + calcJibunRate(element.text()));
			
			// 개시일자
			element = doc.select(".td_1").get(11);
			if (element.text().length() > 1) {
				auctionList2.setOpenDay(StringUtil.convertStrToDate(element.text().replaceAll("[★]", "").substring(0, 8)));
				System.out.println("## openDay ==> " + StringUtil.convertStrToDate(element.text().replaceAll("[★]", "").substring(0, 8)));
			}
			// 건물면적
			element = doc.select(".td_1").get(13);
			auctionList2.setBuildingArea(extractArea(element.text()));
			System.out.println("## buildingArea ==> " + extractArea(element.text()));
			
			// 건물지분비율
			auctionList2.setBuildingRate(calcJibunRate(element.text()));
			System.out.println("## buildAreaRate ==> " + calcJibunRate(element.text()));
			
			// 배당종기일
			element = doc.select(".td_1").get(14);
			if (element.text().length() > 1) {
				auctionList2.setAllotDay(StringUtil.convertStrToDate(element.text().replaceAll("[★]", "").substring(0, 8)));
				System.out.println("## AllotDay ==> " + StringUtil.convertStrToDate(element.text().replaceAll("[★]", "").substring(0, 8)));
			}
			
			element = doc.select(".td_1").get(16);
			if (!element.text().contains("5분이상")){
				auctionList2.setAttention(element.text());
				System.out.println("## 주의사항 ==> " + element.text());
			}
			
			// 시, 군구 코드 찾아서 입력
			// 시코드
			auctionList2.setSi_ind(si_ind);
			// 구코드
			auctionList2.setSidogus_ind(sidogusRepository.findSidonguCodeSQL(sidogu, si_ind));
			System.out.println("sidonggus ==> " + auctionList2.getSidogus_ind());
			
			int _dong = sidogusRepository.findDongCodeSQL(dong, auctionList2.getSidogus_ind(), si_ind);
			if (_dong == -1) {
				System.out.println("## 주소매칭이 안됨!!!");
				return null;
			} else {
				
				auctionList2.setDongs_ind(_dong);
				System.out.println("dong ==> " + _dong);
				
			}
			// 번지 추가
			auctionList2.setBunji1(bunji1);
			auctionList2.setBunji2(bunji2);
			
			System.out.println("############################################################");
			
			// 입력
			auctionListRepository2.save(auctionList2);
			
		//} catch (Exception e) {
			//throw new IOException();
		//}
		
		return null;
	}

	/**
	 * 
	 * @param srcPrice
	 *            70,000,000 (07.08.06) 형태의 데이터를 가격만 추출하여 Long타입으로 변환한다.
	 * @return
	 */
	private Long convertPriceDayToPriceOrder(String srcPrice) {

		boolean havePrice = srcPrice.contains("원");
		Long rtnPrice = 0L;

		if (havePrice) {
			rtnPrice = Long.parseLong(srcPrice.split("\\(")[1].replaceAll("[\\(\\)\\,원]", "").replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
		}

		return rtnPrice;
	}

	/**
	 * 
	 * @param srcPrice
	 *            44,800,000 (64%) 형태의 데이터에서 가격만 추출하여 Long타입으로 변환한다.
	 * @return
	 */
	private Long convertPricePriceToOtherOrder(String srcPrice) {
		Long rtnPrice = 0L;

		String _str = srcPrice.split("\\(")[0].replaceAll("[\\,★]", "").replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
		rtnPrice = Long.parseLong(_str);

		return rtnPrice;
	}

	/**
	 * 
	 * @param srcStr
	 * @return
	 */
	private float calcJibunRate(String srcStr) {
		float rtnRate = 0f;

		boolean isRate = srcStr.contains("지분");

		if (isRate) {
			String[] _area = srcStr.split("중");
			
			float allArea = Float.parseFloat(_area[0].replaceAll("[전체㎡★]", "").replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
			float jibunArea = Float.parseFloat(_area[1].split("\\(")[0].replaceAll("[지분㎡★]", "").replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
			
			System.out.println("## allArea ==> " + allArea + " : " + "## jibunArea ==> " + jibunArea);
			rtnRate = jibunArea/allArea;
		}

		return rtnRate;
	}
	
	/**
	 * 면적을 추출한다.
	 * 
	 * @param srcArea
	 * @return
	 */
	private float extractArea(String srcArea) {
		
		float rtnArea = 0f;
		
		boolean existJesi = srcArea.contains("제시외");
		
		// 제시외 존재 여부
		if (existJesi) {
			
			rtnArea = Float.parseFloat(srcArea.split("제시외")[0].split("㎡")[0].replaceAll("[전체㎡★\\,]", "").replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
			
		} else {
			boolean existJibun = srcArea.contains("지분");
			
			if (existJibun) {
				
				rtnArea = Float.parseFloat(srcArea.split("중")[0].replaceAll("[전체㎡★\\,]", "").replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
				
			} else {
				rtnArea = Float.parseFloat(srcArea.replaceAll("[전체㎡★\\,]", "").replaceAll("(^\\p{Z}+|\\p{Z}+$)", "").substring(0, 5));
			}
		}
		
		return rtnArea;
		
	}
}
