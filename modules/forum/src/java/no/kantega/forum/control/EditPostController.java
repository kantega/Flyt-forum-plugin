package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.model.User;
import no.kantega.forum.model.ForumThread;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 21.des.2005
 * Time: 12:11:54
 * To change this template use File | Settings | File Templates.
 */
public class EditPostController extends AbstractForumFormController {
    private ForumDao dao;


    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String postId = request.getParameter("postId");

        if(postId != null && !postId.equals("0")) {
            long id = Long.parseLong(postId);
            return dao.getPopulatedPost(id);
        } else {
            long id = Long.parseLong(request.getParameter("threadId"));
            String replyId = request.getParameter("replayId");
            //User u = dao.getUser(1);
            //Date d = new Date();

            ForumThread t = dao.getThread(id);

            Post p = new Post();
            //p.setOwner(u);
            p.setPostDate(new Date());
            p.setThread(t);
            if (replyId!= null) {
                p.setReplyToId(Long.parseLong(replyId));
                Post origPost = dao.getPost(Long.parseLong(replyId));
                p.setSubject("SV: " + origPost.getSubject());
                //p.setBody("> " + origPost.getBody());
            }
            return p;
        }

    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
        Post p = (Post) object;
        dao.saveOrUpdate(p);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/forum/viewthread?threadId="+p.getThread().getId()));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
