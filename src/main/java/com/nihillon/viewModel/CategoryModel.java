package com.nihillon.viewModel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nihillon.dao.CommonDao;
import com.nihillon.models.Category;
import com.nihillon.models.Word;
import com.nihillon.utils.DbManager;
import com.nihillon.utils.converter.ToView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Observable;

@Component
public class CategoryModel {
    private final CommonDao dao;

    private ObservableList<CategoryView> categoryViewList = FXCollections.observableArrayList();
    private ObjectProperty<CategoryView> selcetedCategory = new SimpleObjectProperty<>();

    @Autowired
    public CategoryModel(CommonDao dao) {
        this.dao = dao;
    }

    public ObservableList<CategoryView> getCategoryViewList() {
        return categoryViewList;
    }

    public void setCategoryViewList(ObservableList<CategoryView> categoryViewList) {
        this.categoryViewList = categoryViewList;
    }

    public CategoryView getSelcetedCategory() {
        return selcetedCategory.get();
    }

    public ObjectProperty<CategoryView> selcetedCategoryProperty() {
        return selcetedCategory;
    }

    public void setSelcetedCategory(CategoryView selcetedCategory) {
        this.selcetedCategory.set(selcetedCategory);
    }

    public void saveToDataBase(String discription, String categoryName) throws SQLException {
//        CommonDao dao = new CommonDao();
        Category category = new Category();
        category.setDescription(discription);
        category.setCategory(categoryName);
        category.setDateOfAddition(new Date());
        dao.createOrUpdate(category);
        fillWithData();

    }

    public void deleteFromDataBaseById(CategoryView categoryView) throws SQLException {
//        CommonDao dao = new CommonDao();
        dao.deleteById(Category.class, categoryView.getId());
        fillWithData();
    }

    public void deleteFromDataBaseById() throws SQLException {
//        CommonDao dao = new CommonDao();
        dao.deleteById(Category.class, selcetedCategory.get().getId());
    }

    public void fillWithData() throws SQLException {
//        CommonDao dao = new CommonDao();
        categoryViewList.clear();
        dao.queryForAll(Category.class).forEach(category->categoryViewList.add(ToView.toCategoryView(category)));
        DbManager.closeConnection();
    }


    public void updateInDataBase(String newCategoryName) throws SQLException {
//        CommonDao dao = new CommonDao();
        Category categorytmp = dao.findByID(Category.class, selcetedCategory.get().getId());
        categorytmp.setCategory(newCategoryName);
        dao.createOrUpdate(categorytmp);
    }

    private List<Word> getWordWithCurrentCategory() throws SQLException {
//        CommonDao commonDao = new CommonDao();
        Dao wordDao = dao.getDao(Word.class);
        QueryBuilder queryBuilder = wordDao.queryBuilder();
        queryBuilder.where().eq("WORD", dao.findByID(Category.class, selcetedCategory.get().getId()));
        PreparedQuery<Word> query = queryBuilder.prepare();
        DbManager.closeConnection();
        //System.out.println( wordDao.query(query));
        return wordDao.query(query);
    }

}
