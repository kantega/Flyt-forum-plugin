package no.kantega.forum.search;

import no.kantega.commons.log.Log;
import no.kantega.search.index.provider.DocumentProviderHandler;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

public class ForumProviderHandler implements DocumentProviderHandler {
    final IndexWriter writer;
    int docs = 0;
    boolean stopRequested;

    public ForumProviderHandler(IndexWriter writer) {
        this.writer = writer;
    }

    @Override
    public void handleDocument(Document document) {
        try {
            writer.addDocument(document);
        } catch (IOException e) {
            Log.error("Error indexing forum", e);
        }
    }

    @Override
    public boolean isStopRequested() {
        return stopRequested;
    }

    public void setStopRequested(boolean stopRequested) {
        this.stopRequested = stopRequested;
    }
}
