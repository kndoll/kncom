package kr.co.kncom.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.XML;

import com.google.gson.Gson;

public class FileUtil {
	
	/**
	 * 파일 경로에 있는 xml 파일을 리턴한다.
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(String filePath) {
		
		String rtnVal = null;
		
		try {
			rtnVal = readFileToStringBuffer(filePath).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rtnVal;
	}
	
	/**
	 * 해당 경로의 파일을 라인별로 읽어 구분자로 split 후, LIST로 리턴한다.
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	public static List<String[]> readFileToStringArrayList(String filePath, String separator) throws IOException {
		
		List<String[]> rtnList = new ArrayList<String[]>(); 
			
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"euc-kr"));
		
		String strLine = null;
		
		int indexOf = -1;
		int lastIndexOf = -1;
		String _str = null;
		String _replaceStr = null;
		while((strLine = in.readLine()) != null) {
			
			// "안에 분리자를 치환한다.
			indexOf = strLine.indexOf("\"");
			
			if (indexOf != -1) {
				lastIndexOf = strLine.lastIndexOf("\"");
				_str = strLine.substring(indexOf, lastIndexOf+1);
				_replaceStr = _str.replaceAll("[\\,\"]", "");
				
				strLine = strLine.replace(_str, _replaceStr);
			}
			
			rtnList.add(strLine.split("\\" + separator));
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
	
	/**
	 * 파일 존재 유무를 리턴한다.
	 * 
	 * @param filePath
	 * @return [true|false]
	 */
	public static boolean isExistFile(String filePath) {
		
		File file = new File(filePath);
		
		return file.exists();
	}
	
	/**
	 * xml 파일을 map 형태로 변환한다.
	 *  
	 * @param filePath
	 * @return
	 */
	public static Map<String, Object> getMapFromXml(String filePath) {
		
		Gson gson = new Gson();
		Map<String, Object> map = new HashMap<String, Object>();
		String _tmpFileStr = FileUtil.readFileToString(filePath);
		
		// JSON TO MAP
		JSONObject _jsonObj = XML.toJSONObject(_tmpFileStr, true);
		map = (Map<String, Object>) gson.fromJson(_jsonObj.toString(), map.getClass());
		
		return map;
	}
	
	/**
	 * 
	 * @param filePath
	 * @param beanList
	 */
	public static void writeFileFromArr(String filePath, List<String> contentList) {
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			
			StringBuffer sb = new StringBuffer();
			
			for (String _data : contentList) {
				sb.append(_data);
				sb.append("\n");
			}
			
			bw.write(sb.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
