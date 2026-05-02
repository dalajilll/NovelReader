package com.novelreader.source;

import android.content.Context;
import android.util.Log;
import com.novelreader.model.BookSource;
import com.novelreader.model.Novel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookSourceManager {
    private List<BookSource> sources;
    private ExecutorService executorService;
    private Context context;

    public BookSourceManager(Context context) {
        this.context = context;
        this.executorService = Executors.newFixedThreadPool(3);
        initializeDefaultSources();
    }

    private void initializeDefaultSources() {
        sources = new ArrayList<>();
        
        // 默认书源配置
        BookSource source1 = new BookSource();
        source1.setId("1");
        source1.setName("资源网站1");
        source1.setBaseUrl("https://example1.com");
        source1.setSearchUrl("/search?q={key}");
        source1.setBookListRule(".book-list .book-item");
        source1.setBookNameRule(".book-title");
        source1.setBookAuthorRule(".book-author");
        source1.setBookUrlRule("a");
        
        BookSource source2 = new BookSource();
        source2.setId("2");
        source2.setName("资源网站2");
        source2.setBaseUrl("https://example2.com");
        source2.setSearchUrl("/search?keyword={key}");
        source2.setBookListRule(".novel-list li");
        source2.setBookNameRule(".title");
        source2.setBookAuthorRule(".author");
        source2.setBookUrlRule("a");
        
        sources.add(source1);
        sources.add(source2);
    }

    public void searchNovels(String keyword, SearchCallback callback) {
        executorService.execute(() -> {
            List<Novel> allResults = new ArrayList<>();
            
            for (BookSource source : sources) {
                if (source.isEnabled()) {
                    try {
                        List<Novel> results = searchFromSource(source, keyword);
                        allResults.addAll(results);
                    } catch (Exception e) {
                        Log.e("BookSource", "搜索失败: " + source.getName(), e);
                    }
                }
            }
            
            // 在主线程返回结果
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                callback.onSearchComplete(allResults);
            });
        });
    }

    private List<Novel> searchFromSource(BookSource source, String keyword) throws Exception {
        List<Novel> results = new ArrayList<>();
        String searchUrl = source.getBaseUrl() + source.getSearchUrl().replace("{key}", keyword);
        
        Document doc = Jsoup.connect(searchUrl)
                .timeout(10000)
                .get();
        
        Elements bookElements = doc.select(source.getBookListRule());
        
        for (Element bookElement : bookElements) {
            Novel novel = new Novel();
            
            // 提取书名
            Element nameElement = bookElement.selectFirst(source.getBookNameRule());
            if (nameElement != null) {
                novel.setTitle(nameElement.text());
            }
            
            // 提取作者
            Element authorElement = bookElement.selectFirst(source.getBookAuthorRule());
            if (authorElement != null) {
                novel.setAuthor(authorElement.text());
            }
            
            // 提取书籍链接
            Element urlElement = bookElement.selectFirst(source.getBookUrlRule());
            if (urlElement != null) {
                novel.setSourceUrl(urlElement.attr("href"));
            }
            
            if (novel.getTitle() != null && !novel.getTitle().isEmpty()) {
                results.add(novel);
            }
        }
        
        return results;
    }

    public interface SearchCallback {
        void onSearchComplete(List<Novel> results);
    }
}