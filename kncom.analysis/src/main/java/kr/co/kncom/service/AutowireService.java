package kr.co.kncom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kncom.repository.AuctionListRepository;

@Service
public class AutowireService {
	
	@Autowired
	private AuctionListRepository auctionListRepository;
	
	public void printAutowired() {
		System.out.println("## A U T O - W I R E D - S E R V I C E : " + auctionListRepository);
	}
}
