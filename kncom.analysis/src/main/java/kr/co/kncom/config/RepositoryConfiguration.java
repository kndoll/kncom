package kr.co.kncom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import kr.co.kncom.repository.AuctionListRepository;
import kr.co.kncom.repository.AutoWireRepository;

@Configuration
@EnableJpaRepositories({ "kr.co.kncom.repository" })
@EntityScan({ "kr.co.kncom.domain" })
@ComponentScan("kr.co.kncom")
public class RepositoryConfiguration {

	@Autowired
	AuctionListRepository auctionListRepository;

	@Autowired
	AutoWireRepository autoWireRepository;

	@Bean
	AuctionListRepository auctionListRepository() {
		return auctionListRepository;
	}
	
	@Bean
	AutoWireRepository autoWireRepository() {
		return autoWireRepository;
	}

}
