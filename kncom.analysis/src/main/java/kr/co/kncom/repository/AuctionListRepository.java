package kr.co.kncom.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import kr.co.kncom.domain.AuctionList;

public interface AuctionListRepository extends CrudRepository<AuctionList, String> {
	
	List<AuctionList> findBySaledayStartingWithOrderBySaledayAsc(String saleday);
	Long countBySaledayStartingWith(String saleday);
	
}
