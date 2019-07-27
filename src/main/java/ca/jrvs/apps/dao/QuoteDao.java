package ca.jrvs.apps.dao;

import ca.jrvs.apps.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class QuoteDao extends JdbcCrudDao<Quote, String> {

    private static final Logger logger= LoggerFactory.getLogger(Quote.class);
    private static final String TABLE_NAME = "quote";
    private static final String ID_Name = "ticker";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleInsert;

    @Autowired
    public QuoteDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
    }

    public void update(List<Quote> singletonList) {
        String update = "UPDATE " + getTableName() + " SET last_price=?, bid_price=?, bid_size=?, ask_price=?, " +
                "ask_size=? WHERE " + getIdName() + " =?";
        logger.info(update);
        for(Quote x: singletonList){
            getJdbcTemplate().update(update,x.getLastPrice(),x.getBidPrice(),x.getBidSize(),
                    x.getAskPrice(),x.getAskSize(),x.getTicker());
        }

    }

    public List<Quote> findAll() {
    String selectSql= "SELECT *" +" FROM "+ getTableName();
    logger.info(selectSql);
    List<Quote> quotes = jdbcTemplate.query(selectSql, BeanPropertyRowMapper.newInstance(Quote.class));
    return quotes;
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
        return ID_Name;
    }

    @Override
    Class getEntityClass() {
        return QuoteDao.class;
    }

    @Override
    public Quote findById(String id) {
        return super.findById(getIdName(),id,false, getEntityClass());
    }

    @Override
    public Quote save(Quote entity) {
        simpleInsert.execute(new BeanPropertySqlParameterSource(entity));
        return entity;
    }

    @Override
    public void deleteById(String id) {
        super.deleteById(getIdName(),id);
    }
    @Override
    public boolean existsById(String id) {
        return super.existsById(getIdName(), id);
    }


}