package kr.co.kncom.util;

import java.util.Calendar;

public class StringUtil {
	
	/**
	 * 
	 * @param baseDate 기준년월 (12.01.01)
	 * @param offset 증감 오프셋
	 * @return 2013.01.01
	 */
	public static String getDate(String baseDate, int offset) {
		
		
		
		return baseDate;
	}
	
	/**
	 * 현재날짜 기준으로 특정일을 계산
	 * 
	 * @return String data : YYYY-MM-DD
	 */
	public static String getDate(int iDay) {

		Calendar temp = Calendar.getInstance();
		StringBuffer sbDate = new StringBuffer();

		temp.add(Calendar.DAY_OF_MONTH, iDay);

		int nYear = temp.get(Calendar.YEAR);
		int nMonth = temp.get(Calendar.MONTH) + 1;
		int nDay = temp.get(Calendar.DAY_OF_MONTH);

		sbDate.append(nYear);
		sbDate.append("-");
		if (nMonth < 10) {
			sbDate.append("0");
		}
		sbDate.append(nMonth);
		sbDate.append("-");

		if (nDay < 10) {
			sbDate.append("0");
		}
		sbDate.append(nDay);

		return sbDate.toString();
	}
	
	/**
	 * 현재날짜 기준으로 특정일 계산
	 * 
	 * @param iDay 
	 * @param separator 기준 년도와 일자를 구분한다.
	 * @return separator가 "." 인 경우, (YYYY.MM.DD)
	 */
	public static String getDate(int iDay, String separator) {
		return getDate(iDay).replaceAll("-", separator);
	}
	
}
