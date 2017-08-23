package kr.co.kncom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.kncom.domain.Sidos;

public interface SidosRepository extends JpaRepository<Sidos, Integer>{
	
	List<Sidos> findByInd(int ind); 
	
}
