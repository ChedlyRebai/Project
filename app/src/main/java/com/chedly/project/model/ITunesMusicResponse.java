package com.chedly.project.model;

import java.util.List;

public class ITunesMusicResponse {
    private int resultCount;
    private List<MusicItem> results;

    public int getResultCount() {
        return resultCount;
    }

    public List<MusicItem> getResults() {
        return results;
    }
}
