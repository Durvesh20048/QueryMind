package com.durvesh.querymind.dto;

import java.util.List;
import java.util.Map;

public class QueryResponse {
    private String question;
    private String generatedSql;
    private List<Map<String, Object>> rows;
    private String error; // null if everything went fine

    public QueryResponse() {}

    public static QueryResponse success(String question, String sql, List<Map<String, Object>> rows) {
        QueryResponse r = new QueryResponse();
        r.question = question;
        r.generatedSql = sql;
        r.rows = rows;
        return r;
    }

    public static QueryResponse failure(String question, String sql, String error) {
        QueryResponse r = new QueryResponse();
        r.question = question;
        r.generatedSql = sql;
        r.error = error;
        return r;
    }

    // getters/setters — needed for Jackson JSON serialization
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getGeneratedSql() { return generatedSql; }
    public void setGeneratedSql(String generatedSql) { this.generatedSql = generatedSql; }
    public List<Map<String, Object>> getRows() { return rows; }
    public void setRows(List<Map<String, Object>> rows) { this.rows = rows; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
