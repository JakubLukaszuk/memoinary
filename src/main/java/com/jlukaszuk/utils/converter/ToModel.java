package com.jlukaszuk.utils.converter;

import com.jlukaszuk.models.Category;
import com.jlukaszuk.models.SubCategory;
import com.jlukaszuk.models.Word;
import com.jlukaszuk.viewModel.CategoryView;
import com.jlukaszuk.viewModel.SubCategoryView;
import com.jlukaszuk.viewModel.WordView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ToModel {
    private final static DateFormat format = new SimpleDateFormat("dd-MMM-yy");

    public static Category toCategory(CategoryView categoryView) throws ParseException {
        Category category = new Category();
        if (categoryView!=null){
            category.setId(categoryView.getId());
            category.setCategory(categoryView.getCategoryName());
            //Sytem.out.println(categoryView.getDateOfAddition());
            category.setDateOfAddition(format.parse(categoryView.getDateOfAddition()));
            category.setDescription(categoryView.getDescription());
        }
        return category;
    }

    public static SubCategory toSubCategory(SubCategoryView subCategoryView) throws ParseException {
        SubCategory subCategory = new SubCategory();
        subCategory.setId(subCategoryView.getId());
        subCategory.setSubCategory(subCategoryView.getSubCategoryName());
        subCategory.setCategory(toCategory(subCategoryView.getCategoryViewObjectProperty()));
        subCategory.setDescription(subCategoryView.getDescription());
        subCategory.setDateOfAddition(format.parse(subCategoryView.getDateOfAddition()));
        return subCategory;
    }

    public static Word toWord(WordView wordView) throws ParseException {
        Word word = new Word();

        word.setId(wordView.getId());
        word.setMean(wordView.getMean());
        word.setKnowledgeStatus(wordView.isKnowledgeStatus());
        word.setIssue(wordView.getIssue());
        word.setDateOfAddition(format.parse(wordView.getDateOfAddition()));

        Category category = new Category();
        category.setId(wordView.getCategory().getId());
        category.setCategory(wordView.getCategory().getCategoryName());
        category.setDateOfAddition(format.parse(wordView.getCategory().getDateOfAddition()));
        word.setCategory(category);

        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(word.getCategory());
        subCategory.setId(wordView.getSubCategory().getId());
        subCategory.setSubCategory(wordView.getSubCategory().getSubCategoryName());
        subCategory.setDateOfAddition(format.parse(wordView.getSubCategory().getDateOfAddition()));
        word.setSubCategory(subCategory);
        return word;
    }
}
