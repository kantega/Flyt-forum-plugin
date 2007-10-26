<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="tittel">
    <c:out value="${document.project.name}"/>: <c:out value="${document.title}"/>
</kantega:section>

<kantega:section id="summary">
    <div class="activitylistsearch">
    <table cellpadding="0" cellspacing="0">
        <tr class="tableHeading">
            <td colspan="2"><spring:message code="document.summary"/></td>
        </tr>
    <%
        int c = 0;
    %>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="document.category"/>:</td>
            <td class="dottedTd"><c:out value="${document.category.name}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="document.uploader"/>:</td>
            <td class="dottedTd"><pw:resolveuser user="${document.uploader}"/></td>
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
                    <form name="deleteForm" action="deletedocument" method="POST">
                        <input name="documentId" type="hidden" value="<c:out value="${document.id}"/>">
                        <input name="documentId" type="hidden" value="<c:out value="${activityId}"/>">
                    </form>
                    <script LANGUAGE="JavaScript" type="text/javascript">
                    <!--
                    function confirmDelete() {

                        if (confirm("<spring:message code="document.confirmdelete"/>")) {
                            document.deleteForm.submit();
                        }
                    }
                    // -->
                    </script>

                    <a href="javascript:confirmDelete()" >
                        <img style="vertical-align: middle" src="../bitmaps/projectweb/slett.gif" border="0">
                        <spring:message code="general.delete"/>
                    </a>
                </td>
            </tr>
        </pw:haspermission>
    </table>
    </div>
</kantega:section>

<kantega:section id="innhold">
    <div class="activitylistmain">
    <div class="heading"><c:out value="${document.project.name}"/>: <c:out value="${document.title}"/></div>
    <c:forEach items="${document.activities}" var="activity">
        <spring:message code="document.activity"/>
            <a href="activity?activityId=<c:out value="${activity.id}"/>">
                <c:out value="${activity.title}"/>
            </a><br>
    </c:forEach>
    <spring:message code="document.filename"/>:
    <a href="document?action=download&documentId=<c:out value="${document.id}"/>">
        <c:out value="${document.fileName}"/>
    </a><br><br>

    <c:out value="${document.description}"/><br>
    </div>
</kantega:section>


<%@include file="include/design/design.jsf"%>