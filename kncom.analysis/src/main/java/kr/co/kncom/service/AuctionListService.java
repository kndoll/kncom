package kr.co.kncom.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import kr.co.kncom.config.DaoConfig;
import kr.co.kncom.dao.MarketPriceDao;
import kr.co.kncom.domain.AuctionList;
import kr.co.kncom.util.StringUtil;
import kr.co.kncom.vo.MarketPriceVO;

@Service
public class AuctionListService {

	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoConfig.class);
	private MarketPriceDao marketPriceDao = context.getBean(MarketPriceDao.class);

	public List<AuctionList> refineAuctionListData(List<AuctionList> auctionData) throws UnsupportedEncodingException {
		
		AuctionList auctionListVO = null;
		List<AuctionList> rtnAuctionList = new ArrayList<AuctionList>();
		
		for (AuctionList tmpData : auctionData) {
			auctionListVO = new AuctionList();

			// 데이터 변환 - service layer로 이동해야 함.
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

	public String getMarketPriceList(Map params) {

		List<MarketPriceVO> marketPriceList = new ArrayList<MarketPriceVO>();
		Gson gson = new Gson();

		// obj -> json 변환
		marketPriceList = marketPriceDao.getMarketPriceList(params);
		String jsonStr = gson.toJson(marketPriceList);

		return jsonStr;
	}

}
