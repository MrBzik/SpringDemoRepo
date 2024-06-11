package com.mark.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mark.demo.data.entities.Document;
import com.mark.demo.data.entities.OperationType;
import com.mark.demo.data.entities.ParticipantInn;
import com.mark.demo.data.entities.Product;
import com.mark.demo.data.remote.CrptApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DemoApplication {
	
	public static void main(String[] args) {

        CrptApi api;
        try {
            api = CrptApi.getInstance(TimeUnit.MINUTES, 50);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }


        List<Product> products = new ArrayList<>();
        products.add(new Product(
                "", "", "",
                "", "", "", "", "",
                ""
        ));

        api.sendDocument(
                new Document(
                        new ParticipantInn(""),
                        "", "", OperationType.LP_INTRODUCE_GOODS,
                        true, "", "", "",
                        "", "", products, "", ""
                ), "access_token"
        );

    }


}
