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

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Callable;

@Component
public class WordModel {
    final private CommonDao dao;

    private ObservableList<WordView> wordWiewList = FXCollections.observableArrayList();
    private List<WordView> modifed = new ArrayList<>();

    @Autowired
    public WordModel(CommonDao dao) {
        this.dao = dao;
    }

    public void saveToDataBase(String mean, String word, CategoryView categoryView, SubCategoryView subCategoryView, boolean status) throws ParseException, SQLException {
        Word wordToSave = new Word();
        Date current = new Date();

        if (subCategoryView!=null)
        subCategoryView.setCategoryViewObjectProperty(categoryView);
        wordToSave.setDateOfAddition(current);
        if (mean!=null)
        wordToSave.setMean(mean);
        if (word!=null)
        wordToSave.setIssue(word);
        wordToSave.setKnowledgeStatus(status);
        if (categoryView!=null)
        wordToSave.setCategory( ToModel.toCategory(categoryView));
        if (subCategoryView!=null)
        wordToSave.setSubCategory(ToModel.toSubCategory(subCategoryView));
        dao.createOrUpdate(wordToSave);
        fillWithData();
    }

    public void deleteFromDataBaseById(WordView wordView){
        CommonDao dao = new CommonDao();
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

//    public void setSelectedItems()
//    {
//        List<WordView> result = new LinkedList<>();
//        for (WordView wordWiew : wordWiewList)
//        {
//            if (wordWiew.isChecked())
//            {
//                result.add(wordWiew);
//            }
//        }
//        selectedItems = result;
//        System.out.println(selectedItems);
//
//    }

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
        getSelectedItems().forEach(this::deleteFromDataBaseById);
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
                                for (WordView wordView: modifed) {
                                    dao.createOrUpdate(ToModel.toWord(wordView));
                                }
                                return (Void) null;
                            }
                        });
                modifed.clear();
                fillWithData();
    }


    public ObservableList<WordView> getWordWiewList() {
        return wordWiewList;
    }

    public List<WordView> getModifed() {
        return modifed;
    }

    public void filtrWords(String mean, String issue, CategoryView categoryView, SubCategoryView subCategoryView) throws SQLException, ParseException {
        System.out.println("mean: "+mean+" issue: "+issue+" categoryView: "+categoryView+" subCateogory: "+subCategoryView );
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
                if (word.getSubCategory() == subCategoryView)
                    result.add(word);
            }

        } else if (categoryView != null) {
            for (WordView word : wordWiewList) {
                if (word.getCategory() == categoryView)
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


}
