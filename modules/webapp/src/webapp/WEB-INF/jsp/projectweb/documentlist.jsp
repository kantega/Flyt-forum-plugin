<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<%@ include file="adddocument_part.jsp" %>

<kantega:section id="tittel">
    <spring:message code="documentlist.header" arguments="${project.name}"/>
</kantega:section>


<kantega:section id="innhold">
<script type="text/javascript">
    function doSort(order) {
        document.searchform.order.value = order;
        document.searchform.submit();
    }

    function confirmDelete(id) {
        if (confirm("<spring:message code="document.confirmdelete"/>")) {
            document.deleteForm.documentId.value = id;
            document.deleteForm.submit();
        }
    }
</script>
<div class="heading"><spring:message code="documentlist.header" arguments="${project.name}"/></div>
<c:choose>
<c:when test="${not empty documents}">
    <table border="0" cellspacing="0" width="100%">
        <tr>
            <td colspan="6" align="right">
                <kantega:getsection id="add_document"/>
            </td>
        </tr>
        <tr class="tableHeading">
            <td><a href="javascript:doSort('title')"><spring:message code="document.title"/></a></td>
            <td><a href="javascript:doSort('category')"><spring:message code="document.category"/></a></td>
            <td><a href="javascript:doSort('editdate')"><spring:message code="document.editdate"/></a></td>
            <pw:haspermission project="${project}" permission="EDIT_DOCUMENT">
                <c:set var="canEdit" value="true"/>
                <td>&nbsp;</td>
            </pw:haspermission>
            <pw:haspermission project="${project}" permission="DELETE_DOCUMENT">
                <c:set var="canDelete" value="true"/>
                <td>&nbsp;</td>
            </pw:haspermission>
        </tr>
        <c:forEach items="${documents}" var="document" varStatus="status">
            <tr class="tableRow<c:out value="${status.count % 2}"/>" valign="top">
                <td>
                    <a href="document?action=download&documentId=<c:out value="${document.id}"/>" title="<c:out value="${document.description}"/>"><c:out value="${document.title}"/></a>
                </td>
                <td>
                    <c:out value="${document.category.name}"/>
                </td>
                <td>
                    <fmt:formatDate value="${document.editDate}"/>
                </td>
                <c:if test="${canEdit}">
                    <td>
                        <a class="button" style="vertical-align: middle;" href="editdocument?documentId=<c:out value="${document.id}"/>">
                         <img style="vertical-align: middle" src="../bitmaps/projectweb/ikon_rediger.gif" border="0">
                         <spring:message code="general.edit"/>
                     </a>
                    </td>
                </c:if>
                <c:if test="${canDelete}">
                    <td>
                        <a class="button" style="vertical-align: middle;" href="Javascript:confirmDelete('<c:out value="${document.id}"/>')">
                         <img style="vertical-align: middle" src="../bitmaps/projectweb/slett.gif" border="0">
                         <spring:message code="general.delete"/>
                     </a>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
    </table>
</c:when>
<c:otherwise>
    <div style="padding-bottom:10px;"><spring:message code="documentlist.nonefound"/></div>
    <pw:haspermission project="${project}" permission="ADD_DOCUMENT">

        <a class="button" style="vertical-align: middle;"
           href="editdocument?projectId=<c:out value="${project.id}"/>"><img src="../bitmaps/projectweb/ikon_rediger.gif"
                                                                            border="0" style="vertical-align: middle;">
            <spring:message code="documentlist.add"/></a>
    </pw:haspermission>
</c:otherwise>
</c:choose>

<form name="deleteForm" action="deletedocument" method="POST">
    <input name="documentId" type="hidden" value="">
</form>

</kantega:section>

<kantega:section id="relatert innhold">
    <form name="searchform" action="documentlist" method="POST">
    <input type="hidden" name="order">
    <div class="activitylistsearch">
        <div class="heading2"><spring:message code="documentlist.search"/></div>

        <div class="activitysearchsection">
            <spring:message code="documentlist.text"/>:
            <br>
            <table>
                <tr>
                    <td><input name="text" value="<c:out value="${text[0]}"/>" class="activitylistsearchinput"></td>
                    <td><a href="Javascript:document.searchform.submit()"><img src="../bitmaps/projectweb/sok.gif" border="0"></a></td>
                </tr>
            </table>

            <input type="checkbox" name="searchContent" <c:if test="${searchContent != null}">checked</c:if> >
            <spring:message code="documentlist.textsearchcontent"/>
        </div>

        <div class="activitysearchsection">
            <spring:message code="document.category"/>:<br>
            <select multiple name="categories" size="5" class="activitylistselect">
                <option value="-1" <c:if test="${categories[0] eq '-1'}">selected</c:if> ><spring:message code="general.any"/></option>
                <c:forEach items="${allcategories}" var="category">
                    <c:set var="found" value="false"/>
                    <c:forEach items="${categories}" var="CA">
                        <c:if test="${category.id eq ca}">
                            <c:set var="found" value="true"/>
                        </c:if>
                    </c:forEach>
                    <option <c:if test="${found}">selected</c:if> value="<c:out value="${category.id}"/>"><c:out value="${category.name}"/></option>
                </c:forEach>

            </select>
        </div>

    </div>

    <input type="hidden" name="projectId" value="<c:out value="${project.id}"/>">
    </form>

</kantega:section>
<%@ include file="include/design/design.jsf" %>