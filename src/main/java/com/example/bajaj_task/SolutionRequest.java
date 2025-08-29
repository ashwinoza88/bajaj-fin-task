package com.example.bajaj_task;

import lombok.Data;

@Data
public class SolutionRequest {
    private String finalQuery;

    public SolutionRequest(String query) {
        this.finalQuery = query;
    }
}