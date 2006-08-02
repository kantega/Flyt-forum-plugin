<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="title">
    <spring:message code="customerlist"/>
</kantega:section>

<kantega:section id="content">
    <div class="contentmain">
        <script type="text/javascript" >
            function confirmDelete(cId, name) {
                if(confirm('<spring:message code="participants.confirmRemove"/> ' + name +'?')) {
                    var form = document.getElementById("deleteform");
                    form.customerId.value=cId;
                    form.submit();
                }
            }
        </script>
        <form id="deleteform" action="deletecustomer" method="POST">
            <input type="hidden" name="customerId" value="-1"/>
        </form>
        <div class="heading"><spring:message code="customerlist.title"/></div>
        <table border="0" cellspacing="0" width="100%">
            <tr>
                <td colspan="5" align="right">
                       <!-- TODO: Test om brukeren har tilgang til å legge til -->
                       <div style="padding-bottom: 5px">
                           <a style="vertical-align: middle;" class="button" href="addcustomer">
                               <img src="../bitmaps/projectweb/ikon_deltaker.gif" style="vertical-align:middle; border:0;" alt=""><spring:message code="customer.add"/></a>
                       </div>

                </td>
            </tr>
            <!-- TODO: Teste om det finnes kunder og hente dem fra list -->
            <tr class="tableHeading">
                <td><spring:message code="customer.companyname"/></td>
                <td><spring:message code="customer.contact"/></td>
                <td><spring:message code="customer.user.email"/></td>
                <td colspan="2"><spring:message code="customer.user.phone"/></td>
            </tr>
            <tr class="tableRow<c:out value="${status.count % 2}"/>">
                <td><a href="customer?customerId=1">Firmanavnet</a></td>
                <td>Kontaktpersonen</td>
                <td><a href="mailto:kundenspost">E-post fil firmaet</a></td>
                <td>Telefon til firmaet</td>
                <td style="text-align:right;"><a class="button" style="vertical-align:middle;" href="javascript:confirmDelete(1,'navn')">
                    <img src="../bitmaps/projectweb/slett.gif" style="vertical-align:middle; border:0;" alt=""/><spring:message code="customer.delete"/></a></td>
            </tr>
        </table>
    </div>
</kantega:section>

<%@ include file="include/design/design.jsf" %>