package kr.co.kncom.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.stereotype.Service;

@Service
public class AuctionListServiceTest {
	
	@Test
	public void getMarketPriceListTest() {
		
		AuctionListService auctionService = new AuctionListService();
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("index", "111");
		params.put("bidName", "sdfsdsd");
		
		System.out.println("### auctionService ==> " + auctionService.getMarketPriceList(params));
	}
	
}
