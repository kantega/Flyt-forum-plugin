<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>
<%@ page contentType="text/html;charset=utf-8" %>

<div id="flytForumWallTemplates" style="display: none">
    <div class="oa-forum-thread">
        <div class="oa-forum-posts">

        </div>
        <div class="oa-forum-mediablock oa-forum-reply">
            <div class="oa-forum-mediablockContent oa-forum-reply">
                <form>
                    <div>
                        <div class="oa-forum-formElement">
                            <input type="hidden" name="threadId">
                            <label class="oa-forum-hidden"><kantega:label key="forum.comment.inputfield.label" bundle="forum" locale="${forumLocale}"/></label>
                            <textarea rows="1" cols="40" name="body" class="oa-forum-sharefield oa-forum-comment-reply" placeholder="<kantega:label key="forum.comment.inputfield.label" bundle="forum" locale="${forumLocale}"/>"></textarea>
                        </div>
                        <div class="oa-forum-formElement oa-forum-txtR">
                        <span class="oa-forum-share-button">
                            <input type="submit" value="<kantega:label key="forum.wall.comment.submit" bundle="forum" locale="${forumLocale}"/>" name="send" class="oa-forum-share-comment">
                        </span>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="oa-forum-post oa-forum-mediablock">
        <div class="oa-forum-mediablockImage oa-forum-avatar">
            <a href="#">
                <img src="#">
            </a>
        </div>
        <div class="oa-forum-mediablockContent">
            <a name="oa-forum-post_"></a>

            <div class="oa-forum-username">
                <a href="#">:</a>
            </div>

            <a title="Rediger" class="oa-forum-editPost" href="#"></a>
            <a title="Slett" class="oa-forum-deletePost" href="#"></a>
            <a title="Slett" class="oa-forum-deleteThread" href="#"></a>

            <div class="oa-forum-body">
                <p>

                </p>
            </div>
            <div class="oa-forum-editBody oa-forum-hidden">
                <textarea rows="1" cols="40" name="body"></textarea>
            </div>
            <div class="oa-forum-attachments">

            </div>
            <div class="oa-forum-edit oa-forum-hidden">
                <button class="oa-forum-post-cancelEditPost">Avbryt</button>
                <button class="oa-forum-post-submitEditPost">Lagre</button>
            </div>
            <div class="oa-forum-metadata">
                <div style="display: inline;">
                    <a href="#" class="oa-forum-showReplyForm"><kantega:label key="forum.wall.leave.comment" bundle="forum" locale="${forumLocale}"/></a>
                    <span class="oa-forum-divider">&nbsp;&sdot;&nbsp;</span>
                </div>
                <div style="display: inline;">
                    <a href="#" class="oa-forum-like-link oa-forum-unlike"><kantega:label key="forum.wall.likes" bundle="forum" locale="${forumLocale}"/></a>
                    <a href="#" class="oa-forum-like-link oa-forum-like"><kantega:label key="forum.wall.like" bundle="forum" locale="${forumLocale}"/></a>
                    <span class="oa-forum-divider">&nbsp;&sdot;&nbsp;</span>
                </div>
                <div style="display: inline;">
                    <span class="oa-forum-date oa-forum-postDate" date-data=""></span>
                </div>
                <div style="display: inline;">
                    redigert
                    <span class="oa-forum-date oa-forum-date-modified" date-data=""></span>
                </div>
                <div style="display: inline;">
                    i <a href="#"><span class="oa-forum-category"></span></a>
                </div>
            </div>
        </div>
        <div class="oa-forum-mediablock oa-forum-likes oa-forum-fadedText oa-forum-collapsableThread oa-forum-hidden">
            <a href="#" class="oa-forum-showFullThread"></a>
            <a href="#" class="oa-forum-minimizeThread">
                <kantega:label key="forum.wall.morecomments.collaps" bundle="forum" locale="${forumLocale}"/>
            </a>
        </div>
        <div style="clear:both"></div>


        <div class="oa-forum-mediablock oa-forum-likes oa-forum-fadedText oa-forum-collapsableLikes">
            <div class="oa-forum-likers" style="display: inline">

            </div>
            <div class="oa-forum-separatorContainer">
                <div class="oa-forum-likerSeparator"></div>
            </div>
            <a href="#" class='oa-forum-showAllRatings'>
                <div class="oa-forum-otherLikersCount"></div>
                <kantega:label key="forum.wall.likes.others" bundle="forum" locale="${forumLocale}"/>
            </a>
            <kantega:label key="forum.wall.likes.this" bundle="forum" locale="${forumLocale}"/>
            <a href="#" class='oa-forum-minimizeRatings'>
                <kantega:label key="forum.wall.likes.minimize" bundle="forum" locale="${forumLocale}"/>
            </a>
        </div>
    </div>
    <div class="oa-forum-liker oa-forum-separatorContainer">
        <a href="#"></a>
        <div class="oa-forum-likerSeparator"></div>
    </div>
    <a class="oa-forum-attachment" data-download="#" href="#" target="_blank">
        <img src="#" alt="" border="0">
    </a>
    <a class="oa-forum-attachment-doc" href="" target="_blank">
    </a>
    <script type="text/javascript">
        function handleProfileImageNotFound(image){
            image.onerror = "";
            <c:set var="defaultProfileImage"><aksess:getconfig key="forum.defaultProfileImage" default=""/></c:set>
            <c:if test="${not empty defaultProfileImage}">

            image.src = "${pageContext.request.contextPath}${defaultProfileImage}";
            </c:if>
            return true;
        }
    </script>
</div>