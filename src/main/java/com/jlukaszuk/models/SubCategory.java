package com.jlukaszuk.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class SubCategory implements BaseModel {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "CATEGORY_ID")
    private Category category;

    @DatabaseField(columnName = "SUBCATEGORY_NAME")
    private String subCategory;

    @DatabaseField(columnName = "DATE_OF_ADDITION", dataType = DataType.DATE_STRING,
            format = "dd-MMM-yy")
    private Date dateOfAddition;

    public int getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }


    public Date getDateOfAddition() {
        return dateOfAddition;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SubCategory() {
    }

    public void setDateOfAddition(Date dateOfAddition) {
        this.dateOfAddition = dateOfAddition;
    }

    public SubCategory(Category category, String subCategory, Date dateOfAddition) {
        this.category = category;
        this.subCategory = subCategory;
        this.dateOfAddition = dateOfAddition;
    }



    @Override
    public String toString() {
        return "SubCategory{" +
                "id=" + id +
                ", category=" + category +
                ", subCategory='" + subCategory + '\'' +
                ", dateOfAddition=" + dateOfAddition +
                '}';
    }
}
