(function($){
    var threadTemplate = $("#flytForumWallTemplates").find(".oa-forum-thread");
    var postTemplate = $("#flytForumWallTemplates").find(".oa-forum-post");
    var likerTemplate = $("#flytForumWallTemplates").find(".oa-forum-liker");
    var attachmentTemplate = $("#flytForumWallTemplates").find(".oa-forum-attachment");
    var attachmentDocTemplate = $("#flytForumWallTemplates").find(".oa-forum-attachment-doc");
    var getOption = function(options, key) {
        var option = options !== undefined && options !== null ? options[key] : null;
        if (option !== undefined && option !== null) {
            return option;
        }
        throw "Missing option: " + key;
    };

    var isDefined = function(value) {
        return value !== undefined && value !== null;
    };
    var getFirst = function(array, acceptFunction) {
        if ($.isArray(array) && $.isFunction(acceptFunction)) {
            for (var index = 0; index< array.length; index++) {
                if (acceptFunction(array[index])) {
                    return array[index];
                }
            }
        }
        return null;
    };
    var isImage = function(mimeType) {
        return /^image\//i.test(mimeType);
    };
    var appendPost = function(threadPostsElement, postElement) {
        var threadElement = threadPostsElement.closest(".oa-forum-thread");
        threadPostsElement.append(postElement);
        viewShowReplyOrReply(threadElement);
        threadElement.find(".oa-forum-category").html(threadElement.data("thread").forumReference.name);
        postElement.trigger("postAppended");
    };
    var removePost = function(postElement) {
        var threadElement = postElement.closest(".oa-forum-thread");
        postElement.trigger("postRemoved");
        postElement.remove();
        viewShowReplyOrReply(threadElement);
        threadElement.find(".oa-forum-category").html(threadElement.data("thread").forumReference.name);
    };
    var replacePost = function(replacePost, withPost) {
        var threadElement = replacePost.closest(".oa-forum-thread");
        replacePost.replaceWith(withPost);
        viewShowReplyOrReply(threadElement);
        threadElement.find(".oa-forum-category").html(threadElement.data("thread").forumReference.name);
        withPost.trigger("postReplaced", replacePost);
    };
    var viewShowReplyOrReply = function(threadElement) {
        var threadReplyElement = threadElement.find(".oa-forum-reply");
        var threadShowReply = threadElement.find(".oa-forum-showReplyForm");
        if (threadElement.find(".oa-forum-post").length <= 1) {
            threadReplyElement.addClass("oa-forum-hidden");
            threadShowReply.closest("div").removeClass("oa-forum-hidden");
        } else {
            threadReplyElement.removeClass("oa-forum-hidden");
            threadShowReply.closest("div").addClass("oa-forum-hidden");
        }
    };
    var populateAttachmentTemplate = function(imageUrl, imagePreviewUrl, docUrl, attachment) {
        var attachmentClone = null;
        if (isImage(attachment.mimeType)) {
            attachmentClone = attachmentTemplate.clone();
            attachmentClone.attr("data-download", imageUrl.replace("%s", attachment.id));
            attachmentClone.attr("href", imagePreviewUrl.replace("%s", attachment.id));
            var attachmentCloneImg = attachmentClone.find("img");
            attachmentCloneImg.attr("src", imagePreviewUrl.replace("%s", attachment.id));
            attachmentCloneImg.attr("alt", attachment.fileName);
        } else {
            attachmentClone = attachmentDocTemplate.clone();
            attachmentClone.attr("href", docUrl.replace("%s", attachment.id));
        }
        return attachmentClone;
    };
    var populateLikeTemplate = function(userProfileUrl, liker){
        if (!isDefined(liker.userFullName)) {
            return null;
        }
        var likerClone = likerTemplate.clone();
        likerClone.data("liker", liker);
        var likerAnchor = likerClone.find("a");
        likerAnchor.attr("href", userProfileUrl.replace("%s", liker.userid));
        likerAnchor.html(liker.userFullName);
        return likerClone;
    };
    var populatePostTemplate = function(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, post, thread) {
        var postClone = postTemplate.clone();

        postClone.attr("data-postId", post.id);
        postClone.find(".oa-forum-avatar a").attr("href", userProfileUrl.replace("%s", post.owner));
        postClone.find(".oa-forum-avatar img").attr("src", userProfileImageUrl.replace("%s", post.owner));
        postClone.find(".oa-forum-avatar img").attr("onerror", "handleProfileImageNotFound(this);");
        postClone.find(".oa-forum-post_").attr("name", postClone.find(".oa-forum-post_").attr("name") + post.id);
        postClone.find(".oa-forum-username a").attr("href", userProfileUrl.replace("%s", post.owner));
        postClone.find(".oa-forum-username a").html(post.author + postClone.find(".oa-forum-username a").html());

        var postEditAction = getFirst(post.action, function(action) {return action.rel === "update";});
        var postCloneEditPost = postClone.find(".oa-forum-editPost");
        if (isDefined(postEditAction)) {
            postCloneEditPost.attr("href", postEditAction.href);
            postCloneEditPost.data("action", postEditAction);
        } else {
            postCloneEditPost.addClass("oa-forum-hidden");
        }
        var postDeleteAction = getFirst(post.action, function(action) {return action.rel === "delete";});
        var postCloneDeletePost = postClone.find(".oa-forum-deletePost");
        if (isDefined(postDeleteAction)) {
            postCloneDeletePost.attr("href", postDeleteAction.href);
            postCloneDeletePost.data("action", postDeleteAction);
        } else {
            postCloneDeletePost.addClass("oa-forum-hidden");
        }
        if (isDefined(thread)) {
            var threadDeleteAction = getFirst(thread.action, function (action) {
                return action.rel === "delete";
            });
            var postCloneDeleteThread = postClone.find(".oa-forum-deleteThread");
            if (isDefined(threadDeleteAction)) {
                postCloneDeleteThread.attr("href", threadDeleteAction.href);
                postCloneDeleteThread.data("action", threadDeleteAction);
            } else {
                postCloneDeleteThread.addClass("oa-forum-hidden");
            }
        }
        var postLikeAction = getFirst(post.action, function(action) {return action.rel === "like";});
        var postCloneLike = postClone.find(".oa-forum-like");
        if (isDefined(postLikeAction)) {
            postCloneLike.attr("href", postLikeAction.href);
            postCloneLike.data("action", postLikeAction);
        } else {
            postCloneLike.addClass("oa-forum-hidden");
        }
        var postUnlikeAction = getFirst(post.action, function(action) {return action.rel === "unlike";});
        var postCloneUnlike = postClone.find(".oa-forum-unlike");
        if (isDefined(postUnlikeAction)) {
            postCloneUnlike.attr("href", postUnlikeAction.href);
            postCloneUnlike.data("action", postUnlikeAction);
        } else {
            postCloneUnlike.addClass("oa-forum-hidden");
        }

        var postClonePostDate = postClone.find(".oa-forum-postDate");
        if (isDefined(post.postDate)) {
            postClonePostDate.attr("date-data", post.postDate);
            postClonePostDate.html(post.postDate)
            postClonePostDate.prettyDate({serverTime: new Date().toISOString()});
        } else {
            postClonePostDate.closest("div").addClass("oa-forum-hidden");
        }
        var postCloneModifiedDate = postClone.find(".oa-forum-date-modified");
        if (isDefined(post.modifiedDate) && (!isDefined(post.postDate) || post.postDate < post.modifiedDate)) {
            postCloneModifiedDate.attr("date-data", post.modifiedDate);
            postCloneModifiedDate.html(post.modifiedDate)
            postCloneModifiedDate.prettyDate({serverTime: new Date().toISOString()});
        } else {
            postCloneModifiedDate.closest("div").addClass("oa-forum-hidden");
        }

        postClone.find(".oa-forum-body p").text(post.body);
        postClone.find(".oa-forum-editBody [name=body]").text(post.body);
        if (isDefined(post.embed)) {
            try {
                post.embed = $.parseJSON(post.embed);
                //post.embed = isDefined(post.embed) ? $.isArray(post.embed) ? post.embed : [post.embed] : [];
                if (post.embed.length > 0) {
                    postClone.find(".oa-forum-embed-url")
                        .attr("title", post.embed[0].url)
                        .attr("href", post.embed[0].url);
                    postClone.find(".oa-forum-embed-title").text(post.embed[0].title);
                    postClone.find(".oa-forum-embed-description").text(post.embed[0].description);
                    if (isDefined(post.embed[0].html)) {
                        postClone.find(".oa-forum-embed-html").html(post.embed[0].html);
                        postClone.find(".oa-forum-embed-html > iframe")
                            .removeAttr("width")
                            .removeAttr("height");
                    } else {
                        postClone.find(".oa-forum-embed-thumbnail")
                            .attr("src", post.embed[0].thumbnail_url)
                            .css("max-width", imagePreviewWidth + "px")
                            .css("max-height", imagePreviewHeight + "px")

                    }
                    postClone.find(".oa-forum-embed-url").removeClass("oa-forum-hidden");
                    postClone.find(".oa-forum-embed").removeClass("oa-forum-hidden");
                    postClone.find(".oa-forum-body").addClass("oa-forum-hidden");
                }
            } catch (cause) {
                cause = cause;
            }
        }

        if (isDefined(post.like)) {
            postClone.flytCollapsableLikes(options);
            var postCloneCollapsableLikes = postClone.find(".oa-forum-collapsableLikes .oa-forum-likers")
            for (var likeIndex = 0; likeIndex < post.like.length; likeIndex++) {
                var likerClone = populateLikeTemplate(userProfileUrl, post.like[likeIndex]);
                if (isDefined(likerClone)) {
                    postCloneCollapsableLikes.append(likerClone);
                    likerClone.trigger("likerAppended");
                }
            }
        }
        if (isDefined(post.attachment)) {
            var postCloneAttachments = postClone.find(".oa-forum-attachments");
            for (var attachmentIndex = 0; attachmentIndex < post.attachment.length; attachmentIndex++) {
                postCloneAttachments.append(populateAttachmentTemplate(imageUrl, imagePreviewUrl, docUrl, post.attachment[attachmentIndex]));
            }
        }
        return postClone;
    };
    var populateThreadTemplate = function(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, thread) {
        var threadClone = threadTemplate.clone();

        threadClone.attr("data-threadId", thread.id);
        threadClone.data("thread", thread);

        var postCreateAction = getFirst(thread.action, function(action){return action.rel === "create";});
        var threadCloneForm = threadClone.find("form");
        if (isDefined(postCreateAction)) {
            threadCloneForm.attr("action", postCreateAction.href);
            threadCloneForm.find("input[name=threadId]").val(thread.id);
            threadCloneForm.data("action", postCreateAction);
        } else {
            threadCloneForm.remove();
        }

        threadClone.flytCollapsableThread(options);

        var threadClonePosts = threadClone.find(".oa-forum-posts");
        if (isDefined(thread.post)) {
            for (var postIndex = 0; postIndex < thread.post.length; postIndex++) {
                appendPost(threadClonePosts, populatePostTemplate(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, thread.post[postIndex], thread));
            }
        }
        return threadClone;
    };
    $.fn.flytCollapsableThread = function(options) {
        var threadElement = $(this);
        var showFullThreadLabelSeveral = getOption(options, "showFullThreadLabelSeveral");
        var showFullThreadLabelTwo = getOption(options, "showFullThreadLabelTwo");
        var minimized = true;
        var getShowFullThreadLabel = function(postCount, remove) {
            if (remove != undefined && remove != null && remove) {
                postCount--;
            }
            if (postCount > 3) {
                return showFullThreadLabelSeveral.replace("%s", postCount - 1);
            }
            return showFullThreadLabelTwo;
        };
        var showFullThread = function() {
            var postElements = threadElement.find(".oa-forum-post");
            postElements.not(":first-child").not(":last-child").removeClass("oa-forum-hidden");
            postElements.find(".oa-forum-showFullThread").addClass("oa-forum-hidden");
            postElements.find(".oa-forum-minimizeThread").removeClass("oa-forum-hidden");
            minimized = false;
        };
        var minimizeThread = function() {
            var postElements = threadElement.find(".oa-forum-post");
            postElements.not(":first-child").not(":last-child").addClass("oa-forum-hidden");
            postElements.find(".oa-forum-showFullThread").removeClass("oa-forum-hidden");
            postElements.find(".oa-forum-minimizeThread").addClass("oa-forum-hidden");
            minimized = true;
        };
        var onPostChange = function(remove){
            var postElements = threadElement.find(".oa-forum-post");
            if (postElements.length <= 2) {
                postElements.find(".oa-forum-collapsableThread").addClass("oa-forum-hidden");
            } else {
                postElements.find(".oa-forum-collapsableThread").removeClass("oa-forum-hidden");
            }
            postElements.find(".oa-forum-showFullThread").html(getShowFullThreadLabel(threadElement.find(".oa-forum-post").length, remove));
            postElements.not(":first-child").find(".oa-forum-collapsableThread").remove();
            if (minimized) {
                minimizeThread();
            } else {
                showFullThread();
            }
        };
        threadElement.on("postAppended", function() {
            onPostChange();
        });
        threadElement.on("postRemoved", function() {
            onPostChange(true);
            threadElement.find(".oa-forum-post:last-child").prev().removeClass("oa-forum-hidden");
        });
        threadElement.on("postReplaced", function() {
            onPostChange();
        });
        threadElement.on("click", ".oa-forum-showFullThread", function(event) {
            event.preventDefault();
            showFullThread();
        });
        threadElement.on("click", ".oa-forum-minimizeThread", function(event) {
            event.preventDefault();
            minimizeThread();
        });
    };
    $.fn.flytCollapsableLikes = function(options) {
        var postElement = $(this);
        var andSeparator = getOption(options, "andSeparator");
        var minimized = true;
        var showAllRatings = function() {
            minimized = false;
            postElement.find(".oa-forum-liker").removeClass("oa-forum-hidden");
            correctMinimizeMaximizeShouldBeVisible();
            correctSeparators(postElement.find(".oa-forum-likerSeparator").closest(".oa-forum-separatorContainer").not(".oa-forum-hidden"));
        };
        var minimizeRatings = function() {
            minimized = true;
            var likerElements = postElement.find(".oa-forum-liker");
            for (var likerIndex = 2; likerIndex < likerElements.length; likerIndex++) {
                $(likerElements[likerIndex]).addClass("oa-forum-hidden");
            }
            correctMinimizeMaximizeShouldBeVisible();
            correctSeparators(postElement.find(".oa-forum-likerSeparator").closest(".oa-forum-separatorContainer").not(".oa-forum-hidden"));
        };
        var onLikerChange = function() {
            if (minimized) {
                minimizeRatings();
            } else {
                showAllRatings();
            }
        };
        var correctSeparators = function(separatorContainers) {
            var separators = separatorContainers.find(".oa-forum-likerSeparator");
            for (var likerIndex = separators.length - 1; likerIndex >= 0; likerIndex--) {
                if (likerIndex + 1 == separators.length) {
                    $(separators[likerIndex]).html("");
                } else if (likerIndex + 2 == separators.length) {
                    $(separators[likerIndex]).html(andSeparator);
                } else {
                    $(separators[likerIndex]).html(",");
                }
            }
        };
        var correctMinimizeMaximizeShouldBeVisible = function() {
            var likerElements = postElement.find(".oa-forum-liker");
            postElement.find(".oa-forum-otherLikersCount").html(likerElements.length - 2);
            if (likerElements.length == 0) {
                postElement.find(".oa-forum-collapsableLikes").addClass("oa-forum-hidden");
            } else {
                postElement.find(".oa-forum-collapsableLikes").removeClass("oa-forum-hidden");
            }
            if (likerElements.length <= 2) {
                postElement.find(".oa-forum-showAllRatings").addClass("oa-forum-hidden");
                postElement.find(".oa-forum-showAllRatings").prev(".oa-forum-separatorContainer").addClass("oa-forum-hidden");
                postElement.find(".oa-forum-minimizeRatings").addClass("oa-forum-hidden");
            } else if (minimized) {
                postElement.find(".oa-forum-showAllRatings").removeClass("oa-forum-hidden");
                postElement.find(".oa-forum-showAllRatings").prev(".oa-forum-separatorContainer").removeClass("oa-forum-hidden");
                postElement.find(".oa-forum-minimizeRatings").addClass("oa-forum-hidden");
            } else {
                postElement.find(".oa-forum-showAllRatings").addClass("oa-forum-hidden");
                postElement.find(".oa-forum-showAllRatings").prev(".oa-forum-separatorContainer").addClass("oa-forum-hidden");
                postElement.find(".oa-forum-minimizeRatings").removeClass("oa-forum-hidden");
            }
        };
        postElement.on("likerAppended", function(){
            onLikerChange();
        });
        postElement.on("click", ".oa-forum-showAllRatings", function(event){
            event.preventDefault();
            showAllRatings();
            onLikerChange();
        });
        postElement.on("click", ".oa-forum-minimizeRatings", function(event){
            event.preventDefault();
            minimizeRatings();
            onLikerChange();
        });
        correctMinimizeMaximizeShouldBeVisible();
    };
    function shareComment(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight) {
        var postsElement = element.closest(".oa-forum-thread").find(".oa-forum-posts");
        var formElement = element.closest("form");
        var body = formElement.find("[name=body]").val();
        var action = formElement.data("action");
        $.ajax({
            "contentType": "application/json",
            "type": action.method,
            "url": action.href,
            "data": JSON.stringify({body: body})
        }).done(function (data, textStatus, jqXHR) {
            appendPost(postsElement, populatePostTemplate(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, data, postsElement.closest(".oa-forum-thread").data("thread")));
            formElement.find("[name=body]").val("");
        }).fail(function (jqXHR, textStatus, errorThrown) {

        }).always(function (dataOrjqXHR, textStatus, jqXHROrerrorThrown) {

        });
    }

    function editPost(element) {
        var postElement = element.closest(".oa-forum-post");
        postElement.find(".oa-forum-post-submitEditPost").data("action", element.data("action"));
        postElement.find(".oa-forum-edit").toggleClass("oa-forum-hidden");
        var editBody = postElement.find(".oa-forum-editBody");
        editBody.toggleClass("oa-forum-hidden");
    }

    function deletePost(element) {
        var action = element.data("action");
        var postElement = element.closest(".oa-forum-post");
        $.ajax({
            "type": action.method,
            "url": action.href
        }).done(function (data, textStatus, jqXHR) {
            removePost(postElement);
        }).fail(function (jqXHR, textStatus, errorThrown) {

        }).always(function (dataOrjqXHR, textStatus, jqXHROrerrorThrown) {

        });
    }

    function cancelEditPost(element) {
        var postElement = element.closest(".oa-forum-post");
        postElement.find(".oa-forum-edit").toggleClass("oa-forum-hidden");
        postElement.find(".oa-forum-editBody").toggleClass("oa-forum-hidden");
    }

    function submitEditPost(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight) {
        var action = element.data("action");
        var postElement = element.closest(".oa-forum-post");
        var editBody = postElement.find(".oa-forum-editBody");
        postElement.find(".oa-forum-edit").toggleClass("oa-forum-hidden");
        editBody.toggleClass("oa-forum-hidden");
        var body = editBody.find("[name=body]").val();
        $.ajax({
            "contentType": "application/json",
            "type": action.method,
            "url": action.href,
            "data": JSON.stringify({body: body})
        }).done(function (data, textStatus, jqXHR) {
            replacePost(postElement, populatePostTemplate(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, data, postElement.closest(".oa-forum-thread").data("thread")));
        }).fail(function (jqXHR, textStatus, errorThrown) {

        }).always(function (dataOrjqXHR, textStatus, jqXHROrerrorThrown) {

        });
    }

    function showReplyForm(element) {
        element.closest(".oa-forum-thread").find(".oa-forum-reply").removeClass("oa-forum-hidden");
        element.closest("div").addClass("oa-forum-hidden");
    }

    function like(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight) {
        var postElement = element.closest(".oa-forum-post");
        var action = element.data("action");
        $.ajax({
            type: action.method,
            url: action.href
        }).done(function (data, textStatus, jqXHR) {
            replacePost(postElement, populatePostTemplate(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, data, postElement.closest(".oa-forum-thread").data("thread")));
        }).fail(function (jqXHR, textStatus, errorThrown) {

        }).always(function (dataOrjqXHR, textStatus, jqXHROrerrorThrown) {

        });
    }

    function unlike(element) {
        var postElement = element.closest(".oa-forum-post");
        var action = element.data("action");
        $.ajax({
            type: action.method,
            url: action.href
        }).done(function (data, textStatus, jqXHR) {
            postElement.find(".oa-forum-like").removeClass("oa-forum-hidden");
            postElement.find(".oa-forum-unlike").addClass("oa-forum-hidden");
        }).fail(function (jqXHR, textStatus, errorThrown) {

        }).always(function (dataOrjqXHR, textStatus, jqXHROrerrorThrown) {

        });
    }

    function viewAttachment(element) {
        var overlay = document.createElement("div"),
            container = document.createElement("div"),
            image = document.createElement("img"),
            close = document.createElement("a"),
            download = document.createElement("a"),
            underlay = document.createElement("div");

        var spinnerOptions = {
            lines: 11,
            length: 1,
            width: 3,
            radius: 9,
            color: '#ffffff',
            className: 'oa-forum-overlay-spinner'
        };

        container.className = "oa-forum-image-container";
        overlay.className = "oa-forum-overlay";
        underlay.className = "oa-forum-underlay";

        image.className = "oa-forum-image-enlarged";
        image.src = element.attr("data-download");

        download.href = element.attr("data-download");
        download.innerHTML = "Last ned bilde";
        download.className = "oa-forum-image-download oa-forum-image-button";

        close.href = "#";
        close.innerHTML = "Lukk";
        close.className = "oa-forum-image-close oa-forum-image-button";

        overlay.appendChild(underlay);

        container.appendChild(close);
        container.appendChild(download);
        container.appendChild(image);

        imageSpinner = new Spinner(spinnerOptions).spin(document.body);

        document.body.appendChild(overlay);

        image.onload = function () {
            var vw = window.innerWidth;
            var vh = window.innerHeight;
            underlay.style.width = vw + "px";
            underlay.style.height = vh + "px";

            container.style.maxWidth = vw + "px";
            container.style.maxHeight = vh + "px";

            image.style.maxWidth = (vw / 100) * 90 + "px";
            image.style.maxHeight = (vh / 100) * 90 + "px";

            imageSpinner.stop();
            underlay.appendChild(container);
        };

        $(close).on("click", function (e) {
            $(container).remove();
            $(overlay).remove();
            imageSpinner.stop();
            return false;
        });

        $(container).on("click", function (e) {
            e.stopPropagation();
        });

        return false;
    }

    function fetchYounger(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, root, setYoungerAction) {
        var action = element.data("action");
        $.ajax({
            type: action.method,
            url: action.href
        }).done(function (data, textStatus, jqXHR) {
            var threads = []
            for (var threadIndex = 0; threadIndex < data.thread.length; threadIndex++) {
                threads.push(populateThreadTemplate(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, data.thread[threadIndex]));
            }
            root.find(".oa-forum-threads").prepend(threads);
            setYoungerAction(getFirst(data.action, function (action) {
                return action.rel === "younger";
            }));
        }).fail(function (jqXHR, textStatus, errorThrown) {

        }).always(function (dataOrjqXHR, textStatus, jqXHROrerrorThrown) {

        });
    }

    function fetchOlder(element, root, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, setOlderAction) {
        var action = element.data("action");
        $.ajax({
            type: action.method,
            url: action.href
        }).done(function (data, textStatus, jqXHR) {
            for (var threadIndex = 0; threadIndex < data.thread.length; threadIndex++) {
                root.find(".oa-forum-threads").append(populateThreadTemplate(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, data.thread[threadIndex]));
            }
            setOlderAction(getFirst(data.action, function (action) {
                return action.rel === "older";
            }));
        }).fail(function (jqXHR, textStatus, errorThrown) {

        }).always(function (dataOrjqXHR, textStatus, jqXHROrerrorThrown) {

        });
    }
    $.fn.flytThreads = function(options) {
        var root = $(this);
        var contextPath = getOption(options, "contextPath");
        var userProfileUrl = getOption(options, "userProfileUrl");
        var userProfileImageUrl = getOption(options, "userProfileImageUrl");
        var imageUrl = getOption(options, "imageUrl");
        var imagePreviewUrl = getOption(options, "imagePreviewUrl");
        var docUrl = getOption(options, "docUrl");
        var imagePreviewWidth = getOption(options, "imagePreviewWidth");
        var imagePreviewHeight = getOption(options, "imagePreviewHeight");
        var setYoungerAction = function(action) {
            var actionElement = root.find(".oa-forum-younger");
            if (isDefined(action)) {
                actionElement.data("action", action);
                actionElement.attr("href", action.href)
                actionElement.removeClass(".oa-forum-hidden")
            } else {
                actionElement.addClass(".oa-forum-hidden")
            }
        };
        var setOlderAction = function(action) {
            var actionElement = root.find(".oa-forum-older");
            if (isDefined(action)) {
                actionElement.data("action", action);
                actionElement.attr("href", action.href)
                actionElement.removeClass(".oa-forum-hidden")
            } else {
                actionElement.addClass(".oa-forum-hidden")
            }
        };
        var forumThreads = root.find(".oa-forum-threads");
        forumThreads.on("click", ".oa-forum-share-comment", function(event){
            event.preventDefault();
            var element = $(this);
            shareComment(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight);
        });
        forumThreads.on("click", ".oa-forum-editPost", function(event){
            event.preventDefault();
            var element = $(this);
            editPost(element);
        });
        forumThreads.on("click", ".oa-forum-deletePost", function(event){
            event.preventDefault();
            var element = $(this);
            deletePost(element);
        });
        forumThreads.on("click", ".oa-forum-post-cancelEditPost", function(event){
            event.preventDefault();
            var element = $(this);
            cancelEditPost(element);
        });
        forumThreads.on("click", ".oa-forum-post-submitEditPost", function(event){
            event.preventDefault();
            var element = $(this);
            submitEditPost(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight);
        });
        forumThreads.on("click", ".oa-forum-showReplyForm", function(event){
            event.preventDefault();
            var element = $(this);
            showReplyForm(element);
        });
        forumThreads.on("click", ".oa-forum-like", function(event){
            event.preventDefault();
            var element = $(this);
            like(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight);
        });
        forumThreads.on("click", ".oa-forum-unlike", function(event){
            event.preventDefault();
            var element = $(this);
            unlike(element);
        });
        forumThreads.on("click", ".oa-forum-attachment", function(event) {
            event.preventDefault();
            var element = $(this);
            return viewAttachment(element);
        });
        root.on("click", ".oa-forum-younger", function(event) {
            event.preventDefault();
            var element = $(this);
            fetchYounger(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, root, setYoungerAction);
        });
        root.on("click", ".oa-forum-older", function(event) {
            event.preventDefault();
            var element = $(this);
            fetchOlder(element, root, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, setOlderAction);
        });
        $.ajax({
            type: "GET",
            url: contextPath + "/forum/rest/thread?includePosts=true" + (location.search != "" ? "&" + location.search.substring(1) : "")
        }).done(function(data, textStatus, jqXHR){
            for (var threadIndex = 0; threadIndex < data.thread.length; threadIndex++) {
                root.find(".oa-forum-threads").append(populateThreadTemplate(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, data.thread[threadIndex]));
            }
            setYoungerAction(getFirst(data.action, function(action) {return action.rel === "younger";}));
            setOlderAction(getFirst(data.action, function(action) {return action.rel === "older";}));
        }).fail(function(jqXHR, textStatus, errorThrown){

        }).always(function(dataOrjqXHR, textStatus, jqXHROrerrorThrown){

        });
    };
    $.fn.flytThread = function(options, url) {
        var root = $(this);
        var contextPath = getOption(options, "contextPath");
        var userProfileUrl = getOption(options, "userProfileUrl");
        var userProfileImageUrl = getOption(options, "userProfileImageUrl");
        var imageUrl = getOption(options, "imageUrl");
        var imagePreviewUrl = getOption(options, "imagePreviewUrl");
        var docUrl = getOption(options, "docUrl");
        var imagePreviewWidth = getOption(options, "imagePreviewWidth");
        var imagePreviewHeight = getOption(options, "imagePreviewHeight");
        root.on("click", ".oa-forum-share-comment", function(event){
            event.preventDefault();
            var element = $(this);
            shareComment(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight);
        });
        root.on("click", ".oa-forum-editPost", function(event){
            event.preventDefault();
            var element = $(this);
            editPost(element);
        });
        root.on("click", ".oa-forum-deletePost", function(event){
            event.preventDefault();
            var element = $(this);
            deletePost(element);
        });
        root.on("click", ".oa-forum-post-cancelEditPost", function(event){
            event.preventDefault();
            var element = $(this);
            cancelEditPost(element);
        });
        root.on("click", ".oa-forum-post-submitEditPost", function(event){
            event.preventDefault();
            var element = $(this);
            submitEditPost(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight);
        });
        root.on("click", ".oa-forum-showReplyForm", function(event){
            event.preventDefault();
            var element = $(this);
            showReplyForm(element);
        });
        root.on("click", ".oa-forum-like", function(event){
            event.preventDefault();
            var element = $(this);
            like(element, options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight);
        });
        root.on("click", ".oa-forum-unlike", function(event){
            event.preventDefault();
            var element = $(this);
            unlike(element);
        });
        root.on("click", ".oa-forum-attachment", function(event) {
            event.preventDefault();
            var element = $(this);
            return viewAttachment(element);
        });
        return $.ajax({
            type: "GET",
            url: url
        }).done(function(data, textStatus, jqXHR){
            root.append(populateThreadTemplate(options, userProfileUrl, userProfileImageUrl, imageUrl, imagePreviewUrl, docUrl, imagePreviewWidth, imagePreviewHeight, data));
        }).fail(function(jqXHR, textStatus, errorThrown){

        }).always(function(dataOrjqXHR, textStatus, jqXHROrerrorThrown){

        });
    };
})(jQuery);

/**
 * Copyright (c) 2011-2013 Felix Gnass
 * Licensed under the MIT license
 */

(function(t,e){if(typeof exports=="object")module.exports=e();else if(typeof define=="function"&&define.amd)define(e);else t.Spinner=e()})(this,function(){"use strict";var t=["webkit","Moz","ms","O"],e={},i;function o(t,e){var i=document.createElement(t||"div"),o;for(o in e)i[o]=e[o];return i}function n(t){for(var e=1,i=arguments.length;e<i;e++)t.appendChild(arguments[e]);return t}var r=function(){var t=o("style",{type:"text/css"});n(document.getElementsByTagName("head")[0],t);return t.sheet||t.styleSheet}();function s(t,o,n,s){var a=["opacity",o,~~(t*100),n,s].join("-"),f=.01+n/s*100,l=Math.max(1-(1-t)/o*(100-f),t),d=i.substring(0,i.indexOf("Animation")).toLowerCase(),u=d&&"-"+d+"-"||"";if(!e[a]){r.insertRule("@"+u+"keyframes "+a+"{"+"0%{opacity:"+l+"}"+f+"%{opacity:"+t+"}"+(f+.01)+"%{opacity:1}"+(f+o)%100+"%{opacity:"+t+"}"+"100%{opacity:"+l+"}"+"}",r.cssRules.length);e[a]=1}return a}function a(e,i){var o=e.style,n,r;if(o[i]!==undefined)return i;i=i.charAt(0).toUpperCase()+i.slice(1);for(r=0;r<t.length;r++){n=t[r]+i;if(o[n]!==undefined)return n}}function f(t,e){for(var i in e)t.style[a(t,i)||i]=e[i];return t}function l(t){for(var e=1;e<arguments.length;e++){var i=arguments[e];for(var o in i)if(t[o]===undefined)t[o]=i[o]}return t}function d(t){var e={x:t.offsetLeft,y:t.offsetTop};while(t=t.offsetParent)e.x+=t.offsetLeft,e.y+=t.offsetTop;return e}var u={lines:12,length:7,width:5,radius:10,rotate:0,corners:1,color:"#000",direction:1,speed:1,trail:100,opacity:1/4,fps:20,zIndex:2e9,className:"spinner",top:"auto",left:"auto",position:"relative"};function p(t){if(typeof this=="undefined")return new p(t);this.opts=l(t||{},p.defaults,u)}p.defaults={};l(p.prototype,{spin:function(t){this.stop();var e=this,n=e.opts,r=e.el=f(o(0,{className:n.className}),{position:n.position,width:0,zIndex:n.zIndex}),s=n.radius+n.length+n.width,a,l;if(t){t.insertBefore(r,t.firstChild||null);l=d(t);a=d(r);f(r,{left:(n.left=="auto"?l.x-a.x+(t.offsetWidth>>1):parseInt(n.left,10)+s)+"px",top:(n.top=="auto"?l.y-a.y+(t.offsetHeight>>1):parseInt(n.top,10)+s)+"px"})}r.setAttribute("role","progressbar");e.lines(r,e.opts);if(!i){var u=0,p=(n.lines-1)*(1-n.direction)/2,c,h=n.fps,m=h/n.speed,y=(1-n.opacity)/(m*n.trail/100),g=m/n.lines;(function v(){u++;for(var t=0;t<n.lines;t++){c=Math.max(1-(u+(n.lines-t)*g)%m*y,n.opacity);e.opacity(r,t*n.direction+p,c,n)}e.timeout=e.el&&setTimeout(v,~~(1e3/h))})()}return e},stop:function(){var t=this.el;if(t){clearTimeout(this.timeout);if(t.parentNode)t.parentNode.removeChild(t);this.el=undefined}return this},lines:function(t,e){var r=0,a=(e.lines-1)*(1-e.direction)/2,l;function d(t,i){return f(o(),{position:"absolute",width:e.length+e.width+"px",height:e.width+"px",background:t,boxShadow:i,transformOrigin:"left",transform:"rotate("+~~(360/e.lines*r+e.rotate)+"deg) translate("+e.radius+"px"+",0)",borderRadius:(e.corners*e.width>>1)+"px"})}for(;r<e.lines;r++){l=f(o(),{position:"absolute",top:1+~(e.width/2)+"px",transform:e.hwaccel?"translate3d(0,0,0)":"",opacity:e.opacity,animation:i&&s(e.opacity,e.trail,a+r*e.direction,e.lines)+" "+1/e.speed+"s linear infinite"});if(e.shadow)n(l,f(d("#000","0 0 4px "+"#000"),{top:2+"px"}));n(t,n(l,d(e.color,"0 0 1px rgba(0,0,0,.1)")))}return t},opacity:function(t,e,i){if(e<t.childNodes.length)t.childNodes[e].style.opacity=i}});function c(){function t(t,e){return o("<"+t+' xmlns="urn:schemas-microsoft.com:vml" class="spin-vml">',e)}r.addRule(".spin-vml","behavior:url(#default#VML)");p.prototype.lines=function(e,i){var o=i.length+i.width,r=2*o;function s(){return f(t("group",{coordsize:r+" "+r,coordorigin:-o+" "+-o}),{width:r,height:r})}var a=-(i.width+i.length)*2+"px",l=f(s(),{position:"absolute",top:a,left:a}),d;function u(e,r,a){n(l,n(f(s(),{rotation:360/i.lines*e+"deg",left:~~r}),n(f(t("roundrect",{arcsize:i.corners}),{width:o,height:i.width,left:i.radius,top:-i.width>>1,filter:a}),t("fill",{color:i.color,opacity:i.opacity}),t("stroke",{opacity:0}))))}if(i.shadow)for(d=1;d<=i.lines;d++)u(d,-2,"progid:DXImageTransform.Microsoft.Blur(pixelradius=2,makeshadow=1,shadowopacity=.3)");for(d=1;d<=i.lines;d++)u(d);return n(e,l)};p.prototype.opacity=function(t,e,i,o){var n=t.firstChild;o=o.shadow&&o.lines||0;if(n&&e+o<n.childNodes.length){n=n.childNodes[e+o];n=n&&n.firstChild;n=n&&n.firstChild;if(n)n.opacity=i}}}var h=f(o("group"),{behavior:"url(#default#VML)"});if(!a(h,"transform")&&h.adj)c();else i=a(h,"animation");return p});