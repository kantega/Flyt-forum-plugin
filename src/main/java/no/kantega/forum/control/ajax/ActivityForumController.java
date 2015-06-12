package no.kantega.forum.control.ajax;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.BasicForumThread;
import no.kantega.forum.model.BasicPost;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
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
        return handleRequest(request, new Function() {
            @Override
            public ResponseEntity<Object> accept(String username, Interval interval) throws Fault {
                List<BasicForumThread> forumThreads = new ArrayList<>();
                for (ForumThread forumThread : forumDao.getThreadsWithActivityInPeriodWhereParticipantHasPosted(username, interval)) {
                    forumThreads.add(new BasicForumThread(forumThread));
                }
                return new ResponseEntity<Object>(forumThreads, HttpStatus.OK);
            }
        });
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
