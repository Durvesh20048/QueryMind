package com.durvesh.querymind.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class SqlGenerationService {

    private static final String SCHEMA = """
        Table: customers(id INT, name VARCHAR, city VARCHAR, signup_date DATE)
        Table: products(id INT, name VARCHAR, category VARCHAR, price DECIMAL)
        Table: orders(id INT, customer_id INT, order_date DATE, status VARCHAR)
          -- status is one of: DELIVERED, CANCELLED, PENDING
        Table: order_items(id INT, order_id INT, product_id INT, quantity INT)
        """;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL = "llama3.2";
    public String buildPrompt(String question) {
    String instructions = """
    You are a SQL generator. Given a database schema and a question,
    output ONLY a valid SQL SELECT query. No explanation, no markdown,
    no semicolon at the end.
    When comparing text values (names, cities, categories), compare in
    lowercase using LOWER(column) and match with LIKE, for example
    LOWER(category) LIKE '%electronic%'.
    If the question cannot be answered using only the tables and columns in the schema above (for example, it asks about stock levels, employees, or anything not listed), output exactly: NO_ANSWER
    """;

    String fewShotExamples = """
    Question: How many products are there?
    SQL: SELECT COUNT(*) FROM products

    Question: Get me a list of customers whose name starts with A
    SQL: SELECT name FROM customers WHERE name LIKE 'A%'

    Question: How many customers have ordered more than 2 different products?
    SQL: SELECT c.name, COUNT(DISTINCT oi.product_id) AS distinct_products FROM customers c JOIN orders o ON o.customer_id = c.id JOIN order_items oi ON oi.order_id = o.id GROUP BY c.id, c.name HAVING COUNT(DISTINCT oi.product_id) > 2

    Question: List all products in the fitness category
    SQL: SELECT name FROM products WHERE LOWER(category) LIKE '%fitness%'

    Question: List customers from Mumbai
    SQL: SELECT name FROM customers WHERE LOWER(city) LIKE '%mumbai%'

    Question: What is the cheapest product?
    SQL: SELECT name, price FROM products ORDER BY price ASC LIMIT 1

    Question: How many orders has each customer placed?
    SQL: SELECT c.name, COUNT(o.id) FROM customers c LEFT JOIN orders o ON o.customer_id = c.id GROUP BY c.name

    Question: What is the stock level of the Wireless Mouse?
    SQL: NO_ANSWER
 """;

    return instructions + "\n" + SCHEMA + "\n" + fewShotExamples
         + "\nQuestion: " + question + "\nSQL:";
}

   public String generateSql(String question) {
    String prompt = buildPrompt(question);

    Map<String, Object> requestBody = Map.of(
        "model", MODEL,
        "prompt", prompt,
        "stream", false,
        "options", Map.of("temperature", 0)
    );

    Map response = restTemplate.postForObject(OLLAMA_URL, requestBody, Map.class);
    String rawSql = (String) response.get("response");
String cleaned = rawSql
    .replace("```sql", "")
    .replace("```", "")
    .trim();

return cleaned;
}
}
