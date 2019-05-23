package com.estatesoft.ris.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author yxw on 2019-05-23.
 */
@MapperScan("com.estatesoft.ris.wx.mapper")
@SpringBootApplication(scanBasePackages = {"com.estatesoft.ris.wx"})
public class Application{

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(Application.class, args);
        System.out.println("started...");
    }

}
