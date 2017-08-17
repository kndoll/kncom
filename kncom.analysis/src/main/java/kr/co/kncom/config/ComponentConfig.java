package kr.co.kncom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.kncom.service.RealtyStandardData;

@Configuration
public class ComponentConfig {
	
	@Bean
	public RealtyStandardData realtyStandardData() {
		
		return new RealtyStandardData();
	}
	
}
