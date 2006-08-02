<%@ page import="java.util.List,
                 no.kantega.projectweb.model.Project" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/projectweb" prefix="pw" %>

<kantega:section id="title">
   <spring:message code="customer.new"/>
</kantega:section>

<kantega:section id="content">
    <div class="contentmain">
        <form action="" method="post">
            <!-- TODO: Hent ut fra db -->
            <div class="heading"><spring:message code="customer.user.add"/></div>
            <label class="label"><spring:message code="customer.user.username"/></label>
            <input size="25" name="username" value="Brukernavn"/>
            <br />
            <label class="label"><spring:message code="customer.user.name"/>:</label>
            <input size="25" name="name" value="Hent ut navn"/>
            <br />
            <label class="label"><spring:message code="customer.user.email"/>:</label>
            <input size="25" name="email" value="E-post" />
            <br />
            <label class="label"><spring:message code="customer.user.phone"/>:</label>
            <input size="25" name="phone" value="telefon" />
            <br />
            <label class="label"><spring:message code="customer.user.role"/>:</label>
            <input size="25" name="role" value="rolle" />
            <br />
            <label class="label"><spring:message code="customer.user.password"/>:</label>
            <input type="password" size="25" name="password1"/>
            <br/>
            <label class="label"><spring:message code="customer.user.retypepassword"/>:</label>
            <input type="password" size="25" name="password2"/>
            <br/> 
            <input type="submit" value="<spring:message code="general.save"/>"/>
        </form>

    </div>
</kantega:section>

<%@ include file="include/design/design.jsf" %>
