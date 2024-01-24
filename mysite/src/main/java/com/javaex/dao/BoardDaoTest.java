package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardDaoTest {
	 public static void main(String[] args) {
		 // 0. import java.sql.*;
	        Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        
	        try{
	        	// 1. JDBC 드라이버 (Oracle) 로딩
	            Class.forName("oracle.jdbc.driver.OracleDriver");
	            

	            // 2. Connection 얻어오기
	            String url = "jdbc:oracle:thin:@localhost:1521:xe";
	            conn = DriverManager.getConnection(url, "webdb", "1234");
	            System.out.println("접속성공");
	            
	            // 3. SQL문 준비 / 바인딩 / 실행
	            String query = "SELECT NO, title, content, hit, reg_date, user_no, group_no, order_no, depth FROM BOARD b";
	            pstmt = conn.prepareStatement(query);
	            
	            rs = pstmt.executeQuery();
	            
	            // 4.결과처리
	            while (rs.next()) {
	                int no = rs.getInt("no");
	                String title = rs.getString("title");
	                String content = rs.getString("content");
	                System.out.println(no + "\t" + title + "\t" + content + "\t");
	            }

	        } catch (ClassNotFoundException e) {
	            System.out.println("error: 드라이버 로딩 실패 - " + e);
	        } catch (SQLException e) {
	            System.out.println("error:" + e);
	        } finally {
	            // 5. 자원정리
	            try {
	                if (rs != null) { rs.close(); }                
	                if (pstmt != null) { pstmt.close(); }
	                if (conn != null) { conn.close(); }
	            } catch (SQLException e) {
	                System.out.println("error:" + e);
	            }
	 }

}
}
