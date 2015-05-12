<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<kantega:section id="tittel">
    Kategorier
</kantega:section>

<kantega:section id="innhold">

    <form action="deletecategory" name="deletecategory" method="POST">
        <input type="hidden" name="categoryId">
    </form>

        <script type="text/javascript" >
            function deleteCategory(categoryId) {
                if(confirm("<spring:message code="forumcategory.confirmdelete"/>")) {
                    document.deletecategory.categoryId.value = categoryId;
                    document.deletecategory.submit();
                }
            }
        </script>

    <div class="forum-heading">
        <a href=""><spring:message code="forum.title"/></a> >
        <a href="viewcategory?categoryId=<c:out value="${forumcategory.id}"/>"><c:out value="${forumcategory.name}"/></a>
    </div>

    <c:set var="hasforums" value="false"/>
    <kantega:section id="forums">
        <table border="0" cellspacing="0" cellpadding="0" width="100%" class="forum-table">
            <tr class="forum-labelRow">
                <td>
                    <spring:message code="forum.name"/>
                </td>
                    <td>
                    <spring:message code="forum.threads"/>
                </td>
            </tr>

                <c:forEach items="${forumcategory.forums}" var="forum" varStatus="status">
                    <c:set var="hasforums" value="true"/>
                    <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                        <td>
                            <a href="viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a>
                        </td>
                        <td>
                            <c:out value="${forum.numThreads}"/>
                        </td>
                    </tr>
                </c:forEach>

        </table>
    </kantega:section>

    <c:choose>

        <c:when test="${hasforums != 'true'}">
            <spring:message code="forumcategory.empty"/>
        </c:when>

        <c:otherwise>
            <kantega:getsection id="forums"/>
        </c:otherwise>

    </c:choose>
    <forum:haspermisson permission="EDIT_CATEGORY" object="${forumcategory}">

        <div style="padding-top: 10px">
            <a href="editforum?categoryId=<c:out value="${forumcategory.id}"/>"><spring:message code="forum.addforum"/></a>
            |<a href="editcategory?categoryId=<c:out value="${forumcategory.id}"/>"><spring:message code="forumcategory.edit"/></a>
            | <a href="javascript:deleteCategory(<c:out value="${forumcategory.id}"/>)"><spring:message code="forumcategory.delete"/></a>
        </div>
    </forum:haspermisson>
</kantega:section>

<%@include file="include/design/design.jsf"%>
