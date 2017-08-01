package kr.co.kncom.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.co.kncom.repository.SidogusRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuctionListServiceTest {
	
	@Autowired
	private AuctionListService auctionListService;
	
	@Autowired
	private SidogusRepository sidogusRepository;
	
	@Test
	public void getMarketPriceListTest() {
		
		AuctionListService auctionService = new AuctionListService();
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("index", "111");
		params.put("bidName", "sdfsdsd");
		
		System.out.println("### auctionService ==> " + auctionService.getMarketPriceList(params));
	}
	
	@Test
	public void insertAuctionListTest() {
		
		System.out.println("## auctionListService Instance ==> " + auctionListService);
		auctionListService.insertAuctionList();
	}
	
	@Test
	public void selectSidigus() throws UnsupportedEncodingException {
		
		int tmp = sidogusRepository.findSidonguCodeSQL("서울특별시 강남구", 1);
		System.out.println("##tmp ==> " + tmp);
		int tmp2 = sidogusRepository.findDongCodeSQL("대치동", tmp, 1);
		System.out.println("##tmp2 ==> " + tmp2);
	}
}
