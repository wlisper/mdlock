package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@Configuration
public class ScheduleTask {

    @Autowired
    MLocks mLocks;

    @Scheduled(fixedDelay = 100000)
    public void test() {
        LockImpl lock = mLocks.getLock("testService");
        try {
            if (lock.lock(5)) {
                System.out.println("lock ok");
            } else {
                System.out.println("lock error");
            }
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
