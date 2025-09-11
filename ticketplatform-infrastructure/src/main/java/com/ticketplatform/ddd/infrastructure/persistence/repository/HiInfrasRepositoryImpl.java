package com.ticketplatform.ddd.infrastructure.persistence.repository;

import com.ticketplatform.ddd.domain.repository.HiDomainRepository;
import org.springframework.stereotype.Service;

@Service
public class HiInfrasRepositoryImpl implements HiDomainRepository {
    @Override
    public String sayHi(String who) {
        return "Hello Infrastructure " + who;
    }
}
