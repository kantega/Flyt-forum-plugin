package no.kantega.forum.control;

import no.kantega.commons.exception.NotAuthorizedException;
import no.kantega.commons.log.Log;
import no.kantega.publishing.api.cache.SiteCache;
import no.kantega.publishing.api.model.Site;
import no.kantega.publishing.common.data.Content;
import no.kantega.publishing.common.data.ContentIdentifier;
import no.kantega.publishing.common.exception.ContentNotFoundException;
import no.kantega.publishing.common.service.ContentManagementService;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPageInfoController implements Controller{
    private SiteCache siteCache;
    private View jsonView;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Map model = new HashMap();
        String url = request.getParameter("url");
        url = prependHttpProtocolToUrlIfMissing(url);

        String pageTitle = null;
        if (isInternalLink(url, request)) {
            pageTitle = getPageTitleFromInternalPage(url, request);
        }

        if (pageTitle == null) {
            pageTitle = getPageTitle(url);
        }

        model.put("title", pageTitle);
        model.put("url", url);

        return new ModelAndView(jsonView, model);
    }

    private String getPageTitleFromInternalPage(String url, HttpServletRequest request) {
        ContentIdentifier contentIdentifier = getContentIdentierFromUrl(url, request);

        ContentManagementService cms = new ContentManagementService(request);
        try {
            Content content = cms.getContent(contentIdentifier, false);
            if (content != null) {
                return content.getTitle();
            }
        } catch (NotAuthorizedException e) {
            return null;
        }

        return null;
    }

    private String prependHttpProtocolToUrlIfMissing(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

    private String getPageContent(String url) {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                Log.debug(this.getClass().getName(), "Something went wrong trying to get the page title. URL: " + url + " HTTP STATUS: " + method.getStatusLine());
                return null;
            }
            // Read the response body.
            return method.getResponseBodyAsString();
        } catch (Exception e) {
            Log.debug(this.getClass().getName(), "Something went wrong trying to get the page title \n" + e);
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    private String getPageTitle(String url) {
        String pageContent = getPageContent(url);
        String pageTitle = url;
        if (pageContent != null) {
            pageTitle = extractTitleFromPageContent(pageContent);
            if (pageTitle == null) {
                pageTitle = url;
            }
        }
        return pageTitle;
    }

    protected String extractTitleFromPageContent(String pageContent) {
        String titleStartTag = "<title>";
        String titleEndTag = "</title>";
        int titleStartIndex = pageContent.indexOf(titleStartTag);
        if (titleStartIndex == -1) {
            titleStartIndex = pageContent.indexOf(titleStartTag.toUpperCase());
        }
        if (titleStartIndex != -1) {
            int titleEndIndex = pageContent.indexOf(titleEndTag);
            if (titleEndIndex == -1) {
                titleEndIndex = pageContent.indexOf(titleEndTag.toUpperCase());
            }
            if (titleEndIndex != -1) {
                return pageContent.substring(titleStartIndex + titleStartTag.length(), titleEndIndex).trim();
            }
        }
        return null;
    }

    private boolean isInternalLink(String url, HttpServletRequest request) {
        return getContentIdentierFromUrl(url, request) != null;
    }

    private ContentIdentifier getContentIdentierFromUrl(String url, HttpServletRequest request) {
        if (url.contains("://")) {
            url = url.substring(url.indexOf("://") + "://".length(), url.length());
        }

        if (url.contains("/")) {
            String server = url.substring(0, url.indexOf("/"));
            if (isLocalhost(server, request)) {
                url = url.substring(request.getContextPath().length() - 1);
                try {
                    return new ContentIdentifier(url);
                } catch (ContentNotFoundException e) {
                    return null;
                }
            }
        }
        return null;
    }


    private boolean isLocalhost(String server, HttpServletRequest request) {
        List<Site> sites = siteCache.getSites();
        for (Site site : sites) {
            List<String> hosts = site.getHostnames();
            if (hosts.contains(server)) {
                return true;
            }
        }
        return server.equalsIgnoreCase(request.getServerName());
    }

    public void setSiteCache(SiteCache siteCache) {
        this.siteCache = siteCache;
    }

    public void setJsonView(View jsonView) {
        this.jsonView = jsonView;
    }
}
