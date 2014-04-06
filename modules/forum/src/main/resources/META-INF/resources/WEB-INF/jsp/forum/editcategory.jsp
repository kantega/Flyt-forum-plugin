<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ page contentType="text/html;charset=utf-8" %>


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

                <table border="0" width="100%" cellpadding="0" cellspacing="0">
                    <tr class="forum-labelRow">
                        <td><spring:message code="forumcategory.name"/>:</td>
                    </tr>
                    <tr class="forum-tableRow0">
                        <spring:bind path="category.name">
                            <td>
                                <input type="text" name="name" value="<c:out value="${status.value}"/>" size="50" class="forum-editcategory-name">
                                <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>
                    </tr>
                    <tr class="forum-labelRow">
                        <td valign="top"><spring:message code="forumcategory.description"/>:</td>
                    </tr>

                    <tr class="forum-tableRow0">
                        <spring:bind path="category.description">
                            <td>
                                <textarea name="description" rows="5" class="forum-editcategory-description"><c:out value="${status.value}"/></textarea>
                                <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </td>
                        </spring:bind>
                    </tr>
                    <tr class="forum-tableRow1">
                        <td align="left">
                            <input type="submit" class="submit" value="<spring:message code="forumcategory.edit.save"/>">
                        </td>
                    </tr>
                </table>

              </form>
</div>
</kantega:section>

<%@include file="include/design/design.jsf"%>