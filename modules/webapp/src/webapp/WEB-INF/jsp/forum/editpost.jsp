<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ taglib prefix="modules" uri="http://www.kantega.no/aksess/tags/modules" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="contentmain">
        <form method="POST" enctype="multipart/form-data">
            <spring:bind path="post.id">
                <input type="hidden" name="id" value="<c:out value="${status.value}"/>">
            </spring:bind>
            <div class="forum-heading">
                <c:choose>
                    <c:when test="${post.thread.id == 0}"><spring:message code="thread.addthread"/></c:when>
                    <c:when test="${post.id == 0}"><spring:message code="post.addpost"/></c:when>
                    <c:otherwise><c:out value="${post.subject}"/></c:otherwise>
                </c:choose>
            </div>
            <table border="0" cellpadding="0" cellspacing="0">

                <modules:userprofile var="user">
                        <c:choose>
                            <c:when test="${user == null}">
                                <tr class="forum-labelRow">
                                    <td valign="top" colspan="2"><spring:message code="post.name"/>:</td>
                                </tr>
                                <tr class="forum-tableRow0">
                                    <spring:bind path="post.author">
                                        <td>
                                            <input type="text" size="50" name="author" value="<c:out value="${status.value}"/>" class="forum-editpost-author">
                                        </td>
                                        <td>
                                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                                        </td>
                                    </spring:bind>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr class="forum-labelRow">
                                    <td valign="top" colspan="2"><spring:message code="post.name"/>:</td>
                                </tr>

                                <tr class="forum-tableRow0">
                                    <td colspan="2">
                                        <c:out value="${user.fullName}"/>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>

                </modules:userprofile>
                <tr class="forum-labelRow">
                    <td valign="top" colspan="2"><spring:message code="post.subject"/>:</td>
                </tr>

                <tr class="forum-tableRow0">
                    <spring:bind path="post.subject">
                        <td>
                            <input type="text" size="50" name="subject" value="<c:out value="${status.value}"/>" class="forum-editpost-subject">
                        </td>
                        <td>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr class="forum-labelRow">
                    <td valign="top" colspan="2"><spring:message code="post.body"/>:</td>
                </tr>
                <tr class="forum-tableRow0">
                    <spring:bind path="post.body">
                        <td>
                            <textarea name="body" rows="15" cols="50" class="forum-editpost-body"><c:out value="${status.value}"/></textarea>
                            <c:if test="${!post.approved}">
                                <p><span class="forum-info"><spring:message code="post.moderate.info"/></span></p>
                            </c:if>
                        </td>
                        <td>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <forum:haspermisson permission="ATTACH_FILE">
                <tr class="forum-labelRow">
                    <td valign="top" colspan="2"><spring:message code="post.attachments"/>:</td>
                </tr>
                <tr class="forum-tableRow0">
                    <td>
                        <div class="forum-attachments-info">
                            <spring:message code="post.attachments.info"/>
                        </div>

                        <input type="file" name="attachment1"><br>
                        <input type="file" name="attachment2"><br>
                        <input type="file" name="attachment3"><br>
                        <input type="file" name="attachment4"><br>
                        <input type="file" name="attachment5"><br>

                        <div class="forum-attachments-existing">
                            <c:forEach items="${post.attachments}" var="attachment" varStatus="status">
                                <a href="viewattachment?attachmentId=<c:out value="${attachment.id}"/>" target="_blank"><c:out value="${attachment.fileName}"/></a><br>
                            </c:forEach>
                        </div>

                    </td>
                    <td>&nbsp;</td>
                </tr>
                </forum:haspermisson>
                <tr class="forum-tableRow0">
                    <td colspan="2" align="right">
                        <c:choose>
                            <c:when test="${post.thread.id == 0}"><input type="submit" class="submit" value="<spring:message code="thread.edit.save"/>"></c:when>
                            <c:otherwise><input type="submit" class="submit" value="<spring:message code="post.edit.save"/>"></c:otherwise>
                        </c:choose>
                        <input type="button" class="button" onclick="history.back();" value="<spring:message code="cancel"/>">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>
