package com.anhtt.demo.bean;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {
	
	//@JmsListener(destination = "test.queue.test1", containerFactory = "myFactory")
    public void receiveMessage(String message) {
        synchronized (message) {
            System.out.println("Received: " + message);
            try {
                message.wait(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //@JmsListener(destination = "test.queue.test2", containerFactory = "myFactory")
    public void receiveMessage2(String message) {
        synchronized (message) {
            System.out.println("Received: " + message);
            try {
                message.wait(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
