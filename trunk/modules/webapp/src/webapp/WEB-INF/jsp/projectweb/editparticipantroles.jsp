<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<kantega:section id="tittel">
    <spring:message code="participants.editroles.header" arguments="${profile.fullName}"/>
</kantega:section>

<kantega:section id="innhold">
    <div class="contentmain">
        <div class="heading"><spring:message code="participants.editroles.header" arguments="${profile.fullName}"/></div>

        <form action="editparticipantroles" method="POST">
            <input name="participantId" type="hidden" value="<c:out value="${participant.id}"/>">

            <table border="0">
                <tr class="tableHeading">
                    <td><spring:message code="participants.editroles.choose"/></td>
                </tr>

                <c:forEach items="${hasRoles}" var="role" varStatus="status">
                    <tr class="tableRow<c:out value="${status.count % 2}"/>">
                        <td>
                            <input <c:if test="${role.hasRole}">checked</c:if> name="has_<c:out value="${role.role.id}"/>" type="checkbox"> <c:out value="${role.role.name}"/>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td align="right">
                        <input type="submit" value="<spring:message code="general.save"/>">
                    </td>
                </tr>
            </table>
          </form>
    </div>

</kantega:section>

<%@include file="include/design/design.jsf"%>