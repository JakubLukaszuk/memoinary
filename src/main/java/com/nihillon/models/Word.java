package com.nihillon.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;


@DatabaseTable
public class Word implements BaseModel {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "ISSUE")
    private String issue;

    @DatabaseField(columnName = "MEAN")
    private String mean;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "CATEGORY", foreignColumnName = "CATEGORY_NAME")
    private Category category;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "SUBCATEGORY", foreignColumnName = "SUBCATEGORY_NAME")
    private SubCategory subCategory;

    @DatabaseField(columnName = "DATE_OF_ADDITION", dataType = DataType.DATE_STRING,
            format = "dd-MMM-yy")
    private Date dateOfAddition;

    @DatabaseField(columnName = "KNOWLADGE_STATUS")
    private Boolean knowledgeStatus;

    public int getId() {
        return id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public Date getDateOfAddition() {
        return dateOfAddition;
    }

    public void setDateOfAddition(Date dateOfAddition) {
        this.dateOfAddition = dateOfAddition;
    }

    public Boolean getKnowledgeStatus() {
        return knowledgeStatus;
    }

    public void setKnowledgeStatus(Boolean knowledgeStatus) {
        this.knowledgeStatus = knowledgeStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", issue='" + issue + '\'' +
                ", mean='" + mean + '\'' +
                ", category=" + category +
                ", subCategory=" + subCategory +
                ", dateOfAddition=" + dateOfAddition +
                ", knowledgeStatus=" + knowledgeStatus +
                '}';
    }
}
