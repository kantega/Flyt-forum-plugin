package no.kantega.forum.control.ajax;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Attachment;
import no.kantega.forum.model.BasicPost;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-11
 */
@Controller
@RequestMapping("/activity")
public class ActivityForumController {

    private static final DateTimeFormatter ISO_DATE_TIME = ISODateTimeFormat.dateTimeParser();
    private static final String BASE_NAME = ActivityForumController.class.getSimpleName();

    private ForumDao forumDao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    private ResponseEntity<Object> handleRequest(HttpServletRequest request, Function function) {
        try {
            String username = getUsername(request, userResolver);
            if (username == null) {
                throw new Fault(403, getLocalizedMessage(BASE_NAME, "forbidden"));
            }

            Instant from = toInstant(request.getParameter("from"), ISO_DATE_TIME, null);
            Instant to = toInstant(request.getParameter("to"), ISO_DATE_TIME, Instant.now());
            if (from == null) {
                throw new Fault(400, getLocalizedMessage(BASE_NAME, "parameter.mandatory", "from"));
            }

            return function.accept(username, new Interval(from, to));
        } catch (Fault fault) {
            return handleFault(fault);
        }

    }

    @RequestMapping(value = "/postCount", method = RequestMethod.GET)
    public ResponseEntity<Object> postCount(HttpServletRequest request) {
        return handleRequest(request, new Function() {
            @Override
            public ResponseEntity<Object> accept(String username, Interval interval) throws Fault {
                return new ResponseEntity<Object>(forumDao.getPostCountInThreadsWithActivityInPeriodWhereParticipantHasPosted(username, interval), HttpStatus.OK);
            }
        });
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ResponseEntity<Object> posts(HttpServletRequest request) {
        return handleRequest(request, new Function() {
            @Override
            public ResponseEntity<Object> accept(String username, Interval interval) throws Fault {
                List<BasicPost> posts = new ArrayList<>();
                for (Post post : forumDao.getPostsInThreadsWithActivityInPeriodWhereParticipantHasPosted(username, interval)) {
                    posts.add(new BasicPost(post));
                }
                return new ResponseEntity<Object>(posts, HttpStatus.OK);
            }
        });
    }

    @RequestMapping(value = "/threadCount", method = RequestMethod.GET)
    public ResponseEntity<Object> threadCount(HttpServletRequest request) {
        return handleRequest(request, new Function() {
            @Override
            public ResponseEntity<Object> accept(String username, Interval interval) throws Fault {
                return new ResponseEntity<Object>(forumDao.getThreadCountWithActivityInPeriodWhereParticipantHasPosted(username, interval), HttpStatus.OK);
            }
        });
    }

    @RequestMapping(value = "/threads", method = RequestMethod.GET)
    public ResponseEntity<Object> threads(HttpServletRequest request) {
        try {
            String username = getUsername(request, userResolver);
            if (username == null) {
                throw new Fault(403, getLocalizedMessage(BASE_NAME, "forbidden"));
            }

            Instant from = toInstant(request.getParameter("from"), ISO_DATE_TIME, Instant.now());
            Instant to = toInstant(request.getParameter("to"), ISO_DATE_TIME, Instant.now());

            Integer skip = toInteger(request.getParameter("skip"), 0);
            Integer top = toInteger(request.getParameter("top"), Integer.MAX_VALUE);

            Interval interval = new Interval(from, to);
            List<Map<String,Object>> forumThreads = new ArrayList<>();
            List<Long> activityThreadIds = forumDao.getThreadsWithActivityInPeriodWhereParticipantHasPosted(username, interval);

            for (ForumThread forumThread : forumDao.getThreads(username, skip, top)) {
                if (permissionManager.hasPermission(username, Permission.VIEW, forumThread)) {
                    Map<String, Object> basicForumThread = getBasicThread(forumThread);
                    basicForumThread.put("firstPost", getFirstPostInThread(forumThread));
                    basicForumThread.put("activity", activityThreadIds.contains(forumThread.getId()));
                    forumThreads.add(basicForumThread);
                }
            }
            return new ResponseEntity<Object>(forumThreads, HttpStatus.OK);
        } catch (Fault fault) {
            return handleFault(fault);
        }
    }

    private Integer toInteger(String value, int _default) {
        if (value != null) {
            try {
                _default = Integer.parseInt(value);
            } catch (NumberFormatException cause) {
                throw new Fault(400, getLocalizedMessage(BASE_NAME, "integer.parse.NumberFormatException", value));
            } catch (Exception cause) {
                throw new Fault(500, getLocalizedMessage(BASE_NAME, "integer.parse.Exception", value));
            }
        }
        return _default;
    }

    @RequestMapping(value = "/thread", method = RequestMethod.GET)
    public ResponseEntity<Object> thread(HttpServletRequest request) {
        try {
            String username = getUsername(request, userResolver);
            if (username == null) {
                throw new Fault(403, getLocalizedMessage(BASE_NAME, "forbidden"));
            }
            Long threadId = toLong(request.getParameter("threadId"), null);
            if (threadId == null) {
                throw new Fault(400, getLocalizedMessage(BASE_NAME, "parameter.mandatory", "threadId"));
            }
            ForumThread forumThread = forumDao.getPopulatedThread(threadId);
            if (forumThread == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!permissionManager.hasPermission(username, Permission.VIEW, forumThread)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Map<String, Object> basicForumThread = getBasicThread(forumThread);
            basicForumThread.put("firstPost", getFirstPostInThread(forumThread));
            return new ResponseEntity<Object>(basicForumThread, HttpStatus.OK);
        } catch (Fault fault) {
            return handleFault(fault);
        }
    }

    @RequestMapping(value = "/latestTimestamp", method = RequestMethod.GET)
    public ResponseEntity<Object> latestTimestamp(HttpServletRequest request) {
        try {
            String username = getUsername(request, userResolver);
            if (username == null) {
                throw new Fault(403, getLocalizedMessage(BASE_NAME, "forbidden"));
            }
            Instant latestPostDate = forumDao.getLatestPostDateOfPosts(username);
            if (latestPostDate == null) { // No posts were made by this user
                return new  ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
            Instant latestModifiedDate = forumDao.getLatestModifiedDateTimeOfPosts(username);
            Instant latestTimestamp = null;
            if (latestModifiedDate == null) { // No posts were edited by this user
                    latestTimestamp = latestPostDate;
            } else if (latestModifiedDate.isAfter(latestPostDate)) {
                latestTimestamp = latestModifiedDate;
            } else {
                latestTimestamp = latestPostDate;
            }
            return new ResponseEntity<Object>(toString(latestTimestamp), HttpStatus.OK);
        } catch (Fault fault) {
            return handleFault(fault);
        }
    }

    private ResponseEntity<Object> handleFault(Fault fault) {
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("messages", fault.getMessages());
        return new ResponseEntity<Object>(model, HttpStatus.valueOf(fault.getStatusCode()));
    }

    public void setForumDao(ForumDao forumDao) {
        this.forumDao = forumDao;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    private String getUsername(HttpServletRequest request, UserResolver userResolver) {
        String username = null;
        if (request != null) {
            if (userResolver != null) {
                ResolvedUser user = userResolver.resolveUser(request);
                if (user != null) {
                    username = user.getUsername();
                }
            }
        }
        return username;
    }

    /**
     *
     * @param value The value
     * @param dateTimeFormatter The date-time formatter
     * @param _default The default
     * @return the parsed date-time, else _default
     * @throws no.kantega.forum.control.ajax.ActivityForumController.Fault
     */
    private Instant toInstant(String value, DateTimeFormatter dateTimeFormatter, Instant _default) throws Fault {
        if (value != null) {
            try {
                if (dateTimeFormatter != null) {
                    _default = Instant.parse(value, dateTimeFormatter);
                } else {
                    _default = Instant.parse(value);
                }
            } catch (IllegalArgumentException cause) {
                throw new Fault(400, getLocalizedMessage(BASE_NAME, "instant.parse.IllegalArgumentException", value), cause);
            } catch (Exception cause) {
                throw new Fault(500, getLocalizedMessage(BASE_NAME, "instant.parse.Exception", value), cause);
            }
        }
        return _default;
    }

    private Long toLong(String value, Long _default) {
        if (value != null) {
            try {
                _default = Long.parseLong(value);
            } catch (NumberFormatException cause) {
                throw new Fault(400, getLocalizedMessage(BASE_NAME, "long.parse.NumberFormatException", value));
            } catch (Exception cause) {
                throw new Fault(500, getLocalizedMessage(BASE_NAME, "long.parse.Exception", value));
            }
        }
        return _default;
    }

    private String toString(Instant instant) {
        return instant != null ? String.format("\"%s\"", instant.toString()) : null;
    }

    private String getLocalizedMessage(String baseName, String key, String... arguments) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName);
            return String.format(resourceBundle.getString(key), arguments);
        } catch (Exception cause) {}
        try {
            return String.format(key, arguments);
        } catch (Exception cause2) {
            return key;
        }
    }

    private Map<String, Object> getBasicThread(ForumThread forumThread) {
        Map<String,Object> model = new LinkedHashMap<>();
        model.put("id", forumThread.getId());
        model.put("name", forumThread.getName());
        model.put("description", forumThread.getDescription());
        Map<String,Object> basicForum = null;
        try {
            basicForum = getBasicForum(forumThread, forumThread.getForum());
        } catch (Exception cause) {

        }

        model.put("forum", basicForum);
        model.put("createdDate", forumThread.getCreatedDate());
        model.put("numPosts", forumThread.getNumPosts());
        Map<String,Object> lastBasicPost = null;
        try {
            lastBasicPost = getBasicPost(forumThread, forumThread.getLastPost());
        } catch (Exception cause) {}
        model.put("lastPost", lastBasicPost);
        List<Map<String,Object>> basicPosts = null;
        try {
            for (Post post : forumThread.getPosts()) {
                if (basicPosts == null) {
                    basicPosts = new ArrayList<>();
                }
                Map<String, Object> basicPost = getBasicPost(forumThread, post);
                basicPosts.add(basicPost);
            }
        } catch (Exception cause) {}
        model.put("posts", basicPosts);
        model.put("owner", forumThread.getOwner());
        model.put("contentId", forumThread.getContentId());
        model.put("isApproved", forumThread.isApproved());
        model.put("numNewPosts", forumThread.getNumNewPosts());
        model.put("lastPostDate", forumThread.getLastPost());
        model.put("modifiedDate", forumThread.getModifiedDate());
        return model;
    }

    private Map<String, Object> getBasicForum(ForumThread forumThread, Forum forum) {
        Map<String,Object> basicForum = new LinkedHashMap<>();
        basicForum.put("id", forum.getId());
        Map<String,Object> basicForumCategory = null;
        try {
            basicForumCategory = getBasicForumCategory(forum, forum.getForumCategory());
        } catch (Exception cause) {}
        basicForum.put("forumCategory", basicForumCategory);
        basicForum.put("name", forum.getName());
        basicForum.put("description", forum.getDescription());
        basicForum.put("numThreads", forum.getNumThreads());
        basicForum.put("createdDate", forum.getCreatedDate());
        Map<String,Object> lastBasicPost = null;
        try {
            lastBasicPost = getBasicPost(forumThread, forum.getLastPost());
        } catch (Exception cause) {}
        basicForum.put("lastPost", lastBasicPost);
        basicForum.put("owner", forum.getOwner());
        List<Long> threadIds = null;
        try {
            for (ForumThread ft : forum.getThreads()) {
                if (threadIds == null) {
                    threadIds = new ArrayList<>();
                }
                threadIds.add(ft.getId());
            }
        } catch (Exception cause) {}
        basicForum.put("threads", threadIds);
        basicForum.put("anonymousPostAllowed", forum.isAnonymousPostAllowed());
        basicForum.put("attachmentsAllowed", forum.isAttachmentsAllowed());
        basicForum.put("moderator", forum.getModerator());
        List<String> groups = null;
        try {
            for (String group : forum.getGroups()) {
                if (groups == null) {
                    groups = new ArrayList<>();
                }
                groups.add(group);
            }
        } catch (Exception cause) {}
        basicForum.put("groups", groups);
        basicForum.put("numNewPosts", forum.getNumThreads());
        return basicForum;
    }

    private Map<String, Object> getBasicForumCategory(Forum forum, ForumCategory forumCategory) {
        Map<String,Object> basicForumCategory = new LinkedHashMap<>();
        basicForumCategory.put("id", forumCategory.getId());
        basicForumCategory.put("name", forumCategory.getName());
        basicForumCategory.put("description", forumCategory.getDescription());
        basicForumCategory.put("numForums", forumCategory.getNumForums());
        basicForumCategory.put("createdDate", forumCategory.getCreatedDate());
        basicForumCategory.put("owner", forumCategory.getOwner());
        List<Long> forumIds = null;
        try {
            for (Forum f : forumCategory.getForums()) {
                if (forumIds == null) {
                    forumIds = new ArrayList<>();
                }
                forumIds.add(f.getId());
            }
        } catch (Exception cause) {}
        basicForumCategory.put("forums", forumIds);
        return basicForumCategory;
    }

    private Map<String, Object> getBasicPost(ForumThread forumThread, Post post) {
        Map<String,Object> basicPost = new LinkedHashMap<>();
        basicPost.put("id", post.getId());
        try {
            basicPost.put("thread", forumThread.getId());
        } catch (Exception cause) {}

        try {
            basicPost.put("replyToId", post.getReplyToId());
        } catch (Exception cause) {}
        basicPost.put("subject", post.getSubject());
        basicPost.put("body", post.getBody());
        basicPost.put("owner", post.getOwner());
        basicPost.put("postDate", post.getPostDate());
        List<Map<String,Object>> basicAttatchments = null;
        try {
            for (Attachment attachment : post.getAttachments()) {
                if (basicAttatchments == null) {
                    basicAttatchments = new ArrayList<>();
                }
                Map<String, Object> basicAttachment = getBasicAttachment(post, attachment);
                basicAttatchments.add(basicAttachment);
            }
        } catch (Exception cause) {}
        basicPost.put("attachments", basicAttatchments);
        basicPost.put("author", post.getAuthor());
        basicPost.put("approved", post.isApproved());
        basicPost.put("ratingScore", post.getRatingScore());
        basicPost.put("numberOfRatings", post.getNumberOfRatings());
        basicPost.put("modifiedDate", post.getModifiedDate());
        return basicPost;
    }

    private Map<String, Object> getBasicAttachment(Post post, Attachment attachment) {
        Map<String,Object> basicAttachment = new LinkedHashMap<>();
        basicAttachment.put("id", attachment.getId());
        basicAttachment.put("post", post.getId());
        basicAttachment.put("fileName", attachment.getFileName());
        basicAttachment.put("fileSize", attachment.getFileSize());
        basicAttachment.put("mimeType", attachment.getMimeType());
        //basicAttachment.put("data", attachment.getData());
        basicAttachment.put("created", attachment.getCreated());
        return basicAttachment;
    }

    private Map<String, Object> getFirstPostInThread(ForumThread forumThread) {
        Map<String,Object> firstBasicPost = null;
        try {
            Post post = forumDao.getFirstPostInThread(forumThread.getId());
            firstBasicPost = getBasicPost(forumThread, post);
        } catch (Exception cause) {}
        return firstBasicPost;
    }

    private class Fault extends RuntimeException {

        private final int statusCode;

        public Fault(int statusCode) {
            this.statusCode = statusCode;
        }

        public Fault(int statusCode, String message) {
            super(message);
            this.statusCode = statusCode;
        }

        public Fault(int statusCode, String message, Throwable cause) {
            super(message, cause);
            this.statusCode = statusCode;
        }

        public Fault(int statusCode, Throwable cause) {
            super(cause);
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public List<String> getMessages() {
            List<String> messages = new ArrayList<>();
            if (getMessage() != null) {
                messages.add(getMessage());
            }
            Throwable cause = getCause();
            while (cause != null) {
                if (cause.getMessage() != null) {
                    messages.add(cause.getMessage());
                }
                cause = cause.getCause();
            }
            return messages;
        }
    }

    private interface Function {

        ResponseEntity<Object> accept(String username, Interval interval) throws Fault;
    }
}
