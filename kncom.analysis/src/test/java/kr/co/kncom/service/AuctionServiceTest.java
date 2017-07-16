package kr.co.kncom.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AuctionServiceTest {

	@Test
	public void getMarketPriceListTest() {
		
		AuctionService auctionService = new AuctionService();
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("index", "111");
		params.put("bidName", "sdfsdsd");
		
		System.out.println("### auctionService ==> " + auctionService.getMarketPriceList(params));
	}
}
