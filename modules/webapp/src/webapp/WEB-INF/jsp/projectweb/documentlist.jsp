<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="title">
    <spring:message code="documentlist.header" arguments="${project.name}"/>
</kantega:section>

<kantega:section id="content">
<script type="text/javascript">
    function doSort(order) {
        document.searchform.order.value = order;
        document.searchform.submit();
    }
</script>

<form name="searchform" action="documentlist" method="POST">

<table border="0" cellpadding="0" cellspacing="0" height="100%">
<tr>
<td valign="top">
    <div class="activitylistsearch">
        <div class="heading2"><spring:message code="documentlist.search"/></div>
        <input type="hidden" name="order">


        <input type="hidden" name="projectId" value="<c:out value="${project.id}"/>">

        <div class="activitysearchsection">
            <spring:message code="documentlist.text"/>:
            <br>
            <table>
                <tr>
                    <td><input name="text" value="<c:out value="${text[0]}"/>" class="activitylistsearchinput"></td>
                    <td><a href="Javascript:searchform.submit()"><img src="../bitmaps/projectweb/sok.gif"
                                                                      border="0"></a></td>

                </tr>
            </table>
        </div>

        <div class="activitysearchsection">

            <spring:message code="document.status"/>:<br>
            <select multiple name="statuses" size="5" class="activitylistselect">
                <option value="-1" <c:if test="${statuses[0] eq '-1'}">selected</c:if> ><spring:message
                        code="general.any"/></option>
                <c:forEach items="${allstatuses}" var="status">
                    <c:set var="found" value="false"/>
                    <c:forEach items="${statuses}" var="st">
                        <c:if test="${status.id eq st}">
                            <c:set var="found" value="true"/>
                        </c:if>
                    </c:forEach>
                    <option <c:if test="${found}">selected</c:if> value="<c:out value="${status.id}"/>"><spring:message
                            code="activitystatus.${status.code}"/></option>
                </c:forEach>      <%-- Velger � bruke samme oversettinger for status som activity --%>

            </select>
        </div>

        <div align="center">
            <a href="Javascript:searchform.submit()"><img src="../bitmaps/projectweb/sok.gif" border="0"></a>
        </div>

    </div>
</td>
<td valign="top">
<div class="activitylistmain">
<div class="heading"><spring:message code="documentlist.header" arguments="${project.name}"/></div>

<c:choose>
<c:when test="${not empty documents}">
    <table border="0" cellspacing="0">
        <tr>
            <td colspan="3">
                                <%
                                    //TODO: sette ADD_ACTIVITY til ADD_DOCUMENT
                                 %>
                                <pw:haspermission project="${project}" permission="ADD_ACTIVITY">
                                <div style="padding-bottom: 2px;">
                                    <a class="button" style="vertical-align: middle;"
                                       href="editdocument?projectId=<c:out value="${project.id}"/>"><img
                                            src="../bitmaps/projectweb/ikon_rediger.gif" border="0"
                                            style="vertical-align: middle;"><spring:message code="documentlist.add"/>
                                    </a>
                                </div>
                            </pw:haspermission>
            </td>
        </tr>
        <tr class="tableHeading">
            <td><a href="javascript:doSort('title')"><spring:message code="document.title"/></a></td>
            <td><a href="javascript:doSort('status')"><spring:message code="document.status"/></a></td>
            <td><a href="javascript:doSort('status')"><spring:message code="document.editdate"/></a></td>
            <td>&nbsp;</td>
        </tr>
        <c:forEach items="${documents}" var="document" varStatus="status">
            <tr class="tableRow<c:out value="${status.count % 2}"/>">
                <td>
                    <a href="document?documentId=<c:out value="${document.id}"/>"><c:out
                            value="${document.title}"/></a>
                </td>
                <td>
                    <c:out value="${document.status.name}"/>
                </td>
                <td>
                    <c:out value="${document.editDate}"/>
                </td>
                <td>
                    <a href="document?action=download&documentId=<c:out value="${document.id}"/>">
                        <spring:message code="documentlist.download"/>
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:when>
<c:otherwise>
    <div><spring:message code="documentlist.nonefound"/></div>
    <pw:haspermission project="${project}" permission="ADD_DOCUMENT">

        <a class="button" style="vertical-align: middle;"
           href="editdocument?projectId=<c:out value="${project.id}"/>"><img src="../bitmaps/projectweb/ikon_rediger.gif"
                                                                            border="0" style="vertical-align: middle;">
            <spring:message code="documentlist.add"/></a>
    </pw:haspermission>
</c:otherwise>
</c:choose>
</div>

</td>
</tr>
</table>
</form>
</kantega:section>
<%@ include file="include/design/design.jsf" %>