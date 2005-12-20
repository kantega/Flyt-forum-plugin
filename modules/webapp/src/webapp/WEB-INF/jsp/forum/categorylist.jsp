<%@ page import="no.kantega.forum.model.ForumCategory"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%
    List categories = (List) request.getAttribute("categories");
%>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
<%
    for (int i = 0; i < categories.size(); i++) {
        ForumCategory cat = (ForumCategory) categories.get(i);
        %>
        <a href="<%=request.getContextPath() %>/forum/viewcategory?id=<%=cat.getId()%>"><%=cat.getName()%></a><br>
        <%
    }
%>
    <a href="<%=request.getContextPath()%>/forum/addcategory">Ny kategori</a>
</kantega:section>

<%@include file="include/design/design.jsf"%>