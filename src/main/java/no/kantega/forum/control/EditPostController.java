package no.kantega.forum.control;

import no.kantega.commons.client.util.RequestParameters;
import no.kantega.commons.exception.ConfigurationException;
import no.kantega.commons.exception.NotAuthorizedException;
import no.kantega.commons.media.ImageInfo;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Attachment;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.service.ForumPostService;
import no.kantega.forum.util.ForumUtil;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserProfile;
import no.kantega.modules.user.UserProfileManager;
import no.kantega.modules.user.UserResolver;
import no.kantega.publishing.api.configuration.SystemConfiguration;
import no.kantega.publishing.api.content.ContentIdentifier;
import no.kantega.publishing.common.Aksess;
import no.kantega.publishing.common.data.Content;
import no.kantega.publishing.common.data.Multimedia;
import no.kantega.publishing.common.service.ContentManagementService;
import no.kantega.publishing.modules.mailsender.MailSender;
import no.kantega.publishing.multimedia.ImageEditor;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class EditPostController extends AbstractForumFormController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String defaultAllowedFileextensionsString = "png,jpg,jpeg,gif,bmp";
    public static final String allowedFileextensionKey = "forum.attachment.allowedfileextensions";

    private ForumDao dao;
    private UserProfileManager userProfileManager;

    private int defaultMaxImageWidth = 1024;
    private int defaultMaxImageHeight = 768;
    private String imageFormat = "jpg";
    private ForumPostService service;
    private ImageEditor imageEditor;
    private SystemConfiguration configuration;
    private final PolicyFactory sanitizePolicy = new HtmlPolicyBuilder()
            .allowElements("blockquote", "b", "br", "strong", "p", "a")
            .allowUrlProtocols("http", "https")
            .allowAttributes("href").onElements("a")
            .requireRelNofollowOnLinks()
            .toFactory();

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        RequestParameters param = new RequestParameters(request);
        long threadId = param.getLong("threadId");
        long forumId = param.getLong("forumId");
        int contentId = param.getInt("contentId");

        if(threadId != -1) {
            ForumThread thread = dao.getThread(threadId);
            return permissions(Permission.POST_IN_THREAD, thread);
        } else if (forumId != -1) {
            Forum forum = dao.getForum(forumId);
            return permissions(Permission.ADD_THREAD, forum);
        } else if (contentId != -1) {
            long tId = dao.getThreadAboutContent(contentId);
            if (tId > 0) {
                ForumThread thread = dao.getThread(tId);
                return permissions(Permission.POST_IN_THREAD, thread);
            } else {
                ContentManagementService cms = new ContentManagementService(request);
                ContentIdentifier cid = ContentIdentifier.fromContentId(contentId);
                try {
                    Content content = cms.getContent(cid);
                    if (content != null && content.getForumId() > 0) {
                        Forum f = dao.getForum(content.getForumId());
                        return permissions(Permission.ADD_THREAD, f);
                    }

                } catch (NotAuthorizedException e) {
                    log.error("Content has no forum defined" + contentId);
                }
                return null;
            }
        } else {
            long id = Long.parseLong(request.getParameter("postId"));
            Post p = dao.getPost(id);
            return permissions(Permission.EDIT_POST, p);
        }
    }

    private final Pattern startBlockquote = Pattern.compile("<blockquote>");
    private final Pattern endBlockquote = Pattern.compile("</blockquote>");

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        RequestParameters param = new RequestParameters(request);
        long postId = param.getLong("postId");

        Locale locale = RequestContextUtils.getLocale(request);
        if (postId != -1) {
            Post post = dao.getPopulatedPost(postId);
            if (post != null) {
                String body = post.getBody();
                String qStart = getApplicationContext().getMessage("post.quote.starttag", new Object[0], locale);
                String qEnd = getApplicationContext().getMessage("post.quote.endtag", new Object[0], locale);

                body = startBlockquote.matcher(body).replaceAll(qStart);
                body = endBlockquote.matcher(body).replaceAll(qEnd);
                post.setBody(body);
            }
            return post;
        } else {
            long threadId = param.getLong("threadId");
            long replyId = param.getLong("replyId");

            ForumThread t = null;
            if(threadId != -1) {
                t = dao.getThread(threadId);
            } else {
                t = getThreadFromParams(request);
            }

            Post p = new Post();

            ResolvedUser user = userResolver.resolveUser(request);
            if (user != null) {
                p.setOwner(user.getUsername());
                UserProfile userProfile = userProfileManager.getUserProfile(user.getUsername());
                p.setAuthor(userProfile.getFullName());
            }
            p.setPostDate(new Date());
            p.setThread(t);

            // Angi om det trengs å godkjenne innlegget eller ikke
            String username = null;
            if (user != null) {
                username = user.getUsername();
            }
            boolean approved = permissionManager.hasPermission(username, Permission.APPROVE_POST, p);
            p.setApproved(approved);

            if (replyId != -1) {
                String qStart = getApplicationContext().getMessage("post.quote.starttag", new Object[0], locale);
                String qEnd = getApplicationContext().getMessage("post.quote.endtag", new Object[0], locale);
                String qWrote = getApplicationContext().getMessage("post.quote.wrote", new Object[0], locale);

                p.setReplyToId(replyId);
                Post origPost = dao.getPost(replyId);
                if(origPost.getSubject().startsWith("SV:")) {
                    p.setSubject(origPost.getSubject());
                } else {
                    p.setSubject("SV: " + origPost.getSubject());
                }

                String qStr = "\n" + origPost.getAuthor() + " " + qWrote + ":\n\n";

                String body = origPost.getBody();
                body = startBlockquote.matcher(body).replaceAll(qStart);
                body = endBlockquote.matcher(body).replaceAll(qEnd);

                int qEndInx = body.lastIndexOf(qEnd);
                if (qEndInx != -1) {
                    body = body.substring(0, qEndInx + qEnd.length()) + qStr + body.substring(qEndInx + qEnd.length(), body.length());
                } else {
                    body = qStr + body;
                }

                p.setBody(qStart + body + qEnd + "\n");
            }
            return p;
        }
    }

    private ForumThread getThreadFromParams(HttpServletRequest request) throws NotAuthorizedException {
        RequestParameters param = new RequestParameters(request);
        int contentId = param.getInt("contentId");
        long forumId = param.getLong("forumId");

        ResolvedUser user = userResolver.resolveUser(request);

        ForumThread t = null;
        if (contentId != -1) {
            long tId = dao.getThreadAboutContent(contentId);
            if (tId > 0) {
                // Thread exists for this content
                t = dao.getThread(tId);
            } else {
                // Create thread for content
                ContentManagementService cms = new ContentManagementService(request);
                ContentIdentifier cid = ContentIdentifier.fromContentId(contentId);
                Content content = cms.getContent(cid);
                if (content != null && content.getForumId() > 0) {
                    Forum f = dao.getForum(content.getForumId());
                    t = new ForumThread();
                    t.setCreatedDate(new Date());
                    if (user != null) {
                        t.setOwner(user.getUsername());
                    }
                    t.setContentId(contentId);

                    t.setForum(f);
                } else {
                    log.error("Content does not exists or has no forum defined:" + contentId);
                }
            }
        } else {
            Forum f = dao.getForum(forumId);
            t = new ForumThread();
            t.setCreatedDate(new Date());
            if (user != null) {
                t.setOwner(user.getUsername());
            }
            int wallcontentid = param.getInt("wallcontentid");
            if(wallcontentid != -1){
                t.setContentId(wallcontentid);
            }
            t.setForum(f);
        }
        return t;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        Date now = new Date();

        Post p = (Post) object;
        if (ForumUtil.isSpam(request)) {
            return new ModelAndView("nospam");
        }

        if (p != null) {
            p.setModifiedDate(now);
        }

        ResolvedUser user = userResolver.resolveUser(request);

        p.setBody(cleanup(p.getBody(), request));

        // Sett til godkjent dersom bruker har rettighet til dette
        String username = null;
        if (user != null) {
            username = user.getUsername();
        }

        boolean approved = permissionManager.hasPermission(username, Permission.APPROVE_POST, p);

        boolean isNewThread = p.getThread().isNew();

        if (isNewThread) {
            p.getThread().setName(p.getSubject());
            p.getThread().setNumPosts(0);
            p.getThread().setApproved(approved);
            p.getThread().setModifiedDate(now);
            dao.saveOrUpdate(p.getThread());
        }

        p.setApproved(approved);

        p = service.saveOrUpdate(p);

        // Send varsling til moderator om nytt innlegg
        String moderator = p.getThread().getForum().getModerator();
        if (!p.isApproved() && moderator != null && moderator.length() > 0) {
            UserProfile profile = userProfileManager.getUserProfile(moderator);
            if (profile != null && profile.getEmail() != null && profile.getEmail().contains("@")) {
                String from = configuration.getString("mail.from");
                if (from == null) {
                    throw new ConfigurationException("mail.from");
                }

                Map<String, Object> param = new HashMap<>();
                param.put("baseurl", Aksess.getApplicationUrl());
                param.put("post", p);

                try {
                    log.debug("Sender varsel om nytt innlegg til " + profile.getEmail());
                    MailSender.send(from, profile.getEmail(), "Forum, nytt innlegg:" + p.getSubject(), "forum-newpost.vm", param);
                } catch (Exception e) {
                    log.error("Feil ved utsending av mail", e);
                }
            } else {
                log.info("Fant ikke brukerprofil/epost for " + moderator);
            }
        }


        Map<String, Object> map = new HashMap<>();

        RequestParameters param = new RequestParameters(request);
        String redirect = param.getString("redirect");
        if (redirect != null && redirect.startsWith("/")) {
            return new ModelAndView(new RedirectView(redirect));
        } else if (isAjaxRequest(request)) {
            int hiddenForumId = param.getInt("hiddenForumId");
            map.put("hiddenForumId", hiddenForumId);
            if (isNewThread) {
                ForumThread t = p.getThread();
                t.setPosts(new TreeSet<>());
                t.getPosts().add(p);
                map.put("thread", t);
                return new ModelAndView("wall/ajax-thread", map);
            } else {
                map.put("post", p);
                return new ModelAndView("wall/ajax-post", map);
            }
        } else if (p.isApproved()) {
            // Vis tr�den hvis innlegget er godkjent
            map.put("threadId", p.getThread().getId());
            map.put("postId", p.getId());
            return new ModelAndView(new RedirectView("viewthread"), map);
        } else {
            // Vis innlegget hvis det ikke er godkjent
            map.put("postId", p.getId());
            return new ModelAndView(new RedirectView("viewpost"), map);
        }
    }

    /**
     * Handles file attachments, shrinks images if they are to large
     */
    protected void onBind(HttpServletRequest request, Object object) throws Exception {
        Post post = (Post)object;

        Set<Attachment> attachments = post.getAttachments();
        if (attachments == null) {
            attachments = new HashSet<>();
        }
        if(request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            int fileNo = 1;
            MultipartFile file = mRequest.getFile("attachment" + fileNo);
            Date now = new Date();
            while (file != null && file.getSize() > 0) {
                String mimeType = file.getContentType();
                if (mimeType.contains("image") || isAnAllowedFileExtension(file.getOriginalFilename())) {
                    Attachment attachment = new Attachment();
                    attachment.setCreated(now);
                    byte[] bytes = file.getBytes();
                    long size = file.getSize();
                    String filename = file.getOriginalFilename();
                    attachment.setFileName(filename);
                    if (attachment.isImage()) {

                        ImageInfo ii = new ImageInfo();
                        ii.setInput(new ByteArrayInputStream(bytes));

                        // Automatically shrink images to smaller size if needed
                        if (ii.check()) {
                            int width = ii.getWidth();
                            int height = ii.getHeight();

                            int maxImageWidth = configuration.getInt("forum.maxImageWidth", this.defaultMaxImageWidth);
                            int maxImageHeight = configuration.getInt("forum.maxImageHeight", this.defaultMaxImageHeight);
                            if (width > maxImageWidth && height > maxImageHeight) {
                                Multimedia source = new Multimedia();
                                source.setData(bytes);
                                source.setFilename(attachment.getFileName());
                                Multimedia multimedia = imageEditor.resizeMultimedia(source, maxImageWidth, maxImageHeight);

                                bytes = multimedia.getData();
                                size = bytes.length;
                                if (filename.contains(".")) {
                                    filename = filename.substring(0, filename.lastIndexOf(".")) + "." + imageFormat;
                                }
                            }
                        }
                    }

                    attachment.setPost(post);
                    attachment.setData(bytes);
                    attachment.setFileName(filename);
                    attachment.setFileSize(size);
                    attachment.setMimeType(mimeType);
                    attachments.add(attachment);
                }
                fileNo++;
                file = mRequest.getFile("attachment" + fileNo);
            }
        }
        post.setAttachments(attachments);
    }

    private boolean isAnAllowedFileExtension(String originalFilename) {
        String[] allowedFileExtensions = configuration.getString(allowedFileextensionKey, defaultAllowedFileextensionsString).split(",");
        for (String fileExtension : allowedFileExtensions) {
            if (originalFilename.endsWith(fileExtension)) {
                return true;
            }
        }

        return false;
    }

    private String cleanup(String body, HttpServletRequest request) {
        String qStart = getApplicationContext().getMessage("post.quote.starttag", new Object[0], RequestContextUtils.getLocale(request));
        String qEnd = getApplicationContext().getMessage("post.quote.endtag", new Object[0], RequestContextUtils.getLocale(request));
        body = startBlockquote.matcher(body).replaceAll(qStart);
        body = endBlockquote.matcher(body).replaceAll(qEnd);

        return sanitizePolicy.sanitize(body);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || request.getParameter("ajax") != null;
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public void setForumPostService(ForumPostService forumPostService) {
        this.service = forumPostService;
    }

    public void setImageEditor(ImageEditor imageEditor) {
        this.imageEditor = imageEditor;
    }

    public void setConfiguration(SystemConfiguration configuration) {
        this.configuration = configuration;
    }
}
