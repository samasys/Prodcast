package com.primeforce.prodcast.dao;

import com.primeforce.prodcast.businessobjects.State;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Created by Thiru on 29/1/2017.
 */
public class StateMapper implements RowMapper<State>{
    public State mapRow(ResultSet rs, int rowNum) throws SQLException {
        State state = new State();
        state.setStateId(rs.getString("state_id"));
        state.setStateName(rs.getString("state_name"));

        return state;
    }
}