package com.nihillon.viewModel;

import com.nihillon.models.SubCategory;
import javafx.beans.property.*;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SubCategoryView {
    private IntegerProperty id;
    private IntegerProperty categoryId;
    private StringProperty categoryName;
    private StringProperty description;
    private StringProperty dateOfAddition;
    private ObjectProperty<CategoryView> categoryViewObjectProperty;

    public SubCategoryView() {
        id = new SimpleIntegerProperty();
        categoryId = new SimpleIntegerProperty();
        categoryName = new SimpleStringProperty();
        description = new SimpleStringProperty();
        dateOfAddition = new SimpleStringProperty();
        categoryViewObjectProperty = new SimpleObjectProperty<>();
    }

    public CategoryView getCategoryViewObjectProperty() {
        return categoryViewObjectProperty.get();
    }

    public ObjectProperty<CategoryView> categoryViewObjectPropertyProperty() {
        return categoryViewObjectProperty;
    }

    public void setCategoryViewObjectProperty(CategoryView categoryViewObjectProperty) {
        this.categoryViewObjectProperty.set(categoryViewObjectProperty);
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

    public int getCategoryId() {
        return categoryId.get();
    }

    public IntegerProperty categoryIdProperty() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId.set(categoryId);
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public StringProperty categoryNameProperty() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
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
