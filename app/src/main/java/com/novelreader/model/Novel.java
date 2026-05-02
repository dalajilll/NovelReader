package com.novelreader.model;

import java.io.Serializable;

public class Novel implements Serializable {
    private String id;
    private String title;
    private String author;
    private String description;
    private String coverUrl;
    private String sourceUrl;
    private int currentChapter;
    private String lastReadTime;

    public Novel() {}

    public Novel(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }

    public int getCurrentChapter() { return currentChapter; }
    public void setCurrentChapter(int currentChapter) { this.currentChapter = currentChapter; }

    public String getLastReadTime() { return lastReadTime; }
    public void setLastReadTime(String lastReadTime) { this.lastReadTime = lastReadTime; }
}