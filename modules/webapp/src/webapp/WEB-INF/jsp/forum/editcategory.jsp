<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>


<kantega:section id="tittel">
    <spring:message code="forumcategory.edit.title" arguments="${project.name}"/>
</kantega:section>

<kantega:section id="innhold">
<div class="contentmain">
<form method="POST">
                <spring:bind path="category.id">
                    <input type="hidden" name="categoryId" value="<c:out value="${status.value}"/>">
                </spring:bind>
                <div class="heading">
                <c:choose>
                    <c:when test="${forumcategory.id == 0}"><spring:message code="forumcategory.addcategory"/></c:when>
                    <c:otherwise><c:out value="${forumcategory.name}"/></c:otherwise>
                </c:choose>
                </div>

                <table border="0">
                    <tr>
                        <td valign="top"><strong><spring:message code="forumcategory.name"/>:</strong></td>
                        <spring:bind path="category.name">
                            <td>
                                <input type="text" name="name" value="<c:out value="${status.value}"/>">
                            </td>
                            <td>
                                <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>
                    </tr>
                    <tr>
                        <td valign="top"><strong><spring:message code="forumcategory.description"/>:</strong></td>
                        <spring:bind path="category.description">
                            <td>
                                <textarea name="description" rows="5" cols="80"><c:out value="${status.value}"/></textarea>
                            </td>
                            <td>
                                <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <input type="submit" value="<spring:message code="forumcategory.edit.save"/>">
                        </td>
                    </tr>
                </table>

              </form>
</div>
</kantega:section>

<%@include file="include/design/design.jsf"%>