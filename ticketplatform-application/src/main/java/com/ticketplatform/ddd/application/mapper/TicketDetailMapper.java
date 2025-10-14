package com.ticketplatform.ddd.application.mapper;

import com.ticketplatform.ddd.application.model.TicketDetailDTO;
import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import org.springframework.beans.BeanUtils;

public class TicketDetailMapper {
    public static TicketDetailDTO toTicketDetailDTO(TicketDetail ticketDetail) {
            if (ticketDetail == null) {
                return null;
            }
            TicketDetailDTO ticketDetailDTO = new TicketDetailDTO();
            // copy all properties of ticketDetail to ticketDetailDTO
            BeanUtils.copyProperties(ticketDetail, ticketDetailDTO);
            return ticketDetailDTO;

    }
}
