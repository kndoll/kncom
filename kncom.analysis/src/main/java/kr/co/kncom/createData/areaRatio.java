package kr.co.kncom.createData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import kr.co.kncom.service.AreaRatioService;

@Component
public class areaRatio implements CommandLineRunner {
	
	@Autowired
	private AreaRatioService areaRatioService;
	
	@Override
	public void run(String... arg) throws Exception {
		
		if (arg.length > 0) {
			if (arg[0].equals("arearatio")) {
				Path path = Paths.get("X:\\201706lobig\\1");
				
				try {
					Files.walkFileTree(path, areaRatioService);
				} catch (IOException ex) {
					
				}
			}
		}
	}
	
}
