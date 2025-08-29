package com.example.bajaj_task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component // Tells Spring to manage this class
public class AppRunner implements CommandLineRunner {

    @Autowired // Injects the RestTemplate bean we created earlier
    private RestTemplate restTemplate;

    private static final String GENERATE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

@Override
public void run(String... args) throws Exception {
    System.out.println("Application started. Kicking off the process...");

    try {
        // --- Step 1 & 2: Generate Webhook and Get Response ---
        System.out.println("Sending registration request to: " + GENERATE_URL);
        WebhookRequest requestBody = new WebhookRequest();
        HttpEntity<WebhookRequest> requestEntity = new HttpEntity<>(requestBody);

        // This part is changed to get more details
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            GENERATE_URL,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        System.out.println("📬 Server Response Status Code: " + responseEntity.getStatusCode());
        System.out.println("📬 Server Response Body: " + responseEntity.getBody());

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            // Manually parse the JSON response
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            WebhookResponse webhookResponse = mapper.readValue(responseEntity.getBody(), WebhookResponse.class);

            String webhookUrl = webhookResponse.getWebhookUrl();
            String accessToken = webhookResponse.getAccessToken();

            System.out.println("Successfully received webhook URL: " + webhookUrl);
            System.out.println("Successfully received access token.");

            // --- Step 3: Define the SQL Solution ---
            String sqlQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, (SELECT COUNT(*) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e1.DEPARTMENT AND e2.DOB > e1.DOB) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID ORDER BY e1.EMP_ID DESC;";
            System.out.println("Using SQL Query: " + sqlQuery);

            // --- Step 4: Submit the Final Solution ---
            System.out.println("Submitting final query to the webhook URL...");
            SolutionRequest solutionBody = new SolutionRequest(sqlQuery);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            HttpEntity<SolutionRequest> solutionEntity = new HttpEntity<>(solutionBody, headers);

            ResponseEntity<String> finalResponse = restTemplate.exchange(
                webhookUrl,
                HttpMethod.POST,
                solutionEntity,
                String.class
            );

            System.out.println("Submission complete! Status: " + finalResponse.getStatusCode());
            System.out.println("Response Body: " + finalResponse.getBody());

        } else {
            System.err.println("Failed to get a successful response from the server.");
        }

    } catch (Exception e) {
        System.err.println("An error occurred during the process: " + e.getMessage());
        // This will print the full error details
        e.printStackTrace();
    }
}
}