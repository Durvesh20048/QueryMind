package com.durvesh.querymind.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryExecutionService {

    private final JdbcTemplate jdbcTemplate;

    public QueryExecutionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Runs a SQL string and returns each row as a Map<column, value>.
     * Only call this with SQL that has already passed SqlValidationService —
     * this method does no safety checking of its own.
     */
    public List<Map<String, Object>> execute(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
}
