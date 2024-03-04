package pkg08_ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;


public class GetData2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
	  // 도로교통공단_사망교통사고정보서비스 : 사망교통사고정보 Rest 조회
	  StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552061/AccidentDeath/getRestTrafficAccidentDeath");
	  
	  urlBuilder.append("?serviceKey=").append(URLEncoder.encode("YPob1+buAPgjUaMz5IbyEq7gptCtIResC6bhqJehna4WVrWs8pD6DygbP86mRE4uVHrPe9nXG45Vf4aa3/vA==", "UTF-8"));
	  urlBuilder.append("&searchYear=").append(URLEncoder.encode("2022", "UTF-8"));
	  urlBuilder.append("&siDo=").append(URLEncoder.encode("1100", "UTF-8"));
	  urlBuilder.append("&guGun=").append(URLEncoder.encode("1116", "UTF-8"));
	  urlBuilder.append("&type=").append(URLEncoder.encode("json", "UTF-8"));
	  urlBuilder.append("&numOfRows=").append(URLEncoder.encode("10", "UTF-8"));
	  urlBuilder.append("&pageNo=").append(URLEncoder.encode("1", "UTF-8"));

	  // 요청 URL
	  String spec = urlBuilder.toString();
	  
    try {
      
      // URL 객체
      URL url = new URL(spec);
      
      // HttpURLConnection 객체
      HttpURLConnection con = (HttpURLConnection)url.openConnection();
      
      // 문자 입력 스트림(사망 사고 정보 읽기)
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      
      // StringBuilder 객체 (전체 JSON)
      StringBuilder builder = new StringBuilder();
      
      // String 객체 (한 줄)
      String line = null;
      
      // 문자 입력 스트림을 통해 사망사고 정보 읽어 오기
      while((line = in.readLine())!= null){
        builder.append(line);
      }
      
      // API 접속 해제
      con.disconnect();
      
      // 응답 결과
      String responseBody = builder.toString();
      
      // 결과 코드 확인을 위한 JSONObject 객체 생성
      JSONObject obj = new JSONObject(responseBody);
      
      // 결과 코드
      String resultCode = obj.getString("resultCode");
      String resultMsg = obj.getString("resultMsg");

      if(!resultCode.equals("00")) {  // 정상 응답("00")이 아니면 예외 발생
        throw new RuntimeException(resultMsg + "(" + resultCode + ")");
      }
      
      //응답 데이터 타입 & 인코딩
      response.setContentType("application/json; charset=UTF-8");
      
      //응답
      PrintWriter out = response.getWriter();
      out.print(builder.toString());
      out.flush();
      out.close();
      
    } catch (Exception e) {
      // $.ajax().fail() 메소드로 전달되는 응답 만들기
      
      response.setContentType("text/plain; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.print(e.getMessage());
      out.flush();
      out.close();
    }
    
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
