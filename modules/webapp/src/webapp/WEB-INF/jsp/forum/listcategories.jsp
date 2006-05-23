<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core"  prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>
<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading">Forumkategorier:</div>
        <c:forEach items="${categories}" var="category">
            <a href="<aksess:geturl/>/forum/viewcategory?categoryId=<c:out value="${category.id}"/>"><c:out value="${category.name}"/></a><br>

        </c:forEach>
        <forum:haspermisson permission="EDIT_CATEGORY">
            <br><br>
            <a href="<%=request.getContextPath()%>/forum/editcategory">Ny kategori</a>
        </forum:haspermisson>
</kantega:section>

<%@include file="include/design/design.jsf"%>