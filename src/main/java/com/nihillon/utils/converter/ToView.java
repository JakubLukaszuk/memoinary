package com.nihillon.utils.converter;

import com.j256.ormlite.dao.ForeignCollection;
import com.nihillon.models.Category;
import com.nihillon.models.SubCategory;
import com.nihillon.models.Word;
import com.nihillon.viewModel.CategoryModel;
import com.nihillon.viewModel.CategoryView;
import com.nihillon.viewModel.SubCategoryView;
import com.nihillon.viewModel.WordView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


public class ToView {
    public static CategoryView toCategoryView(Category category){
        CategoryView categoryView = new CategoryView();
        categoryView.setCategoryName(category.getCategory());
        categoryView.setId(category.getId());
        categoryView.setDescription(category.getDescription());
        categoryView.setDateOfAddition(category.getDateOfAddition().toString());
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
        subCategoryView.setCategoryName(subCategory.getSubCategory());
        subCategoryView.setId(subCategory.getId());
        subCategoryView.setCategoryId(subCategory.getCategory().getId());
        subCategoryView.setDescription(subCategory.getDescription());
        subCategoryView.setDateOfAddition(subCategory.getDateOfAddition().toString());
        System.out.println(subCategoryView);
        return subCategoryView;
    }



    public static WordView toWordView(Word word){
        WordView wordView = new WordView();
        wordView.setCategory(toCategoryView(word.getCategory()));
        wordView.setSubCategory(toSubCategoryView(word.getSubCategory()));
        wordView.setDateOfAddition(word.getDateOfAddition().toString());
        wordView.setIssue(word.getIssue());
        wordView.setKnowledgeStatus(word.getKnowledgeStatus());
        wordView.setMean(word.getMean());
        wordView.setId(word.getId());
        return wordView;
    }
}
