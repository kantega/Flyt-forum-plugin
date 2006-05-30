<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ taglib prefix="modules" uri="http://www.kantega.no/aksess/tags/modules" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="contentmain">
        <form method="POST">
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
                                            <input type="text" size="50" name="author" value="<c:out value="${status.value}"/>">
                                        </td>
                                        <td>
                                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                                        </td>

                                    </spring:bind>
                                </tr>
                                <%--
                                <tr class="forum-labelRow">
                                    <td valign="top" colspan="2"><spring:message code="post.email"/>:</td>
                                </tr>

                                <tr class="forum-tableRow0">

                                    <spring:bind path="post.email">
                                        <td>
                                            <input type="text" size="50" name="email" value="<c:out value="${status.value}"/>">
                                        </td>
                                        <td>
                                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                                        </td>

                                    </spring:bind>
                                </tr> --%>
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
                                <%--
                                <tr class="forum-labelRow">
                                    <td valign="top" colspan="2"><spring:message code="post.email"/>:</td>
                                </tr>

                                <tr class="forum-tableRow0">
                                    <td colspan="2">
                                        <c:out value="${user.email}"/>
                                    </td>
                                </tr> --%>
                            </c:otherwise>
                        </c:choose>

                </modules:userprofile>
                <tr class="forum-labelRow">
                    <td valign="top" colspan="2"><spring:message code="post.subject"/>:</td>
                </tr>

                <tr class="forum-tableRow0">
                    <spring:bind path="post.subject">
                        <td>
                            <input type="text" size="50" name="subject" value="<c:out value="${status.value}"/>">
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
                            <textarea name="body" rows="10" cols="50"><c:out value="${status.value}"/></textarea>
                        </td>
                        <td>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr class="forum-tableRow0">
                    <td colspan="2" align="right">
                        <input type="submit" value="<spring:message code="post.edit.save"/>">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>
