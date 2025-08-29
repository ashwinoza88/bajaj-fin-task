package com.example.bajaj_task;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class WebhookResponse {
    
    @JsonProperty("webhook")
    private String webhookUrl;

    @JsonProperty("accessToken")
    private String accessToken;
}