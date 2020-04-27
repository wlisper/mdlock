package com.test;

import com.test.mapper.LockMapper;

import java.util.concurrent.TimeUnit;

public class LockImpl {

    LockMapper lockMapper;
    String service;
    String own;
    volatile RefreshThread refreshThread;

    public LockImpl(LockMapper lockMapper, String service, String own) {
        this.lockMapper = lockMapper;
        this.service = service;
        this.own = own;
    }

    public boolean lock(int lockSeconds) {
        if (lockSeconds < 2) {
            throw new RuntimeException("lock time can not less than 2 seconds");
        }
        int ret = lockMapper.lock(service, own, lockSeconds);
        if (ret > 0) {
            if (this.refreshThread == null) {
                this.refreshThread = new RefreshThread(lockSeconds);
                refreshThread.start();
            }
            return true;
        }
        return false;
    }

    public void unlock() {
        try {
            lockMapper.unlock(service, own);
        } finally {
            if (this.refreshThread != null) {
                this.refreshThread.exit();
            }
        }
    }

    @Override
    protected  void finalize() {
        if (this.refreshThread != null) {
            this.refreshThread.exit();
        }
    }

    class RefreshThread extends Thread {

        public RefreshThread(int lockSeconds) {
            this.lockLength = lockSeconds;
            interval = lockSeconds - 1;
        }

        volatile boolean exit = false;
        int interval;
        int lockLength;

        @Override
        public void run() {
            while (!exit) {
                lockMapper.lock(service, own, lockLength);
                try {
                    TimeUnit.SECONDS.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void exit() {
            this.exit = true;
        }
    }
}
