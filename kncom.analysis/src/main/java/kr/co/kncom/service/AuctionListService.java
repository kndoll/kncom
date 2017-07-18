package kr.co.kncom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import kr.co.kncom.dao.MarketConditionDao;
import kr.co.kncom.vo.MarketPriceVO;

public class AuctionListService {

	private MarketConditionDao marketConditionDao = new MarketConditionDao();
	
	public String getMarketPriceList(Map params) {

		List<MarketPriceVO> marketPriceList = new ArrayList<MarketPriceVO>();
		Gson gson = new Gson();

		// obj -> json 변환
		marketPriceList = marketConditionDao.getMarketPriceList(params);
		String jsonStr = gson.toJson(marketPriceList);

		return jsonStr;
	}
}
