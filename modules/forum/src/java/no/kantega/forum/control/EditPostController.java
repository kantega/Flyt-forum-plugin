package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;
import org.cyberneko.html.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;
import org.apache.xml.serializer.OutputPropertiesFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.model.User;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Forum;
import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserProfileManager;
import no.kantega.modules.user.UserProfile;

import java.util.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 12:11:54
 * To change this template use File | Settings | File Templates.
 */
public class EditPostController extends AbstractForumFormController {
    private ForumDao dao;
    private UserResolver userResolver;
    private UserProfileManager userProfileManager;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String postId = request.getParameter("postId");

        if (postId != null && !postId.equals("0")) {
            long id = Long.parseLong(postId);
            return dao.getPopulatedPost(id);
        } else {
            String threadId = request.getParameter("threadId");
            String replyId = request.getParameter("replayId");
            //User u = dao.getUser(1);
            //Date d = new Date();

            ForumThread t = null;
            if(threadId != null) {
                long id = Long.parseLong(threadId);
                t = dao.getThread(id);
            } else {
                String forumId = request.getParameter("forumId");
                Forum f = dao.getForum(Long.parseLong(forumId));
                t = new ForumThread();
                t.setForum(f);
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
            if (replyId != null) {
                String qStart = getApplicationContext().getMessage("post.quote.starttag", new Object[0], RequestContextUtils.getLocale(request));
                String qEnd = getApplicationContext().getMessage("post.quote.endtag", new Object[0], RequestContextUtils.getLocale(request));
                p.setReplyToId(Long.parseLong(replyId));
                Post origPost = dao.getPost(Long.parseLong(replyId));
                if(origPost.getSubject().startsWith("SV:")) {
                    p.setSubject(origPost.getSubject());
                } else {
                    p.setSubject("SV: " + origPost.getSubject());
                }
                String body = origPost.getBody();
                body = body.replaceAll("<blockquote>", qStart).replaceAll("</blockquote>", qEnd);
                p.setBody(qStart + body + qEnd + "\n");
            }
            return p;
        }

    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        Post p = (Post) object;

        p.setBody(cleanup(p.getBody(), request));

        // Lag ny tråd først hvis det er aktuelt
        if(p.getThread().getId() == 0) {
            p.getThread().setName(p.getSubject());
            p.getThread().setNumPosts(0);
            dao.saveOrUpdate(p.getThread());
        }
        dao.saveOrUpdate(p);
        Map map = new HashMap();
        map.put("threadId",new Long(p.getThread().getId()));
        map.put("postId",new Long(p.getId()));

        return new ModelAndView(new RedirectView("viewthread"), map);
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
