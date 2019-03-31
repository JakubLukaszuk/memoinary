package com.nihillon.viewModel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nihillon.dao.CommonDao;
import com.nihillon.models.Category;
import com.nihillon.models.Word;
import com.nihillon.utils.DbManager;
import com.nihillon.utils.converter.ToModel;
import com.nihillon.utils.converter.ToView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

@Component
public class WordModel {
    final private CommonDao dao;

    private ObservableList<WordView> wordWiewList = FXCollections.observableArrayList();
    private ObjectProperty<WordView> selectedWord;
    private List<WordView> selectedItems = new ArrayList<>();

    @Autowired
    public WordModel(CommonDao dao) {
        this.dao = dao;
    }


    public void saveToDataBase(String mean, String word, CategoryView categoryView, SubCategoryView subCategoryView, boolean status) throws ParseException {
        Word wordToSave = new Word();
        Date current = new Date();
        subCategoryView.setCategoryViewObjectProperty(categoryView);
        wordToSave.setDateOfAddition(current);
        wordToSave.setMean(mean);
        wordToSave.setIssue(word);
        wordToSave.setKnowledgeStatus(status);
        wordToSave.setCategory( ToModel.toCategory(categoryView));
        wordToSave.setSubCategory(ToModel.toSubCategory(subCategoryView));
        dao.createOrUpdate(wordToSave);
        fillWithData();
    }

    public void deleteFromDataBaseById(){
        CommonDao dao = new CommonDao();
        dao.deleteById(Word.class, selectedWord.get().getId());
    }

    public void fillWithData() {

        wordWiewList.clear();
        System.out.println(Arrays.toString(dao.queryForAll(Word.class).toArray()));
        dao.queryForAll(Word.class).forEach(word->wordWiewList.add(ToView.toWordView(word)));
    }

    public void setSelectedItems()
    {
        List<WordView> result = new LinkedList<>();
        for (WordView wordWiew : wordWiewList)
        {
            if (wordWiew.isChecked())
            {
                result.add(wordWiew);
            }
        }
        selectedItems = result;

    }

    public void updateInDataBase(String word) {

        Word wordtmp = dao.findByID(Word.class, selectedWord.get().getId());
        wordtmp.setIssue(word);
        dao.createOrUpdate(wordtmp);
    }

    public ObservableList<WordView> getWordWiewList() {
        return wordWiewList;
    }

    public WordView getSelectedWord() {
        return selectedWord.get();
    }

    public ObjectProperty<WordView> selectedWordProperty() {
        return selectedWord;
    }

    public void setSelectedWord(WordView selectedWord) {
        this.selectedWord.set(selectedWord);
    }



}
