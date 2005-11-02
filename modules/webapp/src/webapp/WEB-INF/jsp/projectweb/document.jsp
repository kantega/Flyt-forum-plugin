<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project"%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<kantega:section id="content">
    <h1><c:out value="${document.title}"/>:</h1>
    <ul>
        <li>Status: <c:out value="${document.status}"/></li>
    </ul>
    <hr>
    <h2>Current steps</h2>
    <table border="1">
    <c:forEach items="${current}" var="line">
        <tr>
            <td><c:out value="${line.step.id}"/></td>
            <td><c:out value="${line.step.owner}"/></td>
            <td><c:out value="${line.step.startDate}"/></td>
            <td><c:out value="${line.step.finishDate}"/></td>
            <td><c:out value="${line.step.status}"/></td>
            <td><c:out value="${line.actionDescriptor.name}"/></td>
        </tr>
    </c:forEach>
    </table>
    <h2>Available workflow actions:</h2>
    <ul>
        <c:forEach items="${actions}" var="action">
        <li><a href="documentworkflow?action=<c:out value="${action.id}"/>&workflowId=<c:out value="${document.workflowId}"/>&documentId=<c:out value="${document.id}"/>"><c:out value="${action.name}"/></a></li>
        </c:forEach>
    </ul>
    <h2>Workflow history:</h2>
    <table border="1">
        <c:forEach items="${history}" var="line">
            <tr>
                <td><c:out value="${line.step.id}"/></td>
                <td><c:out value="${line.step.caller}"/></td>
                <td><c:out value="${line.step.startDate}"/></td>
                <td><c:out value="${line.step.finishDate}"/></td>
                <td><c:out value="${line.step.status}"/></td>
                <td><c:out value="${line.actionDescriptor.name}"/></td>
            </tr>
        </c:forEach>
    </table>
</kantega:section>
<%@include file="include/design/design.jsf"%>