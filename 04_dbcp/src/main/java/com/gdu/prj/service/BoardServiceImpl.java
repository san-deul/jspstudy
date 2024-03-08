package com.gdu.prj.service;

import java.util.List;
import java.util.Optional;

import com.gdu.prj.common.ActionForward;
import com.gdu.prj.dao.BoardDao;
import com.gdu.prj.dao.BoardDaoImpl;
import com.gdu.prj.dto.BoardDto;

/*
 * view - filter - controller- service - dao - db
 * 
 */


import jakarta.servlet.http.HttpServletRequest;

public class BoardServiceImpl implements BoardService {

  // service 는 dao를 호출한다.
  private BoardDao boardDao = BoardDaoImpl.getInstance();
  
  
  
  @Override
  public ActionForward addBoard(HttpServletRequest request) {
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    BoardDto board = BoardDto.builder()
                        .title(title)
                        .contents(contents)
                       .build();
    int insertCount = boardDao.insertBoard(board);
    // redirect 경로는 URLMapping으로 작성한다. (외워. 꼭!)
    String view = null;
    if(insertCount == 1) {
      view = request.getContextPath() + "/board/list.brd";  // 
    } else if(insertCount == 0){
      view = request.getContextPath() + "/main.brd";
    }
    // INSERT 이후 이동은 redirect
    return new ActionForward(view, true);
  }

  @Override
  public ActionForward getBoardList(HttpServletRequest request) {
    int boardCount = boardDao.getBoardCount();
    List<BoardDto> boardList = boardDao.selectBoardList(null);
    request.setAttribute("boardCount", boardCount);
    request.setAttribute("boardList", boardList);
    return new ActionForward("/board/list.jsp",false);
  }

  @Override
  public ActionForward getBoardByNo(HttpServletRequest request) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("board_no"));
    int board_no = Integer.parseInt(opt.orElse("0"));
    
    BoardDto board = boardDao.selectBoardByNo(board_no);
    String view = null;
    if(board != null) {
      view = "/board/detail.jsp";
      request.setAttribute("board", board);
    } else {
      view = "/index.jsp";
    }
    
    return new ActionForward(view, false);
  }

  @Override
  public ActionForward editBoard(HttpServletRequest request) { //게시글 번호 가지고 편집화면으로 넘어감..수정이 아님
    String param = request.getParameter("board_no");
    int board_no = 0;
    if(!param.isEmpty()) {
      board_no = Integer.parseInt(param);
    }
    BoardDto board = boardDao.selectBoardByNo(board_no);
    String view = null;
    if(board != null) {
      view = "/board/edit.jsp";
      request.setAttribute("board", board);
    } else {
      view = "/index.jsp";
    }
    return new ActionForward(view, false);
  }

  @Override
  public ActionForward modifyBoard(HttpServletRequest request) { // 얘가 수정
    int board_no = Integer.parseInt(request.getParameter("board_no"));
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    
    BoardDto board = BoardDto.builder()
                        .title(title)
                        .contents(contents)
                        .board_no(board_no)
                       .build();
    int updateCount = boardDao.updateBoard(board);
    String view = null;
    if(updateCount == 0) {
      view = request.getContextPath() + "/main.brd";
    } else {
      view = request.getContextPath() + "/board/detail.brd?board_no=" + board_no;
    }
    return new ActionForward(view, true);
  }

  @Override
  public ActionForward removeBoard(HttpServletRequest request) {
    String param = request.getParameter("board_no");
    int board_no = 0;
    if(!param.isEmpty()) {
      board_no = Integer.parseInt(param);
    }
    int deleteCount = boardDao.deleteBoard(board_no);
    String view = null;
    if(deleteCount == 0) {
      view = request.getContextPath() + "/main.brd";
    } else {
      view = request.getContextPath() + "/board/list.brd";
    }
    return new ActionForward(view, true);
  }
  
  @Override
  public ActionForward removeBoards(HttpServletRequest request) {
    String param = request.getParameter("param");
    int deleteCount = boardDao.deleteBoards(param);
    String view = null;
    if(deleteCount == 0) {
      view = request.getContextPath() + "/main.brd";
    } else {
      view = request.getContextPath() + "/board/list.brd";
    }
    return new ActionForward(view, true);
  }

}
