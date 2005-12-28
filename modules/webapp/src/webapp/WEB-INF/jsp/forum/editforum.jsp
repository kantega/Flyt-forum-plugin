<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="contentmain">
        <form method="POST">
            <spring:bind path="forum.id">
                <input type="hidden" name="id" value="<c:out value="${status.value}"/>">
            </spring:bind>
            <div class="heading">
                <c:choose>
                    <c:when test="${forum.id == 0}"><spring:message code="forum.addforum"/></c:when>
                    <c:otherwise><c:out value="${forum.name}"/></c:otherwise>
                </c:choose>
            </div>
            <table border="0">
                <tr>
                    <td valign="top"><strong><spring:message code="forum.name"/>:</strong></td>
                    <spring:bind path="forum.name">
                        <td>
                            <input type="text" name="name" value="<c:out value="${status.value}"/>">
                        </td>
                        <td>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr>
                    <td valign="top"><strong><spring:message code="forum.description"/>:</strong></td>
                    <spring:bind path="forum.description">
                        <td>
                            <textarea name="description" rows="5" cols="80"><c:out value="${status.value}"/></textarea>
                        </td>
                        <td>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr>
                    <td colspan="2" align="right">
                        <input type="submit" value="<spring:message code="forum.edit.save"/>">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>
