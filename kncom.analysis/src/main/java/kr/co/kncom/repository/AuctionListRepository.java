package kr.co.kncom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.kncom.domain.AuctionList;

public interface AuctionListRepository extends JpaRepository<AuctionList, String> {
	
	List<AuctionList> findBySaledayStartingWithOrderBySaledayAsc(String saleday);
	Long countBySaledayStartingWith(String saleday);
	
}
