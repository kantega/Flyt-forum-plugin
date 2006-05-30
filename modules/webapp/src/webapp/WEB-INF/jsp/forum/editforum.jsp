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
            <div class="forum-heading">
                <c:choose>
                    <c:when test="${forum.id == 0}"><spring:message code="forum.addforum"/></c:when>
                    <c:otherwise><c:out value="${forum.name}"/></c:otherwise>
                </c:choose>
            </div>
            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                <tr class="forum-labelRow">
                    <td valign="top"><spring:message code="forum.name"/>:</td>
                </tr>
                <tr class="forum-tableRow0">
                    <spring:bind path="forum.name">
                        <td>
                            <input type="text" name="name" value="<c:out value="${status.value}"/>" size="50">
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr class="forum-labelRow">
                    <td valign="top"><spring:message code="forum.description"/></td>
                    </tr>
                <tr class="forum-tableRow0">
                    <spring:bind path="forum.description">
                        <td>
                            <textarea name="description" rows="5" cols="50"><c:out value="${status.value}"/></textarea>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr class="forum-tableRow1">
                    <td align="left">
                        <input type="submit" value="<spring:message code="forum.edit.save"/>">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>
