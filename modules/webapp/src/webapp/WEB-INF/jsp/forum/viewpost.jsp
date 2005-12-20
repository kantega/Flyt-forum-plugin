<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <b><c:out value="${post.subject}"/></b><br>
    <c:out value="${post.body}"/><br>
</kantega:section>

<%@ include file="include/design/design.jsf"%>