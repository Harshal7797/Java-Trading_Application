package ca.jrvs.apps.dao;

import ca.jrvs.apps.model.domain.Entity;
import ca.jrvs.apps.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class JdbcCrudDao <E extends Entity, ID> implements CrudRepository<E,ID> {

    private static final Logger logger = LoggerFactory.getLogger(Quote.class);

    abstract public JdbcTemplate getJdbcTemplate();

    abstract public SimpleJdbcInsert simpleJdbcInsert();

    abstract public String getTableName();

    abstract public String getIdName();

    abstract Class  getEntityClass();

    @SuppressWarnings("unchecked")
    @Override
    public E save(E entity) {

        return entity;
    }

    @Override
    public E findById(ID id) {
        return findById(getIdName(), id, false, getEntityClass());
    }

    public E findByIdForUpdate(ID id) {
        return findById(getIdName(), id, true, getEntityClass());
    }

/**
 * @return an entity
 * @throws java.sql.SQLException if sql execution failed
 * @throws ResourceNotFoundException if no entity is found in db
 */
    @SuppressWarnings("unchecked")
    public E findById(String idName, ID id, boolean forUpdate, Class clazz){

    return null;
}

    @Override
    public boolean existsById(ID id){
    return existsById(getIdName(), id);
}

    public boolean existsById(String idName, ID id){
    return true;
    }

    @Override
    public void deleteById(ID id){
         deleteById(getIdName(),id);
    }

    public void deleteById(String idName , ID id){

    }



}
