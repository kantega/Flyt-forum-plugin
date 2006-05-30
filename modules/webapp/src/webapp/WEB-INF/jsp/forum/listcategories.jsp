7<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading">
        <spring:message code="forum.title"/>
    </div>

    <kantega:section id="cats">
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <!--    <tr class="forum-labelRow">
                    <td>
                        <spring:message code="forumcategory.name"/>
                    </td>
                    <td>
                        <spring:message code="forumcategory.forums"/>
                    </td>
                </tr>-->
            <c:forEach items="${categories}" var="category">
                <c:set var="hascats" value="true"/>
                <tr class="forum-labelRow">
                    <td>
                        <c:out value="${category.name}"/>
                    </td>
                    <td align="right" style="font-weight: normal;">
                        <forum:haspermisson permission="EDIT_CATEGORY" object="${category}">
                            <a href="editcategory?categoryId=<c:out value="${category.id}"/>">Endre</a>
                            | <a href="editforum?categoryId=<c:out value="${category.id}"/>">Legg til forum</a>
                            | <a href="deletecategory?categoryId=<c:out value="${category.id}"/>">Slett kategori</a>
                        </forum:haspermisson>
                    </td>
                </tr>

                <c:forEach var="forum" items="${category.forums}"  varStatus="status">
                    <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                        <td>
                            <a href="viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a>
                            <c:if test="${forum.description != ''}">
                                <br><c:out value="${forum.description}"/>
                            </c:if>

                        </td>
                        <td>
                            <c:out value="${forum.numThreads}"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:forEach>
        </table>
    </kantega:section>
    <c:choose>
        <c:when test="${hascats != 'true'}">
            <spring:message code="forumlist.empty"/>
        </c:when>

        <c:otherwise>
            <kantega:getsection id="cats"/>
        </c:otherwise>

    </c:choose>

    <forum:haspermisson permission="EDIT_CATEGORY">
        <br><br>
        <a href="editcategory">Ny kategori</a>
    </forum:haspermisson>


</kantega:section>

<%@ include file="include/design/design.jsf" %>