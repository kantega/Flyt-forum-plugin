<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project"%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>

<kantega:section id="tittel">
    <spring:message code="economy.edit"/>
</kantega:section>

<kantega:section id="innhold">

    <div class="contentmain">
    <div class="heading">
        <spring:message code="economy.edit"/>
    </div>

    <form method="post">

        <table>
            <tr>
                <td><spring:message code="economy.estimated"/>:</td>
                <td>
                    <spring:bind path="activity.estimatedHours">
                        <input name="estimatedHours" value="<c:out value="${status.value}"/>" size="5">
                        <c:out value="${status.errorMessage}"/>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td><spring:message code="economy.used"/>:</td>
                <td>
                    <spring:bind path="activity.usedHours">
                        <input name="usedHours" value="<c:out value="${status.value}"/>" size="5">
                        <c:out value="${status.errorMessage}"/>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td><spring:message code="economy.left"/>:</td>
                <td>
                    <spring:bind path="activity.estimatedLeftHours">
                        <input name="estimatedLeftHours" value="<c:out value="${status.value}"/>" size="5">
                        <c:out value="${status.errorMessage}"/>
                    </spring:bind>
                </td>
            </tr>

        </table>
        <input type="submit" value="<spring:message code="general.save"/>">
    </form>
    </div>
</kantega:section>
<%@include file="include/design/design.jsf"%>