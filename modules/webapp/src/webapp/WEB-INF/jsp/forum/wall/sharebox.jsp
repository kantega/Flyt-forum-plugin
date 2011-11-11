<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div id="oa-forum-newPost">
    <div id="oa-forum-sharebox" class="oa-forum-tabs">
        <ul class="oa-forum-shareListLinks clearfix">
            <li>
                <kantega:label key="forum.share.tabs.preceedinglabel" bundle="forum" locale="${forumLocale}"/>
            <li>
                <a href="#" class="oa-forum-tablink oa-forum-iconLink"><img src="<aksess:geturl/>/bitmaps/forum/forum-share-status.png" alt="<kantega:label key="forum.share.status.label.alttext" bundle="forum" locale="${forumLocale}"/>"><span><kantega:label key="forum.share.status.label" bundle="forum" locale="${forumLocale}"/></span></a>
            </li>
            <li>
                <a href="#" class="oa-forum-tablink oa-forum-iconLink"><img src="<aksess:geturl/>/bitmaps/forum/forum-share-photo.png" alt="<kantega:label key="forum.share.photo.label.alttext" bundle="forum" locale="${forumLocale}"/>"><span><kantega:label key="forum.share.photo.label" bundle="forum" locale="${forumLocale}"/></span></a>
            </li>
            <li>
                <a href="#" class="oa-forum-tablink oa-forum-iconLink"><img src="<aksess:geturl/>/bitmaps/forum/forum-share-link.png" alt="<kantega:label key="forum.share.link.label.alttext" bundle="forum" locale="${forumLocale}"/>"><span><kantega:label key="forum.share.link.label" bundle="forum" locale="${forumLocale}"/></span></a>
            </li>
        </ul>
        <div class="oa-forum-tab-container oa-forum-hidden" id="oa-forum-tab-container-status">
            <form action="${pageContext.request.contextPath}/forum/editpost" class="oa-forum-ajaxForm" method="POST">
                <div class="oa-forum-formElement">
                    <input type="hidden" name="forumId" value="1">
                    <input type="hidden" name="ajax" value="true">
                    <input type="hidden" name="subject" value="Subject">
                    <label class="oa-forum-hidden"><kantega:label key="forum.share.inputfield.status.label" bundle="forum" locale="${forumLocale}"/></label>
                    <textarea rows="1" cols="40" name="body" class="oa-forum-sharefield oa-forum-fadedText"><kantega:label key="forum.share.inputfield.status.label" bundle="forum" locale="${forumLocale}"/></textarea>
                </div>
                <div class="oa-forum-formElement oa-forum-txtR">
                    <span class="oa-forum-share-button">
                        <input type="submit" name="send" value="<kantega:label key="forum.share.status.submit.label" bundle="forum" locale="${forumLocale}"/>">
                    </span>
                </div>
            </form>
        </div>
        <div class="oa-forum-tab-container oa-forum-hidden" id="oa-forum-tab-container-photo">
            <form action="${pageContext.request.contextPath}/forum/editpost" class="oa-forum-ajaxForm" method="POST" enctype="multipart/form-data">
                <div class="oa-forum-formElement">
                    <input type="hidden" name="forumId" value="1">
                    <input type="file" name="attachment1">
                    <input type="hidden" name="ajax" value="true">
                    <input type="hidden" name="subject" value="Subject">
                </div>
                <div class="oa-forum-formElement">
                    <label class="oa-forum-hidden"><kantega:label key="forum.share.inputfield.photo.label" bundle="forum" locale="${forumLocale}"/></label>
                    <textarea rows="1" cols="40" name="body" class="oa-forum-sharefield oa-forum-fadedText"><kantega:label key="forum.share.inputfield.photo.label" bundle="forum" locale="${forumLocale}"/></textarea>
                </div>
                <div class="oa-forum-formElement oa-forum-txtR">
                    <span class="oa-forum-share-button">
                    <input type="submit" name="send" value="<kantega:label key="forum.share.photo.submit.label" bundle="forum" locale="${forumLocale}"/>">
                        </span>
                </div>
            </form>
        </div>
        <div class="oa-forum-tab-container oa-forum-hidden" id="oa-forum-tab-container-link">
            <form action="${pageContext.request.contextPath}/forum/editpost" class="oa-forum-ajaxForm" method="POST" id="oa-forum-linkform">
                <div class="oa-forum-formElement">
                    <label class="oa-forum-hidden"><kantega:label key="forum.share.inputfield.link.label" bundle="forum" locale="${forumLocale}"/></label>
                    <input type="text" id="oa-forum-link-shareUrl" class="oa-forum-sharefield oa-forum-fadedText" value="<kantega:label key="forum.share.inputfield.link.label" bundle="forum" locale="${forumLocale}"/>">
                </div>
                <div class="oa-forum-formElement">
                    <input type="hidden" name="forumId" value="1">
                    <input type="hidden" name="ajax" value="true">
                    <input type="hidden" name="subject" value="Subject">
                    <input type="hidden" name="body" value="" id="oa-forum-link-body">
                    <label class="oa-forum-hidden"><kantega:label key="forum.share.inputfield.link.step2.label" bundle="forum" locale="${forumLocale}"/></label>
                    <textarea rows="1" cols="40" name="comment" id="oa-forum-link-shareComment" class="oa-forum-sharefield oa-forum-fadedText"><kantega:label key="forum.share.inputfield.link.step2.label" bundle="forum" locale="${forumLocale}"/></textarea>
                </div>
                <div class="oa-forum-formElement oa-forum-txtR">
                    <span class="oa-forum-share-button">
                    <input type="submit" name="send" value="<kantega:label key="forum.share.link.submit.step2.label" bundle="forum" locale="${forumLocale}"/>">
                        </span>
                </div>
            </form>
        </div>

    </div>
</div>