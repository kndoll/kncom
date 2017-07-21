package kr.co.kncom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.kncom.service.AuctionListService;

@Configuration
public class ServiceConfig {
	
	@Bean 
	public AuctionListService auctionListService() {
		return new AuctionListService(); 
	}
}
