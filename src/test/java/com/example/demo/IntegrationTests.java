package com.example.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Mutation - 책 생성을 테스트한다.")
    public void testMutation(){
        ObjectNode requestBody = objectMapper.createObjectNode();
        String query = "mutation {\n" +
                "    createBook(bookInput: {\n" +
                "       page: 100 \n" +
                "       title: \"자바의정석\" \n" +
                "}){\n" +
                "        id\n" +
                "        title\n" +
                "        page\n" +
                "    }\n" +
                "}";
        requestBody.put("query", query);
        HttpEntity<ObjectNode> request = new HttpEntity<>(requestBody);
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/graphql",
                HttpMethod.POST,
                request,
                String.class
        );
        System.out.println(response.getBody().toString());
        // 검증로직..
    }

    @Test
    @DisplayName("Query - 책 조회를 테스트한다.")
    public void testQuery(){
        ObjectNode requestBody = objectMapper.createObjectNode();
        String query = "query {\n" +
                "    books {\n" +
                "        id\n" +
                "        title\n" +
                "        page\n" +
                "    }\n" +
                "}";
        requestBody.put("query", query);
        HttpEntity<ObjectNode> request = new HttpEntity<>(requestBody);
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/graphql",
                HttpMethod.POST,
                request,
                String.class
        );
        System.out.println(response.getBody().toString());
        // 검증로직..
    }
}
