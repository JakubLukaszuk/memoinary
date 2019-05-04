package com.jlukaszuk.viewModel;

import com.jlukaszuk.dao.CommonDao;
import com.jlukaszuk.models.Category;
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
public class CategoryModel {
    private final CommonDao dao;

    private ObservableList<CategoryView> categoryViewList = FXCollections.observableArrayList();

    @Autowired
    public CategoryModel(CommonDao dao) {
        this.dao = dao;
    }

    public ObservableList<CategoryView> getCategoryViewList() {
        return categoryViewList;
    }


    public void saveToDataBase(String discription, String categoryName) throws SQLException, IOException {
        Category category = new Category();
        category.setCategory(categoryName);
        category.setDateOfAddition(new Date());
        dao.createOrUpdate(category);
        fillWithData();

    }

    public void deleteFromDataBaseById(CategoryView categoryView) throws SQLException, IOException {
        dao.deleteById(Category.class, categoryView.getId());
        fillWithData();
    }


    public void fillWithData() throws SQLException, IOException {
        categoryViewList.clear();
        dao.queryForAll(Category.class).forEach(category->categoryViewList.add(ToView.toCategoryView(category)));
        DbManager.closeConnection();
    }


}
