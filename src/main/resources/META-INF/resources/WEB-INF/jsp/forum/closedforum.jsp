<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=utf-8" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading">
        <spring:message code="closedforum.title"/>
    </div>


    <spring:message code="closedforum.text"/>


</kantega:section>

<%@ include file="include/design/design.jsf" %>