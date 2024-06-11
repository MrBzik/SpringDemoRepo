package com.mark.demo.data.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

public record Document(
        ParticipantInn description,
        String doc_id,
        String doc_status,
        OperationType doc_type,
        Boolean importRequest,
        String owner_inn,
        String participant_inn,
        String producer_inn,
        String production_date,
        String production_type,
        List<Product> products,
        String reg_date,
        String reg_number) { }
