package com.test;

import com.test.mapper.LockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class MLocks {

    @Autowired
    LockMapper lockMapper;

    @Value("${server.port}")
    int port;

    String own;

    @PostConstruct
    public void postConstruct() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            own = inetAddress.getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public LockImpl getLock(String service) {
        return new LockImpl(lockMapper, service, own);
    }

}
