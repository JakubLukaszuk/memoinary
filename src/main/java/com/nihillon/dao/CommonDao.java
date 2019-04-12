package com.nihillon.dao;

import com.itextpdf.text.log.LoggerFactory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.nihillon.models.BaseModel;
import com.nihillon.utils.DbManager;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class CommonDao {

    protected final ConnectionSource connectionSource;

    public CommonDao() {
        this.connectionSource = DbManager.getConnectionSource();
    }

    public CommonDao(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }


    public <T extends BaseModel, U> void createOrUpdate( BaseModel objectToUpdateOrCreate)  {
        Dao<T, U> dao = (Dao<T, U>) getDao(objectToUpdateOrCreate.getClass());
        try {
            dao.createOrUpdate((T) objectToUpdateOrCreate);
        } catch (SQLException e) {

        }
    }


    public <T extends BaseModel, U> Dao<T, U> getDao(Class<T> tClass) {
        try {
            return DaoManager.createDao(connectionSource, tClass);
        } catch (SQLException e) {

        }
        return null;
    }

    public <T extends BaseModel, U> List<T> returnAll(Class<T> tClass) {
        try {
            Dao<T, U> dao = getDao(tClass);
            return dao.queryForAll();

        } catch (SQLException e) {

                  }
        return null;
    }

    public <T extends BaseModel, U> void refresh(BaseModel bm) {
        try {
            Dao<T, U> dao = getDao((Class<T>) bm.getClass());
            dao.refresh((T) bm);

        } catch (SQLException e) {

        }

    }

    public <T extends BaseModel, I> List<T> queryForAll(Class<T> cls) {

        Dao<T, I> dao = getDao(cls);
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
                   }
        return null;
    }


    public <T extends BaseModel, I> void deleteById(Class<T> cls, Integer id) {

        Dao<T, I> dao = getDao(cls);
        try {
            dao.deleteById((I) id);
        } catch (SQLException e) {
                 }
    }

    public <T extends BaseModel, I> T findByID(Class<T> cls, Integer id) {

        Dao<T, I> dao = getDao(cls);
        try {
            return dao.queryForId((I) id);
        } catch (SQLException e) {
                  }
        return null;

    }
}
