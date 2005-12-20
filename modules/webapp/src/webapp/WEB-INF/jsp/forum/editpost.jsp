<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="contentmain">
        <form method="POST">
            <spring:bind path="post.id">
                <input type="hidden" name="id" value="<c:out value="${status.value}"/>">
            </spring:bind>
            <div class="heading">
                <c:choose>
                    <c:when test="${post.id == 0}"><spring:message code="post.addpost"/></c:when>
                    <c:otherwise><c:out value="${post.subject}"/></c:otherwise>
                </c:choose>
            </div>
            <table border="0">
                <tr>
                    <td valign="top"><strong><spring:message code="post.subject"/>:</strong></td>
                    <spring:bind path="post.subject">
                        <td>
                            <input type="text" name="subject" value="<c:out value="${status.value}"/>">
                        </td>
                        <td>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr>
                    <td valign="top"><strong><spring:message code="post.body"/>:</strong></td>
                    <spring:bind path="post.body">
                        <td>
                            <textarea name="body" rows="5" cols="80"><c:out value="${status.value}"/></textarea>
                        </td>
                        <td>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr>
                    <td colspan="2" align="right">
                        <input type="submit" value="<spring:message code="post.edit.save"/>">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>
