package com.jlukaszuk.dao;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.jlukaszuk.models.BaseModel;
import com.jlukaszuk.utils.DbManager;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class CommonDao {

    private final ConnectionSource connectionSource;

    public CommonDao() throws SQLException {
        this.connectionSource = DbManager.getConnectionSource();
    }

    public CommonDao(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }


    public <T extends BaseModel, U> void createOrUpdate( BaseModel objectToUpdateOrCreate) throws SQLException {
        Dao<T, U> dao = (Dao<T, U>) getDao(objectToUpdateOrCreate.getClass());
        dao.createOrUpdate((T) objectToUpdateOrCreate);
    }

    public <T extends BaseModel, U> Dao<T, U> getDao(Class<T> tClass) throws SQLException {
        return DaoManager.createDao(connectionSource, tClass);
    }

    public <T extends BaseModel, U> List<T> returnAll(Class<T> tClass) throws SQLException {
        Dao<T, U> dao = getDao(tClass);
        return dao.queryForAll();
    }

    public <T extends BaseModel, U> void refresh(BaseModel bm) throws SQLException {
        Dao<T, U> dao = getDao((Class<T>) bm.getClass());
        dao.refresh((T) bm);
    }

    public <T extends BaseModel, I> List<T> queryForAll(Class<T> cls) throws SQLException {
        Dao<T, I> dao = getDao(cls);
        return dao.queryForAll();
    }

    public <T extends BaseModel, I> void deleteById(Class<T> cls, Integer id) throws SQLException {
        Dao<T, I> dao = getDao(cls);
        dao.deleteById((I) id);
    }

    public <T extends BaseModel, I> T findByID(Class<T> cls, Integer id) throws SQLException {
        Dao<T, I> dao = getDao(cls);
        return dao.queryForId((I) id);
    }
}
