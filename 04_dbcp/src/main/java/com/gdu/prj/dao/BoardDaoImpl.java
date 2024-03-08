package com.gdu.prj.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.gdu.prj.dto.BoardDto;

import oracle.jdbc.replay.ConnectionInitializationCallback;

/*
 * view - filter - controller- service - dao - db
 * 
 */

public class BoardDaoImpl implements BoardDao {
  
  // dao 는 db를 처리한다.
  private Connection con;
  private PreparedStatement ps;
  private ResultSet rs;
  
  // Connection Pool 관리를 위한 DataSource 객체 선언
  private DataSource dataSource;
  
  // SingletonPattern
  private static BoardDao boardDao = new BoardDaoImpl();
  private BoardDaoImpl() {
    // META-INF/context.xml 파일에 명시된 Resource 를 이용해 DataSource 객체 생성하기
    try {
      Context context = new InitialContext();
      Context env = (Context)context.lookup("java:comp/env"); //파일찾기위함
      dataSource = (DataSource) env.lookup("jdbc/myoracle"); // 여러 리소스 중 어떤 리소스 가져올건지
    } catch (NamingException e) {
      System.out.println("관련 자원을 찾을 수 없습니다.");
    }
  }
  public static BoardDao getInstance() {
    return boardDao;
  }
  
  
  @Override
  public int insertBoard(BoardDto board) {
    int insertCount = 0;
    try {
      con = dataSource.getConnection(); // dataSoucre객체에서 connection얻어냄. 제일먼저해야
      String sql = "INSERT INTO BOARD_T(BOARD_NO, TITLE, CONTENTS, MODIFIED_AT, CREATED_AT) VALUES(BOARD_SEQ.NEXTVAL, ?, ?, CURRENT_DATE, CURRENT_DATE)";
      ps = con.prepareStatement(sql);
      ps.setString(1, board.getTitle());
      ps.setString(2, board.getContents());
      insertCount = ps.executeUpdate();
      
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      close(); // this.close();
    }
    return insertCount;
  }

  @Override
  public int updateBoard(BoardDto board) {
    int updateCount = 0;
    try {
      con = dataSource.getConnection();
      String sql = "UPDATE BOARD_T SET TITLE = ?, CONTENTS = ?, MODIFIED_AT = CURRENT_DATE WHERE BOARD_NO = ?";
      ps = con.prepareStatement(sql);
      ps.setString(1, board.getTitle());
      ps.setString(2, board.getContents());
      ps.setInt(3, board.getBoard_no());
      updateCount = ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      close();
    }
    return updateCount;
  }

  @Override
  public int deleteBoard(int board_no) {
    int deleteCount = 0;
    try {
       con = dataSource.getConnection();
       String sql = "DELETE FROM BOARD_T WHERE BOARD_NO = ?";
       ps = con.prepareStatement(sql);
       ps.setInt(1, board_no);
       deleteCount = ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      close();
    }
    return deleteCount;
  }
  
  @Override
  public int deleteBoards(String param) {
    int deleteCount = 0;
    try {
      con = dataSource.getConnection();
      String sql = "DELETE FROM BOARD_T WHERE BOARD_NO IN("+ param +")";
      ps = con.prepareStatement(sql);
     
      deleteCount = ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return deleteCount;
  }

  @Override
  public List<BoardDto> selectBoardList(Map<String, Object> map) {
    List<BoardDto> boardList = new ArrayList<>();
    try {
      con = dataSource.getConnection();
      String sql = "SELECT BOARD_NO, TITLE, CONTENTS, MODIFIED_AT, CREATED_AT FROM BOARD_T ORDER BY BOARD_NO DESC";
      ps = con.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()) {
        BoardDto board = BoardDto.builder()
                           .board_no(rs.getInt(1))
                            .title(rs.getString(2))
                            .contents(rs.getString(3))
                            .modified_at(rs.getDate(4))
                            .created_at(rs.getDate(5))
                           .build();
      boardList.add(board);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      close();
    }
    return boardList;
  }

  @Override
  public int getBoardCount() { // 전체게시글개수
    int boardCount = 0;
    try {
      con = dataSource.getConnection();
      String sql = "SELECT COUNT(*) FROM BOARD_T";
      ps = con.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()) {
        boardCount = rs.getInt(1);
      };
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      close();
    }
    return boardCount;
  }

  @Override
  public BoardDto selectBoardByNo(int board_no) { // 게시글 번호 전달받아 
    BoardDto board = null;
    try {
      con = dataSource.getConnection();
      String sql = "SELECT BOARD_NO, TITLE, CONTENTS, MODIFIED_AT, CREATED_AT FROM BOARD_T WHERE BOARD_NO = ?";
      ps = con.prepareStatement(sql);
      ps.setInt(1, board_no);
      rs = ps.executeQuery();
      if(rs.next()) {
        board = BoardDto.builder()
                  .board_no(rs.getInt(1))
                  .title(rs.getString(2))
                  .contents(rs.getString(3))
                  .modified_at(rs.getDate(4))
                  .created_at(rs.getDate(5))
                 .build();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return board;
  }

  @Override
  public void close() {
    try {
      if(rs != null) rs.close();
      if(ps != null) ps.close();
      if(con != null) con.close();   // Connection 반납으로 동작, 닫는 게 아니라
    } catch (Exception e) { 
      e.printStackTrace();
    }
  }

}
