package com.mxt.rabbitmq.producer.controller;

import com.mxt.rabbitmq.producer.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Controller
public class TestController {
    private static String s = "qwertyuioplkjhgfdsazxcvbnm";

    @Autowired
    private ProducerService producerService;
    @RequestMapping("/test/mq")
    @ResponseBody
    public void test() {
        /*for (int i = 0; i < 100; i++) {
            System.out.println(randomStr());
        }
*/
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(()-> {
                while (true) {
                    try {
                        Thread.sleep(100);
                        producerService.send(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now()) + ":" +randomStr());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    public String randomStr() {
        StringBuilder stringBuilder = new StringBuilder();
        int length = s.length();
        Random random = new Random();
        int len = random.nextInt(30) + 10;
        for (int i = 0; i < len; i++) {
            int i1 = random.nextInt(length);
            stringBuilder.append(s.charAt(i1));
        }
        return stringBuilder.toString();
    }

}
