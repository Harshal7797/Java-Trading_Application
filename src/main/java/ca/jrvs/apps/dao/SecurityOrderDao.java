package ca.jrvs.apps.dao;

import ca.jrvs.apps.model.domain.SequrityOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SequrityOrder,Integer> {
    private final static Logger logger = LoggerFactory.getLogger(AccountDao.class);
    private final static String TABLE_NAME = "security_order";
    private final static String ID_NAME = "id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleInsert;

    @Autowired
    public SecurityOrderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME).usingGeneratedKeyColumns(ID_NAME);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public SimpleJdbcInsert getSimpleJdbcInsert() {
        return simpleInsert;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdName() {
        return ID_NAME;
    }

    @Override
    Class getEntityClass() {
        return SequrityOrder.class;
    }

    @Override
    public SequrityOrder findById(Integer id) {
        return super.findById(getIdName(), id, false, getEntityClass());
    }

    @Override
    public SequrityOrder save(SequrityOrder entity) {
        return super.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(getIdName(), id);
    }

    @Override
    public boolean existsById(Integer id) {
        return super.existsById(getIdName(), id);
    }
}
