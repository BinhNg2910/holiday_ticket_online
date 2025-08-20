package com.ticketplatform.ddd.domain.service;

import com.ticketplatform.ddd.domain.model.entity.TicketDetail;

public interface TicketDetailDomainService {
    TicketDetail getTicketDetailById(Long ticketId);
}
