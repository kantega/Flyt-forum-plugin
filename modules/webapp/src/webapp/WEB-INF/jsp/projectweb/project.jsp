<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project"%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="title">
    <c:out value="${project.name}"/>
</kantega:section>

<kantega:section id="content">
    <script type="text/javascript">
        function addComment() {
            document.getElementById("addcomment").style.display = 'block';
        }
    </script>
    <div class="contentmain">
        <div class="heading"><c:out value="${project.name}"/></div>
        <table cellpadding="0" cellspacing="0" width="500">
            <c:if test="${mayEdit}">
                <tr>
                    <td></td>
                    <td align="right" colspan="1">
                        <div style="padding-bottom: 5px">
                            <a class="button" href="editproject?projectId=<c:out value="${project.id}"/>" style="vertical-align: middle;"><img src="../bitmaps/projectweb/ikon_rediger.gif" style="vertical-align: middle; border:0;" alt="Rediger"><spring:message code="project.actions.edit"/></a>
                        </div>
                    </td>
                </tr>

          </c:if>
            <%
                int c = 1;
            %>
            <tr class="tableRow<%=c++ % 2%>">
                <td width="150"><strong><spring:message code="project.name"/>:</strong></td>
                <td><c:out value="${project.name}"/></td>
            </tr>
            <tr class="tableRow<%=c++ % 2%>">
                <td><strong><spring:message code="project.code"/>:</strong></td>
                <td><c:out value="${project.code}"/></td>
            </tr>

            <tr class="tableRow<%=c++ % 2%>">
                <td valign="top"><strong><spring:message code="project.goal"/>:</strong></td>
                <td valign="top"><c:out value="${project.goal}"/></td>
            </tr>
            <tr class="tableRow<%=c++ % 2%>">
                <td valign="top"><strong><spring:message code="project.status"/>:</strong></td>
                <td valign="top"><c:out value="${project.status}"/></td>
            </tr>
            <tr class="tableRow<%=c++ % 2%>">
                <td><strong><spring:message code="project.from-to"/>:</strong></td>
                <td><fmt:formatDate value="${project.startDate}"/> - <fmt:formatDate value="${project.endDate}"/></td>
            </tr>
            <tr class="tableRow<%=c++ % 2%>">
                <td><strong><spring:message code="project.leader"/>:</strong></td>
                <td><pw:resolveuser user="${project.leader}"/></td>
            </tr>


            <tr class="tableRow<%=c++ % 2%>">
                <td><strong><spring:message code="project.public"/>:</strong></td>
                <td>
                    <c:choose>
                        <c:when test="${project.publicProject}"><spring:message code="general.yes"/></c:when>
                        <c:otherwise><spring:message code="general.no"/></c:otherwise>
                    </c:choose>
                </td>
            </tr>

        </table>
        <div style="height:50px"></div>
        <div class="heading">
            <spring:message code="comments"/>
        </div>
        <div style="text-align:right">
            <a class="button" style="vertical-align: middle;" href="javascript:addComment()"><img src="../bitmaps/projectweb/ikon_leggtilkommentar.gif" style="vertical-align: middle; border:0;" alt="Legg til"><spring:message code="activity.addcomment"/></a>
        </div>
        <form action="addprojectcomment" method="POST">
             <div id="addcomment" style="display: none">
                <input name="projectId" type="hidden" value="<c:out value="${project.id}"/>">
                 <label><spring:message code="comment.type"/>:</label>
                 <select>
                     <!-- TODO: Typene bør vel over i db -->
                     <option value=""><spring:message code="comment.type.choose"/></option>
                     <option value="Help"><spring:message code="comment.type.help"/></option>
                     <option value="Change"><spring:message code="comment.type.change"/></option>
                     <option value="bug"><spring:message code="comment.type.bug"/></option>
                 </select>
                 <br>
                 <label>Prioritet:</label> <!-- TODO: Hent fra db -->
                 <select>
                     <option value="">Velg prioritet</option>
                     <option value="critical">Kritisk</option>
                     <option value="high">Høy</option>
                     <option value="normal">Normal</option>
                     <option value="low">Lav</option>
                 </select>
                <textarea name="text" cols="59" rows="5"></textarea><br>
                <input type="submit" value="Legg til">   <!-- TODO: Lagre  -->
             </div>
        </form>
        <div>
            Liste med alle kommentarer <!-- TODO: List opp kommentarer -->
        </div>
   </div>
</kantega:section>
<%@include file="include/design/design.jsf"%>