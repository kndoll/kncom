package kr.co.kncom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.kncom.domain.Jttypelist;

public interface JttypelistRepository extends JpaRepository<Jttypelist, Integer> {
	
	@Query("select count(ind) from Jttypelist "
			+ "where sidogus_ind = :sidogus_ind "
			+ "and dongs_ind = :dongs_ind "
			+ "and bunji1 = :bunji1 "
			+ "and bunji2 = :bunji2")
	int countByJttypelist(@Param("sidogus_ind") int sidogus_ind, 
			@Param("dongs_ind") int dongs_ind, @Param("bunji1") int bunji1, @Param("bunji2") int bunji2);
	
}
