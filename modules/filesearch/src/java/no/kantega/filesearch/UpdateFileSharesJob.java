package no.kantega.filesearch;

import no.kantega.publishing.search.index.IndexManager;
import no.kantega.publishing.search.index.jobs.RebuildIndexJob;
import no.kantega.publishing.search.index.rebuild.ProgressReporter;

public class UpdateFileSharesJob {
    private IndexManager indexManager;
    private String sourceId;

    public void execute() {
        RebuildIndexJob job = new RebuildIndexJob(new ProgressReporter() {
            public void reportProgress(int i, String s, int i1) {

            }

            public void reportFinished() {

            }
        });
        job.setSource(sourceId);
        indexManager.addIndexJob(job);
    }

    public void setIndexManager(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
