package no.kantega.projectweb.control.project;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.control.FormControllerSupport;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:56:55
 * To change this template use File | Settings | File Templates.
 */
public class AddProjectController extends FormControllerSupport {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Project project = new Project();
        project.setPermissionSchemeId(1);
        
        return project;

    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
        Project project = (Project) object;
        dao.saveOrUpdate(project);
        return new ModelAndView(new RedirectView("projectlist"));
    }



}
