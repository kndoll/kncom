package kr.co.kncom.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

public class FileUtil {
	
	/**
	 * 파일 경로에 있는 xml 파일을 json으로 변환하여 리턴한다.
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String readFileToString(String filePath) throws IOException {

		FileInputStream is = new FileInputStream(filePath);

		StringBuffer buffer = new StringBuffer();
		InputStreamReader isr = new InputStreamReader(is, "EUCKR");
		Reader in = new BufferedReader(isr);
		int ch;
		while ((ch = in.read()) > -1) {
			buffer.append((char) ch);
		}
		in.close();

		//JSONObject jsonObject = XML.toJSONObject(buffer.toString(), true);

		return buffer.toString();
	}

	/**
	 * 특정 경로에 있는 파일목록을 리턴한다.
	 * 
	 * @param dirPath
	 * @return
	 */
	public static List<String> getXmlFileNameList(String dirPath) {

		List<String> rtnFileNameList = new ArrayList<String>();

		File dir = new File(dirPath);
		File[] fileList = dir.listFiles();
		
		if (fileList != null) {
			for (File file : fileList) {
				if (file.isFile()) {
					rtnFileNameList.add(file.getName());
				}
			}
		} else {
			rtnFileNameList = null;
		}
		
		return rtnFileNameList;
	}
	
}
