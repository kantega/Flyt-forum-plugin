<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ page contentType="text/html;charset=utf-8" %>

<kantega:section id="innhold">
    <script type="text/javascript" src="../aksess/js/autocomplete.js"></script>
    <div class="contentmain edit-forum">
        <form method="POST" name="myform">
            <spring:bind path="forum.id">
                <input type="hidden" name="id" value="<c:out value="${status.value}"/>">
            </spring:bind>
            <div class="forum-heading">
                <c:choose>
                    <c:when test="${forum.id == 0}"><spring:message code="forum.addforum"/></c:when>
                    <c:otherwise><c:out value="${forum.name}"/></c:otherwise>
                </c:choose>
            </div>
            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                <tr class="forum-labelRow">
                    <td valign="top"><spring:message code="forum.name"/></td>
                </tr>
                <tr class="forum-tableRow0">
                    <spring:bind path="forum.name">
                        <td>
                            <input type="text" name="name" value="<c:out value="${status.value}"/>" size="50" class="forum-editforum-name">
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr class="forum-labelRow">
                    <td valign="top"><spring:message code="forum.description"/></td>
                </tr>
                <tr class="forum-tableRow0">
                    <spring:bind path="forum.description">
                        <td>
                            <textarea name="description" rows="5" class="forum-editforum-description"><c:out value="${status.value}"/></textarea>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr class="forum-labelRow">
                    <td valign="top"><spring:message code="forum.groups"/></td>
                </tr>
                <tr class="forum-tableRow0">
                    <td>
                        <div style="height: 150px; overflow:auto;">
                            <c:forEach items="${groups}" var="group">
                                <input type="checkbox" name="groups" value="<c:out value="${group.id}"/>"
                                <c:forEach items="${forum.groups}" var="selectedGroup">
                                <c:if test="${group.id == selectedGroup}">
                                       checked="checked"
                                </c:if>
                                </c:forEach>
                                        > <c:out value="${group.name}"/><br>
                            </c:forEach>
                        </div>
                        <p>
                            <spring:message code="forum.groups.text"/>
                        </p>
                    </td>
                </tr>
                <tr class="forum-labelRow">
                    <td valign="top"><spring:message code="forum.moderate"/></td>
                </tr>
                <tr class="forum-tableRow0">
                    <td>
                        <p>
                            <spring:message code="forum.moderator.name"/>:<br>
                            <spring:bind path="forum.moderator">
                                <input type="hidden" id="moderator" name="moderator" value="<c:out value="${status.value}"/>">
                                <input type="text" id="moderatortext" name="moderatortext" size="50" maxlength="64" class="forum-editforum-moderator" value="<c:out value="${moderator.fullName}"/>">
                                <script type="text/javascript">
                                    Autocomplete.setup({'inputField' :'moderator', url:'searchusers', 'minChars' :3 });
                                </script>
                                <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                            </spring:bind>
                        </p>
                        <p>
                            <spring:message code="forum.moderator.text"/>
                        </p>

                        <p>
                            <spring:bind path="forum.anonymousPostAllowed">
                                <input type="hidden" name="_<c:out value="${status.expression}"/>">
                                <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"
                                       <c:if test="${status.value}">checked</c:if>/>
                                <spring:message code="forum.anonymous.allow"/>
                            </spring:bind>
                            <br>
                            <spring:bind path="forum.approvalRequired">
                                <input type="hidden" name="_<c:out value="${status.expression}"/>">
                                <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"
                                       <c:if test="${status.value}">checked</c:if>/>
                                <spring:message code="forum.anonymous.moderate"/>
                            </spring:bind>
                        </p>
                    </td>
                </tr>
                <tr class="forum-labelRow">
                    <td valign="top"><spring:message code="forum.attachments"/></td>
                </tr>
                <tr class="forum-tableRow0">
                    <td>
                        <spring:bind path="forum.attachmentsAllowed">
                            <input type="hidden" name="_<c:out value="${status.expression}"/>">
                            <input type="checkbox" name="<c:out value="${status.expression}"/>" value="true"
                                   <c:if test="${status.value}">checked</c:if>/>
                            <spring:message code="forum.attachments.allow"/>
                        </spring:bind>
                    </td>
                </tr>

                <tr class="forum-tableRow1">
                    <td align="left">
                        <input type="submit" class="submit" value="<spring:message code="forum.edit.save"/>">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>
