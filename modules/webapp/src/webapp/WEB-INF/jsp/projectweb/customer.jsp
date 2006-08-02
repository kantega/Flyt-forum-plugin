<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

 <!-- TODO: Hent firmanavn fra db -->
<kantega:section id="title">
    <spring:message code="customer.title"/>: Firmanavn
</kantega:section>

<kantega:section id="content">
    <div class="contentmain">
       <form action="addcustomer">
           <!-- TODO: Hent ut fra db -->
           <input type="hidden" name="companyId" value="1" />
            <div style="float:left; width:33%;">
               <div class="heading">Firmanavn</div>
               Firmaadresse <br />
               Postnr <br />
               Poststed <br />
               <br />
               <input style="float:right;" type="submit" value="<spring:message code="general.edit"/>" />
           </div>
           <div style="float:left;width:33%;">
               <div class="heading">SLA</div>
               Kritisk: x timer <br/>
               Høy: y timer   <br/>
               Middels: z timer/dager  <br/>
               Lav: Z timer/dager   <br/>
           </div>
           <div style="float:left;width:34%;">
               <div class="heading">Dokumenter</div>
               <!-- TODO: Hvis det finnes filer -->
               <table cellspacing="0" border="0" width="100%">
                   <tr class="tableHeading">
                       <td><spring:message code="document.title"/></td>
                       <td><spring:message code="document.category"/></td>
                       <td><spring:message code="document.editdate"/></td>
                   </tr>
                   <tr>
                       <td><a href="">Filnavn</a></td>
                       <td>Kategori</td>
                       <td>Dato</td>
                   </tr>
               </table>
               <a class="button" style="vertical-align: middle;"
                   href="editdocument?projectId=<c:out value="${project.id}"/>"><img src="../bitmaps/projectweb/ikon_rediger.gif"
                                                                                    style="vertical-align: middle;border:0;" alt="">
                    <spring:message code="documentlist.add"/></a>
           </div>
       </form>
        <br />
       <div style="height:20px;"></div>
       <div class="heading2"><spring:message code="projects"/></div>
        <!-- TODO: Hvis prosjekter hent ut og fyll i tabell -->
       <table border="0" cellspacing="0" width="100%">
           <tr>
               <td colspan="5" align="right">
                   <div style="padding-bottom: 5px">
                       <a style="vertical-align: middle;" class="button" href="addproject?customerId=1">
                           <img src="../bitmaps/projectweb/ikon_deltaker.gif" style="vertical-align: middle; border:0;" alt="" /><spring:message code="projectlist.addproject"/></a>
                   </div>
                </td>
           </tr>
           <tr class="tableHeading">
               <td><spring:message code="project.name"/></td>
               <td><spring:message code="project.goal"/></td>
               <td><spring:message code="project.status"/></td>
               <td><spring:message code="project.priority"/></td>
               <td><spring:message code="project.leader"/></td>
           </tr>
       </table>
       <div style="height:20px"></div>
       <div class="heading2"><spring:message code="customer.userlist"/></div>
       <table border="0" cellspacing="0" width="100%">
            <tr>
                <td colspan="6" align="right">
                    <div style="padding-bottom:5px;">
                        <a style="vertical-align:middle;" class="button" href="addcustomeruser?customerId=1">
                            <img src="../bitmaps/projectweb/ikon_deltaker.gif" style="vertical-align:middle;border:0;" alt="" /><spring:message code="customer.userlist.add"/></a>
                    </div>
                </td>
            </tr>
           <tr class="tableHeading">
               <td><spring:message code="customer.user.username"/></td>
               <td><spring:message code="customer.user.name"/></td>
               <td><spring:message code="customer.user.email"/></td>
               <td><spring:message code="customer.user.phone"/></td>
               <td><spring:message code="customer.user.role"/></td>
               <td><spring:message code="general.edit"/></td>
           </tr>
           <tr>
               <!-- TODO: hent fra db -->
               <td>Brukernavn</td>
               <td>Navnet</td>
               <td>Eposten</td>
               <td>Telefon</td>
               <td>Rolle</td>
               <td><a href="addcustomeruser?customerId=1&amp;userId=1"><spring:message code="general.edit"/></a></td>
           </tr>
       </table>
    </div>
</kantega:section>

<%@ include file="include/design/design.jsf" %>