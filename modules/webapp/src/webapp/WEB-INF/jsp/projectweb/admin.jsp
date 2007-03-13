<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="title">
    Administrasjon
</kantega:section>

<kantega:section id="content">
    <div class="contentmain">
    <div class="heading"><spring:message code="admin.heading"/></div>

    <ul>
        <li><a href="administratorlist"><spring:message code="admin.administrators"/></a></li>
    </ul>
    </div>
</kantega:section>

<%@ include file="include/design/design.jsf" %>