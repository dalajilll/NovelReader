package com.novelreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocalBookImportActivity extends AppCompatActivity {
    private Button selectFileButton;
    private TextView selectedFileTextView;
    private Button importButton;
    private ProgressBar importProgressBar;
    
    private Uri selectedFileUri;
    private String fileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_book_import);
        
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        selectFileButton = findViewById(R.id.selectFileButton);
        selectedFileTextView = findViewById(R.id.selectedFileTextView);
        importButton = findViewById(R.id.importButton);
        importProgressBar = findViewById(R.id.importProgressBar);
        
        importButton.setEnabled(false);
        importProgressBar.setVisibility(View.GONE);
    }

    private void setupClickListeners() {
        selectFileButton.setOnClickListener(v -> selectFile());
        importButton.setOnClickListener(v -> importFile());
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"text/plain", "application/epub+zip"});
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedFileUri = data.getData();
                DocumentFile file = DocumentFile.fromSingleUri(this, selectedFileUri);
                
                if (file != null) {
                    selectedFileTextView.setText(file.getName());
                    importButton.setEnabled(true);
                    
                    // 预览文件内容
                    previewFileContent();
                }
            }
        }
    }

    private void previewFileContent() {
        new Thread(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                
                StringBuilder content = new StringBuilder();
                String line;
                int lineCount = 0;
                
                while ((line = reader.readLine()) != null && lineCount < 10) {
                    content.append(line).append("\n");
                    lineCount++;
                }
                
                reader.close();
                inputStream.close();
                
                fileContent = content.toString();
                
                runOnUiThread(() -> {
                    selectedFileTextView.setText(selectedFileTextView.getText() + "\n\n预览:\n" + fileContent);
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "读取文件失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void importFile() {
        if (selectedFileUri == null) {
            Toast.makeText(this, "请先选择文件", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showImportProgress(true);
        
        new Thread(() -> {
            try {
                // 读取文件内容
                InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                
                List<String> chapters = new ArrayList<>();
                StringBuilder chapterContent = new StringBuilder();
                String line;
                int chapterCount = 1;
                
                while ((line = reader.readLine()) != null) {
                    if (isChapterTitle(line)) {
                        if (chapterContent.length() > 0) {
                            chapters.add(chapterContent.toString());
                            chapterContent = new StringBuilder();
                        }
                        chapterContent.append("\n第").append(chapterCount++).append("章\n\n");
                    } else {
                        chapterContent.append(line).append("\n");
                    }
                }
                
                // 添加最后一章
                if (chapterContent.length() > 0) {
                    chapters.add(chapterContent.toString());
                }
                
                reader.close();
                inputStream.close();
                
                // 保存到数据库
                saveImportedBook(chapters);
                
                runOnUiThread(() -> {
                    showImportProgress(false);
                    Toast.makeText(this, String.format("导入成功，共 %d 章", chapters.size()), Toast.LENGTH_SHORT).show();
                    finish();
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showImportProgress(false);
                    Toast.makeText(this, "导入失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private boolean isChapterTitle(String line) {
        // 判断是否是章节标题
        String trimmedLine = line.trim();
        return trimmedLine.matches(".*章.*") || 
               trimmedLine.matches(".*回.*") ||
               trimmedLine.matches(".*Chapter.*") ||
               trimmedLine.matches(".*CHAPTER.*");
    }

    private void saveImportedBook(List<String> chapters) {
        // 这里应该将导入的书籍保存到数据库
        // 因为是本地文件，可以直接保存在内存或缓存
        
        // 这里是简化版本，实际上应该将数据保存到数据库
        // database.novelDao().insertNovel(novelEntity);
        // database.chapterDao().insertChapters(chapterEntities);
    }

    private void showImportProgress(boolean show) {
        importProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        importButton.setEnabled(!show);
        selectFileButton.setEnabled(!show);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LocalBookImportActivity.class);
        context.startActivity(intent);
    }
}