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
import kr.co.kncom.service.AuctionListService;

@Controller
public class AuctionListController {

	AuctionListService auctionService = new AuctionListService();
	@Autowired
	AuctionListRepository auctionListRepository;
	Gson gson = new Gson();

	@RequestMapping("/auctionAnalysis")
	public String auctionAnalysis(Model model, @RequestParam(value = "bidDate", defaultValue = "12.01") String bidDate)
			throws UnsupportedEncodingException {

		System.out.println("## B I D - D A T E ==> " + bidDate);

		// 임시 캐릭터 셋 변경
		List<AuctionList> _auctionList = auctionListRepository.findBySaledayStartingWithOrderBySaledayAsc(bidDate);
		List<AuctionList> auctionList = new ArrayList<AuctionList>();

		AuctionList auctionListVO = null;
		for (AuctionList tmpData : _auctionList) {
			auctionListVO = new AuctionList();

			// 데이터 변환 - service layer로 이동해야 함.
			auctionListVO = tmpData;

			auctionListVO.setInd(new String(tmpData.getInd().getBytes("iso-8859-1"), "euc-kr"));
			auctionListVO.setAddress(new String(tmpData.getAddress().getBytes("iso-8859-1"), "euc-kr"));
			auctionListVO.setGubun(new String(tmpData.getGubun().getBytes("iso-8859-1"), "euc-kr"));
			auctionListVO.setResult(new String(tmpData.getResult().getBytes("iso-8859-1"), "euc-kr"));

			if (tmpData.getAppraisedvalue().length() > 0) {
				auctionListVO.setAppraisedvalue(String.format("%,d", (int)Double.parseDouble(tmpData.getAppraisedvalue()) / 10000));
			}

			if (tmpData.getLowestvalue().length() > 0) {
				auctionListVO.setLowestvalue(String.format("%,d", (int)Double.parseDouble(tmpData.getLowestvalue()) / 10000));
			}

			if (tmpData.getSalevalue().length() > 0) {
				auctionListVO.setSalevalue(String.format("%,d",(int)Double.parseDouble(tmpData.getSalevalue()) / 10000));
			}

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
