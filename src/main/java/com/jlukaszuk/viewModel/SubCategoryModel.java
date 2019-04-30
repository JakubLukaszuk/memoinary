package com.jlukaszuk.viewModel;

import com.jlukaszuk.dao.CommonDao;
import com.jlukaszuk.models.Category;
import com.jlukaszuk.models.SubCategory;
import com.jlukaszuk.utils.DbManager;
import com.jlukaszuk.utils.converter.ToView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;


@Component
public class SubCategoryModel {
    private final CommonDao dao;

    private ObservableList<SubCategoryView> subCategoryViewList = FXCollections.observableArrayList();

    @Autowired
    public SubCategoryModel(CommonDao dao) {
        this.dao = dao;
    }

    public void saveToDataBase(String discription, String subCategoryName, CategoryView category) throws SQLException, IOException {
        SubCategory subCategory = new SubCategory();
        subCategory.setDescription(discription);
        subCategory.setSubCategory(subCategoryName);
        subCategory.setCategory(dao.findByID(Category.class, category.getId()));
        subCategory.setDateOfAddition(new Date());
        dao.createOrUpdate(subCategory);
        fillWithData();
    }

    public void deleteFromDataBaseById(SubCategoryView subCategoryView) throws SQLException, IOException {
        dao.deleteById(SubCategory.class, subCategoryView.getId());
        fillWithData();
    }


    public void fillWithData() throws SQLException, IOException {
        subCategoryViewList.clear();
        dao.queryForAll(SubCategory.class).forEach(subCategory -> subCategoryViewList.add(ToView.toSubCategoryView(subCategory)));
        DbManager.closeConnection();
    }

    public void deleteByCategory(CategoryView categoryView) throws SQLException, IOException {

        for (SubCategoryView subCat: categoryView.getSubCategories())
        {
            for (int i=0; i<subCategoryViewList.size(); i++)
            {
                if (subCat.getId() == subCategoryViewList.get(i).getId()){
                    deleteFromDataBaseById(subCategoryViewList.get(i));
                }
            }
        }

        fillWithData();

    }
}
