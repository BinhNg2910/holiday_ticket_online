package com.ticketplatform.ddd.infrastructure.distributed.redission;

public interface RedisDistributedService {
    RedisDistributedLocker getDistributedLock(String lockKey);
}
