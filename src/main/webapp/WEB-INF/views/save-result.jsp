<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="gukjin.servlet.domain.member.Member" %>
<html>
<head>
 <title>Title</title>
</head>
<body>
    <ul>
        <li>id=<%=((Member)request.getAttribute("member")).getId()%></li>
        <li>username=<%=((Member)request.getAttribute("member")).getUsername()%></li>
        <li>age=<%=((Member)request.getAttribute("member")).getAge()%></li>

        <li>id=${member.id}</li>
    </ul>
    <a href="/index.html">메인</a>
</body>
</html>