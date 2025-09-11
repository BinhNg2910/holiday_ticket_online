package com.ticketplatform.ddd.application.service.ticket.impl;

import com.ticketplatform.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplatform.ddd.application.service.ticket.cache.TicketDetailAppCache;
import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import com.ticketplatform.ddd.domain.repository.TicketDetailDomainRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailAppServiceImpl implements TicketDetailAppService {

    // Call serivce domain module
    @Autowired
    private TicketDetailDomainRepository ticketDetailDomainRepository;

    // Call cache
    @Autowired
    private TicketDetailAppCache ticketDetailAppCache;

    @Override
    public TicketDetail getTicketDetailById(Long ticketId) {
        log.info("Implement Application: {}", ticketId);
//        return ticketDetailAppCache.getTicketDefaultCacheNormal(ticketId, System.currentTimeMillis());
        return ticketDetailAppCache.getTicketDefaultCacheNormal(ticketId, System.currentTimeMillis());
    }}
