<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="tittel">
    <spring:message code="participants.header" arguments="${project.name}"/>
</kantega:section>


<kantega:section id="innhold">
    <div class="contentmain">
    <script type="text/javascript" >
        function confirmDelete(pId, name) {
            if(confirm('<spring:message code="participants.confirmRemove"/> ' + name +'?')) {
                var form = document.getElementById("removeform");
                form.participantId.value=pId;
                form.submit();
            }
        }
    </script>
    <form id="removeform" action="removeparticipant" method="POST">
            <input type="hidden" name="projectId" value="<c:out value="${project.id}"/>"/>
            <input type="hidden" name="participantId" value="-1"/>
        </form>

    <div class="heading"><spring:message code="participants.header" arguments="${project.name}"/></div>

    <c:if test="${allowAdd}">
        <c:forEach items="${searchresult}" var="user">
            <pw:resolveuser user="${user}"/> (<c:out value="${user}"/>)<br>
        </c:forEach>
        <br>
    </c:if>
    <c:choose>
        <c:when test="${not empty participants}">
               <table border="0" cellspacing="0">
                   <tr>
                       <td colspan="6" align="right">
                           <c:if test="${allowAdd}">
                               <div style="padding-bottom: 5px">
                                   <a style="vertical-align: middle;" class="button" href="addparticipant?projectId=<c:out value="${project.id}"/>"><img src="../bitmaps/projectweb/ikon_deltaker.gif" border="0" style="vertical-align: middle;"><spring:message code="participants.add"/></a>
                               </div>
                           </c:if>
                       </td>
                   </tr>
                <tr class="tableHeading">
                    <td><spring:message code="participants.user"/></td>
                    <td><spring:message code="participants.name"/></td>
                    <td><spring:message code="participants.email"/></td>
                    <td><spring:message code="participants.phone"/></td>
                    <td><spring:message code="participants.roles"/></td>
                </tr>

                <c:forEach items="${participants}" var="participant" varStatus="status">


                    <tr class="tableRow<c:out value="${status.count % 2}"/>">
                        <td valign="top">
                            <c:out value="${participant.profile.user}"/>
                        </td>
                        <td valign="top" class="dottedTd">
                            <c:out value="${participant.profile.fullName}"/>
                        </td>
                        <td valign="top" class="dottedTd">
                            <a href="mailto:<c:out value="${participant.profile.email}"/>"><c:out value="${participant.profile.email}"/></a>&nbsp;
                        </td>
                        <td valign="top" class="dottedTd">
                            <c:out value="${participant.profile.phone}"/>&nbsp;
                        </td>
                        <td valign="top" class="dottedTd">
                            <c:forEach items="${participant.participant.roles}" var="role">
                                <c:out value="${role.name}"/><br>
                            </c:forEach>
                            <c:if test="${allowAdd}">
                                <div style="padding-top: 5px">
                                    <a class="button" style="vertical-align: middle" href="editparticipantroles?participantId=<c:out value="${participant.participant.id}"/>"><img style="vertical-align: middle" src="../bitmaps/projectweb/ikon_rediger.gif" border="0"><spring:message code="participants.editRoles"/></a>

                                </div>
                                <div style="padding-top: 2px">
                                    <a class="button" style="vertical-align: middle"  href="javascript:confirmDelete(<c:out value="${participant.participant.id}"/>, '<c:out value="${participant.profile.fullName}"/>')"><img style="vertical-align: middle" src="../bitmaps/projectweb/slett.gif" border="0"><spring:message code="participants.remove"/></a>

                                </div>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            </c:when>
        <c:otherwise>
            Ingen deltakere<br><br>
            <c:if test="${allowAdd}">
                <a href="addparticipant?projectId=<c:out value="${project.id}"/>"><img src="../bitmaps/projectweb/ikon_deltaker.gif" border="0"><spring:message code="participants.add"/></a>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>
</kantega:section>

<%@ include file="include/design/design.jsf" %>