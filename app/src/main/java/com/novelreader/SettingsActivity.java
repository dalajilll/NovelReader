package com.novelreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.novelreader.model.BookSource;

public class SettingsActivity extends AppCompatActivity {
    private EditText nameEditText, baseUrlEditText, searchUrlEditText;
    private EditText bookListRuleEditText, bookNameRuleEditText, bookAuthorRuleEditText;
    private EditText chapterListRuleEditText, chapterNameRuleEditText, contentRuleEditText;
    private Switch enabledSwitch;
    private Button saveButton, testButton;
    
    private BookSource currentSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initializeViews();
        setupClickListeners();
        loadSourceData();
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.nameEditText);
        baseUrlEditText = findViewById(R.id.baseUrlEditText);
        searchUrlEditText = findViewById(R.id.searchUrlEditText);
        bookListRuleEditText = findViewById(R.id.bookListRuleEditText);
        bookNameRuleEditText = findViewById(R.id.bookNameRuleEditText);
        bookAuthorRuleEditText = findViewById(R.id.bookAuthorRuleEditText);
        chapterListRuleEditText = findViewById(R.id.chapterListRuleEditText);
        chapterNameRuleEditText = findViewById(R.id.chapterNameRuleEditText);
        contentRuleEditText = findViewById(R.id.contentRuleEditText);
        enabledSwitch = findViewById(R.id.enabledSwitch);
        saveButton = findViewById(R.id.saveButton);
        testButton = findViewById(R.id.testButton);
    }

    private void setupClickListeners() {
        saveButton.setOnClickListener(v -> saveSource());
        testButton.setOnClickListener(v -> testSource());
    }

    private void loadSourceData() {
        // 从Intent获取书源数据
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("source")) {
            currentSource = (BookSource) intent.getSerializableExtra("source");
            if (currentSource != null) {
                populateFields();
            }
        }
    }

    private void populateFields() {
        nameEditText.setText(currentSource.getName());
        baseUrlEditText.setText(currentSource.getBaseUrl());
        searchUrlEditText.setText(currentSource.getSearchUrl());
        bookListRuleEditText.setText(currentSource.getBookListRule());
        bookNameRuleEditText.setText(currentSource.getBookNameRule());
        bookAuthorRuleEditText.setText(currentSource.getBookAuthorRule());
        chapterListRuleEditText.setText(currentSource.getChapterListRule());
        chapterNameRuleEditText.setText(currentSource.getChapterNameRule());
        contentRuleEditText.setText(currentSource.getContentRule());
        enabledSwitch.setChecked(currentSource.isEnabled());
    }

    private void saveSource() {
        if (validateInput()) {
            if (currentSource == null) {
                currentSource = new BookSource();
            }
            
            currentSource.setName(nameEditText.getText().toString());
            currentSource.setBaseUrl(baseUrlEditText.getText().toString());
            currentSource.setSearchUrl(searchUrlEditText.getText().toString());
            currentSource.setBookListRule(bookListRuleEditText.getText().toString());
            currentSource.setBookNameRule(bookNameRuleEditText.getText().toString());
            currentSource.setBookAuthorRule(bookAuthorRuleEditText.getText().toString());
            currentSource.setChapterListRule(chapterListRuleEditText.getText().toString());
            currentSource.setChapterNameRule(chapterNameRuleEditText.getText().toString());
            currentSource.setContentRule(contentRuleEditText.getText().toString());
            currentSource.setEnabled(enabledSwitch.isChecked());
            
            Toast.makeText(this, "书源保存成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validateInput() {
        if (nameEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入书源名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (baseUrlEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入基础URL", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }

    private void testSource() {
        Toast.makeText(this, "测试功能开发中...", Toast.LENGTH_SHORT).show();
    }

    public static void start(Context context, BookSource source) {
        Intent intent = new Intent(context, SettingsActivity.class);
        if (source != null) {
            intent.putExtra("source", source);
        }
        context.startActivity(intent);
    }
}