package kr.co.kncom.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		
		/*
		Map<String, String> params  = new HashMap<String, String>();
		params.put("filePath", "X:\\201706lobig\\1\\11620\\10200\\409\\100\\daejang_pyojebu.dat"); // 1-1
		areaRatioService.insertAreaRatio(params);
		
		params.put("filePath", "F:\\201704lobig\\1\\11110\\11000\\126\\0\\daejang_pyojebu.dat"); // 2-1
		areaRatioService.insertAreaRatio(params);
		
		Path path = Paths.get("X:\\201706lobig");
		
		try {
		    Files.walkFileTree(path, areaRatioService);
		} catch (IOException ex) {
			
		}
		*/
	}
	
	/* - private method 테스트 적용
	@Test
	public void calcDasedaeFromFloorTest() {
		
		areaRatioService.calcDasedaeFromFloor("F:\\201704lobig\\1\\11110\\10100\\1\\0\\daejang_jygyarea.dat");
		
	}
	*/

}
