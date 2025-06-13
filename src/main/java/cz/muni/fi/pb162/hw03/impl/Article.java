package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw02.HasLabels;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public record Article(String title, String date,
                      String hits, LinkedHashSet<String> labels) implements HasLabels {
    public Article {
        Objects.requireNonNull(title, "Title should not be null");
        Objects.requireNonNull(labels, "Labels should not be null");
        Objects.requireNonNull(date, "Date should not be null");
        Objects.requireNonNull(hits, "Hits should not be null");

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return getTitle().equals(article.getTitle()) && getDate().equals(article.getDate())
                && getHits().equals(article.getHits());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLabels(), getTitle(), getDate(), getHits());
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", title, date, hits, labelsToString());
    }

    public String labelsToString() {
        return String.join(" ", labels);
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
