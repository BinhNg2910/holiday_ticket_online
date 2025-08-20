package com.ticketplatform.ddd.infrastructure.distributed.redission;

import java.util.concurrent.TimeUnit;

public interface RedisDistributedLocker {
    // Here only write interface for distributed Lock

    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    void lock(long leaseTime, TimeUnit unit);

    void unlock();

    boolean isLocked();

    boolean isHeldByThread(long threadId);

    boolean isHeldByCurrentThread();
}
