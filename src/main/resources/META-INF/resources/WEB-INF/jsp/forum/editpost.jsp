<%@ page import="no.kantega.forum.util.ForumUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ taglib prefix="modules" uri="http://www.kantega.no/aksess/tags/modules" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ page contentType="text/html;charset=utf-8" %>

<kantega:section id="innhold">
    <div class="contentmain edit-post">
        <script type="text/javascript">
            function submitForm() {
                var form = document.editpost;
                var a = '';
            <%
            ForumUtil.getNoSpamCode(out);
            %>
                form.nospam.value = a;
                form.submit();
            }
        </script>
        <noscript><h1><spring:message code="post.noscript"/></h1></noscript>
        <form method="POST" enctype="multipart/form-data" name="editpost">
            <input type="hidden" name="nospam" value="">
            <spring:bind path="post.id">
                <input type="hidden" name="id" value="<c:out value="${status.value}"/>">
            </spring:bind>
            <div class="forum-heading">
                <c:choose>
                    <c:when test="${post.thread.id == 0}"><spring:message code="thread.addthread"/></c:when>
                    <c:when test="${post.id == 0}"><spring:message code="post.addpost"/></c:when>
                    <c:otherwise><c:out value="${post.subject}"/></c:otherwise>
                </c:choose>
            </div>
            <table border="0" cellpadding="0" cellspacing="0">

                <modules:userprofile var="user">
                    <c:choose>
                        <c:when test="${user == null}">
                            <tr class="forum-labelRow">
                                <td valign="top" colspan="2"><spring:message code="post.name"/>:</td>
                            </tr>
                            <tr class="forum-tableRow0">
                                <spring:bind path="post.author">
                                    <td>
                                        <input type="text" size="50" name="author" value="<c:out value="${status.value}"/>" class="forum-editpost-author">
                                    </td>
                                    <td>
                                        <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                                    </td>
                                </spring:bind>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr class="forum-labelRow">
                                <td valign="top" colspan="2"><spring:message code="post.name"/>:</td>
                            </tr>

                            <tr class="forum-tableRow0">
                                <td colspan="2">
                                    <c:out value="${user.fullName}"/>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>

                </modules:userprofile>
                <tr class="forum-labelRow">
                    <td valign="top" colspan="2">
                        <c:choose>
                            <c:when test="${post.thread.id == 0}"><spring:message code="post.subject.new"/></c:when>
                            <c:otherwise><spring:message code="post.subject"/></c:otherwise>
                        </c:choose>:</td>
                </tr>

                <tr class="forum-tableRow0">
                    <spring:bind path="post.subject">
                        <td>
                            <input type="text" size="50" name="subject" value="<c:out value="${status.value}"/>" class="forum-editpost-subject">
                        </td>
                        <td>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <tr class="forum-labelRow">
                    <td valign="top" colspan="2"><spring:message code="post.body"/>:</td>
                </tr>
                <tr class="forum-tableRow0">
                    <spring:bind path="post.body">
                        <td>
                            <textarea name="body" rows="15" class="forum-editpost-body"><c:out value="${status.value}"/></textarea>
                            <c:if test="${!post.approved}">
                                <div class="forum-message"><spring:message code="post.moderate.info"/></div>
                            </c:if>
                        </td>
                        <td>
                            <spring:message code="forum.validation.${status.errorCode}" text="${status.errorMessage}"/>
                        </td>
                    </spring:bind>
                </tr>
                <forum:haspermisson permission="ATTACH_FILE" object="${post}">
                    <tr class="forum-labelRow">
                        <td valign="top" colspan="2"><spring:message code="post.attachments"/>:</td>
                    </tr>
                    <tr class="forum-tableRow0">
                        <td>
                            <div class="forum-attachments-info">
                                <spring:message code="post.attachments.info"/>
                            </div>

                            <input type="file" name="attachment1"><br>
                            <input type="file" name="attachment2"><br>
                            <input type="file" name="attachment3"><br>
                            <input type="file" name="attachment4"><br>
                            <input type="file" name="attachment5"><br>

                            <div class="forum-attachments-existing">
                                <c:forEach items="${post.attachments}" var="attachment" varStatus="status">
                                    <c:choose>
                                        <c:when test="${attachment.image}">
                                            <a href="viewattachment?attachmentId=<c:out value="${attachment.id}"/>" target="_blank"><c:out value="${attachment.fileName}"/></a><br>
                                        </c:when>
                                        <c:otherwise>
                                            <a class="oa-forum-attachment" href="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=<c:out value="${attachment.id}"/>" target="_blank">
                                                <c:out value="${attachment.fileName}"/>
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>

                        </td>
                        <td>&nbsp;</td>
                    </tr>
                </forum:haspermisson>
                <tr class="forum-tableRow0">
                    <td colspan="2" align="right">
                        <c:choose>
                            <c:when test="${post.thread.id == 0}"><input type="button" class="submit" onclick="submitForm()" value="<spring:message code="thread.edit.save"/>"></c:when>
                            <c:when test="${post.id == 0}"><input type="button" class="submit" onclick="submitForm()" value="<spring:message code="post.edit.savenew"/>"></c:when>
                            <c:otherwise><input type="button" class="submit" onclick="submitForm()" value="<spring:message code="post.edit.save"/>"></c:otherwise>
                        </c:choose>
                        <input type="button" class="button" onclick="history.back();" value="<spring:message code="cancel"/>">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</kantega:section>

<%@include file="include/design/design.jsf"%>
