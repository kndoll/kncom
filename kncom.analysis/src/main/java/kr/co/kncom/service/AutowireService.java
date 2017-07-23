package kr.co.kncom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kncom.repository.AutoWireRepository;

@Service
public class AutowireService {
	
	@Autowired
	private AutoWireRepository autoWireRepository;
	
	public void select() {
		System.out.println(autoWireRepository.count());
	}
}
