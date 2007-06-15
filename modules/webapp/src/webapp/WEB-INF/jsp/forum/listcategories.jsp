q<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<kantega:section id="tittel">
    <spring:message code="forum.title"/>
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading">
        <spring:message code="forum.title"/>
    </div>

    <c:if test="${unapprovedPostCount != null && unapprovedPostCount > 0}">
        <div class="forum-message">
        <a href="listunapproved"><spring:message code="forumcategory.unapprovedposts"/> <strong><c:out value="${unapprovedPostCount}"/></strong> <spring:message code="forumcategory.unapprovedposts2"/></a>
        </div>
    </c:if>

    <kantega:section id="cats">
        <table width="100%" cellpadding="0" cellspacing="0" border="0">

            <c:forEach items="${categories}" var="category">
                <c:set var="hascats" value="true"/>
                <tr class="forum-labelRow">
                    <td valign="top">&nbsp;</td>
                    <td valign="top">
                        <c:set var="canedit" value="false"/>
                        <forum:haspermisson permission="EDIT_CATEGORY" object="${category}">
                            <a href="viewcategory?categoryId=<c:out value="${category.id}"/>"><c:out value="${category.name}"/></a>
                            <c:set var="canedit" value="true"/>
                        </forum:haspermisson>
                        <c:if test="${canedit != 'true'}">
                            <c:out value="${category.name}"/>
                        </c:if>
                    </td>
                    <td>
                        <spring:message code="forumlist.lastpost"/>
                    </td>
                    <td>
                        <spring:message code="forum.threads"/>
                    </td>
                </tr>

                <c:forEach var="forum" items="${category.forums}"  varStatus="status">
                    <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                        <td valign="top">
                            <c:choose>
                                <c:when test="${forum.lastPost != null && forum.lastPost.postDate.time > lastVisit.time}">
                                    <img src="../bitmaps/forum/new.gif" alt="<spring:message code="post.icon.new"/>" title="<spring:message code="post.icon.new"/>">
                                </c:when>
                                <c:when test="${forum.numThreads > 10}">
                                    <img src="../bitmaps/forum/hot.gif" alt="<spring:message code="post.icon.hot"/>" title="<spring:message code="post.icon.hot"/>">
                                </c:when>
                                <c:otherwise>
                                    <img src="../bitmaps/forum/normal.gif" alt="">
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td valign="top">
                            <a href="viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a>
                            <c:if test="${forum.description != ''}">
                                <div style="padding-top: 4px"><c:out value="${forum.description}"/></div>
                            </c:if>

                        </td>
                        <td valign="top">
                            <c:if test="${forum.lastPost != null}">
                                <a href="viewthread?threadId=<c:out value="${forum.lastPost.thread.id}"/>#post_<c:out value="${forum.lastPost.id}"/>"><c:out value="${forum.lastPost.subject}"/></a><br>
                                av <c:out value="${forum.lastPost.author}"/>
                            </c:if>
                        </td>
                        <td valign="top">
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
        <a href="editcategory"><spring:message code="forumcategory.addcategory"/></a>
    </forum:haspermisson>
    <br>Sist besøk: <c:out value="${lastVisit}"/>
</kantega:section>

<%@ include file="include/design/design.jsf" %>