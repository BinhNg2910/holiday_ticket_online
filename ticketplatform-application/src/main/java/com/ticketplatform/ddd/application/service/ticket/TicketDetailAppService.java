package com.ticketplatform.ddd.application.service.ticket;

import com.ticketplatform.ddd.application.model.TicketDetailDTO;
import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import org.springframework.stereotype.Service;

public interface TicketDetailAppService {
    TicketDetailDTO getTicketDetailById(Long ticketId, Long version);
    // order ticket
    boolean orderTicketByUser(Long ticketId);
}
