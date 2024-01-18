package com.javaex.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.dao.UserDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	
		String actionName =request.getParameter("a");
		System.out.println("user:" + actionName);
		
		if("joinform".equals(actionName)) {
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinform.jsp");
			
			/*
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinform.jsp");
			rd.forward(request, response);
			*/
			
		} else if("join".equals(actionName)) {
      String name = request.getParameter("name");
      String email = request.getParameter("email");
      String password = request.getParameter("password");
      String gender = request.getParameter("gender");
      UserVo vo = new UserVo(name,email,password,gender);
      
      UserDao dao = new UserDaoImpl();
      dao.insert(vo);
      
      RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinsuccess.jsp");
      rd.forward(request, response);
    
    // id(email) 중복검사 : api/emailCheck.jsp 대신 적용
    } else if("idcheck".equals(actionName)) {
      String email = request.getParameter("email");
      UserDao dao = new UserDaoImpl();
      
//      Enumeration e = request.getParameterNames();
//      while ( e.hasMoreElements() ){
//        String name = (String) e.nextElement();
//        String[] values = request.getParameterValues(name);   
//        for (String value : values) {
//          System.out.println(this.getClass().getName() + ".name=" + name + ",value=" + value);
//        }   
//      }
      
      response.setContentType("text/html; charset=UTF-8");

      // "true", "false" 문자열이 반환되므로 ajax에서 결과값으로 받아서 처리
      response.getWriter().write(dao.idCheck(email)); 
      
    } else if("modify".equals(actionName)){
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			UserVo vo = new UserVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setGender(gender);
			
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			
			int no = authUser.getNo();
			vo.setNo(no);
			
			UserDao dao = new UserDaoImpl();
			dao.update(vo);
			
			authUser.setName(name);
			
			WebUtil.forward(request, response, "/WEB-INF/views/main/index.jsp");
			
		} else if("modifyform".equals(actionName)) {
			
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			int no = authUser.getNo(); // 회원번호
			
			UserDao dao = new UserDaoImpl();
			UserVo userVo = dao.getUser(no);
			System.out.println(userVo.toString());
			
			request.setAttribute("userVo", userVo);
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyform.jsp");
			
		} else if("loginform".equals(actionName)){
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp");
			rd.forward(request, response);
			
		} else if("login".equals(actionName)){
			String email = request.getParameter("email");
			String password =request.getParameter("password");
			
			UserDao dao = new UserDaoImpl();
			UserVo vo = dao.getUser(email, password);
			
			if(vo==null) {
				System.out.println("실패");
				response.sendRedirect("/mysite/user?a=loginform&result=fail");
			} else {
				System.out.println("성공");
				HttpSession session = request.getSession(true);//세션 만들기
				session.setAttribute("authUser", vo);
				
				response.sendRedirect("/mysite/main");
				return;
			}
			
		} else if("logout".equals(actionName)){
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate(); //위랑 둘 중 하나만 해도 됨.
			response.sendRedirect("/mysite/main");
			
		} else {
			
			WebUtil.redirect(request, response, "/mysite/main");
			/*response.sendRedirect("/mysite/main");*/
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
