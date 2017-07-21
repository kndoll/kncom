package kr.co.kncom.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import kr.co.kncom.config.ServiceConfig;
import kr.co.kncom.domain.AuctionList;
import kr.co.kncom.repository.AuctionListRepository;
import kr.co.kncom.service.AuctionListService;

@Controller
public class AuctionListController {
	
	@Autowired
	AuctionListRepository auctionListRepository;
	
	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServiceConfig.class);
	AuctionListService auctionListService = context.getBean(AuctionListService.class);
	  
	Gson gson = new Gson();

	@RequestMapping("/auctionAnalysis")
	public String auctionAnalysis(Model model, @RequestParam(value = "bidDate", defaultValue = "12.01") String bidDate)
			throws UnsupportedEncodingException {

		List<AuctionList> _auctionList = auctionListRepository.findBySaledayStartingWithOrderBySaledayAsc(bidDate);
		// 데이터를 정제한다.
		List<AuctionList> auctionList = auctionListService.refineAuctionListData(_auctionList);

		model.addAttribute("bidDate", bidDate);
		model.addAttribute("totalCnt", auctionListRepository.countBySaledayStartingWith(bidDate));
		model.addAttribute("auctionList", auctionList);

		return "auctionAnalysis";
	}

	@RequestMapping(value = "/auctionMarketPrice", method = RequestMethod.GET)
	public @ResponseBody String auctionMarketPrice(Model model, @RequestParam HashMap<String, String> params) {

		return auctionListService.getMarketPriceList(params);
	}

}
