package com.ticketplatform.ddd.application.service.ticket.impl;

import com.ticketplatform.ddd.application.mapper.TicketDetailMapper;
import com.ticketplatform.ddd.application.model.TicketDetailDTO;
import com.ticketplatform.ddd.application.model.cache.TicketDetailCache;
import com.ticketplatform.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplatform.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.ticketplatform.ddd.application.service.ticket.cache.TicketDetailCacheServiceRefactor;
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
    private TicketDetailCacheService ticketDetailAppCache;

    @Autowired
    private TicketDetailCacheServiceRefactor ticketDetailCacheServiceRefactor;

    @Override
    public TicketDetailDTO getTicketDetailById(Long ticketId, Long version) {
        log.info("Implement application: {}, {}", ticketId, version);
        TicketDetailCache ticketDetailCache = ticketDetailCacheServiceRefactor.getTicketDetailCache(ticketId, version);
        // mapper TicketDetail to TicketDetailDTO
        TicketDetailDTO ticketDetailDTO = TicketDetailMapper.toTicketDetailDTO(ticketDetailCache.getTicketDetail());
        ticketDetailDTO.setVersion(ticketDetailCache.getVersion());
        return ticketDetailDTO;
    }

    @Override
    public boolean orderTicketByUser(Long ticketId) {
        return ticketDetailAppCache.orderTicketByUser(ticketId);
    }
}
