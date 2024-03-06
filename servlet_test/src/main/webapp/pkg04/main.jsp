<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

 <% String contextPath = request.getContextPath();%>
 
<form id="frm"
      method="POST"
      action="<%=contextPath%>/pkg04/save.jsp">
  <div>
    <label for="name">작성자</label>
    <input type="text" name="name" id="name">
  </div>
  <div>
    <label for="title">제목</label>
    <input type="text" name="title" id="title">
  </div>
  <div>
    <textarea name="contents"></textarea>
  </div>
  
  <button type="submit">작성완료</button>   
  <button type="reset">작성초기화</button>   
      
</form>

  <script>
  document.getElementById('frm').addEventListener('submit',(evt)=>{
	  
	  let dates = new Date();   

	  let year = dates.getFullYear();
	  let month = dates.getMonth() + 1; 
	  let date = dates.getDate();  
	  //let day = today.getDay(); 
	  
	  let today = String(year)+"-"+ String(month)+ "-"+String(date);
	  
	  let name = document.getElementById('name');
	  let title = document.getElementById('title');
	  alert(today+ name.value+"-"+title.value +'파일이 생성되었습니다.');
  })
  </script>


</body>
</html>