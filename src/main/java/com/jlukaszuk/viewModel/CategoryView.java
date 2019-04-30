package com.jlukaszuk.viewModel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

@Component
public class CategoryView {

    private IntegerProperty id;
    private ObservableList<SubCategoryView> subCategories;
    private StringProperty categoryName;
    private StringProperty description;
    private StringProperty dateOfAddition;

    public int getId() {
        return id.get();
    }

    public CategoryView() {
        id = new SimpleIntegerProperty();
        subCategories = FXCollections.observableArrayList();
        categoryName = new SimpleStringProperty();
        description = new SimpleStringProperty();
        dateOfAddition = new SimpleStringProperty();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public ObservableList<SubCategoryView> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ObservableList<SubCategoryView> subCategories) {
        this.subCategories = subCategories;
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public StringProperty categoryNameProperty() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.setValue(categoryName);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
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

    @Override
    public String toString() {
        return  categoryName.getValue();
    }
}
