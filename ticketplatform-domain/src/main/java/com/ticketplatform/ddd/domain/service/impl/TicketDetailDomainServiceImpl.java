package com.ticketplatform.ddd.domain.service.impl;

import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import com.ticketplatform.ddd.domain.repository.TicketDetailDomainRepository;
import com.ticketplatform.ddd.domain.service.TicketDetailDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketDetailDomainServiceImpl implements TicketDetailDomainService {
    // Call repository in domain
    @Autowired
    private TicketDetailDomainRepository ticketDetailDomainRepository;

    @Override
    public TicketDetail getTicketDetailById(Long ticketId) {
        return ticketDetailDomainRepository.findById(ticketId).orElse(null);
    }
}
