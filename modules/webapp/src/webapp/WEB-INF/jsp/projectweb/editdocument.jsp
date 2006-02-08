<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>

<kantega:section id="title">
    <c:choose>
        <c:when test="${document.id == 0}"><spring:message code="document.new"/></c:when>
        <c:otherwise><spring:message code="document.edit"/></c:otherwise>
    </c:choose>

</kantega:section>

<kantega:section id="content">

    <div class="contentmain">
        <div class="heading">
            <c:choose>
                <c:when test="${document.id == 0}"><spring:message code="document.new"/></c:when>
                <c:otherwise><spring:message code="document.edit"/></c:otherwise>
            </c:choose>
        </div>

        <form method="post">

            <table>
                <tr>
                    <td><spring:message code="document.title"/>:</td>
                    <td>
                        <spring:bind path="document.title">
                            <input name="title" value="<c:out value="${status.value}"/>">
                            <c:out value="${status.errorMessage}"/>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><spring:message code="document.status"/>:</td>
                    <td>
                        <spring:bind path="document.status">
                            <select name="status">
                                <c:forEach items="${statuses}" var="status">
                                    <option <c:if test="${status.id == document.status.id}">selected</c:if> value="<c:out value="${status.id}"/>">
                                        <c:out value="${status.name}"/></option>
                                </c:forEach>
                            </select>
                            <c:out value="${status.errorMessage}"/>
                        </spring:bind>

                    </td>
                </tr>
                <tr>
                    <td><spring:message code="document.file"/>:</td>
                    <td>
                        <spring:bind path="document.content">
                            <input type="file" name="file">
                            <c:out value="${status.errorMessage}"/>
                        </spring:bind>

                    </td>
                </tr>
            </table>
            <input type="submit" value="<spring:message code="general.save"/>">
        </form>
    </div>
</kantega:section>
<%@ include file="include/design/design.jsf" %>