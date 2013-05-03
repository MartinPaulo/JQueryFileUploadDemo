<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<!DOCTYPE html">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Logout</title>
</head>
<body>
	<p>
		Goodbye <%=request.getRemoteUser()%>!
		<%session.invalidate();	%>
	
	<p><a href="index.jsp">Click here to go to test servlet</a></p>
</body>
</html>