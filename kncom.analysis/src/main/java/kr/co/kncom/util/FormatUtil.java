package kr.co.kncom.util;

public class FormatUtil {
	
	/**
	 * 소숫점 두자리를 반올림하여 리턴한다.
	 * @return
	 * @throws Exception 
	 * @throws Exceptio 
	 */
	public static float round(float srcNum) {
		
		float rtnVal = 0f;
		
		if (StringUtil.isStringDouble(Float.toString(srcNum)) && !Float.isNaN(srcNum) && !Float.isInfinite(srcNum)){
			rtnVal = (float)(Math.round(srcNum*100) / 100.0);
		} 
		
		return rtnVal;
	}
	
}
