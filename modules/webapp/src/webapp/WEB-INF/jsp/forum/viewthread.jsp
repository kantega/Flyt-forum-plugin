<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="modules" uri="http://www.kantega.no/aksess/tags/modules" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <form action="deletepost" name="deletepost" method="POST">
        <input type="hidden" name="postId">
    </form>

    <form action="deletethread" name="deletethread" method="POST">
        <input type="hidden" name="threadId">
    </form>

    <script type="text/javascript" >
        function deletePost(postId) {
            if(confirm("<spring:message code="post.delete"/>")) {
                document.deletepost.postId.value = postId;
                document.deletepost.submit();
            }
        }
        function deleteThread(threadId) {
            if(confirm("<spring:message code="thread.confirmdelete"/>")) {
                document.deletethread.threadId.value = threadId;
                document.deletethread.submit();
            }
        }
    </script>


    <div class="forum-heading">
        <a href="."><spring:message code="forum.title"/></a> >
        <a href="viewforum?forumId=<c:out value="${thread.forum.id}"/>"><c:out value="${thread.forum.name}"/></a> >
        <a href="viewthread?threadId=<c:out value="${thread.id}"/>"><c:out value="${thread.name}"/></a>
    </div>

    <p><c:out value="${thread.description}"/></p>
    <kantega:section id="controls">

        <forum:haspermisson permission="POST_IN_THREAD" object="${thread}">
            <a href="editpost?threadId=<c:out value="${thread.id}"/>"><spring:message code="post.add"/></a>
        </forum:haspermisson>
        <forum:haspermisson permission="EDIT_THREAD" object="${thread}">
            | <a href="editthread?threadId=<c:out value="${thread.id}"/>"><spring:message code="thread.edit"/> </a>
        </forum:haspermisson>
        <forum:haspermisson permission="DELETE_THREAD" object="${thread}">
            | <a href="javascript:deleteThread(<c:out value="${thread.id}"/>)"><spring:message code="thread.delete"/> </a>
        </forum:haspermisson>

    </kantega:section>

    <div style="padding-bottom: 10px;text-align:right;">
        <kantega:getsection id="controls"/>
    </div>
    <div>

        <c:if test="${pages > 1}">
            <table width="100%" cellpadding="0" cellspacing="0" style="padding-bottom: 5px">
                <tr>
                    <td align="right">
                        <spring:message code="thread.pagexofy" arguments="${current+1},${pages}"/>:

                        <c:forEach var="index" items="${startindexes}" varStatus="status">
                            <c:if test="${startindex == index}">
                                <c:set var="current" value="-current"/>
                            </c:if>
                            <c:if test="${startindex != index}">
                                <c:set var="current" value=""/>
                            </c:if>
                            <a class="forum-pagenavigation<c:out value="${current}"/>" href="viewthread?threadId=<c:out value="${thread.id}"/>&amp;startIndex=<c:out value="${index}"/>"><c:out value="${status.index+1}"/></a>
                        </c:forEach>
                    </td>
                </tr>
            </table>

        </c:if>



    </div>

    <c:choose>
        <c:when test="${empty posts}">
            <spring:message code="thread.empty"/>
        </c:when>

        <c:otherwise>

            <table width="100%" cellpadding="0" cellspacing="0">
                <c:forEach items="${posts}" var="post" varStatus="status">
                    <tr class="forum-labelRow">
                        <td>
                            <a name="post_<c:out value="${post.id}"/>"></a>
                            <c:out value="${post.subject}"/>
                            <br>
                            <span style="font-weight: normal;">(<c:out value="${post.author}"/>, <fmt:formatDate value="${post.postDate}" pattern="dd.MM.yyyy"/>
                                <fmt:formatDate value="${post.postDate}" pattern="HH:mm"/>)</span>
                        </td>
                        <td align="right" style="font-weight: normal;">

                            <forum:haspermisson permission="EDIT_POST" object="${post}">
                                <a href="editpost?postId=<c:out value="${post.id}"/>">Endre</a> |
                            </forum:haspermisson>

                            <forum:haspermisson permission="POST_IN_THREAD" object="${thread}">
                                <a href="editpost?replyId=<c:out value="${post.id}"/>&threadId=<c:out value="${post.thread.id}"/>">Svar</a>
                            </forum:haspermisson>

                            <forum:haspermisson permission="DELETE_POST" object="${post}">
                                | <a href="javascript:deletePost(<c:out value="${post.id}"/>)">Slett</a>

                            </forum:haspermisson>
                        </td>
                    </tr>
                    <tr class="forum-tableRow0">
                        <td colspan="2">
                            <div class="forum-body">
                                <c:out value="${post.body}" escapeXml="false"/>
                            </div>
                            <c:if test="${post.attachments != null}">
                                <div class="forum-attachments">
                                    <c:forEach items="${post.attachments}" var="attachment" varStatus="status">
                                        <c:choose>
                                            <c:when test="${attachment.image}">
                                                <a href="viewattachment?attachmentId=<c:out value="${attachment.id}"/>" target="_blank"><img src="viewattachment?attachmentId=<c:out value="${attachment.id}"/>&width=100&height=100" alt="<c:out value="${attachment.fileName}"/>" border="0"></a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="oa-forum-attachment" href="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=<c:out value="${attachment.id}"/>" target="_blank">
                                                    <c:out value="${attachment.fileName}"/>
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </div>
                            </c:if>

                        </td>
                    </tr>

                </c:forEach>
            </table>

        </c:otherwise>
    </c:choose>

    <div style="padding-top: 10px;text-align:right;">
        <kantega:getsection id="controls"/>
    </div>
</kantega:section>

<%@ include file="include/design/design.jsf"%>