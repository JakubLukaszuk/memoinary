package com.nihillon.viewModel;

import javafx.beans.property.*;
import org.springframework.stereotype.Component;

@Component
public class WordView {
    private IntegerProperty id;
    private StringProperty issue;
    private StringProperty mean;
    private BooleanProperty knowledgeStatus;
    private StringProperty dateOfAddition;
    private SimpleObjectProperty<CategoryView> category;
    private SimpleObjectProperty<SubCategoryView> subCategory;
    private BooleanProperty checked = new SimpleBooleanProperty();



    public WordView() {
        id = new SimpleIntegerProperty();
        issue = new SimpleStringProperty();
        mean = new SimpleStringProperty();
        knowledgeStatus = new SimpleBooleanProperty();
        dateOfAddition = new SimpleStringProperty();
        category = new SimpleObjectProperty<>();
        subCategory = new SimpleObjectProperty<>();
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getIssue() {
        return issue.get();
    }

    public StringProperty issueProperty() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue.set(issue);
    }

    public String getMean() {
        return mean.get();
    }

    public StringProperty meanProperty() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean.set(mean);
    }

    public boolean isKnowledgeStatus() {
        return knowledgeStatus.get();
    }

    public BooleanProperty knowledgeStatusProperty() {
        return knowledgeStatus;
    }

    public void setKnowledgeStatus(boolean knowledgeStatus) {
        this.knowledgeStatus.set(knowledgeStatus);
    }

    public String getDateOfAddition() {
        return dateOfAddition.get();
    }

    public StringProperty dateOfAdditionProperty() {
        return dateOfAddition;
    }

    public void setDateOfAddition(String dateOfAddition) {
        this.dateOfAddition.set(dateOfAddition);
    }

    public boolean isChecked() {
        return checked.get();
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }


    public CategoryView getCategory() {
        return category.get();
    }

    public SimpleObjectProperty<CategoryView> categoryProperty() {
        return category;
    }

    public void setCategory(CategoryView category) {
        this.category.set(category);
    }

    public SubCategoryView getSubCategory() {
        return subCategory.get();
    }

    public SimpleObjectProperty<SubCategoryView> subCategoryProperty() {
        return subCategory;
    }

    public void setSubCategory(SubCategoryView subCategory) {
        this.subCategory.set(subCategory);
    }

    @Override
    public String toString() {
        return "id: "+id.getValue()+" issue: "+issue.getValue()+" mean: "+mean.getValue()+" Subcategory: "+subCategory.get().subCategoryNameProperty().getValue()+" Category :"+category.get().categoryNameProperty().getValue()+" status: "+knowledgeStatus.getValue()+" date: "+dateOfAddition.getValue();
    }
}
