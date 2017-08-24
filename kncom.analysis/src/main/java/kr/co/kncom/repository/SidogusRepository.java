package kr.co.kncom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.kncom.domain.Sidogus;

public interface SidogusRepository extends JpaRepository<Sidogus, Integer>{
	
	@Query(value="select ind from Sidogus where name = :name and si_ind = :si_ind", nativeQuery=true)
	int findSidonguCodeSQL(@Param("name") String name, @Param("si_ind") int si_ind);
	
	@Query(value="select ind from Newdongs where name = :name and sigogus_ind = :sigogus_ind and si_ind = :si_ind", nativeQuery=true)
	int findDongCodeSQL(@Param("name") String name, @Param("sigogus_ind") int sigogus_ind, @Param("si_ind") int si_ind);
	
	@Query(value="select convert(concat(e1.ind,'\\\\',e2.ind,'\\\\',e3.ind,'\\\\'), char(100)) filepath from Sidos e1, Sidogus e2, Newdongs e3 where e1.ind = :ind and e1.ind = e2.si_ind and e2.si_ind = e3.si_ind and e2.ind = e3.sigogus_ind order by e1.ind, e2.ind, e3.ind asc", nativeQuery=true)
	List<String> findDongFilePath(@Param("ind") int ind);
	
	@Query(value="select convert(concat(si_ind, '\\\\', ind, '\\\\'), char(100)) filePath from sidogus where si_ind = :si_ind order by ind asc", nativeQuery=true)
	List<String> findSidogusFilePath(@Param("si_ind") int si_ind);
}
