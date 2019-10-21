package com.estatesoft.ris.wx.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class HouseService {

    @Async("async")
    public void bantchUpdate() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("dddd");

    }
}
