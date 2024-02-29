package pkg07_Cookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DeleteCookie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	  // 요청 & 요청 파라미터
	  request.setCharacterEncoding("UTF-8");
	  String cookieName = request.getParameter("cookieName");
	  
	  // 쿠키 삭제(쿠키 유지 시간이 0인 쿠키를 만들어서 덮어쓰기 한다.)
	  Cookie cookie = new Cookie(cookieName, "아무의미없는값"); // 공백주면 오류남!
	  
	  cookie.setMaxAge(0);
	  response.addCookie(cookie);
	  
	  
	  
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
