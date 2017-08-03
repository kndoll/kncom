package kr.co.kncom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.kncom.domain.AreaRatio;
import kr.co.kncom.domain.AreaRatioMultiId;

public interface AreaRatioRepository extends JpaRepository<AreaRatio, AreaRatioMultiId>{
	
}
