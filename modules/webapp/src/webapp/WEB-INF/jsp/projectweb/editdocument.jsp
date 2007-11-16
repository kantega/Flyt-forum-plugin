<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>

<kantega:section id="tittel">
    <c:choose>
        <c:when test="${document.id == 0}"><spring:message code="document.new"/></c:when>
        <c:otherwise><spring:message code="document.edit"/></c:otherwise>
    </c:choose>

</kantega:section>

<kantega:section id="innhold">

    <div class="contentmain">
        <div class="heading">
            <c:choose>
                <c:when test="${document.id == 0}"><spring:message code="document.new"/></c:when>
                <c:otherwise><spring:message code="document.edit"/></c:otherwise>
            </c:choose>
        </div>

        <form name="editform" method="post" enctype="multipart/form-data">
            <c:if test="${activityId != null}">
                <input type="hidden" name="activityId" value="<c:out value="${activityId}"/>">
            </c:if>
            <input type="hidden" name="attachedActivityId" value="<c:out value="${attachedActivityId}"/>">
            <input type="hidden" name="detatchActivity" value="">

            <c:if test="${document.id == 0}">
                <input type="hidden" name="newdocument" value="true"/>
            </c:if>
            <table>
                <tr>
                    <td><spring:message code="document.title"/>:</td>
                    <td>
                        <spring:bind path="document.title">
                            <input name="title" value="<c:out value="${status.value}"/>">
                           <spring:message code="document.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><spring:message code="document.category"/>:</td>
                    <td>
                        <spring:bind path="document.category">
                            <select name="category">
                                <c:forEach items="${categories}" var="category">
                                    <option <c:if test="${category.id == document.category.id}">selected</c:if> value="<c:out value="${category.id}"/>"><c:out value="${category.name}"/></option>
                                </c:forEach>
                            </select>
                            <c:out value="${status.errorMessage}"/>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><spring:message code="document.file"/>:</td>
                    <td>
                        <input type="file" name="contentFile">
                        <%
                            String errorMsg = request.getParameter("errormessage");
                            if( errorMsg != null){
                                out.println(errorMsg);
                            }
                        %>
                    </td>
                </tr>
                <tr>
                    <td valign="top"><spring:message code="document.description"/>:</td>
                    <td>
                        <spring:bind path="document.description">
                            <textarea name="description" cols="20" rows="10"><c:out value="${document.description}"/></textarea><br>
                            <c:out value="${status.errorMessage}"/>
                        </spring:bind>

                    </td>
                </tr>
                <c:if test="${activityId == null}">
                    <tr>
                        <td valign="top"><spring:message code="document.activities"/>:</td>
                        <td>

                            <c:forEach items="${document.activities}" var="activity">
                                <c:out value="${activity.title}"/><br/>
                            </c:forEach>
                            <c:if test="${not empty(activities)}">
                                <br/><br/>
                                <select name="addActivityId">
                                    <option value=""><spring:message code="document.attachactivity"/></option>
                                    <c:forEach items="${activities}" var="activity">
                                        <option value="<c:out value="${activity.id}"/>"><c:out value="${activity.title}"/></option>
                                    </c:forEach>
                                </select>
                            </c:if>
                        </td>
                    </tr>
                </c:if>
            </table>
            <input type="submit" value="<spring:message code="general.save"/>">
        </form>
    </div>
</kantega:section>
<%@ include file="include/design/design.jsf" %>