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

  public List<BoardVo> getList() {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<BoardVo> list = new ArrayList<BoardVo>();

    try {
      conn = getConnection();

      String query = "select b.no, b.title, b.hit, b.reg_date, b.user_no, u.name "
          + " from board b, users u "
          + " where b.user_no = u.no "
          + " order by no desc";

      pstmt = conn.prepareStatement(query);

      rs = pstmt.executeQuery();

      while (rs.next()) {
        int no = rs.getInt("no");
        String title = rs.getString("title");
        int hit = rs.getInt("hit");
        String regDate = rs.getString("reg_date");
        int userNo = rs.getInt("user_no");
        String userName = rs.getString("name");

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

      String query = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, u.name "
          + "from board b, users u "
          + "where b.user_no = u.no "
          + "and b.no = ?";

      pstmt = conn.prepareStatement(query);
      pstmt.setInt(1, no);

      rs = pstmt.executeQuery();

      while (rs.next()) {
        String title = rs.getString("title");
        String content = rs.getString("content");
        int hit = rs.getInt("hit");
        String regDate = rs.getString("reg_date");
        int userNo = rs.getInt("user_no");
        String userName = rs.getString("name");

        boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName);
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

      String query = "insert into board values (seq_board_no.nextval, ?, ?, 0, sysdate, ?,0,0,0)";
      pstmt = conn.prepareStatement(query);

      pstmt.setString(1, vo.getTitle());
      pstmt.setString(2, vo.getContent());
      pstmt.setInt(3, vo.getUserNo());

      count = pstmt.executeUpdate();

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
}
