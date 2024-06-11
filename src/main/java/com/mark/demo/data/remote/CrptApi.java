package com.mark.demo.data.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mark.demo.data.entities.Document;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private static CrptApi instance;
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private Long requestsTimeStampLimiter = 0L;
    private int requestsMade = 0;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    private CrptApi(TimeUnit timeUnit, int requestLimit){
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        objectMapper = new ObjectMapper();
        httpClient = HttpClient.newHttpClient();
    }

    public static CrptApi getInstance(
            TimeUnit timeUnit, int requestLimit
    ) throws InstantiationException{
        if(requestLimit <= 0){
            throw new InstantiationException("requestLimit should be greater than 0");
        }
        if(instance == null){
            instance = new CrptApi(timeUnit, requestLimit);
        }

        return instance;
    }


    public void sendDocument(Document document, String accessToken){

        if(!assertLimitNotReached()) return;

        try {
            String documentJson = objectMapper.writeValueAsString(document);
            System.out.println("sending: " + documentJson);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                    .header("Authorization", accessToken)
                    .POST(HttpRequest.BodyPublishers.ofString(documentJson))
                    .build();
           HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
           System.out.println("response: " + response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getLocalizedMessage());
        }
    }


    private synchronized Boolean assertLimitNotReached(){
        if(requestsTimeStampLimiter == 0L || requestsTimeStampLimiter > System.currentTimeMillis()){
            requestsTimeStampLimiter = System.currentTimeMillis() + timeUnit.toMillis(1);
            requestsMade = 0;
        }

        if(requestsMade == requestLimit) return false;

        requestsMade ++;

        return true;
    }
}
