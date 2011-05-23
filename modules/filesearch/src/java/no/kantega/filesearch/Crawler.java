package no.kantega.filesearch;

import jcifs.smb.*;
import no.kantega.commons.exception.SystemException;
import no.kantega.publishing.common.cache.SiteCache;
import no.kantega.publishing.common.data.enums.ContentStatus;
import no.kantega.publishing.common.data.enums.ContentVisibilityStatus;
import no.kantega.publishing.search.extraction.TextExtractor;
import no.kantega.publishing.search.extraction.TextExtractorSelector;
import no.kantega.search.index.Fields;
import no.kantega.search.index.provider.DocumentProvider;
import no.kantega.search.index.provider.DocumentProviderHandler;
import no.kantega.search.index.rebuild.ProgressReporter;
import no.kantega.search.result.SearchHit;
import no.kantega.search.result.SearchHitContext;

import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class Crawler implements DocumentProvider {

    private List roots;
    private Logger log = Logger.getLogger(getClass());

    private TextExtractorSelector textExtractorSelector;
    public static final String TYPE_FILE_ON_SHARE = "FileOnShare";
    public static final String FILE_NAME = "FileName";
    public static final String FILE_PATH = "FilePath";
    private static final String FILE_HOST = "FileHost";
    private String sourceId;
    private static final String FILE_URL = "FileUrl";
    private static final String FILE_URL_AND_LAST_MODIFIED = "FileUrlLastModified";

    private Set excluded = new HashSet();

    private SmbFileFilter fileFilter = new DefaultSmbFileFilter();
    private static final String FILE_OWNER = "FileOwner";

    private void provideFile(SmbFile smbFile, int siteId, DocumentProviderHandler documentProviderHandler, ProgressReporter progressReporter) {
        TextExtractor extractor = textExtractorSelector.select(smbFile.getName());
        try {

            if(extractor != null && fileFilter.accept(smbFile) && !isExcluded(smbFile.getURL().getPath())) {
                if(log.isDebugEnabled()) {
                    log.debug("Processing file : " + smbFile.getURL().toString());
                }

                URL url = smbFile.getURL();
                Document doc = new Document();
                doc.add(new Field(Fields.TITLE, smbFile.getName(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.add(new Field(Fields.DOCTYPE, TYPE_FILE_ON_SHARE, Field.Store.YES, Field.Index.UN_TOKENIZED));
                /*
                log.debug("Owner: " + smbFile.getOwnerUser().getAccountName());
                ACE[] security = smbFile.getSecurity();
                for (int i = 0; i < security.length; i++) {
                    ACE ace = security[i];
                    log.debug("Security: " +ace.getSID().getAccountName().toLowerCase());
                    if(ace.getSID().getType() == SID.SID_TYPE_USER)  {
                        doc.add(new Field(FILE_OWNER, ace.getSID().getAccountName().toLowerCase(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                    }
                }
                */
                doc.add(new Field(Fields.SITE_ID, Integer.toString(siteId), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.add(new Field(FILE_NAME, smbFile.getName(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.add(new Field(FILE_PATH, url.getPath(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.add(new Field(FILE_HOST, url.getHost(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.add(new Field(FILE_URL, url.toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.add(new Field(Fields.FILE_TYPE, getSuffix(smbFile.getName()).toLowerCase(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.add(new Field(FILE_URL_AND_LAST_MODIFIED, smbFile.getURL().toString() +":" + smbFile.getLastModified(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                String text = extractor.extractText(smbFile.getInputStream(), smbFile.getName());
                String summary = text.substring(0, (text.length() > Fields.SUMMARY_LENGTH) ? Fields.SUMMARY_LENGTH : text.length())  + (text.length() > Fields.SUMMARY_LENGTH  ? "..." : "");
                doc.add(new Field(Fields.CONTENT, smbFile.getName() +" " + text, Field.Store.NO, Field.Index.TOKENIZED));
                doc.add(new Field(Fields.SUMMARY, summary, Field.Store.YES, Field.Index.NO));
                doc.add(new Field(Fields.LAST_MODIFIED, DateTools.timeToString(smbFile.getLastModified(), DateTools.Resolution.MILLISECOND), Field.Store.YES, Field.Index.UN_TOKENIZED));
                // TODO: this is a hack
                doc.add(new Field(Fields.CONTENT_STATUS, Integer.toString(ContentStatus.PUBLISHED), Field.Store.YES, Field.Index.UN_TOKENIZED));
                doc.add(new Field(Fields.CONTENT_VISIBILITY_STATUS, Integer.toString(ContentVisibilityStatus.ACTIVE), Field.Store.YES, Field.Index.UN_TOKENIZED));

                documentProviderHandler.handleDocument(doc);

            }
        } catch (IOException e) {
            log.error("IOException processing file " + smbFile.getURL(), e);
        }
    }

    private boolean isExcluded(String path) {
        for(Iterator ex = excluded.iterator(); ex.hasNext(); ) {
            String exclude = (String) ex.next();
            if(path.startsWith(exclude)) {
                return true;
            }
        }
        return false;
    }


    public void provideDocuments(DocumentProviderHandler documentProviderHandler, ProgressReporter progressReporter) {
        Iterator roots = this.roots.iterator();


        while (roots.hasNext()) {
            Root root = (Root) roots.next();
            String password = root.getPassword();
            String username = root.getUsername();
            try {

                int siteId = SiteCache.getSiteByAlias(root.getSiteAlias()).getId();

                SmbFile file = new SmbFile(root.getUrl(), new NtlmPasswordAuthentication(null, username, password));
                provideDocuments(file, siteId, documentProviderHandler, progressReporter);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (SystemException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    private String getSuffix(String filename) {

        if(filename != null && filename.indexOf(".") >= 0) {
            return filename.substring(filename.lastIndexOf(".") +(filename.endsWith(".") ? 0 : 1));
        } else {
            return "";
        }

    }



    private void provideDocuments(SmbFile directory, int siteId, DocumentProviderHandler documentProviderHandler, ProgressReporter progressReporter) {
        try {
            SmbFile[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                SmbFile file = files[i];
                if(!isExcluded(file.getURL().getPath())) {
                    if(file.isDirectory()) {
                        provideDocuments(file, siteId, documentProviderHandler, progressReporter);
                    } else if(file.isFile()) {
                        provideFile(file, siteId, documentProviderHandler, progressReporter);
                    }
                }

            }
        } catch(SmbAuthException e) {
            log.error(e);
        } catch (SmbException e) {
            log.error(e);
        }
    }

    public Document provideDocument(String string) {
        return null;
    }

    public Term getDeleteTerm(String string) {
        return new Term(FILE_URL, string);
    }

    public Term getDeleteAllTerm() {
        return new Term(Fields.DOCTYPE, TYPE_FILE_ON_SHARE);
    }

    public SearchHit createSearchHit() {
        return new FileSearchHit();
    }
    
    public void processSearchHit(SearchHit searchHit, SearchHitContext context, Document document) {
        FileSearchHit hit = (FileSearchHit) searchHit;
        String host = document.get(FILE_HOST);
        String path = document.get(FILE_PATH);
        String owner = document.get(FILE_OWNER);

        hit.setOwner(owner);
        hit.setUrl("file://" + host + path);
        hit.setTitle(document.get(Fields.TITLE));
        
        List trails = new ArrayList();
        File parent = new File(path).getParentFile();
        while(parent != null && !"".equals(parent.getName())) {
            trails.add(0, parent.getName());
            parent = parent.getParentFile();

        }
        hit.setTrail(trails);


    }

    public void setRoots(List roots) {
        this.roots = roots;
    }

    public void setTextExtractorSelector(TextExtractorSelector textExtractorSelector) {
        this.textExtractorSelector = textExtractorSelector;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDocumentType() {
        return TYPE_FILE_ON_SHARE;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setFileFilter(SmbFileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public void setExcluded(Set excluded) {
        this.excluded = excluded;
    }
}
