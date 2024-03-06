<%@page import="java.io.FileWriter"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String name = request.getParameter("name");
String title = request.getParameter("title");
String contents = request.getParameter("contents");

String uploadPath = application.getRealPath("upload");

File uploadDir = new File(uploadPath);
if(!uploadDir.exists()){
  uploadDir.mkdirs();
}

String uploadName = title + name + ".txt";


session.setAttribute("uploadName", uploadName);

File uploadFile = new File(uploadDir, uploadName);

BufferedWriter writer = new BufferedWriter(new FileWriter(uploadFile));

writer.write(name+"\n");
writer.write(title+"\n");
writer.write(contents+"\n");
writer.flush();
writer.close();

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>