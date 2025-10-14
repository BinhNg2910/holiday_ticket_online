package com.ticketplatform.ddd.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ticketplatform.ddd.application.model.cache.TicketDetailCache;
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
public class TicketDetailCacheServiceRefactor {

    @Autowired
    private RedisDistributedService redisDistributedService;

    @Autowired // declare cache
    private RedisInfrasService redisInfrasService;

    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    @Autowired
    private final static Cache<Long, TicketDetailCache> ticketDetailLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(8)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public boolean orderTicketByUser(Long ticketId) {
        ticketDetailLocalCache.invalidate(ticketId); // remove local cache
        redisInfrasService.deleteKey(genEventItemKey(ticketId));
        return true;
    }

    /**
     * get ticket item by in in cache
     * @param ticketId
     * @param version
     * @return
     */
    public TicketDetailCache getTicketDetailCache(Long ticketId, Long version) {
        // 1. get item from local cache
        TicketDetailCache ticketDetailCache = getTicketDetailLocalCache(ticketId);

        // YES
        if (ticketDetailCache != null) {
            // User: version, cache: version
            if (version == null) {
                log.info("01: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }
            if (version.equals(ticketDetailCache.getVersion())) {
                log.info("02: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }

            // version < ticketDetailCache.getVersion()
            if (version < ticketDetailCache.getVersion()) {
                log.info("03: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }
            if (version > ticketDetailCache.getVersion()) {
                return getTicketDetailDistributedCache(ticketId);
            }
        } else {
            log.info("Ticket detail not found {}", ticketId);
        }

        return getTicketDetailDistributedCache(ticketId);
    }


    /**
     * get ticket from database
     * @param ticketId
     * @return
     */
    public TicketDetailCache getTicketDetailDatabase(Long ticketId) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genEventItemKeyLock(ticketId));
        try {
            // 1. Create lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);

            // Notice: wheather it is successfull or not -> must unlock anyway
            if (!isLock) {
                return null; // return retry
            }
            // Get cache
            TicketDetailCache ticketDetailCache = redisInfrasService.getObject(genEventItemKey(ticketId), TicketDetailCache.class);

            // 2.Yes
            if (ticketDetailCache != null) {
                return ticketDetailCache;
            }

            TicketDetail ticketDetail = ticketDetailDomainService.getTicketDetailById(ticketId);
            ticketDetailCache = new TicketDetailCache().withClone(ticketDetail).withVersion(System.currentTimeMillis());

            // set data to distributed cache
            redisInfrasService.setObjectTTL(genEventItemKey(ticketId), ticketDetailCache);
            return ticketDetailCache;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    /**
     * get ticket from distributed cache
     * @param ticketId
     * @return
     */
    public TicketDetailCache getTicketDetailDistributedCache(Long ticketId) {
        // 1 - get data
        TicketDetailCache ticketDetailCache = redisInfrasService.getObject(genEventItemKey(ticketId), TicketDetailCache.class);
        if(ticketDetailCache == null){
            log.info("GET TICKET FROM DISTRIBUTED LOCK");
            ticketDetailCache = getTicketDetailDatabase(ticketId);
        }

        // 2 - put data to local cahce
        // lock
        ticketDetailLocalCache.put(ticketId, ticketDetailCache);
        // unlock
        log.info("GET TICKET FROM DISTRIBUTED CACHE");
        return ticketDetailCache;
    }

    /**
     * get ticket from local cache
     * @param ticketId
     * @return
     */
    public TicketDetailCache getTicketDetailLocalCache(Long ticketId) {
        // get cache from GUAVA
        // get cache from Caffein
        return ticketDetailLocalCache.getIfPresent(ticketId);
    }
    private String genEventItemKey(Long itemId) {
//        System.out.println("PRO_TICKET:ITEM:" + itemId);
        return "PRO_TICKET:ITEM:" + itemId;
    }

    private String genEventItemKeyLock(Long ticketId) {
        return "PRO_LOCK_KEY_ITEM" + ticketId;
    }
}
