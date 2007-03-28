<%@ page import="no.kantega.projectweb.control.activity.ActivityDto"%>
<%@ page import="no.kantega.projectweb.model.Activity"%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="tittel">
    <spring:message code="activitylist.header" arguments="${project.name}"/>
</kantega:section>

<kantega:section id="innhold">
<script type="text/javascript">
    function doSort(order) {
        document.searchform.order.value=order;
        document.searchform.submit();
    }

    function selectView(view) {
        document.searchform.view.value = view;
        document.searchform.submit();
    }
</script>



<div class="activitylistmain">
<div class="heading"><spring:message code="activitylist.header" arguments="${project.name}"/></div>

<c:choose>
<c:when test="${not empty activities}">
<c:choose>


<c:when test="${param.view == null or param.view eq '' or param.view eq 'list'}">
    <table border="0" cellspacing="0" width="100%">
        <tr>
            <td colspan="6">
                <table width="100%">
                    <tr>
                        <td>
                            <input onClick="selectView('list')" type="radio" name="view" value="list" <c:if test="${param.view == null or param.view eq 'list'}">checked</c:if>> Liste
                            <input onClick="selectView('time')" type="radio" name="view" value="time" <c:if test="${param.view eq 'time'}">checked</c:if>> Tidslinje
                        </td>
                        <td align="right">
                            <pw:haspermission project="${project}" permission="ADD_ACTIVITY">
                                <div style="padding-bottom: 2px;">
                                    <a class="button" style="vertical-align: middle;" href="addactivity?projectId=<c:out value="${project.id}"/>"><img src="../bitmaps/projectweb/ikon_rediger.gif" border="0" style="vertical-align: middle;"><spring:message code="activitylist.add"/></a>
                                </div>
                            </pw:haspermission>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr class="tableHeading">
            <td><a href="javascript:doSort('title')"><spring:message code="activity.title"/></a></td>
            <td><a href="javascript:doSort('assignee')"><spring:message code="activity.assignee"/></a></td>
            <td><a href="javascript:doSort('status')"><spring:message code="activity.status"/></a></td>
            <td><a href="javascript:doSort('endDate')"><spring:message code="activity.endDate"/></a></td>
            <td><a href="javascript:doSort('phase')"><spring:message code="activity.phase"/></a></td>
            <td><a href="javascript:doSort('priority')"><spring:message code="activity.priority"/></a></td>
        </tr>
        <c:forEach items="${activities}" var="activity" varStatus="status">
            <tr class="tableRow<c:out value="${status.count % 2}"/>">
                <td>
                    <a href="activity?activityId=<c:out value="${activity.activity.id}"/>"><c:out value="${activity.activity.title}"/></a>
                </td>
                <td>
                    <c:out value="${activity.assigneeProfile.fullName}"/>
                </td>
                <td>
                    <c:out value="${activity.activity.status.name}"/>
                </td>
                <td>
                    <fmt:formatDate value="${activity.activity.endDate}"/>
                </td>
                <td>
                    <c:out value="${activity.activity.projectPhase.name}"/>
                </td>
                <td>
                    <c:out value="${activity.activity.priority.name}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:when>
<c:when test="${param.view eq 'time'}">
    <table border="0" cellspacing="0" width="100%">
        <%
            Activity firstActivity = (Activity) request.getAttribute("firstactivity");
            Activity lastActivity = (Activity) request.getAttribute("lastactivity");
            long time = -1;
            if(firstActivity != null && lastActivity != null) {
                time = lastActivity.getEndDate().getTime() - firstActivity.getStartDate().getTime();
            }

        %>
        <tr>
            <td colspan="2">
                <table width="100%">
                    <tr>
                        <td>
                            <input onClick="selectView('list')" type="radio" name="view" value="list" <c:if test="${param.view == null or param.view eq 'list'}">checked</c:if>> Liste
                            <input onClick="selectView('time')" type="radio" name="view" value="time" <c:if test="${param.view eq 'time'}">checked</c:if>> Tidslinje
                        </td>
                        <td align="right">
                            <pw:haspermission project="${project}" permission="ADD_ACTIVITY">
                                <div style="padding-bottom: 2px;">
                                    <a class="button" style="vertical-align: middle;" href="addactivity?projectId=<c:out value="${project.id}"/>"><img src="../bitmaps/projectweb/ikon_rediger.gif" border="0" style="vertical-align: middle;"><spring:message code="activitylist.add"/></a>
                                </div>
                            </pw:haspermission>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr class="tableHeading">
            <td>Tittel</td>
            <td>
                <table width="100%">
                    <tr class="tableHeading">
                        <td>
                            <c:out value="${firstactivity.startDate}"/>
                        </td>
                        <td align="right">
                            <c:out value="${lastactivity.endDate}"/>
                        </td>
                    </tr>
                </table>
            </td>
            <td>
                Gj. t.
            </td>
        </tr>
        <c:forEach items="${activities}" var="activity" varStatus="status">
            <tr class="tableRow<c:out value="${status.count % 2}"/>">
                <%
                    ActivityDto activity = (ActivityDto)pageContext.getAttribute("activity");

                    double start = 0;
                    double end = 1;
                    String color= "8fd3a6";
                    String border="none";
                    if(firstActivity != null && lastActivity != null ) {
                        if(activity.getActivity().getStartDate() != null ) {
                            long startTime = activity.getActivity().getStartDate().getTime() - firstActivity.getStartDate().getTime();
                            start = (double)((double)startTime/(double)time);
                        } else {
                            color = "lightgray";
                            border = "1px dotted gray";
                        }
                        if(activity.getActivity().getEndDate() != null) {
                            long endTime = activity.getActivity().getEndDate().getTime() - firstActivity.getStartDate().getTime();
                            end = (double)((double)endTime/(double)time);
                        } else {
                            color = "lightgray";
                            border = "1px dotted gray";
                        }
                    }
                    double widtd = end-start;
                %>

                <td>
                    <a href="activity?activityId=<c:out value="${activity.activity.id}"/>"><c:out value="${activity.activity.title}"/></a>
                </td>
                <td class="dottedTd">

                    <div style="width: 100%; height: 15px" title="<c:if test="${activity.activity.startDate != null}">Fra: <fmt:formatDate value="${activity.activity.startDate}"/></c:if> <c:if test="${activity.activity.endDate != null}">Til: <fmt:formatDate value="${activity.activity.endDate}"/></c:if>">
                        <div style="position: relative; left: <%=(int)(start*500)%>px; width:<%=(int)(widtd*500)%>px; height:80%; background-color: <%=color%>; margin:2px; border: <%=border%>;" ></div>
                    </div>
                </td>

                <td>
                    <c:out value="${activity.activity.estimatedLeftHours}"/>
                </td>
            </tr>
        </c:forEach>
    </table>

</c:when>
</c:choose>
</c:when>
<c:otherwise>
    <div><spring:message code="activitylist.nonefound"/></div>
    <pw:haspermission project="${project}" permission="ADD_ACTIVITY">

        <a class="button" style="vertical-align: middle;" href="addactivity?projectId=<c:out value="${project.id}"/>"><img src="../bitmaps/projectweb/ikon_rediger.gif" border="0" style="vertical-align: middle;"><spring:message code="activitylist.add"/></a>
    </pw:haspermission>
</c:otherwise>
</c:choose>
</div>

</kantega:section>

<kantega:section id="relatert innhold">
<form name="searchform" action="activitylist" method="POST">
<input type="hidden" name="order">
<input type="hidden" name="view" value="<c:out value="${param.view}"/>">
<input type="hidden" name="projectId" value="<c:out value="${project.id}"/>">
<div class="activitylistsearch">
<div class="heading2"><spring:message code="activitylist.search"/></div>

<div class="activitysearchsection">
    <spring:message code="activitylist.text"/>:
    <br>
    <table>
        <tr>
            <td><input name="text" value="<c:out value="${text[0]}"/>" class="activitylistsearchinput"></td>
            <td><a href="Javascript:searchform.submit()"><img src="../bitmaps/projectweb/sok.gif" border="0"></a></td>

        </tr>
    </table>
</div>
<div class="activitysearchsection">
    <spring:message code="activity.phase"/>:<br>
        <c:forEach items="${allphases}" var="phase">
            <c:choose>
                <c:when test="${phases == null}">
                    <c:set var="selected" value="true"/>
                </c:when>
                <c:otherwise>
                    <c:set var="selected" value="false"/>
                </c:otherwise>
            </c:choose>
            <c:forEach items="${phases}" var="st">
                <c:if test="${phase.id eq st}">
                    <c:set var="selected" value="true"/>
                </c:if>
            </c:forEach>
            <input type="checkbox" name="phases" value="<c:out value="${phase.id}"/>" <c:if test="${selected}">checked="checked"</c:if>><c:out value="${phase.name}"/><br>
        </c:forEach>
</div>

    <div class="activitysearchsection">

    <spring:message code="activity.status"/>:<br>
        <c:forEach items="${allstatuses}" var="status">
            <c:choose>
                <c:when test="${statuses == null}">
                    <c:set var="selected" value="true"/>
                </c:when>
                <c:otherwise>
                    <c:set var="selected" value="false"/>
                </c:otherwise>
            </c:choose>
            <c:forEach items="${statuses}" var="st">
                <c:if test="${status.id eq st}">
                    <c:set var="selected" value="true"/>
                </c:if>
            </c:forEach>
            <input type="checkbox" name="statuses" value="<c:out value="${status.id}"/>" <c:if test="${selected}">checked="checked"</c:if>><c:out value="${status.name}"/><br>
        </c:forEach>
</div>

<div class="activitysearchsection">

    <spring:message code="activity.priority"/>:<br>
        <c:forEach items="${allpriorities}" var="priority">
            <c:choose>
                <c:when test="${priorities == null}">
                    <c:set var="selected" value="true"/>
                </c:when>
                <c:otherwise>
                    <c:set var="selected" value="false"/>
                </c:otherwise>
            </c:choose>
            <c:forEach items="${priorities}" var="pr">
                <c:if test="${priority.id eq pr}">
                    <c:set var="selected" value="true"/>
                </c:if>
            </c:forEach>
            <input type="checkbox" name="priorities" value="<c:out value="${priority.id}"/>" <c:if test="${selected}">checked="checked"</c:if>><c:out value="${priority.name}"/><br>
        </c:forEach>
</div>
<div class="activitysearchsection">
    <spring:message code="activity.assignee"/>:<br>
    <select name="assignees" class="activitylistselect">
        <option value="-1" <c:if test="${assignees[0] eq '-1'}">selected</c:if>><spring:message code="general.any"/></option>
        <c:forEach items="${allparticipants}" var="participant">
            <option value="<c:out value="${participant.profile.user}"/>"  <c:if test="${assignees[0] eq participant.profile.user}">selected</c:if>><c:out value="${participant.profile.fullName}"/></option>
        </c:forEach>
    </select>
</div>
<div class="activitysearchsection">
    <spring:message code="activity.reporter"/>:<br>
    <select  name="reporters" class="activitylistselect">
        <option value="-1" <c:if test="${reporters[0] eq '-1'}">selected</c:if>><spring:message code="general.any"/></option>
        <c:forEach items="${allparticipants}" var="participant">
            <option value="<c:out value="${participant.profile.user}"/>" <c:if test="${reporters[0] eq participant.profile.user}">selected</c:if>><c:out value="${participant.profile.fullName}"/></option>
        </c:forEach>
    </select>
</div>
<!--<div align="right">
    <a href="Javascript:searchform.submit()"><img src="../bitmaps/projectweb/sok.gif" border="0"></a>
</div>-->

</div>

</form>
</kantega:section>
<%@include file="include/design/design.jsf"%>