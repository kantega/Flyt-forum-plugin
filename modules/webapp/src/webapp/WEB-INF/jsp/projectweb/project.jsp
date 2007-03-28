<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="tittel">
    <c:out value="${project.name}"/>
</kantega:section>

<kantega:section id="innhold">
    <div class="contentmain">
    <div class="heading"><c:out value="${project.name}"/></div>
    <table cellpadding="0" cellspacing="0" width="100%">
        <c:if test="${mayEdit}">
            <tr>
                <td></td>
                <td align="right" colspan="1">
                    <div style="padding-bottom: 5px">
                        <a class="button" href="editproject?projectId=<c:out value="${project.id}"/>" style="vertical-align: middle;"><img src="../bitmaps/projectweb/ikon_rediger.gif" border="0" style="vertical-align: middle;"><spring:message code="project.actions.edit"/></a>
                    </div>
                </td>
            </tr>

      </c:if>
        <%
            int c = 1;
        %>
        <tr class="tableRow<%=c++ % 2%>">
            <td width="150"><strong><spring:message code="project.name"/>:</strong></td>
            <td><c:out value="${project.name}"/></td>
        </tr>
        <tr class="tableRow<%=c++ % 2%>">
            <td><strong><spring:message code="project.code"/>:</strong></td>
            <td><c:out value="${project.code}"/></td>
        </tr>

        <tr class="tableRow<%=c++ % 2%>">
            <td valign="top"><strong><spring:message code="project.goal"/>:</strong></td>
            <td valign="top"><c:out value="${project.goal}"/></td>
        </tr>
        <tr class="tableRow<%=c++ % 2%>">
            <td valign="top"><strong><spring:message code="project.status"/>:</strong></td>
            <td valign="top"><c:out value="${project.status}"/></td>
        </tr>
        <tr class="tableRow<%=c++ % 2%>">
            <td><strong><spring:message code="project.from-to"/>:</strong></td>
            <td><fmt:formatDate value="${project.startDate}"/> - <fmt:formatDate value="${project.endDate}"/></td>
        </tr>
        <tr class="tableRow<%=c++ % 2%>">
            <td><strong><spring:message code="project.leader"/>:</strong></td>
            <td><pw:resolveuser user="${project.leader}"/></td>
        </tr>

        
        <tr class="tableRow<%=c++ % 2%>">
            <td><strong><spring:message code="project.public"/>:</strong></td>
            <td>
                <c:choose>
                    <c:when test="${project.publicProject}"><spring:message code="general.yes"/></c:when>
                    <c:otherwise><spring:message code="general.no"/></c:otherwise>
                </c:choose>
            </td>
        </tr>

    </table>


    </div>
</kantega:section>
<%@include file="include/design/design.jsf"%>