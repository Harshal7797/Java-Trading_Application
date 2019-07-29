package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AccountDao extends JdbcCrudDao<Account, Integer> {
    private final static Logger logger = LoggerFactory.getLogger(AccountDao.class);
    private final static String TABLE_NAME = "account";
    private final static String ID_NAME = "id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleInsert;

    @Autowired
    public AccountDao(DataSource dataSource) {
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
        return Account.class;
    }

    @Override
    public Account findById(Integer traderId) {
        return super.findById(getIdName(),traderId,false, getEntityClass());
    }

    @Override
    public Account save(Account entity) {
        return super.save(entity);
    }

    @Override
    public void deleteById(Integer traderId) {
        super.deleteById(getIdName(),traderId);
    }

    @Override
    public boolean existsById(Integer traderID) {
        return super.existsById(getIdName(), traderID);
    }

    /**
     * @retrun updated account or null if id not found
     */
    public Account updateAmountById(Integer id, Double amount){
        if(super.existsById(id)){
            String sql = "UPDATE " + TABLE_NAME  +" SET amount=? WHERE id =?" ;
            int row = jdbcTemplate.update(sql, amount, id);
            logger.debug("Update amount row = "+ row);
            if(row!=1){
                throw new IncorrectResultSizeDataAccessException(1, row);
            }
            return findById(id);
        }

        return null;
    }
}