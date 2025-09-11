package com.ticketplatform.ddd.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ticketplatform.ddd.domain.model.entity.TicketDetail;
import com.ticketplatform.ddd.domain.service.TicketDetailDomainService;
import com.ticketplatform.ddd.infrastructure.cache.redis.RedisInfrasService;
import com.ticketplatform.ddd.infrastructure.distributed.redission.RedisDistributedLocker;
import com.ticketplatform.ddd.infrastructure.distributed.redission.RedisDistributedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketDetailAppCache {

    @Autowired
    private RedisDistributedService redisDistributedService;

    @Autowired // declare cache
    private RedisInfrasService redisInfrasService;

    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    @Autowired
    private final static Cache<Long, TicketDetail> ticketDetailLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(8)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public TicketDetail getTicketDefaultCacheNormal(long id, long version) {
        // 1. get ticket item by redis
        TicketDetail ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
//        TicketDetail ticketDetail = null; // test prometheus for mysql tracking, just query on datbase
        // 2. YES -> hit cache
        if (ticketDetail != null) {
            log.info("From CACHE {}, {}, {}", id, version, ticketDetail);
            return ticketDetail;
        }

        // 3. NO -> Missing cache
        log.info("Cache MISS for {}, {}", id, version);
        // 4. Get data in DBS
        ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
        log.info("From DBS {}, {}, {}", id, version, ticketDetail);

        // 5. check ticket item
        if (ticketDetail != null) { // PROBLEM: if ticketDetail get from DBS null? -> continue querying -> not good
            // 6. set cache
//            redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
            redisInfrasService.setObjectTTL(genEventItemKey(id), ticketDetail);
        }
        return ticketDetail;
    }

    public TicketDetail getTicketDefaultCacheVip(long id, long version) {
        log.info("Implement getTicketDefaultCacheVip -> {}, {}", id, version);
        TicketDetail ticketDetail = ticketDetailDomainService.getTicketDetailById(id); // redisInfrasService.getObject(getEventItemKey(id), TicketDetail.class);
        // YES
        if (ticketDetail != null) {
            log.info("Ticket detail exist {}", ticketDetail);
            return ticketDetail;
        } else {
            log.info("Ticket detail not found {}", id);
        }

        // create lock process with KEY
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("PRO_LOCK_KEY_ITEM" + id);
        try {
            // 1. create lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);

            // NOTICE: although success or not -> must unLock anyway
            // NOTICE: although success or not -> must unLock anyway
            // NOTICE: although success or not -> must unLock anyway

            if (!isLock) {
                return ticketDetail;
            }

            // get cache
            ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
            log.info("From CACHE {}, {}, {}", id, version, ticketDetail);

            //2. YES -> data exist in cache
            if (ticketDetail != null) {

                return ticketDetail;
            }

            // 3. if still not have data in catch -> query database
            ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
            log.info("From DBS -> {}, {}, {}", id, version, ticketDetail);

            // if in DB still does not have data -> return exists
            if (ticketDetail == null) {
                log.info("TICKET NOT EXISTS ... {}", version);
                // set
                redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
                return ticketDetail;
            }

            // if have data -> set redis
            redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
            return ticketDetail;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // NOTICE: although success or not -> must unLock anyway
            // NOTICE: although success or not -> must unLock anyway
            // NOTICE: although success or not -> must unLock anyway
            locker.unlock();
        }
    }

    private TicketDetail getTicketDetailLocalCache(Long id) {
        try {
            return ticketDetailLocalCache.getIfPresent(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TicketDetail getTicketDefaultCacheVipLocal(long id, long version) {
        // 1. Get item local cache
        TicketDetail ticketDetail = getTicketDetailLocalCache(id);

        // YES
        if (ticketDetail != null) {
            log.info("Ticket detail from LOCAL CACHE exist {}", ticketDetail);
            return ticketDetail;
        } else {
            log.info("Ticket detail from LOCAL CACHE not found {}", id);
        }

        // 2. Get distributed cache in redis (not exist in local cache)
        ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
        if (ticketDetail != null) {
            log.info("From distributed cache exist {}", ticketDetail);
            ticketDetailLocalCache.put(id, ticketDetail);
            return ticketDetail;
        }

        // create lock process with KEY
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("PRO_LOCK_KEY_ITEM" + id);
        try {
            // 1. create lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // NOTICE: although success or not -> must unLock anyway
            // NOTICE: although success or not -> must unLock anyway
            // NOTICE: although success or not -> must unLock anyway

            if (!isLock) {
                    return ticketDetail;
            }

            // get cache
            ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
            log.info("From CACHE {}, {}, {}", id, version, ticketDetail);

            //2. YES -> data exist in cache
            if (ticketDetail != null) {
                return ticketDetail;
            }

            // 3. if still not have data in catch -> query database
            ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
            log.info("From DBS -> {}, {}, {}", id, version, ticketDetail);

            // if in DB still does not have data -> return exists
            if (ticketDetail == null) {
                log.info("TICKET NOT EXISTS ... {}", version);
                // set
                redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
                ticketDetailLocalCache.put(id, ticketDetail);
                return ticketDetail;
            }

            // if have data -> set redis
            redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
            return ticketDetail;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // NOTICE: although success or not -> must unLock anyway
            // NOTICE: although success or not -> must unLock anyway
            // NOTICE: although success or not -> must unLock anyway
            locker.unlock();
        }
    }

    private String genEventItemKey(Long itemId) {
        System.out.println("PRO_TICKET:ITEM:" + itemId);
        return "PRO_TICKET:ITEM:" + itemId;
    }
}
