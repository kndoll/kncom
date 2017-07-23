package kr.co.kncom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.kncom.domain.AuctionList;

public interface AutoWireRepository  extends JpaRepository<AuctionList, String> {

}
