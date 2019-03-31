package com.nihillon.utils.converter;

import com.nihillon.models.Category;
import com.nihillon.models.SubCategory;
import com.nihillon.models.Word;
import com.nihillon.viewModel.CategoryView;
import com.nihillon.viewModel.SubCategoryView;
import com.nihillon.viewModel.WordView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class ToView {
    private static DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
    public static CategoryView toCategoryView(Category category){
        CategoryView categoryView = new CategoryView();
        categoryView.setCategoryName(category.getCategory());
        categoryView.setId(category.getId());
        categoryView.setDateOfAddition(formatter.format(category.getDateOfAddition()));
        ObservableList<SubCategoryView> subCategoryViewObservableList = FXCollections.observableArrayList();
        category.getSubCategoryForeignCollection().forEach(subCategory -> subCategoryViewObservableList.add(toSubCategorySimpleView(subCategory)));
        categoryView.setSubCategories(subCategoryViewObservableList);
        return categoryView;
    }



    public static SubCategoryView toSubCategoryView(SubCategory subCategory){
        SubCategoryView subCategoryView = toSubCategorySimpleView(subCategory);
        subCategoryView.setCategoryViewObjectProperty(toCategoryView(subCategory.getCategory()));
        System.out.println(subCategoryView);
        return subCategoryView;
    }

    public static SubCategoryView toSubCategorySimpleView(SubCategory subCategory){
        SubCategoryView subCategoryView = new SubCategoryView();
        subCategoryView.setSubCategoryName(subCategory.getSubCategory());
        subCategoryView.setId(subCategory.getId());
        subCategoryView.setCategoryId(subCategory.getCategory().getId());
        subCategoryView.setDescription(subCategory.getDescription());
        subCategoryView.setDateOfAddition(formatter.format(subCategory.getDateOfAddition()));
        System.out.println(subCategoryView);
        return subCategoryView;
    }



    public static WordView toWordView(Word word){
        WordView wordView = new WordView();
        if (word.getCategory()!=null)
        wordView.setCategory(toCategoryView(word.getCategory()));
        if (word.getSubCategory()!=null)
        wordView.setSubCategory(toSubCategoryView(word.getSubCategory()));
        wordView.setDateOfAddition(word.getDateOfAddition().toString());
        wordView.setIssue(word.getIssue());
        if (!word.getKnowledgeStatus())
        wordView.setKnowledgeStatus(word.getKnowledgeStatus());
        wordView.setMean(word.getMean());
        wordView.setId(word.getId());
        return wordView;
    }
}
