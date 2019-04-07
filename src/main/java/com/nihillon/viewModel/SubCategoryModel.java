package com.nihillon.viewModel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nihillon.dao.CommonDao;
import com.nihillon.models.Category;
import com.nihillon.models.SubCategory;
import com.nihillon.models.Word;
import com.nihillon.utils.DbManager;
import com.nihillon.utils.converter.ToView;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

@Component
public class SubCategoryModel {
    private final CommonDao dao;

    private ObservableList<SubCategoryView> subCategoryViewList = FXCollections.observableArrayList();
    private ObjectProperty<SubCategoryView> selcetedSubCategory;

    @Autowired
    public SubCategoryModel(CommonDao dao) {
        this.dao = dao;
    }

    public void saveToDataBase(String discription, String subCategoryName, CategoryView category) {
        SubCategory subCategory = new SubCategory();
        subCategory.setDescription(discription);
        subCategory.setSubCategory(subCategoryName);
        subCategory.setCategory(dao.findByID(Category.class, category.getId()));
        subCategory.setDateOfAddition(new Date());
        dao.createOrUpdate(subCategory);
        fillWithData();
    }

    public void deleteFromDataBaseById(SubCategoryView subCategoryView){
        dao.deleteById(SubCategory.class, subCategoryView.getId());
        fillWithData();
    }

    public void deleteFromDataBaseById(){
        dao.deleteById(SubCategory.class, selcetedSubCategory.get().getId());
    }

    public void fillWithData() {
        subCategoryViewList.clear();
        dao.queryForAll(SubCategory.class).forEach(subCategory -> subCategoryViewList.add(ToView.toSubCategoryView(subCategory)));
        DbManager.closeConnection();
        //System.out.println(subCategoryViewList);
    }

    public void deleteByCategory(CategoryView categoryView) throws SQLException {

        TransactionManager.callInTransaction(DbManager.getConnectionSource(),
                new Callable<Void>()
                {
                    public Void call() throws Exception
                    {
                        for (SubCategoryView subCat: categoryView.getSubCategories())
                        {
                            for (int i=0; i<subCategoryViewList.size(); i++)
                            {
                                if (subCat.getId() == subCategoryViewList.get(i).getId()){
                                    deleteFromDataBaseById(subCategoryViewList.get(i));
                                }
                            }
                        }
                        return (Void) null;
                    }
                });


        fillWithData();

    }


    public void updateInDataBase(String newCategoryName) {

        SubCategory subCategorytmp = dao.findByID(SubCategory.class, selcetedSubCategory.get().getId());
        subCategorytmp.setSubCategory(newCategoryName);
        dao.createOrUpdate(subCategorytmp);
    }

//    private List<Word> getWordWithCurrentSubCategory() throws SQLException {
//        CommonDao commonDao = new CommonDao();
//        Dao dao = commonDao.getDao(Word.class);
//        QueryBuilder queryBuilder = dao.queryBuilder();
//        queryBuilder.where().eq("WORD", commonDao.findByID(Category.class, selcetedCategory.get().getId()));
//        PreparedQuery<Word> query = queryBuilder.prepare();
//        DbManager.closeConnection();
//        System.out.println( dao.query(query));
//        return dao.query(query);
//    }
}
