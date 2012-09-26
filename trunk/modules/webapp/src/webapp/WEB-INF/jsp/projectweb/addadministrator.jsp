<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project"%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<kantega:section id="tittel">
    <spring:message code="administrators.add"/>
</kantega:section>


<kantega:section id="innhold">
    <div class="contentmain">

        <div class="heading"><spring:message code="administrators.add" /></div>

        <form action="addadministrator" method="POST">
            <input name="query" value="<c:out value="${query}"/>"> <input type="submit" value="<spring:message code="general.search" />">
          </form>
        <c:choose>
        <c:when test="${not empty results}">
            <form name="adduserform" action="addadministrator" method="POST">
                <input type="hidden" name="user">
             </form>
            <script>
                function addUser(user) {
                    document.adduserform.user.value = user;
                    document.adduserform.submit();
                }
            </script>
                <table>
                    <tr class="tableHeading">
                        <td colspan="3">
                            <spring:message code="general.result"/>
                        </td>
                    </tr>
                    <c:forEach items="${results}" var="result" varStatus="status">
                        <tr class="tableRow<c:out value="${status.count % 2}"/>">
                            <td>
                                <c:out value="${result.user}"/>
                            </td>
                            <td class="dottedTd">
                                <c:out value="${result.fullName}"/>
                                <c:if test="${result.source != null}">
                                    (<c:out value="${result.source}"/>)
                                </c:if>
                            </td>

                            <td class="dottedTd">
                                <c:if test="${currentAdmins[result.user] == null}">
                                    <a href="javascript:addUser('<c:out value="${result.user}"/>')"><spring:message code="general.add"/></a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>

        </c:when>
        <c:when test="${param.query != null}">
            <spring:message code="administrators.add.nomatch"/>
        </c:when>
        </c:choose>
    </div>

</kantega:section>

<%@include file="include/design/design.jsf"%>