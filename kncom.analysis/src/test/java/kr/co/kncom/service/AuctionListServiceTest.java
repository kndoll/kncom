package kr.co.kncom.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AuctionListServiceTest {

	@Test
	public void getMarketPriceListTest() {
		
		AuctionListService auctionService = new AuctionListService();
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("index", "111");
		params.put("bidName", "sdfsdsd");
		
		System.out.println("### auctionService ==> " + auctionService.getMarketPriceList(params));
	}
	
	@Test
	public void getAuctionListTest() {
		
		/*
		AuctionListService auctionListService = new AuctionListService();
		
		List<AuctionList> testResult = auctionListService.getAuctionList("12.01");
		
		if (testResult != null) {
			for (AuctionList loop : testResult) {
				System.out.println("## TEST DATA ==> " + loop.getAddress());
			}
		}
		*/
		
	}
	
}
