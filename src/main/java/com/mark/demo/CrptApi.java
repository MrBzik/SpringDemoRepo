package com.mark.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CrptApi {
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
        return new CrptApi(timeUnit, requestLimit);
    }


    public synchronized void sendDocument(Document document, String accessToken){

        if(!assertLimitNotReached()) return;

        try {
            String documentJson = objectMapper.writeValueAsString(document);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                    .header("Authorization", accessToken)
                    .POST(HttpRequest.BodyPublishers.ofString(documentJson))
                    .build();
           httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getLocalizedMessage());
        }
    }


    private Boolean assertLimitNotReached(){
        if(requestsTimeStampLimiter == 0L || requestsTimeStampLimiter > System.currentTimeMillis()){
            requestsTimeStampLimiter = System.currentTimeMillis() + timeUnit.toMillis(1);
            requestsMade = 0;
        }

        if(requestsMade == requestLimit) return false;

        requestsMade ++;

        return true;
    }


    public record Document(
        ParticipantInn description,
        String doc_id,
        String doc_status,
        OPERATION_TYPE type,
        Boolean importRequest,
        String owner_inn,
        String participant_inn,
        String producer_inn,
        Date production_date,
        String production_type,
        List<Product> products,
        Date reg_date,
        String reg_number
    ){

        enum OPERATION_TYPE {
            LP_INTRODUCE_GOODS,
            LP_CONTRACT_COMISSIONING,
            LP_GOODS_IMPORT,
            LP_FTS_INTRODUCE,
            CROSSBORDER,
            LP_SHIP_GOODS_CROSSBORDER,
            EAS_CROSSBORDER,
            LP_ACCEPT_GOODS
        }

        public record Product(
            String certificate_document,
            Date certificate_document_date,
            String certificate_document_number,
            String owner_inn,
            String producer_inn,
            Date production_date,
            String tnved_code,
            String uit_code,
            String uitu_code
        ) {}

        public record ParticipantInn(
                String participantInn
        ){}

    }
}
