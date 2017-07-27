package kr.co.kncom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.kncom.domain.Sidogus;

public interface SidogusRepository extends JpaRepository<Sidogus, Integer>{
	
	@Query(value="select ind from Sidogus where name = :name and si_ind = :si_ind", nativeQuery=true)
	int findSidonguCodeSQL(@Param("name") String name, @Param("si_ind") int si_ind);
	
	@Query(value="select ifnull(ind, -1) from Newdongs where name = :name and sigogus_ind = :sigogus_ind and si_ind = :si_ind", nativeQuery=true)
	int findDongCodeSQL(@Param("name") String name, @Param("sigogus_ind") int sigogus_ind, @Param("si_ind") int si_ind);
}
