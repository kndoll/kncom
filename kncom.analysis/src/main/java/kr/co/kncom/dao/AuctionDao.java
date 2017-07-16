package kr.co.kncom.dao;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.co.kncom.vo.AuctionVO;

/**
 * 환경 설정 임시 테스트 dao 절때 이렇게 작업하면 안됨.
 * 
 * @author kndoll
 * @deprecated
 *
 */
public class AuctionDao {

	private final String DRIVER = "com.mysql.jdbc.Driver";
	private final String URL = "jdbc:mysql://127.0.0.1/finsy";
	private final String USER = "root";
	private final String PW = "root";
	
	public List<AuctionVO> getAuctionList(String bidDate) throws SQLException {

		Connection conn = null;
		Statement stmt = null;

		List<AuctionVO> rtnListVO = new ArrayList<AuctionVO>();

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PW);

			stmt = conn.createStatement();

			String sql;
			sql = "SELECT * FROM AUCTIONLIST WHERE SALEDAY LIKE '" + bidDate + "%' ORDER BY SALEDAY ";
			ResultSet rs = stmt.executeQuery(sql);

			AuctionVO _auctionVO = null;

			while (rs.next()) {

				_auctionVO = new AuctionVO();
				try {
					_auctionVO.setInd(new String(rs.getBytes("ind"), "UTF-8"));
					_auctionVO.setAddress(new String(rs.getBytes("address"), "UTF-8"));
					_auctionVO.setGubun(new String(rs.getBytes("gubun"), "UTF-8"));
					_auctionVO.setAppraisedvalue(rs.getString("appraisedvalue"));
					_auctionVO.setLowestvalue(rs.getString("lowestvalue"));
					_auctionVO.setSalevalue(rs.getString("salevalue"));
					_auctionVO.setLandarea(rs.getString("landarea"));
					_auctionVO.setBuildingarea(rs.getString("buildingarea"));
					_auctionVO.setSaleday(rs.getString("saleday"));
					_auctionVO.setOpenday(rs.getString("openday"));
					_auctionVO.setCloseday(rs.getString("closeday"));
					_auctionVO.setResult(new String(rs.getBytes("result"), "UTF-8"));
					_auctionVO.setSidogus_ind(rs.getString("sidogus_ind"));
					_auctionVO.setDongs_ind(rs.getString("dongs_ind"));
					_auctionVO.setBunji1(rs.getString("bunji1"));
					_auctionVO.setBunji2(rs.getString("bunji2"));
					_auctionVO.setX(rs.getString("x"));
					_auctionVO.setY(rs.getString("y"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				rtnListVO.add(_auctionVO);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rtnListVO;
	}
	
	public int getAuctionCnt(String bidDate) {

		Connection conn = null;
		Statement stmt = null;

		int rtnCnt = 0;

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PW);

			stmt = conn.createStatement();

			String sql;
			sql = "SELECT COUNT(*) FROM AUCTIONLIST WHERE SALEDAY LIKE '" + bidDate + "%'";
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next()) rtnCnt = rs.getInt(1);
			
			System.out.println("## T O T A L - C N T ==> " + rtnCnt);
			
			rs.close();
			stmt.close();
			conn.close();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rtnCnt;
	}
}
