package kr.co.kncom.config;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import kr.co.kncom.service.AuctionListService;

public class ServiceConfigTest {

	  @Test
	  public void serivceConfigTest(){
		  AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServiceConfig.class);
		  AuctionListService auctionListService = context.getBean(AuctionListService.class);
		  
		  System.out.println("## " + auctionListService);
		  
	  }
	  
}
