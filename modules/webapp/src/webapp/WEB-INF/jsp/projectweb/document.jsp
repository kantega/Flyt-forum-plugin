<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="title">
    <c:out value="${document.project.name}"/>: <c:out value="${document.title}"/>
</kantega:section>

<kantega:section id="summary">
        <tr class="tableHeading">
            <td colspan="2"><spring:message code="document.summary"/></td>
        </tr>
    <%
        int c = 0;
    %>
        <tr class="tableRow<%=c++ % 2%>">
            <td>Id:</td>
            <td class="dottedTd"><c:out value="${document.project.code}"/><c:out value="${document.id}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="document.filename"/>:</td>
            <td class="dottedTd">
                <a href="document?action=download&documentId=<c:out value="${document.id}"/>">
                <c:out value="${document.fileName}"/></a>
            </td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="document.category"/>:</td>
            <td class="dottedTd"><c:out value="${document.category.name}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="document.type"/>:</td>
            <td class="dottedTd"><c:out value="${document.contentType}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="document.uploader"/>:</td>
            <td class="dottedTd"><c:out value="${document.uploader}"/></td>
        </tr>
        <pw:haspermission project="${document.project}" permission="EDIT_DOCUMENT">
            <tr  class="tableRow<%=c++ % 2%>">
                <td colspan="2"  align="right">
                     <a href="editdocument?documentId=<c:out value="${document.id}"/>">
                         <img style="vertical-align: middle" src="../bitmaps/projectweb/ikon_rediger.gif" border="0">
                         <spring:message code="general.edit"/>
                     </a>
                </td>
            </tr>
        </pw:haspermission>
        <pw:haspermission project="${document.project}" permission="DELETE_DOCUMENT">
            <tr  class="tableRow<%=c++ % 2%>">
                <td colspan="2"  align="right">

                    <script LANGUAGE="JavaScript">
                    <!--
                    function confirmSubmit()
                    {
                    var agree=confirm("<spring:message code="document.confirmdelete"/>");
                    if (agree)
                        return true ;
                    else
                        return false ;
                    }
                    // -->
                    </script>

                    <a onclick="return confirmSubmit()" href="document?action=delete&documentId=<c:out value="${document.id}"/>">
                        <img style="vertical-align: middle" src="../bitmaps/projectweb/slett.gif" border="0">
                        <spring:message code="general.delete"/>
                    </a>
                </td>
            </tr>
        </pw:haspermission>
    </kantega:section>

<kantega:section id="main">
    <div class="heading"><c:out value="${document.project.name}"/>: <c:out value="${document.title}"/></div>

    <kantega:hassection id="message">
        <kantega:getsection id="message"/>
    </kantega:hassection><br>

    <c:out value="${document.description}"/><br>
</kantega:section>

<kantega:section id="content">
    <table border="0" cellspacing="0">
        <tr>
            <td valign="top">
                <div class="activitylistsearch">
                    <table cellpadding="0" cellspacing="0">
                        <kantega:getsection id="summary"/>
                    </table>
                </div>
            </td>
            <td valign="top">
                <div class="activitylistmain">
                    <kantega:getsection id="main"/>
                </div>
    </td>
    </tr>
    </table>
</kantega:section>

<%@include file="include/design/design.jsf"%>