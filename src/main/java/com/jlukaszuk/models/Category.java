package com.jlukaszuk.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Category implements BaseModel {

    @DatabaseField(generatedId = true)
    private int id;

    @ForeignCollectionField
    private ForeignCollection<SubCategory> subCategoryForeignCollection;

    @DatabaseField(columnName = "CATEGORY_NAME")
    private String category;

    @DatabaseField(columnName = "DATE_OF_ADDITION", dataType = DataType.DATE_STRING,
            format = "dd-MMM-yy")
    private Date dateOfAddition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ForeignCollection<SubCategory> getSubCategoryForeignCollection() {
        return subCategoryForeignCollection;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Date getDateOfAddition() {
        return dateOfAddition;
    }

    public void setDateOfAddition(Date dateOfAddition) {
        this.dateOfAddition = dateOfAddition;
    }

    public void setSubCategoryForeignCollection(ForeignCollection<SubCategory> subCategoryForeignCollection) {
        this.subCategoryForeignCollection = subCategoryForeignCollection;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", subCategoryForeignCollection=" + subCategoryForeignCollection +
                ", category='" + category + '\'' +
                ", dateOfAddition=" + dateOfAddition +
                '}';
    }
}
