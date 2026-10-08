package com.durvesh.querymind.controller;

import com.durvesh.querymind.dto.QueryRequest;
import com.durvesh.querymind.dto.QueryResponse;
import com.durvesh.querymind.service.QueryExecutionService;
import com.durvesh.querymind.service.SqlGenerationService;
import com.durvesh.querymind.service.SqlValidationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // prototype only — restrict this before shipping
public class QueryController {

    private final SqlGenerationService sqlGenerationService;
    private final SqlValidationService sqlValidationService;
    private final QueryExecutionService queryExecutionService;

    public QueryController(SqlGenerationService sqlGenerationService,
                            SqlValidationService sqlValidationService,
                            QueryExecutionService queryExecutionService) {
        this.sqlGenerationService = sqlGenerationService;
        this.sqlValidationService = sqlValidationService;
        this.queryExecutionService = queryExecutionService;
    }

    @PostMapping("/query")
    public QueryResponse query(@RequestBody QueryRequest request) {
        String question = request.getQuestion();
        String sql = sqlGenerationService.generateSql(question);
        if (sql.trim().equalsIgnoreCase("NO_ANSWER")) {
    return QueryResponse.failure(question, sql, "This question can't be answered with the current database schema.");
}

        if (!sqlValidationService.isValid(sql)) {
            return QueryResponse.failure(question, sql, sqlValidationService.reasonInvalid(sql));
        }

        try {
            var rows = queryExecutionService.execute(sql);
            return QueryResponse.success(question, sql, rows);
        } catch (Exception e) {
            return QueryResponse.failure(question, sql, "Query execution failed: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
