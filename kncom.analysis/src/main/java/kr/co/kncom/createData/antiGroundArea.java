package kr.co.kncom.createData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import kr.co.kncom.service.AntiGroundAreaService;

@Component
public class antiGroundArea implements CommandLineRunner {
	
	@Autowired
	private AntiGroundAreaService antiGroundAreaService;
	
	@Override
	public void run(String... arg) throws Exception {
		
		if (arg.length > 0) {
			if (arg[0].equals("antiGroundArea")) {
				Path path = Paths.get("X:\\201706lobig");
				
				try {
					Files.walkFileTree(path, antiGroundAreaService);
				} catch (IOException ex) {
					
				}
			}
		}
	}
	
}
