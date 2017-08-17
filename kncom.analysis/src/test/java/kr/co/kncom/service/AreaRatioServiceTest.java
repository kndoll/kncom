package kr.co.kncom.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.co.kncom.util.FormatUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaRatioServiceTest {
	
	@Autowired
	private AreaRatioService areaRatioService;
	
	@Test
	public void insertAreaRatioTest() {
		
		/*
		Map<String, String> params  = new HashMap<String, String>();
		params.put("filePath", "X:\\201706lobig\\1\\11110\\11100\\90\\2\\daejang_pyojebu.dat"); // 1-1
		areaRatioService.insertAreaRatio(params);
		
		params.put("filePath", "F:\\201704lobig\\1\\11110\\11000\\126\\0\\daejang_pyojebu.dat"); // 2-1
		areaRatioService.insertAreaRatio(params);
		
		 */
		Path path = Paths.get("X:\\201706lobig\\1");
		
		try {
		    Files.walkFileTree(path, areaRatioService);
		} catch (IOException ex) {
			
		}
	}
	
	@Test
	public void calcDasedaeFromFloorTest() {
		
		//float test = (float)Math.abs(1 - (0.0 / 0.0));
		//System.out.println("test ==> " + FormatUtil.round(test));
		
		//areaRatioService.calcDasedaeFromFloor("F:\\201704lobig\\1\\11110\\10100\\1\\0\\daejang_jygyarea.dat");
		
	}

}
