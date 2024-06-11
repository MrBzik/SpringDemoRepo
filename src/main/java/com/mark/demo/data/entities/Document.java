package com.mark.demo.data.entities;

import com.mark.demo.data.remote.CrptApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Document {
    ParticipantInn description;
    String doc_id;
    String doc_status;
    OPERATION_TYPE doc_type;
    Boolean importRequest;
    String owner_inn;
    String participant_inn;
    String producer_inn;
    Date production_date;
    String production_type;
    List<Product> products;
    Date reg_date;
    String reg_number;

    private Document(
            ParticipantInn description, String doc_id, String doc_status,
            OPERATION_TYPE doc_type, Boolean importRequest, String owner_inn,
            String participant_inn, String producer_inn, Date production_date,
            String production_type, List<Product> products, Date reg_date,
            String reg_number
    ){
        this.description = description;
        this.doc_id = doc_id;
        this.doc_status = doc_status;
        this.doc_type = doc_type;
        this.importRequest = importRequest;
        this.owner_inn = owner_inn;
        this.participant_inn = participant_inn;
        this.producer_inn = producer_inn;
        this.production_date = production_date;
        this.production_type = production_type;
        this.products = products;
        this.reg_date = reg_date;
        this.reg_number = reg_number;
    }

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
    ) { }

    public record ParticipantInn(
            String participantInn
    ){
    }
    public class Builder {
        private static final String UNSPECIFIED = "UNSPECIFIED";
        private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ParticipantInn description = null;
        String doc_id = UNSPECIFIED;
        String doc_status = UNSPECIFIED;
        OPERATION_TYPE doc_type = null;
        Boolean importRequest = false;
        String owner_inn = UNSPECIFIED;
        String participant_inn = UNSPECIFIED;
        String producer_inn = UNSPECIFIED;
        Date production_date = null;
        String production_type = UNSPECIFIED;
        List<Product> products = Collections.emptyList();
        Date reg_date = null;
        String reg_number = UNSPECIFIED;

        public Builder setParticipantInn(String participantInn) {
            return this;
        }
    }
}
