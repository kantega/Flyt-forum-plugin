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
<!--        <tr  class="tableRow<%=c++ % 2%>">
            <td><spring:message code="activity.type"/>:</td>
            <td class="dottedTd"><c:out value="${activity.type.name}"/></td>
        </tr>-->
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

<kantega:section id="documents">
        <tr class="tableHeading">
            <td colspan="2"><spring:message code="activity.documents"/></td>
        </tr>
    <%
        int c = 0;
    %>
    <c:forEach items="${documents}" var="document" varStatus="status">
        <tr class="tableRow<%=c++ % 2%>">
            <td colspan="2">
                <a href="document?documentId=<c:out value="${document.id}"/>&activityId=<c:out value="${activity.id}"/>">
                     <pw:limittextlength value="${document.title}" length="17"/>
                </a><br>
            </td>
        </tr>
    </c:forEach>
        <tr class="tableRow0">
            <td colspan="2" align="right">
                <kantega:getsection id="add_document"/>
            </td>
        </tr>
</kantega:section>

<kantega:section id="innhold">
    <script>
        function addComment() {
            document.getElementById("addcomment").style.display = 'block';
        }
    </script>
    <div class="heading"><c:out value="${activity.project.name}"/>: <c:out value="${activity.title}"/></div>

    <c:out value="${activity.description}"/><br>

        <pw:haspermission project="${activity.project}" permission="ACTIVITY_ADD_COMMENT">
            <table width="100%">
            <tr>
                    <td align="right">
                        <a class="button" style="vertical-align: middle;" href="javascript:addComment()"><img src="../bitmaps/projectweb/ikon_leggtilkommentar.gif" border="0"  style="vertical-align: middle;"><spring:message code="activity.addcomment"/></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="addcomment" style="display: none">
                            <form action="addactivitycomment" method="POST">
                                <input name="activityId" type="hidden" value="<c:out value="${activity.id}"/>">
                                <textarea name="text" style="width: 100%; height: 100px"></textarea><br>
                                <input type="submit" value="<spring:message code="activity.addcomment"/>">
                            </form>
                        </div>
                    </td>

                </tr>
                </table>
            </pw:haspermission>
        <table width="100%">
            <c:forEach items="${activity.comments}" var="comment">
                <tr class="tableHeading" style="font-weight: normal;">
                    <td>
                        <span style="font-weight: bold;"><pw:resolveuser user="${comment.user}"/></span> (<fmt:formatDate value="${comment.date}" type="both"/>)
                    </td>
                </tr>
                <tr>
                    <td>
                        <spring:escapeBody><pw:text2html text="${comment.text}"/></spring:escapeBody>
                    </td>
                </tr>
            </c:forEach>

        </table>

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
        <table cellpadding="0" cellspacing="0">
            <kantega:getsection id="summary"/>
            <kantega:getsection id="economy"/>
            <kantega:getsection id="workflowactions"/>
            <kantega:getsection id="documents"/>
        </table>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>