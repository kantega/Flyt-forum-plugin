<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project"%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>


<kantega:section id="tittel">
    <c:choose>
        <c:when test="${project.id == 0}"><spring:message code="projectlist.addproject"/></c:when>
        <c:otherwise><c:out value="${project.name}"/></c:otherwise>
    </c:choose>
</kantega:section>

<kantega:section id="innhold">
<div class="contentmain">
            <form method="POST">
                <spring:bind path="project.id">
                    <input type="hidden" name="projectId" value="<c:out value="${status.value}"/>">
                </spring:bind>
                <div class="heading">
                <c:choose>
                    <c:when test="${project.id == 0}"><spring:message code="projectlist.addproject"/></c:when>
                    <c:otherwise><c:out value="${project.name}"/></c:otherwise>
                </c:choose>
                </div>

                <table border="0">
                    <tr>
                        <td><strong><spring:message code="project.name"/>:</strong></td>
                        <spring:bind path="project.name">
                            <td>
                                <input name="name" value="<c:out value="${status.value}"/>">
                            </td>
                            <td>
                                <spring:message code="project.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>

                    </tr>
                    <tr>
                        <td><strong><spring:message code="project.code"/>:</strong></td>
                        <spring:bind path="project.code">
                            <td>
                                <input name="code" value="<c:out value="${status.value}"/>">
                            </td>
                            <td>
                                <spring:message code="project.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>
                    </tr>

                    <tr>
                        <td valign="top"><strong><spring:message code="project.goal"/>:</strong></td>
                        <spring:bind path="project.goal">

                            <td>
                                <textarea name="goal"  cols="20" rows="4"><c:out value="${status.value}"/></textarea>
                            </td>
                            <td>
                                <spring:message code="project.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>
                    </tr>
                    <tr>
                        <td valign="top"><strong><spring:message code="project.status"/>:</strong></td>
                        <spring:bind path="project.status">
                            <td>
                                <textarea cols="20" rows="4" name="status"><c:out value="${status.value}"/></textarea>
                            </td>
                            <td>
                                <spring:message code="project.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>

                    </tr>
                    <tr>
                        <td valign="top"><strong><spring:message code="project.from-to"/>:</strong></td>
                        <spring:bind path="project.startDate" ignoreNestedPath="false">
                            <td>
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
                            </td>
                            <td>
                                <spring:message code="project.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>

                    </tr>

                    <tr>
                        <td></td>
                        <spring:bind path="project.endDate">
                            <td>

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
                            </td>
                            <td>
                                <spring:message code="project.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>

                    </tr>
                    <tr>
                        <td><strong><spring:message code="project.leader"/></strong></td>
                        <spring:bind path="project.leader">
                            <td>

                                <select name="leader">
                                    <c:forEach items="${profiles}" var="profile">
                                        <option value="<c:out value="${profile.profile.user}"/>" <c:if test="${status.value eq profile.profile.user}">selected</c:if> ><c:out value="${profile.profile.fullName}"/></option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <spring:message code="project.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>
                    </tr>
                   
                    <tr>
                        <td><strong><spring:message code="project.public"/>:</strong></td>
                        <td>
                            <spring:bind path="project.publicProject">
                                <input type="hidden" name="_<c:out value="${status.expression}"/>">
                                <input type="checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${status.value}">checked</c:if>>
                            </spring:bind>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <input type="submit" value="<spring:message code="project.edit.save"/>">
                        </td>
                    </tr>
                </table>

              </form>
</div>
</kantega:section>

<%@include file="include/design/design.jsf"%>