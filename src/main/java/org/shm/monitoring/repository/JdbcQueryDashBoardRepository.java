package org.shm.monitoring.repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.shm.monitoring.web.rest.dto.SerieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.core.OneToManyResultSetExtractor;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.data.jdbc.query.SqlDeleteCallback;
import org.springframework.data.jdbc.query.SqlInsertCallback;
import org.springframework.data.jdbc.query.SqlInsertWithKeyCallback;
import org.springframework.data.jdbc.query.SqlUpdateCallback;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



/**
 */
@Repository
@Transactional
public class JdbcQueryDashBoardRepository {


    @Inject
    JdbcTemplate jdbcTemplate;


    public List<SerieDTO> selectSeries(){

    List<SerieDTO> series = this.jdbcTemplate.query(
        "select name , test.m as month,  coalesce(nb,0) as nb\n" +
            "from  projectconfiguration\n" +
            "cross join (SELECT X as  m\n" +
            "  FROM SYSTEM_RANGE(1,12) ) test\n" +
            "left join ( SELECT month(date) as month , configuration_name,count(1) as nb FROM RESPONSE res " +
            "where type='ERROR' \n" +
            "group by month(date),configuration_name) on month  =test.m and name=configuration_name\n" +
            "order by name, m ",
        new ResultSetExtractor<List<SerieDTO> > (){
            public List<SerieDTO>  extractData(ResultSet rs) throws SQLException, DataAccessException {


                Map<String, SerieDTO> seriesById = new HashMap<>();
                while (rs.next()) {
                    String name = rs.getString("name");
                    Long data = rs.getLong("nb");
                    SerieDTO serieDTO = seriesById.get(name);
                    if (serieDTO == null) {
                        serieDTO = new SerieDTO();
                        serieDTO.setName(name);
                        seriesById.put(name,serieDTO);
                    }
                    serieDTO.addData(data);
                }
                Collection<SerieDTO> series = seriesById.values();
                return  new ArrayList(series);
            }

        });

        return series;
    }

}
