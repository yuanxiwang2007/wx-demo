package com.estatesoft.ris.wx.service;

import com.estatesoft.ris.wx.Application;
import com.estatesoft.ris.wx.service.redis.QrRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class QrRedisServiceTest {

    @Autowired
    private QrRedisService qrRedisService;

    @Test
    public void putkeyTest() {

        qrRedisService.putDeviceIdTicketToCache("1321312","values");
        String dd=qrRedisService.getDeviceIdTicketFromCache("1321312");

        String deviceId="10d07afb34d9";
        String linktoken="34WRW45ERFCVXGDFGFD";

        qrRedisService.putDeviceIdLinkTokenToCache(deviceId,linktoken);
        System.out.println("get"+qrRedisService.getLinkTokenFromCache(deviceId));
        qrRedisService.deleteLinkTokenByDeviceId(deviceId);
        System.out.println("get1"+qrRedisService.getLinkTokenFromCache(deviceId));

    }
}
