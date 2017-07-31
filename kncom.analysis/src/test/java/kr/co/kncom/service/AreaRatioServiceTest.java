package kr.co.kncom.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaRatioServiceTest {
	
	@Autowired
	private AreaRatioService areaRatioService;
	
	@Test
	public void insertAreaRatioTest() {
		
		Map<String, String> params  = new HashMap<String, String>();
		params.put("filePath", "F:\\201704lobig\\1\\11110\\10100\\1\\0\\daejang_pyojebu.dat");
		
		areaRatioService.insertAreaRatio(params);
		
	}
	
	/* - private method 테스트 적용
	@Test
	public void calcDasedaeFromFloorTest() {
		
		areaRatioService.calcDasedaeFromFloor("F:\\201704lobig\\1\\11110\\10100\\1\\0\\daejang_jygyarea.dat");
		
	}
	*/

}
