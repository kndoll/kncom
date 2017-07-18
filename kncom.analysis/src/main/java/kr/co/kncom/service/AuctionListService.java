package kr.co.kncom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import kr.co.kncom.dao.MarketPriceDao;
import kr.co.kncom.domain.AuctionList;
import kr.co.kncom.repository.AuctionListRepository;
import kr.co.kncom.vo.MarketPriceVO;

@Service
public class AuctionListService {

	private MarketPriceDao marketConditionDao = new MarketPriceDao();
	
	@Autowired
	private AuctionListRepository auctionListRepository;
	
	public List<AuctionList> getAuctionList(String bidDate) {
		
		List<AuctionList> auctionList = auctionListRepository.findBySaledayStartingWithOrderBySaledayAsc(bidDate);
		
		return auctionList;
	}
	
	public String getMarketPriceList(Map params) {
		
		List<MarketPriceVO> marketPriceList = new ArrayList<MarketPriceVO>();
		Gson gson = new Gson();

		// obj -> json 변환
		marketPriceList = marketConditionDao.getMarketPriceList(params);
		String jsonStr = gson.toJson(marketPriceList);
		
		System.out.println("## params ==> " + params.get("bidDate"));
		
		return jsonStr;
	}
	
}
