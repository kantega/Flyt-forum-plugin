<%@ page import="no.kantega.forum.util.ForumUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <script type="text/javascript">
        function submitForm() {
            var form = document.editthread;
            var a = '';
            <%
            ForumUtil.getNoSpamCode(out);
            %>
            form.nospam.value = a;
            form.submit();
        }
    </script>
    <noscript><h1><spring:message code="post.noscript"/></h1></noscript>
    <div class="contentmain">
        <form method="POST" name="editthread">
            <input type="hidden" name="nospam" value="">
            <spring:bind path="thread.id">
                <input type="hidden" name="id" value="<c:out value="${status.value}"/>">
            </spring:bind>
            <div class="forum-heading">
                <c:choose>
                    <c:when test="${thread.id == 0}"><spring:message code="thread.addthread"/></c:when>
                    <c:otherwise><c:out value="${thread.name}"/></c:otherwise>
                </c:choose>
            </div>
            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                <tr class="forum-labelRow">
                    <td valign="top"><spring:message code="thread.name"/>:</td>
                </tr>
                <tr class="forum-tableRow0">
                    <spring:bind path="thread.name">
                        <td>
                            <input type="text" name="name" value="<c:out value="${status.value}"/>" class="forum-editthread-name">
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr class="forum-labelRow">
                    <td valign="top"><spring:message code="thread.description"/>:</td>
                </tr>
                <tr class="forum-tableRow0">
                    <spring:bind path="thread.description">
                        <td>
                            <textarea name="description" rows="5" cols="80"><c:out value="${status.value}"/></textarea>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr class="forum-tableRow1">
                    <td colspan="2" align="left">
                        <input type="button" class="submit" onclick="submitForm()" value="<spring:message code="thread.edit.save"/>">
                        <input type="button" class="button" onclick="history.back();" value="<spring:message code="cancel"/>">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>
