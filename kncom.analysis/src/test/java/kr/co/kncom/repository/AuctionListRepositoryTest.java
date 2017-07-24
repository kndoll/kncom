package kr.co.kncom.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.co.kncom.dao.MarketPriceDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuctionListRepositoryTest {

	@Autowired
	private AuctionListRepository auctionListRepository;
	
	@Autowired
	MarketPriceDao marketPriceDao;
		
	@Test
	public void select() {
		
//		Long count = auctionListRepository.countBySaledayStartingWith("12.01");
	}
}
