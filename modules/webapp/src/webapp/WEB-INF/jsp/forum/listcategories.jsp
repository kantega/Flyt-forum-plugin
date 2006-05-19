<%@ page import="no.kantega.forum.model.ForumCategory"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core"  prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <table border="1" cellspacing="0" cellpadding="0" width="400">
        <tr><td class="tblHeading">Forumkategorier:</td></tr>
        <% int i = 1; %>
        <c:forEach items="${categories}" var="category">
            <%
                String cssClass = "tblNormal";
                if ((i % 2) == 0) {
                    cssClass = "tblSelected";
                }
            %>
            <tr><td class="<%=cssClass%>"><a href="<%=request.getContextPath()%>/forum/viewcategory?categoryId=<c:out value="${category.id}"/>"><c:out value="${category.name}"/></a></td></tr>
            <% i++; %>
        </c:forEach>
        <tr><td>&nbsp;</td></tr>
        <tr><td><a href="<%=request.getContextPath()%>/forum/addcategory">Ny kategori</a></td></tr>
    </table>
</kantega:section>

<%@include file="include/design/design.jsf"%>