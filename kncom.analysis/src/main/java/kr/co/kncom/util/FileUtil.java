package kr.co.kncom.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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
		return readFileToStringBuffer(filePath).toString();
	}
	
	/**
	 * 해당 경로의 파일을 라인별로 읽어 LIST로 리턴한다.
	 * @param filePath
	 * @return
	 */
	public static List<String[]> readFileToStringArrayList(String filePath, String separator) {
		
		List<String[]> rtnList = new ArrayList<String[]>(); 
		
		try {
			
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"euc-kr"));
			
			String strLine = null;
			
			while((strLine = in.readLine()) != null) {
				rtnList.add(strLine.split("\\" + separator));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rtnList;
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
	
	/**
	 * 파일을 읽어 스트링버퍼로 리턴한다.
	 * 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private static StringBuffer readFileToStringBuffer(String filePath)
			throws FileNotFoundException, UnsupportedEncodingException, IOException {
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

		return buffer;
	}
}
