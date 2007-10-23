<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<%@ include file="adddocument_part.jsp" %>


<kantega:section id="tittel">
<c:out value="${activity.project.name}"/>: <c:out value="${activity.title}"/>
</kantega:section>

<kantega:section id="summary">
        <tr class="tableHeading">
            <td colspan="2"><spring:message code="activity.summary"/></td>
        </tr>
    <%
        int c = 0;
    %>
        <tr class="tableRow<%=c++ % 2%>">
            <td>Id:</td>
            <td class="dottedTd"><c:out value="${activity.project.code}"/><c:out value="${activity.id}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="activity.phase"/>:</td>
            <td class="dottedTd"><c:out value="${activity.projectPhase.name}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="activity.status"/>:</td>
            <td class="dottedTd"><c:out value="${activity.status.name}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="activity.priority"/>:</td>
            <td class="dottedTd"><c:out value="${activity.priority.name}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="activity.startDate"/>:</td>
            <td class="dottedTd"><fmt:formatDate value="${activity.startDate}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="activity.endDate"/></td>
            <td class="dottedTd"><fmt:formatDate value="${activity.endDate}"/></td>
        </tr>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="activity.assignee"/>:</td>
            <td class="dottedTd"><pw:resolveuser user="${activity.assignee}"/></td>
        </tr>
        <tr class="tableRow<%=c++ % 2%>">
            <td><spring:message code="activity.reporter"/>:</td>
            <td class="dottedTd"><pw:resolveuser user="${activity.reporter}"/></td>
        </tr>
        <pw:haspermission project="${activity.project}" permission="EDIT_ACTIVITY">
            <tr  class="tableRow0">
                <td colspan="2"  align="right">
                     <a href="editactivitysummary?activityId=<c:out value="${activity.id}"/>"><spring:message code="general.edit"/></a>
                </td>
            </tr>
        </pw:haspermission>

</kantega:section>

<kantega:section id="economy">
        <tr class="tableHeading">
            <td colspan="2"><spring:message code="economy"/></td>
        </tr>
    <%
        int c = 0;
    %>
        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="economy.estimated"/>:</td>
            <td class="dottedTd"><c:out value="${activity.estimatedHours}"/></td>
        </tr>
        <tr class="tableRow<%=c++ % 2%>">
            <td><spring:message code="economy.used"/>:</td>
            <td class="dottedTd"><c:out value="${activity.estimatedHours}"/></td>
        </tr>
        <tr class="tableRow<%=c++ % 2%>">
            <td><spring:message code="economy.left"/>:</td>
            <td class="dottedTd"><c:out value="${activity.estimatedLeftHours}"/></td>
        </tr>
        <pw:haspermission project="${activity.project}" permission="EDIT_ACTIVITY">
        <tr class="tableRow0">
            <td colspan="2" align="right">
                 <a href="editactivityeconomy?activityId=<c:out value="${activity.id}"/>"><spring:message code="general.edit"/></a>
            </td>
        </tr>
    </pw:haspermission>

</kantega:section>

<kantega:section id="innhold">
    <script language="Javascript" type="text/javascript">
        function addComment() {
            document.getElementById("addcomment").style.display = 'block';
        }

        function confirmDelete(id) {
            if (confirm("<spring:message code="document.confirmdelete"/>")) {
                document.deleteForm.documentId.value = id;
                document.deleteForm.submit();
            }
        }
    </script>
    <div class="heading"><spring:message code="activity.heading"/>: <c:out value="${activity.title}"/></div>

    <c:out value="${activity.description}"/><br>
    <br>
    <table border="0" cellspacing="0" width="100%">
        <tr>
            <td colspan="5" align="right">
                <kantega:getsection id="add_document"/>
            </td>
        </tr>
        <tr class="tableHeading">
            <td><spring:message code="document.title"/></td>
            <td><spring:message code="document.editdate"/></td>
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
                <td><img style = "vertical-align: middle;" src="..<c:out value="${document.iconUrl}"/>"/>
                    <a href="document?action=download&documentId=<c:out value="${document.id}"/>"  title="<c:out value="${document.description}"/>"><c:out value="${document.title}"/></a>
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
    <br>
        <pw:haspermission project="${activity.project}" permission="ACTIVITY_ADD_COMMENT">
            <table width="100%">
                <tr>
                    <td align="right">
                        <a class="button" style="vertical-align: middle;" href="javascript:addComment()"><img src="../bitmaps/projectweb/ikon_leggtilkommentar.gif" border="0"  style="vertical-align: middle;"><spring:message code="activity.addcomment"/></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <script type="text/javascript">
                            function checkComment() {
                                if (document.comment.text.value == "") {
                                    alert('<spring:message code="activity.addcomment.missingtext"/>');
                                    document.comment.focus();
                                    return false;
                                }

                                return true;
                            }
                        </script>
                        <div id="addcomment" style="display: none">
                            <form action="addactivitycomment" name="comment" method="POST" onsubmit="return checkComment()">
                                <input name="activityId" type="hidden" value="<c:out value="${activity.id}"/>">
                                <textarea name="text" style="width: 100%; height: 100px"></textarea><br>
                                <input type="submit" value="<spring:message code="activity.addcomment"/>">
                            </form>
                        </div>
                    </td>

                </tr>
            </table>
            </pw:haspermission>
        <table width="100%" cellspacing="0" cellpadding="4">
            <tr class="tableHeading">
                <td>
                    <spring:message code="activity.comments"/>
                </td>
            </tr>
            <%
                int c = 1;
            %>
            <c:forEach items="${activity.comments}" var="comment">
                <tr class="tableHeading" style="font-weight: normal;">
                    <td class="tableRow<%=(c%2)%>">
                        <span style="font-weight: bold;"><pw:resolveuser user="${comment.user}"/></span> (<fmt:formatDate value="${comment.date}" type="both"/>)
                    </td>
                </tr>
                <tr>
                    <td class="tableRow<%=(c%2)%>">
                        <spring:escapeBody><pw:text2html text="${comment.text}"/></spring:escapeBody>
                    </td>
                </tr>
                <%
                    c++;
                %>
            </c:forEach>

        </table>
        <form name="deleteForm" action="deletedocument" method="POST">
            <input name="documentId" type="hidden" value="">
        </form>
</kantega:section>

<kantega:section id="workflowactions">
        <tr class="tableHeading">
            <td colspan="2"><spring:message code="activity.workflowactions"/></td>
        </tr>

        <c:forEach items="${actions}" var="action" varStatus="status">
            <tr class="tableRow<c:out value="${status.count % 2}"/>">
                <td colspan="2"><a href="activityworkflow?action=<c:out value="${action.id}"/>&workflowId=<c:out value="${activity.workflowId}"/>&activityId=<c:out value="${activity.id}"/>"><c:out value="${action.name}"/></a></td>
            </tr>
        </c:forEach>

</kantega:section>

<kantega:section id="relatert innhold">
    <div class="activitylistsearch">
        <table cellpadding="0" cellspacing="0" width="100%">
            <kantega:getsection id="summary"/>
            <kantega:getsection id="economy"/>
            <kantega:getsection id="workflowactions"/>
        </table>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>