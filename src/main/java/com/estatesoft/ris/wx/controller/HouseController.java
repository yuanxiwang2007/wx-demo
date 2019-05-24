package com.estatesoft.ris.wx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/house")
public class HouseController {

    @GetMapping("/hello")
    public String hello(){

        return "hello"+new Date().getTime();
    }
}
