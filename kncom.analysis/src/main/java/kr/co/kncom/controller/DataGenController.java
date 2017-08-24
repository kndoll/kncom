package kr.co.kncom.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	private String daejangRootDir;
	
	@Value("${writeFilePath}")
	private String writeFilePath;
	
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
	
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public void getDownLoad(HttpServletResponse response) throws IOException {
		
		FileInputStream fis = new FileInputStream(new File(writeFilePath)); 
		
		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename="+writeFilePath);
		response.setContentType("txt/plain");
		
		OutputStream out = response.getOutputStream();
		FileCopyUtils.copy(fis, out);
		
		out.flush();
	} 
}
