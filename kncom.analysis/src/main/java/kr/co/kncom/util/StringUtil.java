package kr.co.kncom.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class StringUtil {

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
	 * 
	 * 대상월을 기준으로 특정월을 계산
	 * 
	 * @param offset
	 * @param targetDate
	 *            201701
	 * @return
	 */
	public static String getDateFromTargetDate(int offset, String targetDate) {

		String rtnDate = null;

		Calendar cal = Calendar.getInstance();

		if (targetDate != null) {

			int year = Integer.parseInt(targetDate.substring(0, 4));
			int month = Integer.parseInt(targetDate.substring(4, 6));
			int day = 1;

			// 대상일 설정
			cal.set(year, month - 1, day);
			cal.add(Calendar.MONTH, offset);

			Date calDate = cal.getTime();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM", Locale.getDefault());

			rtnDate = formatter.format(calDate);

		} else {
			rtnDate = null;
		}

		return rtnDate;
	}

	/**
	 * 현재날짜 기준으로 특정일 계산
	 * 
	 * @param iDay
	 * @param separator
	 *            기준 년도와 일자를 구분한다.
	 * @return separator가 "." 인 경우, (YYYY.MM.DD)
	 */
	public static String getDate(int iDay, String separator) {
		return getDate(iDay).replaceAll("-", separator);
	}

	/**
	 * 전달 받은 String의 문자열을 확인한다.
	 * 
	 * @param originalStr
	 */
	public static void checkCharSet(String originalStr) {

		String[] charSet = { "utf-8", "euc-kr", "ksc5601", "iso-8859-1", "x-windows-949" };

		for (int i = 0; i < charSet.length; i++) {
			for (int j = 0; j < charSet.length; j++) {
				try {
					System.out.println("[" + charSet[i] + "," + charSet[j] + "] = "
							+ new String(originalStr.getBytes(charSet[i]), charSet[j]));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * MAP 형식의 파라미터의 키와 값을 PRINT한다.
	 * 
	 * @param params
	 */
	public static void printParamter(Map<String, String> params) {

		Set<String> keys = params.keySet();

		System.out.println("##### P A R A M E T E R #####");
		for (String key : keys) {
			System.out.println(key + " ==> " + params.get(key));
		}
		System.out.println("#############################");
	}

	/**
	 * 문자열이 숫자타입인지 확인
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isStringDouble(String s) {
		boolean rtnval = false;

		try {
			Double.parseDouble(s);
			rtnval = true;
		} catch (NumberFormatException e) {
			System.out.println("## '" + s + "' is not number format!!!");
		}

		return rtnval;
	}

	/**
	 * 문자열 타입을 Date 타입으로 변환한다.
	 * 
	 * @param fromDate
	 *            (12.02.02)
	 * @return
	 */
	public static Date convertStrToDate(String fromDate) {

		Date to = null;
		String[] _arr = fromDate.split("\\.");

		String from = "20" + _arr[0] + "-" + _arr[1] + "-" + _arr[2];

		SimpleDateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			to = convertFormat.parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return to;
	}

	/**
	 * euckr 로 캐릭터셋을 변환한다.
	 * 
	 * @param srcStr
	 * @return
	 */
	public static String convertUtf8ToEuckr(String srcStr) {

		String rtnStr = null;

		try {
			rtnStr = new String(srcStr.getBytes("utf-8"), "euckr");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return rtnStr;
	}
	
	/**
	 * 배열자료의 디버깅을 위해 인덱스와 데이터를 매칭하여 보여준다.
	 * 
	 * @param arr 배열데이터
	 * @param title 타이틀
	 */
	public static void printIndexData(String[] arr, String title) {
		
		System.out.println("##############" + title + "##############");
		
		int i = 0;
		for (String _str : arr) {
			System.out.println("## index[" + i + "] " + " : " + _str);
			i++;
		}
		
		System.out.println("########################################");
	}
	
	
}
