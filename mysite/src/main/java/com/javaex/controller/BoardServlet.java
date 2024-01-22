package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String actionName = request.getParameter("a");
		System.out.println("board:" + actionName);

		if ("list".equals(actionName)) {
			// 리스트 가져오기
			
			//페이지네이션
			int totalRecord=0; //전체레코드수 : 페이징 하려면 계산해야해서 필요함
			int numPerPage=10; // 페이지당 레코드 수 : (초기값) 페이지당 10개 보여줄게요
			int pagePerBlock=10; //블럭당 페이지수 
			
			int totalPage=0; //전체 페이지 수
			int totalBlock=0;  //전체 블럭수 
			
			int nowPage=1; // 현재페이지
			int nowBlock=1;  //현재블럭
			
			if (request.getParameter("nowPage") != null) {
				nowPage = Integer.parseInt(request.getParameter("nowPage"));
			}
			
			int start = (nowPage - 1) * numPerPage + 1; //디비의 select 시작번호
			int end = start + numPerPage - 1; //시작번호로 부터 가져올 select 갯수 : rownum 쓸 때 필요함.
			
			String keyWord = "";
			String keyField = ""; //검색을 위해 준비된 변수들	
			start = (nowPage * numPerPage) - numPerPage;
			end = numPerPage;
			
			BoardDao dao = new BoardDaoImpl();
			System.out.println("keyField=[" + keyField + "]");
			System.out.println("keyWord=["+keyWord+"]");
			System.out.println("start=["+start+"]");
			System.out.println("end=["+end+"]");
			
			// 페이징 및 검색 파라미터 설정
			if (request.getParameter("keyWord") != null) {
			    keyWord = request.getParameter("keyWord");
			    keyField = request.getParameter("keyField");
			}
			if (request.getParameter("reload") != null) {
			    if (request.getParameter("reload").equals("true")) {
			        keyWord = "";
			        keyField = "";
			    }
			}
			 
			totalRecord = dao.getTotalCount(keyField, keyWord); //1
			totalPage = (int)Math.ceil((double)totalRecord / numPerPage);  //전체페이지수 1: 올림이나 소수점 버리기
			nowBlock = (int)Math.ceil((double)nowPage/pagePerBlock); //현재블럭 계산 1		  
			totalBlock = (int)Math.ceil((double)totalPage / pagePerBlock);  //전체블럭계산 1
			
			List<BoardVo> list = dao.getList( keyField, keyWord, start, end);
			System.out.println(list.toString());
		    // JSP 페이지로 데이터 전달
			request.setAttribute("totalRecord", totalRecord);
			request.setAttribute("nowPage", nowPage);
			request.setAttribute("numPerPage", numPerPage);
			request.setAttribute("totalPage", totalPage);
			request.setAttribute("pagePerBlock", pagePerBlock);
			request.setAttribute("start", start);
			request.setAttribute("end", end);
			request.setAttribute("keyField", keyField);
			request.setAttribute("keyWord", keyWord);
			request.setAttribute("listSize", list.size()); // 리스트 사이즈 설정
				

			int pageStart = (nowBlock - 1) * pagePerBlock + 1; //하단 페이지 시작번호
			int pageEnd = pageStart + pagePerBlock - 1;
			if (pageEnd > totalPage) {
			    pageEnd = totalPage;
			}
			
			request.setAttribute("nowBlock", nowBlock); 
			request.setAttribute("totalBlock", totalBlock); 
			request.setAttribute("pageStart", pageStart);
			request.setAttribute("pageEnd", pageEnd);
			
			// BoardServlet.java의 doGet 메서드 내에 로그 추가
			System.out.println("nowPage: " + nowPage);
			System.out.println("pageStart: " + pageStart);
			System.out.println("pageEnd: " + pageEnd);

			// 리스트 화면에 보내기
			request.setAttribute("list", list);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		} else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			System.out.println(boardVo.toString());

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");
		} else if ("modify".equals(actionName)) {
			// 게시물 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo vo = new BoardVo(no, title, content);
			BoardDao dao = new BoardDaoImpl();
			
			dao.update(vo);
			
			WebUtil.redirect(request, response, "/mysite/board?a=list");
		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite/board?a=list");
			}

		} else if ("write".equals(actionName)) {
			UserVo authUser = getAuthUser(request);

			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			int userNo = authUser.getNo();
			System.out.println("userNo : ["+userNo+"]");
			System.out.println("title : ["+title+"]");
			System.out.println("content : ["+content+"]");

			BoardVo vo = new BoardVo(title, content, userNo);
			BoardDao dao = new BoardDaoImpl();
			dao.insert(vo);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else if ("delete".equals(actionName)) {
			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao dao = new BoardDaoImpl();
			dao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else {
			WebUtil.redirect(request, response, "/mysite/board?a=list");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}

}
