package com.nihillon.utils.converter;

import com.nihillon.models.Category;
import com.nihillon.models.SubCategory;
import com.nihillon.models.Word;
import com.nihillon.viewModel.CategoryView;
import com.nihillon.viewModel.SubCategoryView;
import com.nihillon.viewModel.WordView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ToModel {
    private static DateFormat format = new SimpleDateFormat("dd-MMM-yy");
    public static Category toCategory(CategoryView categoryView) throws ParseException {
        Category category = new Category();
        category.setCategory(categoryView.getCategoryName());
        System.out.println(categoryView.getDateOfAddition());
        category.setDateOfAddition(format.parse(categoryView.getDateOfAddition()));
        category.setDescription(categoryView.getDescription());
        return category;
    }

    public static SubCategory toSubCategory(SubCategoryView subCategoryView) throws ParseException {
        SubCategory subCategory = new SubCategory();
        subCategory.setSubCategory(subCategoryView.getSubCategoryName());
        subCategory.setCategory(toCategory(subCategoryView.getCategoryViewObjectProperty()));
        subCategory.setDescription(subCategoryView.getDescription());
        subCategory.setDateOfAddition(format.parse(subCategoryView.getDateOfAddition()));
        return subCategory;
    }

    public static Word toWord(WordView wordView) throws ParseException {
        Word word = new Word();
        word.setDateOfAddition( format.parse(wordView.getDateOfAddition()));
        word.setSubCategory(toSubCategory(wordView.getSubCategory()));
        word.setCategory(toCategory(wordView.getCategory()));
        word.setMean(wordView.getMean());
        word.setKnowledgeStatus(wordView.isKnowledgeStatus());
        word.setIssue(wordView.getIssue());
        return word;
    }
}
