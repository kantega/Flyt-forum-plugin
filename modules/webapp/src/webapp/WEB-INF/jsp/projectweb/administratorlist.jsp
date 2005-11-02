<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project" %>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="title">
    <spring:message code="administrators.heading" arguments="${project.name}"/>
</kantega:section>


<kantega:section id="content">
    <div class="contentmain">
    <script type="text/javascript" >
        function confirmDelete(pId, name) {
            if(confirm('<spring:message code="administrators.confirmRemove"/> ' + name +'?')) {
                var form = document.getElementById("removeform");
                form.user.value=pId;
                form.submit();
            }
        }
    </script>
    <form id="removeform" action="removeadministrator" method="POST">
            <input type="hidden" name="user" value="-1"/>
        </form>

    <div class="heading"><spring:message code="administrators.heading"/></div>

        <c:forEach items="${searchresult}" var="user">
            <pw:resolveuser user="${user}"/> (<c:out value="${user}"/>)<br>
        </c:forEach>
        <br>
        <c:choose>
        <c:when test="${not empty administrators}">
               <table border="0" cellspacing="0">
                   <tr>
                       <td colspan="6" align="right">
                           <div style="padding-bottom: 5px">
                           <a class="button" style="vertical-align: middle;" href="addadministrator"><img src="../bitmaps/projectweb/ikon_deltaker.gif" border="0" style="vertical-align: middle;"><spring:message code="administrators.add"/></a>
                               </div>
                       </td>
                   </tr>
                <tr class="tableHeading">
                    <td><spring:message code="administrators.user"/></td>
                    <td><spring:message code="administrators.name"/></td>
                    <td></td>
                </tr>

                <c:forEach items="${administrators}" var="administrator" varStatus="status">


                    <tr class="tableRow<c:out value="${status.count % 2}"/>">
                        <td valign="top">
                            <c:out value="${administrator.user}"/>
                        </td>
                        <td valign="top" class="dottedTd">
                            <pw:resolveuser user="${administrator.user}"/>
                        </td>
                        <td valign="top" class="dottedTd">
                            <a class="button" style="vertical-align: middle" href="javascript:confirmDelete('<c:out value="${administrator.user}"/>','<pw:resolveuser user="${administrator.user}"/>')"><img style="vertical-align: middle" src="../bitmaps/projectweb/slett.gif" border="0"><spring:message code="administrators.remove"/></a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            </c:when>
        <c:otherwise>
            Ingen administratorer<br>
            <a href="addadministrator?projectId=<c:out value="${project.id}"/>"><img src="../bitmaps/projectweb/ikon_deltaker.gif" border="0"><spring:message code="administrators.add"/></a>
        </c:otherwise>
    </c:choose>
</div>
</kantega:section>

<%@ include file="include/design/design.jsf" %>