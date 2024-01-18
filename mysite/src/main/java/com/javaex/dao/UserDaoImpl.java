package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDaoImpl implements UserDao{
  private Connection getConnection() throws SQLException {
    Connection conn = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
      conn = DriverManager.getConnection(dburl, "webdb", "1234");
    } catch (ClassNotFoundException e) {
      System.err.println("JDBC 드라이버 로드 실패!");
    }
    return conn;
  }
  
	public int update(UserVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			if(vo.getPassword() == "") {
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = "update users set name = ?, gender = ? where no = ?";
				pstmt = conn.prepareStatement(query);

				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getGender());
				pstmt.setInt(3, vo.getNo());
				
				count = pstmt.executeUpdate();
				
			} else {
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = "update users set name = ?, password = ?, gender = ? where no = ?";
				pstmt = conn.prepareStatement(query);

				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getPassword());
				pstmt.setString(3, vo.getGender());
				pstmt.setInt(4, vo.getNo());
				
				count = pstmt.executeUpdate();
			}
			
			// 4.결과처리
			System.out.println(count + "건 수정완료");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;

	}

	public int insert(UserVo vo) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "insert into users values (seq_users_no.nextval, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}

	// 정보가 맞는 회원정보를 리턴 (있으면 Vo, 없으면 Null)
	public UserVo getUser(String email, String password) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVo vo = null;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select no, name from users where email = ? and password = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, email);
			pstmt.setString(2, password);

			rs = pstmt.executeQuery();

			// 4.결과처리
			if (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");

				vo = new UserVo();
				vo.setNo(no);
				vo.setName(name);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return vo;

	}

	public UserVo getUser(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVo vo = null;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select no, name, email, gender from users where no = ?";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				no = rs.getInt("no");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String gender = rs.getString("gender");

				vo = new UserVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setEmail(email);
				vo.setGender(gender);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return vo;

	}

  public String idCheck(String email) {
 // 0. import java.sql.*;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    @SuppressWarnings("unused")
    UserVo vo = null;
    int cnt = 0;
    
    try {
      conn = getConnection();

      // 3. SQL문 준비 / 바인딩 / 실행
      String sql = " select count(*) cnt " + 
                   "   from users " + 
                   "  where email = ?";

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, email);
      rs = pstmt.executeQuery();

      // 4.결과처리
      rs.next();
      cnt = rs.getInt("cnt");  // 1      0
      System.out.println("cnt:" + cnt);
    } catch (SQLException e) {
      System.out.println("error:" + e);
    } finally {
      // 5. 자원정리
      try {
        if(rs != null)    rs.close();
        if(pstmt != null) pstmt.close();
        if(conn != null)  conn.close();
      } catch (SQLException e) {
        System.out.println("error:" + e);
      }
    }
    if(cnt == 1){
      return "false";
    }else{
      return "true";
    }
  }

}
