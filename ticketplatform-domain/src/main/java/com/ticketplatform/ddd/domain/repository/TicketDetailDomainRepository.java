package com.ticketplatform.ddd.domain.repository;

import com.ticketplatform.ddd.domain.model.entity.TicketDetail;

import java.util.Optional;

public interface TicketDetailDomainRepository {
    Optional<TicketDetail> findById(long id);
}
