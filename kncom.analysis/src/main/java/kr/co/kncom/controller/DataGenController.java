package kr.co.kncom.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.kncom.service.AntiGroundAreaService;
import kr.co.kncom.service.AreaRatioService;
import kr.co.kncom.service.AroundQuoteService;

@Controller
public class DataGenController {
	
	@Autowired
	private AreaRatioService areaRatioService;
	@Autowired
	private AntiGroundAreaService antiGroundAreaService;
	@Autowired
	private AroundQuoteService aroundQuoteService;
	
	@Value("${daejangRootDir}")
	String daejangRootDir;
	
	@RequestMapping("/dataGen")
	public String dataGen(Model model, @RequestParam(value="type", required=false) String type) {
		
		
		Path path = Paths.get(daejangRootDir+"1");
		
		if (type != null) {
		switch(type) {
			
			// 용적률 클린징
			case "areaRatio" :
				
				try {
					Files.walkFileTree(path, areaRatioService);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
				// csv 다운로드
				
				
				break;
				
			// 대지면적 클린징
			case "antiGroundArea" :
				
				try {
					Files.walkFileTree(path, antiGroundAreaService);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
				break;
				
			case "aroundQuote" :
				
				aroundQuoteService.insertAroundQuote();
				
				break;
			}
			
		}
		
		return "dataGen";
	}
	
}
