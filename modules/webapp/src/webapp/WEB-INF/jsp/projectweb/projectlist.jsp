<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="tittel">
    <spring:message code="projectlist.projects" arguments="${project.name}"/>
</kantega:section>

<kantega:section id="innhold">
    <div class="contentmain">
    <div class="heading"><spring:message code="projectlist.projects"/>:</div>

    <table cellpadding="0" cellspacing="0" width="100%">
        <pw:hasglobalpermission permission="ADMINISTRATOR">
            <tr>
                <td colspan="4" align="right">
                    <div style="padding-bottom: 5px">
                        <a style="vertical-align:middle;" class="button" href="addproject"><img src="../bitmaps/projectweb/ikon_leggtilkommentar.gif" border="0" style="vertical-align:middle;"><spring:message code="projectlist.addproject"/></a>
                    </div>
                </td>
            </tr>
        </pw:hasglobalpermission>
        <tr class="tableHeading">
            <td><spring:message code="project.name"/></td>
            <td><spring:message code="project.goal"/></td>
            <td><spring:message code="project.status"/></td>
            <td><spring:message code="project.leader"/></td>
        </tr>
        <c:forEach items="${projects}" var="project" varStatus="status">
            <tr class="tableRow<c:out value="${status.count % 2}"/>">
                <td valign="top">
                    <a href="project?projectId=<c:out value="${project.id}"/>">
                        <strong><c:out value="${project.name}"/></strong>
                    </a>

                </td>
                <td valign="top" width="250" class="dottedTd">
                    <c:out value="${project.goal}"/>&nbsp;
                </td>
                <td valign="top" width="250" class="dottedTd">
                    <c:out value="${project.status}"/>&nbsp;
                </td>
                <td valign="top" class="dottedTd">
                    <c:set var="email"><pw:resolveuser user="${project.leader}" info="email"/></c:set>
                    <a href="mailto:<c:out value="${email}"/>"><c:out value="${email}"/></a>&nbsp;
                </td>
            </tr>
        </c:forEach>
    </table>

    <c:if test="${not empty activities}">
    <br>
    <div class="heading"><spring:message code="projectlist.myactivities"/>:</div>

    <table border="0" cellspacing="0" width="100%">
        <tr class="tableHeading">
            <td><spring:message code="activity.title"/></td>
            <td><spring:message code="activity.status"/></td>
            <td><spring:message code="activity.endDate"/></td>
            <td><spring:message code="activity.phase"/></td>
            <td><spring:message code="activity.priority"/></td>
        </tr>
        <c:forEach items="${activities}" var="activity" varStatus="status">
            <tr class="tableRow<c:out value="${status.count % 2}"/>">
                <td>
                    <a href="activity?activityId=<c:out value="${activity.id}"/>"><c:out value="${activity.title}"/></a>
                </td>
                <td>
                    <c:out value="${activity.status.name}"/>
                </td>
                <td>
                    <fmt:formatDate value="${activity.endDate}"/>
                </td>
                <td>
                    <c:out value="${activity.projectPhase.name}"/>
                </td>
                <td>
                    <c:out value="${activity.priority.name}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
    </c:if>
    </div>
</kantega:section>
<%@include file="include/design/design.jsf"%>