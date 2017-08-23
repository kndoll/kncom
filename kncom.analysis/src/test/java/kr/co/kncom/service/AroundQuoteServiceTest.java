package kr.co.kncom.service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AroundQuoteServiceTest {
	
	@Autowired
	AroundQuoteService aroundQuoteService;
	
	@Test
	public void insertAroundQuoteTest() {
		
		aroundQuoteService.insertAroundQuote();
		
		/*
		Path path = Paths.get("X:\\201706lobig\\1");
		
		try {
		    Files.walkFileTree(path, aroundQuoteService);
		} catch (IOException ex) {
			
		}
		*/
		
		
	}
	
}
