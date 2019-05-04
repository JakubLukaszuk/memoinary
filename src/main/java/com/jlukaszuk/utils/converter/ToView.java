package com.jlukaszuk.utils.converter;

import com.jlukaszuk.models.Category;
import com.jlukaszuk.models.SubCategory;
import com.jlukaszuk.models.Word;
import com.jlukaszuk.viewModel.CategoryView;
import com.jlukaszuk.viewModel.SubCategoryView;
import com.jlukaszuk.viewModel.WordView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class ToView {
    private final static DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
    public static CategoryView toCategoryView(Category category){
        CategoryView categoryView = new CategoryView();
        if (category!=null){
            categoryView.setCategoryName(category.getCategory());
            categoryView.setId(category.getId());
            categoryView.setDateOfAddition(formatter.format(category.getDateOfAddition()));
            ObservableList<SubCategoryView> subCategoryViewObservableList = FXCollections.observableArrayList();
            category.getSubCategoryForeignCollection().forEach(subCategory -> subCategoryViewObservableList.add(toSubCategorySimpleView(subCategory)));
            categoryView.setSubCategories(subCategoryViewObservableList);
        }


        return categoryView;
    }



    public static SubCategoryView toSubCategoryView(SubCategory subCategory){
        SubCategoryView subCategoryView = toSubCategorySimpleView(subCategory);
        if (subCategory!=null) {
            subCategoryView.setCategoryViewObjectProperty(toCategoryView(subCategory.getCategory()));
        }
        //System.out.println(subCategoryView);
        return subCategoryView;
    }

    public static SubCategoryView toSubCategorySimpleView(SubCategory subCategory){
        SubCategoryView subCategoryView = new SubCategoryView();
        if (subCategory!=null){
            subCategoryView.setSubCategoryName(subCategory.getSubCategory());
            subCategoryView.setId(subCategory.getId());
            subCategoryView.setCategoryId(subCategory.getCategory().getId());
            subCategoryView.setDateOfAddition(formatter.format(subCategory.getDateOfAddition()));
        }

        return subCategoryView;
    }



    public static WordView toWordView(Word word){
        WordView wordView = new WordView();
        wordView.setCategory(toCategoryView(word.getCategory()));
        wordView.setSubCategory(toSubCategoryView(word.getSubCategory()));
        wordView.setKnowledgeStatus(word.getKnowledgeStatus());
        wordView.setDateOfAddition(formatter.format(word.getDateOfAddition()));
        if (word.getIssue()!=null)
        wordView.setIssue(word.getIssue());
        else
            word.setIssue("");
        if (!word.getKnowledgeStatus())
        wordView.setKnowledgeStatus(word.getKnowledgeStatus());
        if (word.getMean()!=null)
        wordView.setMean(word.getMean());
        else
            word.setMean("");
        wordView.setId(word.getId());
        return wordView;
    }
}
