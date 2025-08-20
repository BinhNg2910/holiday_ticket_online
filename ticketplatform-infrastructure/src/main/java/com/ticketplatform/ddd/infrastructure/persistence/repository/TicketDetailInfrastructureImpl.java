package com.ticketplatform.ddd.infrastructure.persistence.repository;

import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import com.ticketplatform.ddd.domain.repository.TicketDetailDomainRepository;
import com.ticketplatform.ddd.infrastructure.persistence.mapper.TicketDetailJPAMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TicketDetailInfrastructureImpl implements TicketDetailDomainRepository {

    // Call JPA mapper
    @Autowired
    private TicketDetailJPAMapper ticketDetailJPAMapper;

    @Override
    public Optional<TicketDetail> findById(long id) {
        log.info("Implement infrastructure: {}", id);
        return ticketDetailJPAMapper.findById(id);
    }
}
