package kr.co.kncom.migration;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import kr.co.kncom.util.FileUtil;

public class HtmlParser {
	
	public static void main(String[] args) {
		try {
			
			Document doc = Jsoup.parseBodyFragment(FileUtil.readFileToString("P:\\html\\1.txt"));
			
			Element ind = doc.select("title").first();  
			System.out.println("## index ==> " + ind.text());
			
			Element address = doc.select(".td_1").get(0);
			System.out.println("## address ==> " + address.text());
			
			Element gubun = doc.select(".td_1").get(3);
			System.out.println("## gubun ==> " + gubun.text());
			
			Element saleDay = doc.select(".td_1").get(5);
			System.out.println("## saleday ==> " + saleDay.text());
			
			Element appraisedValue = doc.select(".td_1").get(6);
			System.out.println("## appraisedValue ==> " + appraisedValue.text());
			
			Element closeDay = doc.select(".td_1").get(8);
			System.out.println("## closeDay ==> " + closeDay.text());
			
			Element lowestValue = doc.select(".td_1").get(9);
			System.out.println("## lowestValue ==> " + lowestValue.text());
			
			Element landArea = doc.select(".td_1").get(10);
			System.out.println("## landArea ==> " + landArea.text());
			
			Element openDay = doc.select(".td_1").get(11);
			System.out.println("## openDay ==> " + openDay.text());
			
			Element buildingArea = doc.select(".td_1").get(13);
			System.out.println("## buildingArea ==> " + buildingArea.text());
			
			Element allotDay = doc.select(".td_1").get(14);
			System.out.println("## allotDay ==> " + allotDay.text());
			
			Element attention = doc.select(".td_1").get(14);
			System.out.println("## attention ==> " + attention.text());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
