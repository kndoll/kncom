package kr.co.kncom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.kncom.dao.MarketPriceDao;

@Configuration
public class DaoConfig {

	@Bean
	public MarketPriceDao marketPriceDao() {
		return new MarketPriceDao();
	}
	
}
