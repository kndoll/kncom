package kr.co.kncom.dao;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MarketPriceDaoTest {
	
	@Test
	public void getMarketPriceListTest() {
		
		MarketPriceDao dao = new MarketPriceDao();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("index", "11710_10100_198_5");
		params.put("bidName", "테스트");
		params.put("bidDate", "12.02.01");
		params.put("suffix", "pv4");
		
		dao.getMarketPriceList(params);
	}

}
