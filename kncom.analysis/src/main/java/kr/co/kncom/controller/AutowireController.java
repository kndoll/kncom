package kr.co.kncom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.kncom.repository.AuctionListRepository;

@Controller
public class AutowireController {
	
	@Autowired
	private AuctionListRepository auctionListRepository;
	
	@RequestMapping("/autowired")
	public String autoWired() {
		System.out.println("## A U T O - W I R E D : " + auctionListRepository);
		
		return "autowired";
	}
	
}
