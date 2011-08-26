<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>

<div id="oa-forum-newPost">
    <div id="oa-forum-sharebox" class="oa-forum-tabs">
        <ul class="oa-forum-shareListLinks clearfix">
            <li>
                <kantega:label key="forum.share.tabs.preceedinglabel" bundle="forum"/>
            <li>
                <a href="#" class="oa-forum-tablink oa-forum-iconLink"><img src="<aksess:geturl/>/bitmaps/forum/forum-share-status.png" alt="<kantega:label key="forum.share.status.label.alttext" bundle="forum"/>"><span><kantega:label key="forum.share.status.label" bundle="forum"/></span></a>
            </li>
            <li>
                <a href="#" class="oa-forum-tablink oa-forum-iconLink"><img src="<aksess:geturl/>/bitmaps/forum/forum-share-photo.png" alt="<kantega:label key="forum.share.photo.label.alttext" bundle="forum"/>"><span><kantega:label key="forum.share.photo.label" bundle="forum"/></span></a>
            </li>
            <li>
                <a href="#" class="oa-forum-tablink oa-forum-iconLink"><img src="<aksess:geturl/>/bitmaps/forum/forum-share-link.png" alt="<kantega:label key="forum.share.link.label.alttext" bundle="forum"/>"><span><kantega:label key="forum.share.link.label" bundle="forum"/></span></a>
            </li>
        </ul>
        <div class="oa-forum-tab-container" id="oa-forum-tab-container-status">
            <form action="${pageContext.request.contextPath}/forum/editpost" class="oa-forum-ajaxForm" method="POST">
                <div class="oa-forum-formElement">
                    <input type="hidden" name="forumId" value="1">
                    <input type="hidden" name="ajax" value="true">
                    <input type="hidden" name="subject" value="Subject">
                    <label class="oa-forum-hidden"><kantega:label key="forum.share.inputfield.status.label" bundle="forum"/></label>
                    <textarea rows="1" cols="40" name="body" class="oa-forum-sharefield oa-forum-fadedText"><kantega:label key="forum.share.inputfield.status.label" bundle="forum"/></textarea>
                </div>
                <div class="oa-forum-formElement oa-forum-txtR">
                    <input type="submit" name="send" value="<kantega:label key="forum.share.status.submit.label" bundle="forum"/>">
                </div>
            </form>
        </div>
        <div class="oa-forum-tab-container oa-forum-hidden" id="oa-forum-tab-container-photo">
            <form action="${pageContext.request.contextPath}/forum/editpost" class="oa-forum-ajaxForm" method="POST">
                <div class="oa-forum-formElement">
                    <input type="hidden" name="forumId" value="1">
                    <input type="file" name="attachment1">
                    <input type="hidden" name="ajax" value="true">
                    <input type="hidden" name="subject" value="Subject">
                    <input type="hidden" name="shareurl">
                </div>
                <div class="oa-forum-formElement">
                    <label class="oa-forum-hidden"><kantega:label key="forum.share.inputfield.photo.label" bundle="forum"/></label>
                    <textarea rows="1" cols="40" name="body" class="oa-forum-sharefield oa-forum-fadedText"><kantega:label key="forum.share.inputfield.photo.label" bundle="forum"/></textarea>
                </div>
                <div class="oa-forum-formElement oa-forum-txtR">
                    <input type="submit" name="send" value="<kantega:label key="forum.share.photo.submit.label" bundle="forum"/>">
                </div>
            </form>
        </div>
        <div class="oa-forum-tab-container oa-forum-hidden" id="oa-forum-tab-container-link">
            <form action="<aksess:geturl/>/emgs/getPageInfo?url=" id="oa-forum-linkform-step1">
                <div class="oa-forum-formElement">
                    <label class="oa-forum-hidden"><kantega:label key="forum.share.inputfield.link.label" bundle="forum"/></label>
                    <input type="text" id="oa-forum-link-shareUrl" class="oa-forum-sharefield oa-forum-fadedText" value="<kantega:label key="forum.share.inputfield.link.label" bundle="forum"/>">
                </div>
                <div class="oa-forum-formElement oa-forum-txtR">
                    <input type="submit" value="<kantega:label key="forum.share.link.submit.step1.label" bundle="forum"/>">
                </div>
            </form>

            <div class="oa-forum-link-preview-container">
                <h3 class="oa-forum-title"><a href=""></a></h3>
                <p class="oa-forum-shareurl oa-forum-fadedText"></p>

                <form action="${pageContext.request.contextPath}/forum/editpost" class="oa-forum-ajaxForm" method="POST" id="oa-forum-linkform-step2">
                    <div class="oa-forum-formElement">
                        <input type="hidden" name="forumId" value="1">
                        <input type="hidden" name="ajax" value="true">
                        <input type="hidden" name="subject" value="Subject">
                        <input type="hidden" name="body">
                        <input type="hidden" id="oa-forum-link-shareTitle">
                        <label class="oa-forum-hidden"><kantega:label key="forum.share.inputfield.link.step2.label" bundle="forum"/></label>
                        <textarea rows="1" cols="40" name="sharecomment" id="oa-forum-link-shareComment" class="oa-forum-fadedText oa-forum-sharefield"><kantega:label key="forum.share.inputfield.link.step2.label" bundle="forum"/></textarea>
                    </div>
                    <div class="oa-forum-formElement">
                        <input type="submit" name="send" value="<kantega:label key="forum.share.link.submit.step2.label" bundle="forum"/>">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>