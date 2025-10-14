package com.ticketplatform.ddd.controller;

import com.ticketplatform.ddd.application.model.TicketDetailDTO;
import com.ticketplatform.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplatform.ddd.controller.model.enums.ResultUtil;
import com.ticketplatform.ddd.controller.model.fe.ResultMessageModel;
import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketDetailController {

    @Autowired
    private TicketDetailAppService ticketDetailAppService;

    /**
     * Get ticket detail by ticket ID and detail ID
     * @param ticketId
     * @param detailId
     * @return
     */
    @GetMapping("/{ticketId}/detail/{detailId}")
    public ResultMessageModel<TicketDetailDTO> getTicketDetail(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("detailId") Long detailId,
            @RequestParam(name = "version", required = false) Long version
    ) {
        return ResultUtil.data(ticketDetailAppService.getTicketDetailById(detailId, version));
    }

    /**
     * Order ticket by user
     * @param ticketId
     * @param detailId
     * @return
     */
    @GetMapping("/{ticketId}/detail/{detailId}/order")
    public boolean orderTicket(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("detailId") Long detailId
    ) {
        return ticketDetailAppService.orderTicketByUser(ticketId);
    }
}
