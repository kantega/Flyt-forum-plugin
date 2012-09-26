<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="add_document">
    <pw:haspermission project="${project}" permission="ADD_DOCUMENT">
        <div style="padding-bottom: 2px;">

            <c:choose>
                <c:when test="${activity.id != null}">
                    <c:set var="parameters" value="projectId=${project.id}&activityId=${activity.id}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="parameters" value="projectId=${project.id}"/>
                </c:otherwise>
            </c:choose>
            <a class="button" style="vertical-align: middle;"
               href="editdocument?<c:out value="${parameters}"/>"><img
                    src="../bitmaps/projectweb/ikon_rediger.gif" border="0"
                    style="vertical-align: middle;"><spring:message code="documentlist.add"/>
            </a>

        </div>
    </pw:haspermission>

</kantega:section>
