package kr.co.kncom.repository;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JttypelistRepositoryTest {

	@Autowired
	private JttypelistRepository jttypelistRepository;
	
	@Test
	public void countJttypelistTest() {
		
		int count = jttypelistRepository.countByJttypelist(11110, 10900, 30, 5);
		assertTrue(count > 0);
		
		count = jttypelistRepository.countByJttypelist(11111, 0, 0, 0);
		assertTrue(count == 0);
	}

}
