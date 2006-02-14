package no.kantega.projectweb.control;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContextAware;
import no.kantega.projectweb.propertyeditors.ActivityTypeEditor;
import no.kantega.projectweb.propertyeditors.ActivityPriorityEditor;
import no.kantega.projectweb.propertyeditors.ProjectPhaseEditor;
import no.kantega.projectweb.propertyeditors.DocumentCategoryEditor;
import no.kantega.projectweb.model.ActivityType;
import no.kantega.projectweb.model.ActivityPriority;
import no.kantega.projectweb.model.ProjectPhase;
import no.kantega.projectweb.model.DocumentCategory;
import no.kantega.projectweb.dao.ProjectWebDao;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 17:57:14
 * To change this template use File | Settings | File Templates.
 */
public class FormControllerSupport extends SimpleFormController implements ApplicationContextAware {
    protected ProjectWebDao dao;

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {

        Map resolvers = getWebApplicationContext().getBeansOfType(LocaleResolver.class);
        Locale locale;
        if(resolvers.size()>0 ) {
            locale = ((LocaleResolver)resolvers.values().iterator().next()).resolveLocale(request);
        } else {
            locale = Locale.getDefault();
        }
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        CustomDateEditor editor = new CustomDateEditor(format, true);
        binder.registerCustomEditor(Date.class, editor);

        binder.registerCustomEditor(ActivityPriority.class, new ActivityPriorityEditor(dao));
        binder.registerCustomEditor(ActivityType.class, new ActivityTypeEditor(dao));
        binder.registerCustomEditor(ProjectPhase.class, new ProjectPhaseEditor(dao));
        binder.registerCustomEditor(DocumentCategory.class, new DocumentCategoryEditor(dao));
       // binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());

    }


    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    protected ProjectWebDao getDao() {
        return dao;
    }

}
