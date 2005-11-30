<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project"%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<kantega:section id="content">

    <h1>Documents for <c:out value="${project.name}"/>:</h1>
Add document:<br>
<form action="addocument" method="post">
        Title: <input name="title"><br>
        <input type="hidden" name="projectId" value="<c:out value="${project.id}"/>">
        <input type="submit" value="Add document">

    </form>
    <hr>
    <ul>
      <c:forEach items="${project.documents}" var="document">
        <li><a href="<c:out value="document?documentId=${document.id}"/>"><c:out value="${document.title}"/></a>: <c:out value="${document.status}"/></li>
      </c:forEach>
    </ul>
</kantega:section>
<%@include file="include/design/design.jsf"%>