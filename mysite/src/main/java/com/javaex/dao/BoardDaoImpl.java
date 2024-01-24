package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDaoImpl implements BoardDao {

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

  public List<BoardVo> getList(String keyField, String keyWord, int start, int end) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<BoardVo> list = new ArrayList<>();

	    try {
	        conn = getConnection();

	        if(keyWord.equals("null") || keyWord.equals("")) {
	        	
	        	// 페이징 : 정다윤
	        	
	        	// 수정된 쿼리: 게시물 목록을 가져오는 쿼리에 rownum 조건을 적용
	        	String query = "SELECT * FROM ( "
	        			+ "  SELECT ROWNUM RN, A.* FROM ( "
	        			+ "    SELECT B.NO, B.TITLE, U.NAME, B.HIT, TO_CHAR(B.REG_DATE, 'yy-mm-dd hh:mi') REG_DATE, U.NO AS USER_NO "
	        			+ "    FROM BOARD B, USERS U "
	        			+ "    WHERE B.USER_NO = U.NO "
	        			+ "    ORDER BY NO DESC "
	        			+ "  ) A "
	        			+ "  WHERE ROWNUM <= ?+? "
	        			+ ") WHERE RN > ?";
	        	
	        	pstmt = conn.prepareStatement(query);
	        	pstmt.setInt(1, start);
	        	pstmt.setInt(2, end);
	        	pstmt.setInt(3, start);
	        	System.out.println("현재 1번 문장을 사용중입니다. ");
	        } else {
	        	
	        	//검색 : 원하경 및 김규호
	        	
	        	//검색 필드 & 검색어 조건이 추가된 쿼리
	        	String query = "SELECT * \r\n"
	        			+ "FROM (   SELECT ROWNUM RN, A.* \r\n"
	        			+ "			FROM (    SELECT B.NO, B.TITLE, U.NAME, B.HIT, TO_CHAR(B.REG_DATE, 'yy-mm-dd hh:mi') REG_DATE, U.NO AS USER_NO    \r\n"
	        			+ "						FROM BOARD B, USERS U    \r\n"
	        			+ "						WHERE B.USER_NO = U.NO \r\n"
	        			+ "						AND "+ keyField +" LIKE ? \r\n"
	        			+ "						ORDER BY NO DESC   ) A \r\n"
	        			+ "			WHERE ROWNUM <= ?+? )\r\n"
	        			+ "WHERE RN > ?";
	        	
	        	pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%" + keyWord + "%");
				pstmt.setInt(2, start);
				pstmt.setInt(3, end);
				pstmt.setInt(4, start);
				System.out.println("현재 2번 문장을 사용중입니다. ");
	        }

	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            int no = rs.getInt("NO");
	            String title = rs.getString("TITLE");
	            int hit = rs.getInt("HIT");
	            String regDate = rs.getString("REG_DATE");
	            int userNo = rs.getInt("USER_NO");
	            String userName = rs.getString("NAME");

	            BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
	            list.add(vo);
	        }
	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    } finally {
	        try {
	            if (rs != null) {
	              rs.close();
	            }
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

	    return list;
	}

  public BoardVo getBoard(int no) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    BoardVo boardVo = null;

    try {
      conn = getConnection();

      // Update the hit count when fetching the post
      updateHit(no);

      String query = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, u.name, b.filename "
          + "from board b, users u "
          + "where b.user_no = u.no "
          + "and b.no = ?";

      pstmt = conn.prepareStatement(query);
      pstmt.setInt(1, no);

      rs = pstmt.executeQuery();
      System.out.println(query);
      
      while (rs.next()) {
        String title = rs.getString("title");
        String content = rs.getString("content");
        int hit = rs.getInt("hit");
        String regDate = rs.getString("reg_date");
        int userNo = rs.getInt("user_no");
        String userName = rs.getString("name");
        String fileName = rs.getString("filename");

        boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName);
        boardVo.setFileName(fileName);
      }

    } catch (SQLException e) {
      System.out.println("error:" + e);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
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

    return boardVo;
  }

  public int insert(BoardVo vo) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    int count = 0;

    try {
      conn = getConnection();

      System.out.println("vo.userNo : [" + vo.getUserNo() + "]");
      System.out.println("vo.title : [" + vo.getTitle() + "]");
      System.out.println("vo.content : [" + vo.getContent() + "]");

      String query = "insert into board(no, title, content, hit, reg_date, user_no, filename ) values (seq_board_no.nextval, ?, ?, 0, sysdate, ?,?)";
      pstmt = conn.prepareStatement(query);

      pstmt.setString(1, vo.getTitle());
      pstmt.setString(2, vo.getContent());
      pstmt.setInt(3, vo.getUserNo());
      pstmt.setString(4, vo.getFileName());
      count = pstmt.executeUpdate();
      System.out.println(query);
      System.out.println(count + "건 등록");

    } catch (SQLException e) {
      System.out.println("error:" + e);
    } finally {
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

  public int delete(int no) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    int count = 0;

    try {
      conn = getConnection();

      String query = "delete from board where no = ?";
      pstmt = conn.prepareStatement(query);

      pstmt.setInt(1, no);

      count = pstmt.executeUpdate();

      System.out.println(count + "건 삭제");

    } catch (SQLException e) {
      System.out.println("error:" + e);
    } finally {
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

  public int update(BoardVo vo) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    int count = 0;

    try {
      conn = getConnection();

      String query = "update board set title = ?, content = ? where no = ? ";
      pstmt = conn.prepareStatement(query);

      pstmt.setString(1, vo.getTitle());
      pstmt.setString(2, vo.getContent());
      pstmt.setInt(3, vo.getNo());

      count = pstmt.executeUpdate();

      System.out.println(count + "건 수정");

    } catch (SQLException e) {
      System.out.println("error:" + e);
    } finally {
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

  public void updateHit(int no) {
    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
      conn = getConnection();

      String query = "update board set hit = hit + 1 where no = ?";
      pstmt = conn.prepareStatement(query);
      pstmt.setInt(1, no);

      pstmt.executeUpdate();

    } catch (SQLException e) {
      System.out.println("error:" + e);
    } finally {
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
  }

	public int getTotalCount(String keyField, String keyWord) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int totalCount = 0;
		try {
			conn = getConnection();
			if (keyWord.equals("null") || keyWord.equals("")) {
				sql = "select count(no) from board";
				pstmt = conn.prepareStatement(sql);
			} else {
				sql = "SELECT count(NO) \r\n "
					+ " FROM (SELECT  B.NO, B.TITLE, U.NAME AS NAME, B.HIT, B.REG_DATE, U.NO AS USER_NO \r\n "
					+ "		FROM BOARD B, USERS U \r\n "
					+ "		WHERE B.USER_NO = U.NO) \r\n "
					+ " WHERE " + keyField + " LIKE ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + keyWord + "%");

				System.out.println("getTotalCount로 들어오나 봅니다 ");
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalCount = rs.getInt(1);
				System.out.println("totalCount"+totalCount);
			}
		}catch (SQLException e) {
		      System.out.println("error:" + e);
	    } finally {
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
		return totalCount;
	}
	
}
