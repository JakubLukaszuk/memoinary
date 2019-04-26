package com.nihillon.viewModel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nihillon.dao.CommonDao;
import com.nihillon.models.Word;
import com.nihillon.utils.DbManager;
import com.nihillon.utils.converter.ToModel;
import com.nihillon.utils.converter.ToView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.util.calendar.LocalGregorianCalendar;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

@Component
public class WordModel {
    final private CommonDao dao;

    private ObservableList<WordView> wordWiewList = FXCollections.observableArrayList();

    @Autowired
    public WordModel(CommonDao dao) {
        this.dao = dao;
    }

    public void saveToDataBase(String mean, String issue, CategoryView categoryView, SubCategoryView subCategoryView, boolean status) throws ParseException, SQLException {
        Word wordToSave = new Word();
        Date current = new Date();

        if (subCategoryView!=null)
        subCategoryView.setCategoryViewObjectProperty(categoryView);
        wordToSave.setDateOfAddition(current);
        if (mean!=null)
        wordToSave.setMean(mean);
        if (issue!=null)
        wordToSave.setIssue(issue);
        wordToSave.setKnowledgeStatus(status);
        if (categoryView!=null)
        wordToSave.setCategory( ToModel.toCategory(categoryView));
        if (subCategoryView!=null)
        wordToSave.setSubCategory(ToModel.toSubCategory(subCategoryView));
        dao.createOrUpdate(wordToSave);
        fillWithData();
    }


    public void deleteFromDataBaseById(WordView wordView) throws SQLException {
        dao.deleteById(Word.class, wordView.getId());
    }


    public void fillWithData() throws SQLException {

        wordWiewList.clear();
        TransactionManager.callInTransaction(DbManager.getConnectionSource(),
                new Callable<Void>()
                {
                    public Void call() throws Exception
                    {
                        dao.queryForAll(Word.class).forEach(word->wordWiewList.add(ToView.toWordView(word)));
                        return (Void) null;
                    }
                });
        DbManager.closeConnection();
    }


    private List<WordView> getSelectedItems()
    {
        List<WordView> result = new LinkedList<>();
        for (WordView wordWiew : wordWiewList)
        {
            if (wordWiew.isChecked())
            {
                result.add(wordWiew);
            }
        }
        return result;

    }

    public void deleteSelectedWords() throws SQLException {
        //getSelectedItems().forEach(this::deleteFromDataBaseById);
        System.out.println(getSelectedItems().get(0).getMean());
        getSelectedItems().forEach(item -> {
            try {
                deleteFromDataBaseById(item);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        fillWithData();
    }

    public void deleteBySubCategory(SubCategoryView subCategoryView) throws SQLException {
        TransactionManager.callInTransaction(DbManager.getConnectionSource(),
                new Callable<Void>()
                {
                    public Void call() throws Exception
                    {
                        for (WordView wordView : wordWiewList) {
                            if (wordView.getSubCategory().getId() == subCategoryView.getId()){
                                deleteFromDataBaseById(wordView);
                            }
                        }
                        return (Void) null;
                    }
                });
        fillWithData();
    }

    public void deleteByCategory(CategoryView categoryView) throws SQLException {
        TransactionManager.callInTransaction(DbManager.getConnectionSource(),
                new Callable<Void>()
        {
            public Void call() throws Exception
            {
                for (WordView wordView : wordWiewList) {
                    if (wordView.getCategory().getId() == categoryView.getId()){
                        deleteFromDataBaseById(wordView);
                    }
                }
                return (Void) null;
            }
        });
        fillWithData();
    }


    public void updateInDataBase() throws ParseException, SQLException {

                TransactionManager.callInTransaction(DbManager.getConnectionSource(),
                        new Callable<Void>()
                        {
                            public Void call() throws Exception
                            {
                                for (WordView wordView: wordWiewList) {
                                    if (wordView.isModifed())
                                        dao.createOrUpdate(ToModel.toWord(wordView));
                                    wordView.setModifed(false);
                                }
                                return (Void) null;
                            }
                        });

                fillWithData();
    }

    public void realoadDataFromDB() throws SQLException {
        fillWithData();
    }


    public ObservableList<WordView> getWordWiewList() {
        return wordWiewList;
    }


    public void filtrWords(String mean, String issue, CategoryView categoryView, SubCategoryView subCategoryView) throws SQLException, ParseException {
        fillWithData();
        if (categoryView!=null)
            filtrByCategory(categoryView, subCategoryView);
        if (!mean.equals("") || !issue.equals(""))
            filtrByWords(mean, issue);
    }

    private void filtrByCategory(CategoryView categoryView, SubCategoryView subCategoryView) throws SQLException, ParseException
    {
        List<WordView> result = new LinkedList<>();

        if (subCategoryView != null) {
            for (WordView word : wordWiewList) {
                if (word.getSubCategory().equals(subCategoryView))
                    result.add(word);
            }

        } else if (categoryView != null) {
            for (WordView word : wordWiewList) {
                if (word.getCategory().equals(categoryView))
                    result.add(word);
            }
        }

        wordWiewList.clear();
        wordWiewList.addAll(result);
    }

    private void filtrByWords(String mean, String issue)
    {
        List<WordView> result = new LinkedList<>();

        if (!mean.equals("") && issue.equals("")){
            for (WordView word: wordWiewList) {
                if (word.getMean().contains(mean))
                    result.add(word);
            }
        }
        else{
            for (WordView word: wordWiewList) {
                if (word.getIssue().contains(issue))
                    result.add(word);
            }
        }
        if (!issue.equals("") && !issue.equals("")){
            for (WordView word: wordWiewList) {
                if (word.getIssue().contains(issue) && !word.getMean().contains(mean))
                    result.add(word);
            }
        }
        wordWiewList.clear();
        wordWiewList.addAll(result);
    }

    public void repleaceByViewFromDB(WordView wordView) throws SQLException {
        WordView resived = ToView.toWordView(dao.findByID(Word.class, wordView.getId()));
        if (resived!=null){
            for (WordView word: wordWiewList) {
                if (word.getId() == resived.getId()){
                    Collections.replaceAll(wordWiewList, word, resived);
                }
            }
        }
    }

    public void saveToDataBase(WordView wordView) throws SQLException {
        Word wordToSave = new Word();
        Date current = new Date();
        wordToSave.setKnowledgeStatus(wordView.isKnowledgeStatus());
        wordToSave.setIssue(wordView.getIssue());
        wordToSave.setMean(wordView.getMean());
        wordToSave.setDateOfAddition(current);
        dao.createOrUpdate(wordToSave);
    }
}
