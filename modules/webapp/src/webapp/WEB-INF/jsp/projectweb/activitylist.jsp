<%@ page import="no.kantega.projectweb.control.activity.ActivityDto"%>
<%@ page import="no.kantega.projectweb.model.Activity"%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="title">
    <spring:message code="activitylist.header" arguments="${project.name}"/>
</kantega:section>

<kantega:section id="content">
    <script type="text/javascript">
        function doSort(order) {
            document.searchform.order.value=order;
            document.searchform.submit();
        }
    </script>
    <form name="searchform" action="activitylist" method="POST">

    <table border="0" cellpadding="0" cellspacing="0" height="100%">
        <tr>
            <td valign="top">
                <div class="activitylistsearch">
                <div class="heading2"><spring:message code="activitylist.search"/></div>
                    <input type="hidden" name="order">


                    <input type="hidden" name="projectId" value="<c:out value="${project.id}"/>">

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
                    <select multiple name="phases" size="5" class="activitylistselect">
                        <option value="-1" <c:if test="${phases[0] eq '-1'}">selected</c:if> ><spring:message code="general.any"/></option>
                        <c:forEach items="${allphases}" var="phase">
                            <c:set var="found" value="false"/>
                            <c:forEach items="${phases}" var="st">
                                <c:if test="${phase.id eq st}">
                                    <c:set var="found" value="true"/>
                                </c:if>
                            </c:forEach>
                            <option <c:if test="${found}">selected</c:if> value="<c:out value="${phase.id}"/>"><c:out value="${phase.name}"/></option>
                        </c:forEach>

                    </select>
                        </div>
                    <!--
                    <div class="activitysearchsection">
                    <spring:message code="activity.type"/>:<br>
                    <select multiple name="types" size="5" class="activitylistselect">
                        <option value="-1" <c:if test="${types[0] eq '-1'}">selected</c:if> ><spring:message code="general.any"/></option>
                        <c:forEach items="${alltypes}" var="type">
                            <c:set var="found" value="false"/>
                            <c:forEach items="${types}" var="st">
                                <c:if test="${type.id eq st}">
                                    <c:set var="found" value="true"/>
                                </c:if>
                            </c:forEach>
                            <option <c:if test="${found}">selected</c:if> value="<c:out value="${type.id}"/>"><c:out value="${type.name}"/></option>
                        </c:forEach>

                    </select>
                        </div>-->
                    <div class="activitysearchsection">

            <spring:message code="activity.status"/>:<br>
                    <select multiple name="statuses" size="5" class="activitylistselect">
                        <option value="-1" <c:if test="${statuses[0] eq '-1'}">selected</c:if> ><spring:message code="general.any"/></option>
                        <c:forEach items="${allstatuses}" var="status">
                            <c:set var="found" value="false"/>
                            <c:forEach items="${statuses}" var="st">
                                <c:if test="${status.id eq st}">
                                    <c:set var="found" value="true"/>
                                </c:if>
                            </c:forEach>
                            <option <c:if test="${found}">selected</c:if> value="<c:out value="${status.id}"/>"><spring:message code="activitystatus.${status.code}"/></option>
                        </c:forEach>

                    </select>
                        </div>

                    <div class="activitysearchsection">

                    <spring:message code="activity.priority"/>:<br>
                    <select multiple name="priorities" size="5" class="activitylistselect">
                        <option value="-1" <c:if test="${priorities[0] eq '-1'}">selected</c:if> ><spring:message code="general.any"/></option>

                        <c:forEach items="${allpriorities}" var="priority">
                            <c:set var="found" value="false"/>
                            <c:forEach items="${priorities}" var="pr">
                                <c:if test="${priority.id eq pr}">
                                    <c:set var="found" value="true"/>
                                </c:if>
                            </c:forEach>
                            <option <c:if test="${found}">selected</c:if> value="<c:out value="${priority.id}"/>"><c:out value="${priority.name}"/></option>
                        </c:forEach>
                    </select>
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
                    </td>
    <td valign="top">
        <div class="activitylistmain">
    <div class="heading"><spring:message code="activitylist.header" arguments="${project.name}"/></div>

    <c:choose>
    <c:when test="${not empty activities}">
        <c:choose>


        <c:when test="${param.view == null or param.view eq 'list'}">
    <table border="0" cellspacing="0">
        <tr>
            <td colspan="7">
                <table width="100%">
                    <tr>
                        <td>
                            <input onClick="searchform.submit()" type="radio" name="view" value="list" <c:if test="${param.view == null or param.view eq 'list'}">checked</c:if>> Liste
                            <input onClick="searchform.submit()" type="radio" name="view" value="time" <c:if test="${param.view eq 'time'}">checked</c:if>> Tidslinje
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
            <td><a href="javascript:doSort('usedHours')"><spring:message code="economy.used"/></a></td>
            <td><a href="javascript:doSort('leftHours')"><spring:message code="economy.left"/></a></td>
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
              <td align="right">
                  <c:out value="${activity.activity.usedHours}"/>
              </td>
              <td align="right">
                  <c:out value="${activity.activity.estimatedLeftHours}"/>
              </td>
          </tr>
      </c:forEach>
    </table>
    </c:when>
            <c:when test="${param.view eq 'time'}">
                <table border="0" cellspacing="0" >
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
                                    <input onClick="searchform.submit()" type="radio" name="view" value="list" <c:if test="${param.view == null or param.view eq 'list'}">checked</c:if>> Liste
                                    <input onClick="searchform.submit()" type="radio" name="view" value="time" <c:if test="${param.view eq 'time'}">checked</c:if>> Tidslinje
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
                            <td widtd="500" class="dottedTd">

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

    </td>
        </tr>
  </table>
</form>
</kantega:section>
<%@include file="include/design/design.jsf"%>