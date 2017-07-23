package kr.co.kncom.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.co.kncom.dao.MarketPriceDao;
import kr.co.kncom.service.AutowireService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuctionListRepositoryTest {

	@Autowired
	private AuctionListRepository auctionListRepository;
	
	@Autowired
	AutoWireRepository autowireRepository;
	
	@Autowired
	MarketPriceDao marketPriceDao;
	
	@Autowired
	private AutowireService autowireService;
	
	@Test
	public void select() {
		
		Long count = auctionListRepository.countBySaledayStartingWith("12.01");
		
		System.out.println("## count ==> " + count);
		System.out.println("## count2 ==> " + autowireRepository.count());
		System.out.println("## marketPriceDao Instance ==> " + marketPriceDao);
		
	}
	
	@Test
	public void serviceTest() {
		autowireService.select();
	}
	
}
