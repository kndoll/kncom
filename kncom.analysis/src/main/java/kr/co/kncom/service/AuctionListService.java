package kr.co.kncom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import kr.co.kncom.dao.MarketConditionDao;
import kr.co.kncom.domain.AuctionList;
import kr.co.kncom.repository.AuctionListRepository;
import kr.co.kncom.vo.MarketPriceVO;

@Component
public class AuctionListService {

	private MarketConditionDao marketConditionDao = new MarketConditionDao();
	
	@Autowired
	private AuctionListRepository auctionListRepository;
	
	public String getMarketPriceList(Map params) {

		List<MarketPriceVO> marketPriceList = new ArrayList<MarketPriceVO>();
		Gson gson = new Gson();

		// obj -> json 변환
		marketPriceList = marketConditionDao.getMarketPriceList(params);
		String jsonStr = gson.toJson(marketPriceList);

		return jsonStr;
	}
	
	public List<AuctionList> getAuctionList(String bidDate) {
		
		List<AuctionList> auctionList = auctionListRepository.findBySaledayStartingWithOrderBySaledayAsc(bidDate);
		
		return auctionList;
	}
}
