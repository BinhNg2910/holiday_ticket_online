package com.ticketplatform.ddd.application.model.cache;

import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import lombok.Data;

@Data
public class TicketDetailCache {
    private Long version;
    private TicketDetail ticketDetail;

    public TicketDetailCache withClone(TicketDetail ticketDetail) {
        this.ticketDetail = ticketDetail;
        return this;
    }

    public TicketDetailCache withVersion(Long version) {
        this.version = version;
        return this;
    }
}
