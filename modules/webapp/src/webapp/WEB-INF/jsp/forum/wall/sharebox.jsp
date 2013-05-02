<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div id="oa-forum-newPost">
    <div id="oa-forum-sharebox">
        <form action="${pageContext.request.contextPath}/forum/editpost" class="oa-forum-ajaxForm" method="POST">
            <div class="oa-forum-formElement">
                <c:choose>
                    <c:when test="${forumId > 0}">
                        <input type="hidden" name="forumId" value="${forumId}">
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="forumId" value="${defaultPostForumId}">
                    </c:otherwise>
                </c:choose>
                <input type="hidden" name="ajax" value="true">
                <input type="hidden" name="hiddenForumId" value="${hiddenForumId}"/>
                <input type="hidden" name="subject" value="Subject">
                <label class="oa-forum-hidden">${helptextLabel}</label>
                <%-- helptextLabel is set in RenderWallTag--%>
                <textarea rows="1" cols="40" name="body" id="oa-forum-sharebox-textarea" class="oa-forum-sharefield oa-forum-fadedText">${helptextLabel}</textarea>

            </div>

            <div id="oa-forum-sharebox-buttons" class="oa-forum-hidden">
                <div id="oa-forum-share-add-attachment" class="oa-forum-formElement">
                    <a href="#"><kantega:label key="forum.share.inputfield.addfile" bundle="forum" locale="${forumLocale}"/></a>
                </div>
                <div id="oa-forum-share-attachment" class="oa-forum-formElement oa-forum-hidden">
                    <input type="file" name="attachment1">
                </div>

                <div class="oa-forum-formElement oa-forum-txtR">
                <span class="oa-forum-share-button">
                    <input type="submit" name="send" value="<kantega:label key="forum.share.submit.label" bundle="forum" locale="${forumLocale}"/>">
                </span>
                </div>
                <div style="clear:both;"></div>
            </div>
        </form>
    </div>
</div>