package no.kantega.forum.control;

import no.kantega.publishing.api.content.ContentIdentifier;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.publishing.common.data.Content;
import no.kantega.publishing.common.service.ContentManagementService;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.dao.ForumDao;

import java.util.Date;


public class StartThreadController extends AbstractController  {

    private ForumDao dao;

    private Logger log = Logger.getLogger(getClass());

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int contentId = Integer.parseInt(request.getParameter("contentId"));

        Content c = new ContentManagementService(request).getContent(ContentIdentifier.fromContentId(contentId));

        if(c.getForumId() <= 0) {
            log.error("Content " + c.getId() +" does not have a forum attached to it");
            return null;
        } else if(dao.getThreadAboutContent(c.getId()) > 0) {
            log.error("Content " + c.getId() +" already has thread " +dao.getThreadAboutContent(c.getId()) +" created for it");
            return null;
        }
        else {
            Forum f = dao.getForum(c.getForumId());

            ForumThread t = new ForumThread();
            t.setCreatedDate(new Date());
            t.setForum(f);
            t.setName(c.getTitle());
            t.setContentId(c.getId());


            dao.saveOrUpdate(t);

            return new ModelAndView(new RedirectView("editpost"), "threadId", Long.toString(t.getId()));
        }
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
