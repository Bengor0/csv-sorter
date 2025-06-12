package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw02.HasLabels;

import java.util.Objects;
import java.util.Set;

public class Article implements HasLabels {
    private final Set<String> labels;
    private final String title;
    private final String date;
    private final String hits;

    public Article(Set<String> labels, String title, String date, String hits) {
        Objects.requireNonNull(title, "Title should not be null");
        Objects.requireNonNull(labels, "Labels should not be null");
        Objects.requireNonNull(date, "Date should not be null");
        Objects.requireNonNull(hits, "Hits should not be null");

        this.labels = labels;
        this.title = title;
        this.date = date;
        this.hits = hits;
    }

    @Override
    public Set<String> getLabels() {
        return this.labels;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public String getHits() {
        return this.hits;
    }
}
