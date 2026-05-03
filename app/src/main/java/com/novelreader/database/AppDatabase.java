package com.novelreader.database;

import androidx.room.*;
import androidx.room.OnConflictStrategy;
import java.util.List;

@Entity(tableName = "novels")
public class NovelEntity {
    @PrimaryKey
    public int id;
    public String title;
    public String author;
    public String description;
    public String coverUrl;
    public String sourceUrl;
    public int currentChapter;
    public String lastReadTime;
    public boolean isDownloaded;
    public boolean isFavorite;
    public long readingTime; // 阅读时长，单位秒
}

@Entity(tableName = "chapters")
public class ChapterEntity {
    @PrimaryKey
    public int id;
    public int novelId;
    public String title;
    public String content;
    public int chapterIndex;
    public boolean isDownloaded;
}

@Entity(tableName = "bookmarks")
public class BookmarkEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int novelId;
    public int chapterIndex;
    public int position; // 在章节中的位置
    public String note;
    public String createTime;
}

@Entity(tableName = "reading_stats")
public class ReadingStatsEntity {
    @PrimaryKey
    public int novelId;
    public long totalReadingTime;
    public long lastReadTime;
    public int chaptersRead;
    public double readingSpeed; // 字/分钟
}

@Dao
public interface NovelDao {
    @Query("SELECT * FROM novels ORDER BY lastReadTime DESC")
    List<NovelEntity> getAllNovels();
    
    @Query("SELECT * FROM novels WHERE isFavorite = 1")
    List<NovelEntity> getFavoriteNovels();
    
    @Query("SELECT * FROM novels WHERE isDownloaded = 1")
    List<NovelEntity> getDownloadedNovels();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNovel(NovelEntity novel);
    
    @Update
    void updateNovel(NovelEntity novel);
    
    @Delete
    void deleteNovel(NovelEntity novel);
    
    @Query("UPDATE novels SET currentChapter = :chapter, lastReadTime = :time WHERE id = :novelId")
    void updateReadingProgress(int novelId, int chapter, String time);
}

@Dao
public interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE novelId = :novelId ORDER BY chapterIndex")
    List<ChapterEntity> getChaptersByNovel(int novelId);
    
    @Query("SELECT * FROM chapters WHERE novelId = :novelId AND chapterIndex = :index")
    ChapterEntity getChapter(int novelId, int index);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChapter(ChapterEntity chapter);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChapters(List<ChapterEntity> chapters);
    
    @Query("UPDATE chapters SET content = :content, isDownloaded = 1 WHERE id = :chapterId")
    void updateChapterContent(int chapterId, String content);
}

@Dao
public interface BookmarkDao {
    @Query("SELECT * FROM bookmarks WHERE novelId = :novelId ORDER BY createTime DESC")
    List<BookmarkEntity> getBookmarksByNovel(int novelId);
    
    @Insert
    void insertBookmark(BookmarkEntity bookmark);
    
    @Delete
    void deleteBookmark(BookmarkEntity bookmark);
}

@Dao
public interface ReadingStatsDao {
    @Query("SELECT * FROM reading_stats WHERE novelId = :novelId")
    ReadingStatsEntity getStatsByNovel(int novelId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateStats(ReadingStatsEntity stats);
    
    @Query("SELECT SUM(totalReadingTime) FROM reading_stats")
    long getTotalReadingTime();
    
    @Query("SELECT COUNT(*) FROM reading_stats WHERE date(lastReadTime/1000, 'unixepoch') = date('now')")
    int getTodayReadingCount();
}

@Database(entities = {NovelEntity.class, ChapterEntity.class, BookmarkEntity.class, ReadingStatsEntity.class}, 
          version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract NovelDao novelDao();
    public abstract ChapterDao chapterDao();
    public abstract BookmarkDao bookmarkDao();
    public abstract ReadingStatsDao readingStatsDao();
    
    private static volatile AppDatabase INSTANCE;
    
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "novel_reader_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

public class Converters {
    @TypeConverter
    public static String fromTimestamp(Long value) {
        return value == null ? null : new Date(value).toString();
    }

    @TypeConverter
    public static Long dateToTimestamp(String date) {
        return date == null ? null : System.currentTimeMillis();
    }
}
