package com.nihillon.viewModel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nihillon.dao.CommonDao;
import com.nihillon.models.Category;
import com.nihillon.models.Word;
import com.nihillon.utils.DbManager;
import com.nihillon.utils.converter.ToView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;

@Component
public class WordModel {

    private ObservableList<WordView> wordWiewList = FXCollections.observableArrayList();
    private ObjectProperty<WordView> selectedWord;
    private List<WordView> selectedItems = new ArrayList<>();


    public void saveToDataBase(String mean, String word) {
        CommonDao dao = new CommonDao();
        Word wordToSave = new Word();
        Date current = new Date();
        wordToSave.setDateOfAddition(current);
        dao.createOrUpdate(wordToSave);
    }

    public void deleteFromDataBaseById(){
        CommonDao dao = new CommonDao();
        dao.deleteById(Word.class, selectedWord.get().getId());
    }

    public void fillWithData() {
        CommonDao dao = new CommonDao();
        wordWiewList.clear();
        System.out.println(Arrays.toString(dao.queryForAll(Word.class).toArray()));
        dao.queryForAll(Word.class).forEach(word->wordWiewList.add(ToView.toWordView(word)));
    }

    public ObservableList<WordView> getWordWiewList() {
        return wordWiewList;
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
        CommonDao dao = new CommonDao();
        Word wordtmp = dao.findByID(Word.class, selectedWord.get().getId());
        wordtmp.setIssue(word);
        dao.createOrUpdate(wordtmp);
    }


}
