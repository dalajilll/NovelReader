package com.novelreader.model;

public class BookSource {
    private String id;
    private String name;
    private String baseUrl;
    private String searchUrl;
    private String charset;
    private boolean enabled;
    private String bookListRule;
    private String bookNameRule;
    private String bookAuthorRule;
    private String bookUrlRule;
    private String chapterListRule;
    private String chapterNameRule;
    private String chapterUrlRule;
    private String contentRule;

    public BookSource() {
        this.charset = "UTF-8";
        this.enabled = true;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getSearchUrl() { return searchUrl; }
    public void setSearchUrl(String searchUrl) { this.searchUrl = searchUrl; }

    public String getCharset() { return charset; }
    public void setCharset(String charset) { this.charset = charset; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getBookListRule() { return bookListRule; }
    public void setBookListRule(String bookListRule) { this.bookListRule = bookListRule; }

    public String getBookNameRule() { return bookNameRule; }
    public void setBookNameRule(String bookNameRule) { this.bookNameRule = bookNameRule; }

    public String getBookAuthorRule() { return bookAuthorRule; }
    public void setBookAuthorRule(String bookAuthorRule) { this.bookAuthorRule = bookAuthorRule; }

    public String getBookUrlRule() { return bookUrlRule; }
    public void setBookUrlRule(String bookUrlRule) { this.bookUrlRule = bookUrlRule; }

    public String getChapterListRule() { return chapterListRule; }
    public void setChapterListRule(String chapterListRule) { this.chapterListRule = chapterListRule; }

    public String getChapterNameRule() { return chapterNameRule; }
    public void setChapterNameRule(String chapterNameRule) { this.chapterNameRule = chapterNameRule; }

    public String getChapterUrlRule() { return chapterUrlRule; }
    public void setChapterUrlRule(String chapterUrlRule) { this.chapterUrlRule = chapterUrlRule; }

    public String getContentRule() { return contentRule; }
    public void setContentRule(String contentRule) { this.contentRule = contentRule; }
}