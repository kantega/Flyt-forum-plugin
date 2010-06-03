package no.kantega.forum.control;

import no.kantega.commons.configuration.Configuration;
import no.kantega.commons.exception.ConfigurationException;
import no.kantega.commons.exception.NotAuthorizedException;
import no.kantega.commons.log.Log;
import no.kantega.commons.media.ImageInfo;
import no.kantega.commons.client.util.RequestParameters;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Attachment;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.PermissionObject;
import no.kantega.forum.permission.Permissions;
import no.kantega.forum.util.ForumUtil;
import no.kantega.forum.util.ImageHelper;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserProfile;
import no.kantega.modules.user.UserProfileManager;
import no.kantega.modules.user.UserResolver;
import no.kantega.publishing.common.Aksess;
import no.kantega.publishing.common.data.Content;
import no.kantega.publishing.common.data.ContentIdentifier;
import no.kantega.publishing.common.service.ContentManagementService;
import no.kantega.publishing.modules.mailsender.MailSender;
import no.kantega.publishing.api.rating.RatingNotificationListener;
import no.kantega.publishing.api.comments.CommentNotificationListener;
import no.kantega.publishing.api.comments.CommentNotification;
import no.kantega.publishing.spring.RootContext;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.cyberneko.html.parsers.SAXParser;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 12:11:54
 * To change this template use File | Settings | File Templates.
 */
public class EditPostController extends AbstractForumFormController {
    private static final String SOURCE = "EditPostController";

    private ForumDao dao;
    private UserProfileManager userProfileManager;

    private int maxImageWidth = 1024;
    private int maxImageHeight = 768;
    private String imageFormat = "jpg";

    public PermissionObject[] getRequiredPermissions(HttpServletRequest request) {
        RequestParameters param = new RequestParameters(request);
        long threadId = param.getLong("threadId");
        long forumId = param.getLong("forumId");
        int contentId = param.getInt("contentId");

        if(threadId != -1) {
            ForumThread thread = dao.getThread(threadId);
            return permissions(Permissions.POST_IN_THREAD, thread);
        } else if (forumId != -1) {
            Forum forum = dao.getForum(forumId);
            return permissions(Permissions.ADD_THREAD, forum);
        } else if (contentId != -1) {
            long tId = dao.getThreadAboutContent(contentId);
            if (tId > 0) {
                ForumThread thread = dao.getThread(tId);
                return permissions(Permissions.POST_IN_THREAD, thread);
            } else {
                ContentManagementService cms = new ContentManagementService(request);
                ContentIdentifier cid = new ContentIdentifier();
                cid.setContentId(contentId);
                Content content = null;
                try {
                    content = cms.getContent(cid);
                    if (content != null && content.getForumId() > 0) {
                        Forum f = dao.getForum(content.getForumId());
                        return permissions(Permissions.ADD_THREAD, f);
                    }

                } catch (NotAuthorizedException e) {
                    Log.error(this.getClass().getName(), "Content has no forum defined" + contentId, null, null);
                }
                return null;
            }
        } else {
            long id = Long.parseLong(request.getParameter("postId"));
            Post p = dao.getPost(id);
            return permissions(Permissions.EDIT_POST, p);
        }
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        RequestParameters param = new RequestParameters(request);
        long postId = param.getLong("postId");

        if (postId != -1) {
            Post post = dao.getPopulatedPost(postId);
            if (post != null) {
                String body = post.getBody();
                String qStart = getApplicationContext().getMessage("post.quote.starttag", new Object[0], RequestContextUtils.getLocale(request));
                String qEnd = getApplicationContext().getMessage("post.quote.endtag", new Object[0], RequestContextUtils.getLocale(request));

                body = body.replaceAll("<blockquote>", qStart);
                body = body.replaceAll("</blockquote>", qEnd);
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
            boolean approved = permissionManager.hasPermission(username, Permissions.APPROVE_POST, p);
            p.setApproved(approved);


            if (replyId != -1) {
                String qStart = getApplicationContext().getMessage("post.quote.starttag", new Object[0], RequestContextUtils.getLocale(request));
                String qEnd = getApplicationContext().getMessage("post.quote.endtag", new Object[0], RequestContextUtils.getLocale(request));
                String qWrote = getApplicationContext().getMessage("post.quote.wrote", new Object[0], RequestContextUtils.getLocale(request));

                p.setReplyToId(replyId);
                Post origPost = dao.getPost(replyId);
                if(origPost.getSubject().startsWith("SV:")) {
                    p.setSubject(origPost.getSubject());
                } else {
                    p.setSubject("SV: " + origPost.getSubject());
                }

                String qStr = "\n" + origPost.getAuthor() + " " + qWrote + ":\n\n";

                String body = origPost.getBody();
                body = body.replaceAll("<blockquote>", qStart).replaceAll("</blockquote>", qEnd);

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

        ForumThread t = null;
        if (contentId != -1) {
            long tId = dao.getThreadAboutContent(contentId);
            if (tId > 0) {
                // Thread exists for this content
                t = dao.getThread(tId);
            } else {
                // Create thread for content
                ContentManagementService cms = new ContentManagementService(request);
                ContentIdentifier cid = new ContentIdentifier();
                cid.setContentId(contentId);
                Content content = cms.getContent(cid);
                if (content != null && content.getForumId() > 0) {
                    Forum f = dao.getForum(content.getForumId());
                    t = new ForumThread();
                    t.setTopics(getTopicsFromRequest(request));
                    t.setContentId(contentId);
                    t.setForum(f);
                } else {
                    Log.error(this.getClass().getName(), "Content does not exists or has no forum defined:" + contentId, null, null);
                }
            }
        } else {
            Forum f = dao.getForum(forumId);
            t = new ForumThread();
            t.setTopics(getTopicsFromRequest(request));
            t.setForum(f);
        }
        return t;
    }

    private Set getTopicsFromRequest(HttpServletRequest request) {
        Set<String> topics = new TreeSet<String>();
        String[] topicArray = request.getParameterValues("topic");
        if (topicArray != null ){
            for (String t : topicArray) {
                topics.add(t);
            }
        }
        return topics;
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception{
        Map referenceData = new HashMap();

        Post post = (Post)command;
        String forumId = request.getParameter("forumId");
        String contentId = request.getParameter("contentId");

        if ((forumId != null || contentId != null) && post.getThread().getForum().getTopicMapId() != null) {
            // This is a new thread, allow user to add topics
            referenceData.put("addTopics", Boolean.TRUE);
        }

        return referenceData;
    }


    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {

        Post p = (Post) object;
        if (ForumUtil.isSpam(request)) {
            return new ModelAndView(new RedirectView("nospam"));
        }

        ResolvedUser user = userResolver.resolveUser(request);

        p.setBody(cleanup(p.getBody(), request));

        // Sett til godkjent dersom bruker har rettighet til dette
        String username = null;
        if (user != null) {
            username = user.getUsername();
        }

        boolean approved = permissionManager.hasPermission(username, Permissions.APPROVE_POST, p);
        // Lag ny tråd først hvis det er aktuelt
        if(p.getThread().getId() == 0) {
            p.getThread().setName(p.getSubject());
            p.getThread().setNumPosts(0);
            p.getThread().setApproved(approved);
            dao.saveOrUpdate(p.getThread());
        }

        p.setApproved(approved);

        dao.saveOrUpdate(p);

        if (p.getThread().getContentId() > 0) {
            CommentNotification notification = new CommentNotification();
            notification.setContext("content");
            notification.setObjectId("" + p.getThread().getContentId());
            ForumThread thread = dao.getThread(p.getThread().getId());
            notification.setNumberOfComments(thread.getNumPosts());
            notification.setCommentId(String.valueOf(p.getId()));
            notification.setCommentTitle(p.getSubject());
            notification.setCommentSummary(p.getBody());
            notification.setCommentAuthor(p.getAuthor());
            Map commentNotificationListenerBeans = RootContext.getInstance().getBeansOfType(CommentNotificationListener.class);
            if (commentNotificationListenerBeans != null && commentNotificationListenerBeans.size() > 0)  {
                for (CommentNotificationListener notificationListener : (Iterable<? extends CommentNotificationListener>) commentNotificationListenerBeans.values()) {
                    notificationListener.newCommentNotification(notification);
                }
            }
        }

        // Lagring av vedlegg
        Set attachments = p.getAttachments();
        if (attachments != null) {
            Iterator it = attachments.iterator();
            while(it.hasNext()) {
                Attachment attachment = (Attachment)it.next();
                if (attachment.getId() > 0) {
                    dao.saveOrUpdate(attachment);
                }
            }
        }

        // Send varsling til moderator om nytt innlegg
        String moderator = p.getThread().getForum().getModerator();
        if (!p.isApproved() && moderator != null && moderator.length() > 0) {
            UserProfile profile = userProfileManager.getUserProfile(moderator);
            if (profile != null && profile.getEmail() != null && profile.getEmail().indexOf("@") != -1) {
                Configuration config = Aksess.getConfiguration();
                String from = config.getString("mail.from");
                if (from == null) {
                    throw new ConfigurationException("mail.from", SOURCE);
                }

                Map param = new HashMap();
                param.put("baseurl", Aksess.getApplicationUrl());
                param.put("post", p);

                try {
                    Log.debug(SOURCE, "Sender varsel om nytt innlegg til " + profile.getEmail(), null, null);
                    MailSender.send(from, profile.getEmail(), "Forum, nytt innlegg:" + p.getSubject(), "forum-newpost.vm", param);
                } catch (Exception e) {
                    Log.error(SOURCE, e, null, null);
                }
            } else {
                Log.info(SOURCE, "Fant ikke brukerprofil/epost for " + moderator, null, null);
            }
        }


        Map map = new HashMap();

        String redirect = request.getParameter("redirect");
        if (redirect != null && redirect.startsWith("/")) {
            return new ModelAndView(new RedirectView(redirect));
        } else if (p.isApproved()) {
            // Vis tråden hvis innlegget er godkjent
            map.put("threadId", new Long(p.getThread().getId()));
            map.put("postId", new Long(p.getId()));
            return new ModelAndView(new RedirectView("viewthread"), map);
        } else {
            // Vis innlegget hvis det ikke er godkjent
            map.put("postId", new Long(p.getId()));
            return new ModelAndView(new RedirectView("viewpost"), map);
        }
    }

    /**
     * Handles file attachments, shrinks images if they are to large
     * @param request
     * @param object
     * @throws Exception
     */
    protected void onBind(HttpServletRequest request, Object object) throws Exception {
        Post post = (Post)object;

        Set attachments = post.getAttachments();
        if (attachments == null) {
            attachments = new HashSet();
        }
        if(request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            int fileNo = 1;
            MultipartFile file = mRequest.getFile("attachment" + fileNo);
            while (file != null && file.getSize() > 0) {
                String mimeType = file.getContentType();
                if (mimeType.indexOf("image") != -1) {
                    // We only accept images as file upload type for now
                    Attachment attachment = new Attachment();

                    byte[] bytes = file.getBytes();
                    long size = file.getSize();
                    String filename = file.getOriginalFilename();

                    ImageInfo ii = new ImageInfo();
                    ii.setInput(new ByteArrayInputStream(bytes));

                    // Automatically shrink images to smaller size if needed
                    if (ii.check()) {
                        int width = ii.getWidth();
                        int height = ii.getHeight();

                        if (width > maxImageWidth && height > maxImageHeight) {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bos.write(bytes);

                            ByteArrayOutputStream bout = ImageHelper.resizeImage(bos, maxImageWidth, maxImageHeight, imageFormat, 85);
                            bytes = bout.toByteArray();
                            size = bytes.length;
                            if (filename.indexOf(".") != -1) {
                                filename = filename.substring(0, filename.lastIndexOf(".")) + "." + imageFormat;
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


        // Cannot bind directly, save topics on thread
        String[] topicIds = request.getParameterValues("topics");
        if (topicIds != null && topicIds.length > 0) {
            Set topics = new HashSet();
            for (String t : topicIds) {
                if (t != null && t.length() > 0) {
                    topics.add(t);
                }
            }
            post.getThread().setTopics(topics);
        }
    }

    private String cleanup(String body, HttpServletRequest request) {
        String qStart = getApplicationContext().getMessage("post.quote.starttag", new Object[0], RequestContextUtils.getLocale(request));
        String qEnd = getApplicationContext().getMessage("post.quote.endtag", new Object[0], RequestContextUtils.getLocale(request));

        body = body.replaceAll(qStart, "<blockquote>");
        body = body.replaceAll(qEnd, "</blockquote>");

        SAXParser parser = new SAXParser();


        XMLFilterImpl xmlFilter = new XMLFilterImpl() {
            Set legalTags = new HashSet();

            {
                legalTags.add("blockquote");
                legalTags.add("b");
                legalTags.add("br");
                legalTags.add("strong");

            }

            public void startElement(String string, String string1, String string2, Attributes attributes) throws SAXException {
                if (legalTags.contains(string1)) {
                    super.startElement(string, string1, string2, attributes);    //To change body of overridden methods use File | Settings | File Templates.
                }
            }

            public void endElement(String string, String string1, String string2) throws SAXException {
                if (legalTags.contains(string1)) {
                    super.endElement(string, string1, string2);    //To change body of overridden methods use File | Settings | File Templates.
                }
            }

        };

        StringWriter sw = new StringWriter();

        try {

            parser.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
            parser.setProperty("http://cyberneko.org/html/properties/names/elems", "match");

            parser.setContentHandler(xmlFilter);


            SAXTransformerFactory fac = (SAXTransformerFactory) TransformerFactory.newInstance();

            TransformerHandler handler = fac.newTransformerHandler();
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

            URL resourceUrl = contextClassLoader.getResource("no/kantega/xml/serializer/XMLEntities.properties");

            handler.getTransformer().setOutputProperty(OutputPropertiesFactory.S_KEY_ENTITIES, resourceUrl.toString());
            handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
            xmlFilter.setContentHandler(handler);
            handler.setResult(new StreamResult(sw));
            parser.parse(new InputSource(new StringReader(body)));
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return sw.toString();

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
}
