package kr.co.kncom.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.kncom.service.AreaRatioService;

@Controller
public class AreaRatioController {
	
	@Autowired
	AreaRatioService areaRatioService;
	
	@RequestMapping(value = "/areaRatioCreate", method = RequestMethod.GET)
	public @ResponseBody String areaRatioCreate(Model model) {
		
		Path path = Paths.get("X:\\201706lobig");
		
		try {
		    Files.walkFileTree(path, areaRatioService);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return "ok";
	}
	
}
