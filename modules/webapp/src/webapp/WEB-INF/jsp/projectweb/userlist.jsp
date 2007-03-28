<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
  <head><title></title></head>
  <body>
    <h1>Users</h1>
    Add user:<br>
    <form action="adduser" method="post">
    Username: <input name="username"><br>
    Full name: <input name="fullname"><br>
    Email: <input name="email"><br>
    <input type="submit" value="Add user">
    </form>

    <hr>
    <ul>
      <c:forEach items="${users}" var="user">
        <li><c:out value="${user.fullName}"/> (<c:out value="${user.name}"/>) : <c:out value="${user.email}"/></li>
      </c:forEach>
    </ul>

    </body>
</html>