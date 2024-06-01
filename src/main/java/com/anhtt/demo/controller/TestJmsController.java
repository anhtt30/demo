package com.anhtt.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestJmsController {

    @Autowired
    JmsTemplate jmsTemplate;

    @GetMapping("/sendMessage/{message}")
    public String sendMessage(@PathVariable String message) {

        jmsTemplate.convertAndSend("test.queue.test1", message);
        return message;
    }
    @GetMapping("/sendMessage2/{message}")
    public String sendMessage2(@PathVariable String message) {

        jmsTemplate.convertAndSend("test.queue.test2", message);
        return message;
    }

}
