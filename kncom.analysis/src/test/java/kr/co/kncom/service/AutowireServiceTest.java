package kr.co.kncom.service;

import org.junit.Test;

public class AutowireServiceTest {

	@Test
	public void test() {
		
		AutowireService auto = new AutowireService();
		
		auto.printAutowired();
	}

}
