<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="oa-forum-newPost">

    <div class="oa-forum-sharebox">
        <c:if test="${isForumWallSearch}">
            <%@include file="search-input.jsp"%>
        </c:if>
        <form action="${pageContext.request.contextPath}/forum/editpost" class="oa-forum-ajaxForm" method="POST">
            <div class="oa-forum-formElement">
                <c:choose>
                    <c:when test="${selectedForumId > 0}">
                        <input type="hidden" name="forumId" value="${selectedForumId}">
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="forumId" value="${defaultPostForumId}">
                    </c:otherwise>
                </c:choose>
                <input type="hidden" name="ajax" value="true">
                <input type="hidden" name="hiddenForumId" value="${hiddenForumId}"/>
                <c:if test="${not empty aksess_this}">
                    <c:set var="addContentIdIfFound"><aksess:getconfig key="forum.addContentIdIfFound" default="false"/></c:set>
                    <c:if test="${addContentIdIfFound}">
                        <input type="hidden" name="wallcontentid" value="${aksess_this.id}"/>
                    </c:if>
                </c:if>
                <input type="hidden" name="subject" value="Subject">
                <label class="oa-forum-sharebox-label oa-forum-hidden">${helptextLabel}</label>
                <%-- helptextLabel is set in RenderWallTag--%>
                <textarea rows="1" cols="40" name="body" class="oa-forum-sharebox-textarea oa-forum-sharefield" placeholder="${helptextLabel}"></textarea>
            </div>

            <div class="oa-forum-sharebox-buttons oa-forum-hidden">
                <div class="oa-forum-share-add-attachment oa-forum-formElement">
                    <a href="#"><kantega:label key="forum.share.inputfield.addfile" bundle="forum" locale="${forumLocale}"/></a>
                </div>
                <div class="oa-forum-share-attachment oa-forum-formElement oa-forum-hidden">
                    <input type="file" name="attachment1">
                </div>

                <div class="oa-forum-formElement oa-forum-txtR">
                <span class="oa-forum-share-button">
                    <input class="oa-forum-submit" type="submit" name="send" value="<kantega:label key="forum.share.submit.label" bundle="forum" locale="${forumLocale}"/>">
                </span>
                </div>
                <div style="clear:both;"></div>
            </div>
        </form>
    </div>
</div>
