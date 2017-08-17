package kr.co.kncom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.kncom.domain.AntiGroundArea;
import kr.co.kncom.domain.RealtyMultiId;

public interface AntiGroundAreaRepository extends JpaRepository<AntiGroundArea, RealtyMultiId>{
	
}
