if (typeof jQuery == 'undefined') {
    alert("jQuery is not loaded! OpenAksess forum requires jQuery to function.");
} else {
    if (!jQuery.fn.prettyDate) {
        alert('The OpenAksess jQuery plugin "prettyDate" is not loaded! OpenAksess forum requires "prettyDate" to function properly.');
    }
    if (!jQuery.fn.ajaxForm) {
        alert('The jQuery plugin "ajaxForm" is not loaded! OpenAksess forum requires "ajaxForm" to function properly.');
    }
}

$(document).ready(function(){

    $('#oa-forum-sharebox .oa-forum-sharefield').elastic();

    // Handles posting of status and photo
    $('#oa-forum-tab-container-status .oa-forum-ajaxForm,  #oa-forum-tab-container-photo .oa-forum-ajaxForm').ajaxForm({
        beforeSubmit: function(arr, $form, options) {
            var containerHeight = $form.parent().height();
            $form.parent().css("height", containerHeight);
            $form.fadeOut(300, function(){
                $('<div class="oa-forum-submit-animation">Submitting...</div>').css("line-height", containerHeight  + "px").css("text-align", "center").hide().appendTo($form.parent()).fadeIn(300);
            });
            return true;
        },
        resetForm: true,
        success: prependNewThread
    });

    // Handles posting of link
    $('#oa-forum-tab-container-link .oa-forum-ajaxForm').ajaxForm({
        beforeSubmit: function(arr, $form, options) {
            // 1
            var $container = $("#oa-forum-tab-container-link");
            $container.css("height", $container.height());

            // 2
            $form.closest(".oa-forum-link-preview-container").fadeOut(300, function(){
                var containerHeightNewForm = $("#oa-forum-linkform-step1").height();

                // 3
                $container.animate({height: containerHeightNewForm + "px"}, 400, function(){
                    // 4
                    var $animation = $('<div class="oa-forum-submit-animation">Submitting...</div>');
                    $animation.css("line-height", containerHeightNewForm  + "px").css("text-align", "center").hide().appendTo($container).fadeIn(300, function(){
                        window.setTimeout(function(){
                            // 5
                            $animation.fadeOut(300, function(){
                                // 6
                                $("#oa-forum-linkform-step1").fadeIn(300);
                            })
                        }, 1200);
                    });
                });
                //$form.closest(".oa-forum-tab-container").animate({height:containerHeight}, 200);
            });
            return true;
        },
        success: prependNewThreadForLink,
        beforeSerialize: function($form, options) {
            var body = '<a href="' + $("#oa-forum-link-ShareUrl").val() + '" class="">' + $("#oa-forum-link-shareTitle").val() + '</a>';
            body += '<p class="oa-forum-fadedText">' + $form.find("input[name='shareurl']").val() + '</p>';
            var comment = $("#oa-forum-link-shareComment").val();
            if (comment != "") {
                body += '<p>' + comment + '</p>';
            }
            $(".oa-forum-link-preview-container input[name=body]").val(body);
        },
        resetForm: true
    });

    // Appends new "shares" to top of wall.
    function prependNewThread(responseText, statusText, xhr, $form)  {
        // Hide animation, show form
        window.setTimeout(function(){     // Timeout to allow the animation to complete
            $(".oa-forum-submit-animation").fadeOut(300, function(){
                $form.fadeIn(300);
                var $newThread = $(responseText);
                $newThread.find(".oa-forum-date").prettyDate({serverTime: serverTime, locale: "en"});
                $newThread.hide().prependTo(".oa-forum-threads").slideDown();
                $(this).remove();
            });
        }, 1200);
    }

// Appends new "shares" to top of wall.
    function prependNewThreadForLink(responseText, statusText, xhr, $form)  {
        // Hide animation, show form
        window.setTimeout(function(){     // Timeout to allow the animation to complete
            $(".oa-forum-submit-animation").fadeOut(300, function(){
                $("#oa-forum-linkform-step1").fadeIn(300);
                var $newThread = $(responseText);
                $newThread.find(".oa-forum-date").prettyDate({serverTime: serverTime, locale: "en"});
                $newThread.hide().prependTo(".oa-forum-threads").slideDown();
                $(this).remove();
            });
        }, 1200);
    }


// Handles shifting between the "share tabs" (status, photo and link)
    $("#oa-forum-newPost .oa-forum-tablink").click(function(e){
        e.preventDefault();
        var index = $(this).parent().prevAll().length -1; // -1 because the first element is the <li> containing "Share:"
        var $tabs = $("#oa-forum-newPost .oa-forum-tab-container");
        $tabs.addClass("oa-forum-hidden");
        $tabs.eq(index).removeClass("oa-forum-hidden");
        return false;
    });

// Handles input field label, hiding and removing these on/out-of focus.
    $(".oa-forum-sharefield").live("focusin", function(){
        var $field = $(this);
        var fieldVal = $field.val().trim();
        var fieldLabel = $field.siblings("label").text();
        if (fieldVal == fieldLabel) {
            $field.val("");
            $field.removeClass("oa-forum-fadedText");
        }
    });
    $(".oa-forum-sharefield").live("focusout", function(){
        var $field = $(this);
        var fieldVal = $field.val().trim();
        var fieldLabel = $field.siblings("label").text();
        if (fieldVal == "") {
            $field.val(fieldLabel);
            $field.addClass("oa-forum-fadedText");
        }
    });

// Handles submitting url, retrieving url details and displaying a view for the returned field.
    var $linkPreviewContainer = $(".oa-forum-link-preview-container");
    $linkPreviewContainer.hide();
    $("#oa-forum-linkform-step1").submit(function(event){
        event.preventDefault();
        var $oaLinkFormStep1 = $(this);
        // Animating posting process
        var containerHeight = $oaLinkFormStep1.parent().height();
        $oaLinkFormStep1.parent().css("height", containerHeight);
        $oaLinkFormStep1.fadeOut(300, function(){
            $('<div class="oa-forum-submit-animation">Submitting...</div>').css("line-height", containerHeight  + "px").hide().appendTo($oaLinkFormStep1.parent()).fadeIn(300);
        });
        var formAction = $oaLinkFormStep1.attr("action");
        var shareUrl = $("#oa-forum-link-shareUrl").val();
        $.getJSON(formAction + shareUrl, function(data) {
            window.setTimeout(function(){
                $linkPreviewContainer.find(".oa-forum-title a").text(data["title"]).attr("href", data["url"]);
                $linkPreviewContainer.find(".oa-forum-shareurl").text(data["url"]);
                $("#oa-forum-linkform-step2").find("input[name='shareurl']").val(data["url"]);
                $("#oa-forum-link-shareTitle").val(data["title"]);
                var $animation = $oaLinkFormStep1.parent().find(".oa-forum-submit-animation");
                $animation.fadeOut(300, function(){
                    var height = $linkPreviewContainer.css("height");
                    $oaLinkFormStep1.parent().animate({height:height}, 200, function(){
                        $linkPreviewContainer.fadeIn(300);
                        $animation.remove();
                        var $inputField = $("#oa-forum-link-shareUrl");
                        $inputField.val($inputField.siblings("label").text());
                    });
                })
            }, 500);
        });
    });

// Handles loading and animation of the wall.
    var $forumContent = $("#oa-forum-forumContent");
    $forumContent.hide();
    var forumWallUrl = contextPath + "/forum/listPosts?forumId=" + forumId + "&numberOfPostsToShow=" + maxthreads;
    $forumContent.load(forumWallUrl, function(responseText, textStatus, XMLHttpRequest){
        $(".oa-forum-date", $forumContent).each(function(){
            $(this).prettyDate({serverTime: serverTime, locale: "en"});
        });
        window.setTimeout(function() { // Pausing for 200 ms for better user experience
            $("#oa-forum-loading-animation").fadeOut(200, function(){
                $forumContent.fadeIn(250);
            });
        },200);
    });

// link listener to display hidden comments in a thread.
    $(".oa-forum-showFullThread").live("click", function(event){
        event.preventDefault();
        $(this).closest(".oa-forum-collapsedComments").hide();
        $(this).closest(".oa-forum-posts").find(".oa-forum-post").removeClass("oa-forum-hidden");
    });

// Handles showing comment form on a thread
    $(".oa-forum-showReplyForm").live("click", function(event){
        event.preventDefault();
        $(this).closest(".oa-forum-thread").find(".oa-forum-reply").removeClass("oa-forum-hidden").find("textarea").focus();
    })

// Handles posting comments and updating gui on a thread
    var enterKeyCode = 13;
    $(".oa-forum-threads .oa-forum-comment-reply").live("keydown", function(event){
        if (event.keyCode == enterKeyCode) {
            event.preventDefault();
            var $commentTextarea = $(this);
            var $commentForm = $commentTextarea.closest("form");
            var commentFormAction = $commentForm.attr("action");
            var comment = $commentTextarea.val().trim();
            $commentTextarea.val("");
            if (comment.length > 0){
                var threadId = $("input[name=threadId]", $commentForm).val();
                var formParams = { threadId:  threadId, body: comment, subject: "subject" };

                // Animating posting process
                var containerHeight = $commentForm.parent().height();
                $commentForm.parent().css("height", containerHeight);
                $commentForm.fadeOut(300, function(){
                    $('<div class="oa-forum-submit-animation">Submitting...</div>').css("line-height", containerHeight  + "px").hide().appendTo($commentForm.parent()).fadeIn(300);
                });

                $.post(commentFormAction, formParams, function(data) {
                    window.setTimeout(function(){
                        var $newPost = $(data);
                        $newPost.find(".oa-forum-date").prettyDate({serverTime: serverTime, locale: "en"});
                        $newPost.hide();
                        $newPost.appendTo($commentTextarea.closest(".oa-forum-thread").find(".oa-forum-posts"));
                        var $animation = $commentForm.parent().find(".oa-forum-submit-animation");
                        $animation.fadeOut(300, function(){
                            $commentForm.fadeIn(300);
                            $newPost.slideDown();
                            $animation.remove();
                        })
                    }, 1200);
                });
            }
        }
    });
});