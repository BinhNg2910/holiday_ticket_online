package com.ticketplatform.ddd.application.service.ticket;

import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import org.springframework.stereotype.Service;

public interface TicketDetailAppService {
    TicketDetail getTicketDetailById(Long ticketId);
}
