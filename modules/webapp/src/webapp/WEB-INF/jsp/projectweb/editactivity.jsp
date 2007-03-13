<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project"%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>

<kantega:section id="title">
    <c:choose>
        <c:when test="${activity.id == 0}"><spring:message code="activity.new"/></c:when>
        <c:otherwise><spring:message code="activity.edit"/></c:otherwise>
    </c:choose>

</kantega:section>

<kantega:section id="content">

    <div class="contentmain">
    <div class="heading">
        <c:choose>
            <c:when test="${activity.id == 0}"><spring:message code="activity.new"/></c:when>
            <c:otherwise><spring:message code="activity.edit"/></c:otherwise>
        </c:choose>
        </div>

    <form method="post">

        <table>
            <tr>
                <td><spring:message code="activity.title"/>:</td>
                <td>
                    <spring:bind path="activity.title">
                        <input name="title" value="<c:out value="${status.value}"/>">
                        <c:out value="${status.errorMessage}"/>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td><spring:message code="activity.phase"/>:</td>
                <td>
                    <spring:bind path="activity.projectPhase">
                        <select name="projectPhase">
                            <option <c:if test="${activity.projectPhase == null}">selected</c:if> value="-1">Velg fase</option>
                            <c:forEach items="${phases}" var="phase">
                                <option <c:if test="${phase.id == activity.projectPhase.id}">selected</c:if> value="<c:out value="${phase.id}"/>"><c:out value="${phase.name}"/></option>
                            </c:forEach>
                        </select>
                        <c:out value="${status.errorMessage}"/>
                        <br>
                    </spring:bind>
                </td>
            </tr>
            <%--<tr>
                <td><spring:message code="activity.type"/>:</td>
                <td>
                    <spring:bind path="activity.type">
                        <select name="type">
                            <c:forEach items="${types}" var="type">
                                <option <c:if test="${type.id == activity.type.id}">selected</c:if> value="<c:out value="${type.id}"/>"><c:out value="${type.name}"/></option>
                            </c:forEach>
                        </select>
                        <c:out value="${status.errorMessage}"/>
                        <br>
                    </spring:bind>
                </td>
            </tr>
            --%>
            <tr>
                <td><spring:message code="activity.priority"/>:</td>
                <td>
                    <spring:bind path="activity.priority">
                        <select name="priority">
                            <c:forEach items="${priorities}" var="priority">
                                <option <c:if test="${priority.id == activity.priority.id}">selected</c:if> value="<c:out value="${priority.id}"/>"><c:out value="${priority.name}"/></option>
                            </c:forEach>
                        </select>
                        <c:out value="${status.errorMessage}"/>
                    </spring:bind>

                </td>
            </tr>
            <tr>
                <td><spring:message code="duration.startDate"/>:</td>
                <td>
                      <spring:bind path="activity.startDate">
                          <input id="startDate" name="startDate" value="<c:out value="${status.value}"/>" size="8">
                          <a href="#" id="startDateButton" class="button"><img src="../bitmaps/projectweb/mini_velg.gif" border="0"> Velg</a>
                          <script type="text/javascript">
                              Calendar.setup(
                              {
                                  inputField  : "startDate",         // ID of the input field
                                  ifFormat    : "%d.%m.%y",    // the date format
                                  button      : "startDateButton"       // ID of the button
                              }
                                      );
                          </script>
                          <c:out value="${status.errorMessage}"/>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td><spring:message code="duration.endDate"/>:</td>
                <td>
                    <spring:bind path="activity.endDate">
                        <input id="endDate" name="endDate" value="<c:out value="${status.value}"/>" size="8">
                        <a href="#" id="endDateButton" class="button"><img src="../bitmaps/projectweb/mini_velg.gif" border="0"> Velg</a>
                          <script type="text/javascript">
                              Calendar.setup(
                              {
                                  inputField  : "endDate",         // ID of the input field
                                  ifFormat    : "%d.%m.%y",    // the date format
                                  button      : "endDateButton"       // ID of the button
                              }
                                      );
                          </script>
                        <spring:message code="activity.validation.${status.errorCode}" text="${status.errorMessage}"/>
                  </spring:bind>
                </td>
            </tr>
            <tr>
                <td><spring:message code="activity.assignee"/>:</td>
                <td>
                    <spring:bind path="activity.assignee">
                        <select name="assignee">
                            <c:forEach items="${profiles}" var="profile">
                                <option value="<c:out value="${profile.profile.user}"/>" <c:if test="${status.value eq profile.profile.user}">selected</c:if> ><c:out value="${profile.profile.fullName}"/></option>
                            </c:forEach>
                        </select>
                        <c:out value="${status.errorMessage}"/>
                  </spring:bind>
                </td>
            </tr>
            <tr>
                <td><spring:message code="activity.reporter"/>:</td>
                <td>
                    <spring:bind path="activity.reporter">
                        <select name="reporter">
                            <c:forEach items="${profiles}" var="profile">
                                <option value="<c:out value="${profile.profile.user}"/>" <c:if test="${status.value eq profile.profile.user}">selected</c:if> ><c:out value="${profile.profile.fullName}"/></option>
                            </c:forEach>
                        </select>
                        <c:out value="${status.errorMessage}"/>
                  </spring:bind>
                </td>
            </tr>
            <tr>
                <td><spring:message code="activity.description"/>:</td>
                <td>
                    <spring:bind path="activity.description">
                        <textarea name="description" cols="50" rows="10"><c:out value="${status.value}"/></textarea><br>
                        <c:out value="${status.errorMessage}"/>
                  </spring:bind>
                </td>
            </tr>

        </table>
        <input type="submit" value="<spring:message code="general.save"/>">
    </form>
    </div>
</kantega:section>
<%@include file="include/design/design.jsf"%>