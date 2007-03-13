package no.kantega.projectweb.tags;

import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: steinarline
 * Date: 15.feb.2006
 * Time: 09:55:12
 * To change this template use File | Settings | File Templates.
 */
public class LimitTextLengthTag extends TagSupport {
    String value = null;
    int length = 0;

    public int doStartTag() throws JspException {
        if (value!=null){
            value = ExpressionEvaluationUtils.evaluateString("value", value, pageContext);
            if (value.length()>length){
                value = value.substring(0,length-3)+"...";
            }
        }

        try {
            pageContext.getOut().write(value);
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }


    public void setLength(int length) {
        this.length = length;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
