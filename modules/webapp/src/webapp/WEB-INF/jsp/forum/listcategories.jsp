<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading">Forumkategorier:</div>

    <c:choose>
        <c:when test="${empty categories}">
        </c:when>
        <c:otherwise>
            <table width="100%" cellpadding="0" cellspacing="0">
                <tr class="forum-labelRow">
                    <td>
                        <spring:message code="forumcategory.name"/>
                    </td>
                    <td>
                        <spring:message code="forumcategory.forums"/>
                    </td>
                </tr>
                <c:forEach items="${categories}" var="category" varStatus="status">
                    <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                        <td>
                            <a href="viewcategory?categoryId=<c:out value="${category.id}"/>"><c:out value="${category.name}"/></a>
                        </td>
                        <td>
                            <c:out value="${category.numForums}"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>

    </c:choose>

    <forum:haspermisson permission="EDIT_CATEGORY">
        <br><br>
        <a href="editcategory">Ny kategori</a>
    </forum:haspermisson>


</kantega:section>

<%@ include file="include/design/design.jsf" %>