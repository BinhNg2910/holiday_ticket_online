package com.ticketplatform.ddd.controller;

import com.ticketplatform.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplatform.ddd.controller.model.enums.ResultUtil;
import com.ticketplatform.ddd.controller.model.fe.ResultMessageModel;
import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketDetailController {

    @Autowired
    private TicketDetailAppService ticketDetailAppService;

    @GetMapping("/{ticketId}/detail/{detailId}")
    public ResultMessageModel<TicketDetail> getTicketDetail(
            @PathVariable("ticketId") long ticketId,
            @PathVariable("detailId") long detailId
    ) {
        log.info("Van Binh Nguyen");
        log.info("ticketId: {}, detailId: {}", ticketId, detailId);
//        return ResultUtil;
//        return null;
        return ResultUtil.data(ticketDetailAppService.getTicketDetailById(detailId));
    }
}
