<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<!-- TODO: Endre hvis eksisterende kunde -->
<kantega:section id="title">
   <spring:message code="customer.new"/>
</kantega:section>

<kantega:section id="content">
    <div class="contentmain">
        <form action="" method="post">
            <div style="width:50%;float:left">
                <!-- TODO:Endre hvis eksisterende kunde -->
                <div class="heading"><spring:message code="customer.add"/></div>
                <label class="label"><spring:message code="customer.companyname"/>:</label>
                <input size="25" name="companyname" value="Hent ut firmanavn"/>
                <br>
                <label class="label"><spring:message code="customer.companyaddress"/>:</label>
                <input size="25" name="companyaddres" value="Hent ut adresse"/>
                <br>
                <label class="label"><spring:message code="customer.companypostalcode"/>:</label>
                <input size="4" name="postalcode" value="Hent ut"/>
                <br>
                <label class="label"><spring:message code="customer.companyplace"/>:</label>
                <input size="25" name="place" value="Hent ut" />
                <br>
                <input style="float:right;" type="submit" value="<spring:message code="general.save"/>"/>
            </div>
            <div style="width:50%;float:left">
                <!-- TODO: Hent fra spring og db -->
                <div class="heading">Responstider</div>
                <label>Kritisk:</label>
                <input size="2" name="critical" value=""/>
                &nbsp;
                <select>
                    <option value="criticalhours">Timer</option>
                    <option value="criticaldays">Dager</option>
                </select>
                <br/>
            </div>
        </form>

    </div>
</kantega:section>

<%@ include file="include/design/design.jsf" %>
