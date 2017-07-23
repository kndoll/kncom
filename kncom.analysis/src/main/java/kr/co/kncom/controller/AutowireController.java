package kr.co.kncom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.kncom.dao.MarketPriceDao;
import kr.co.kncom.repository.AuctionListRepository;

@Controller
public class AutowireController {
	
	@Autowired
	private AuctionListRepository auctionListRepository;
	
	@Autowired
	private MarketPriceDao marketPriceDao;
	
	@RequestMapping("/autowired")
	public String autoWired(Model model) {
		System.out.println("## A U T O - W I R E D : " + auctionListRepository);
		
		model.addAttribute("auctionListRepository", auctionListRepository);
		model.addAttribute("marketPriceDao", marketPriceDao);
		
		return "autowired";
	}
	
}
