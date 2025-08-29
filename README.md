# Bajaj Finserv Health - Automated Hiring Challenge Solution

## Overview
This project is an **automated, server-less Java application** built with **Spring Boot** to solve the **Qualifier 1 Technical Challenge** from **Bajaj Finserv Health**.  
The application runs autonomously on startup, communicates with external REST APIs, solves a given SQL problem, and securely submits the solution — **without any manual intervention**.

It demonstrates a **robust, event-driven, and fully automated approach** to problem-solving.

---

## Core Functionality
Upon launch, the application executes the following sequence:

1. Automated Registration  
   - Sends a `POST` request to `/generateWebhook` with personal details (Name, Registration No., Email).  

2. Dynamic Webhook & JWT Handling  
   - Receives a response containing a **unique webhook URL** and **JWT accessToken** for secure authentication.  

3. Problem Resolution  
   - Identifies the correct SQL problem based on registration number (`22BCE7932`, which is even).  
   - Selects and applies the corresponding SQL solution.  

4. Secure Submission  
   - Submits the final SQL query via a `POST` request to the webhook URL.  
   - Includes the JWT in the `Authorization` header for **secure API communication**.  

---

## The Challenge: SQL Problem Statement
For **even registration numbers**, the given problem was:

> Problem:  
> Calculate the number of employees who are younger than each employee, grouped by their respective departments.  
> For each employee, return the count of employees in the same department whose age is less than theirs.

---

## SQL Solution
The following query solves the problem by correlating the `EMPLOYEE` table with itself:

```sql
SELECT
    e1.EMP_ID,
    e1.FIRST_NAME,
    e1.LAST_NAME,
    d.DEPARTMENT_NAME,
    (
        SELECT COUNT(*)
        FROM EMPLOYEE e2
        WHERE e2.DEPARTMENT = e1.DEPARTMENT
          AND e2.DOB > e1.DOB
    ) AS YOUNGER_EMPLOYEES_COUNT
FROM
    EMPLOYEE e1
JOIN
    DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
ORDER BY
    e1.EMP_ID DESC;
```

---

## Technologies & Design Choices
- Framework: Spring Boot `3.5.5`  
- Language: Java `17`  
- Build Tool: Apache Maven  

### Key Libraries
- Spring Web: REST API calls via `RestTemplate`.  
- Lombok: Cleaner POJOs with minimal boilerplate.  

---

## Architectural Decisions
- Server-less Execution:  
  Used `CommandLineRunner` to execute logic immediately at startup (no exposed endpoints).  

- Type-Safe Data Handling:  
  Created POJOs (`WebhookRequest`, `WebhookResponse`, `SolutionRequest`) to handle API request/response safely.  

- Secure API Communication:  
  Implemented JWT authentication by including the token in `Authorization` headers.  

---

## How to Run This Project

### Prerequisites
- Java JDK 17+  
- Apache Maven 3.6+

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   ```

2. Navigate to the project directory:
   ```bash
   cd bajaj-task
   ```

3. Build the project using Maven:
   ```bash
   mvn clean package
   ```

4. Run the application:
   ```bash
   java -jar target/bajaj-task-0.0.1-SNAPSHOT.jar
   ```

The application will automatically:
- Register via API  
- Receive webhook + JWT  
- Solve the SQL challenge  
- Submit the solution securely  

Check your terminal logs for the **full execution flow**.

---
