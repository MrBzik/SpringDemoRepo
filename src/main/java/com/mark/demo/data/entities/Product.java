package com.mark.demo.data.entities;

public record Product(
        String certificate_document,
        String certificate_document_date,
        String certificate_document_number,
        String owner_inn,
        String producer_inn,
        String production_date,
        String tnved_code,
        String uit_code,
        String uitu_code
) { }
