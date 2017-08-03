package kr.co.kncom.util;

public class FormatUtil {
	
	/**
	 * 소숫점 두자리를 반올림하여 리턴한다.
	 * @return
	 */
	public static float round(float srcNum) {
		
		float rtnVal = (float)(Math.round(srcNum*100) / 100.0);
		
		return rtnVal;
	}
	
}
