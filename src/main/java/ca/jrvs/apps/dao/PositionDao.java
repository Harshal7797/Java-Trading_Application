package ca.jrvs.apps.dao;

import ca.jrvs.apps.model.domain.Position;
import ca.jrvs.apps.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PositionDao {
    private static final Logger logger= LoggerFactory.getLogger(Quote.class);

    private final String TABLE_NAME = "trader";
    private final String ID_NAME = "account_id";

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public PositionDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Position> findById(Integer accountId){
        if (accountId == null){
            throw new IllegalArgumentException("ID can't be null");
        }
        List<Position> tmp = null;
        String selectSql = "SELECT * FROM " + TABLE_NAME +" WHERE " + ID_NAME + " =?";
        logger.info(selectSql);

        try {
            tmp = (List<Position>) jdbcTemplate.queryForObject(selectSql, BeanPropertyRowMapper.newInstance(Position.class),accountId);
        }catch (EmptyResultDataAccessException e){
            logger.debug("Can't find trader id:" + accountId, e);
        }
        if(tmp == null){
            throw new ResourceNotFoundException("Resource not found");
        }
        return tmp;
    }
    public Position findByTickerAndId(String ticker, Integer accountId ){
        if ( ticker.isEmpty() || accountId == null){
            throw new IllegalArgumentException("Ticker and account ID cannot be null can't be null");
        }
        String sql = "SELECT * FROM " + TABLE_NAME + "WHERE " + ID_NAME + " =?" + " AND ticker = ?";
    Position position = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Position.class), accountId, ticker);

    return position;
    }

}

