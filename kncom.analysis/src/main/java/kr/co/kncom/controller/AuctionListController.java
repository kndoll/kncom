package kr.co.kncom.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import kr.co.kncom.domain.AuctionList;
import kr.co.kncom.repository.AuctionListRepository;
import kr.co.kncom.service.AuctionService;

@Controller
public class AuctionListController {

	AuctionService auctionService = new AuctionService();
	@Autowired
	AuctionListRepository auctionListRepository;
	Gson gson = new Gson();
	
	@RequestMapping("/auctionAnalysis")
	public String auctionAnalysis(Model model, @RequestParam(value = "bidDate", defaultValue = "12.01") String bidDate) throws UnsupportedEncodingException {
		
		System.out.println("## B I D - D A T E ==> " + bidDate);
		
		// 임시 캐릭터 셋 변경
		List<AuctionList> _auctionList = auctionListRepository.findBySaledayStartingWithOrderBySaledayAsc(bidDate);
		List<AuctionList> auctionList = new ArrayList<AuctionList>();
		
		// 문자셋 변환
		String _ind = null;
		String _address = null;
		String _result = null;
		
		AuctionList auctionListVO = null;
		for(AuctionList tmpData : _auctionList) {
			auctionListVO = new AuctionList();
			auctionListVO = tmpData;
			
			_ind = new String(tmpData.getInd().getBytes("iso-8859-1"), "euc-kr");
			_address = new String(tmpData.getAddress().getBytes("iso-8859-1"), "euc-kr");
			_result = new String(tmpData.getResult().getBytes("iso-8859-1"), "euc-kr");
			
			auctionListVO.setInd(_ind);
			auctionListVO.setAddress(_address);;
			auctionListVO.setResult(_result);
			
			auctionList.add(auctionListVO);
		}
		
		model.addAttribute("bidDate", bidDate);
		model.addAttribute("totalCnt", auctionListRepository.countBySaledayStartingWith(bidDate));
		model.addAttribute("auctionList", auctionListRepository.findBySaledayStartingWithOrderBySaledayAsc(bidDate));
		
		return "auctionAnalysis";
	}
	
	@RequestMapping(value = "/auctionMarketPrice", method = RequestMethod.GET)
	public @ResponseBody String auctionMarketPrice(Model model, @RequestParam HashMap<String, String> params) {
		
		return auctionService.getMarketPriceList(params);
	}

}
