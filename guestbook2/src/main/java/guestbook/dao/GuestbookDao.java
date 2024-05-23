package guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import guestbook.vo.GuestbookVo;

public class GuestbookDao {
	
	private Connection connection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.0.192:3306/webdb?charset=utf-8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:"+e);
		}
		
		return conn;
	}
	
	public boolean insert(GuestbookVo vo) {
		boolean result = false;
		
		try (
				Connection conn = connection();
				) {
			
			// 3. Statement 생성하기
			String sql = "insert into guestbook values(null, ?, ?, ?, now())";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			// 4. binding
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContents());
			
			// 4. SQL 실행
			int count = pstmt.executeUpdate();
			
			// 5. 결과처리
			result = count == 1;
			
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		
		return result;
	}

	public boolean deleteByNoAndPassword(Long no, String password) {
		boolean result = false;
		
		try (
				Connection conn = connection();
				) {
			
			// 3. Statement 생성하기
			String sql = "delete from guestbook where no = ? and password = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			// 4. binding
			pstmt.setLong(1, no);
			pstmt.setString(2, password);
			
			int count = pstmt.executeUpdate();
			
			// 5. 결과처리
			result = count == 1;
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}		
		return result;
	}

	public List<GuestbookVo> findAll() {
		List<GuestbookVo> result = new ArrayList<>();
		
		
		ResultSet rs = null;
		try (
				Connection conn = connection();
				PreparedStatement pstmt = conn.prepareStatement("select no, name, contents, date_format(reg_date, '%Y-%m-%d %H:%i:%s') as date from guestbook order by date desc");
				) {

			// 5. SQL 실행
			rs = pstmt.executeQuery();
			
			// 6. 결과처리
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String contents = rs.getString(3);
				String regDate = rs.getString(4);
				
				GuestbookVo vo = new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setContents(contents);
				vo.setRegDate(regDate);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:"+e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("error:"+e);
				}
			}
		}
		return result;
	}
}
