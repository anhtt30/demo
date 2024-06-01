package com.anhtt.demo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;

@Configuration
@EnableJms
public class JmsConfig implements JmsListenerConfigurer {
	@Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1"); // Set concurrency limits
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setPubSubDomain(false); // false for a Queue, true for a Topic
        return template;
    }

    @Bean
    public BrokerService broker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        return broker;
    }


    private void processMessage(String message) {
        synchronized (message) {
            System.out.println("Received: " + message);
            try {
                message.wait(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar jmsListenerEndpointRegistrar) {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId("myJmsEndpoint1");
        endpoint.setDestination("test.queue.test1");
        endpoint.setMessageListener(message -> {
            try {
                processMessage(message.getJMSMessageID());
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }

        });
        jmsListenerEndpointRegistrar.registerEndpoint(endpoint);

        SimpleJmsListenerEndpoint endpoint2 = new SimpleJmsListenerEndpoint();
        endpoint2.setId("myJmsEndpoint2");
        endpoint2.setDestination("test.queue.test2");
        endpoint2.setMessageListener(message -> {
            try {
                processMessage(message.getJMSMessageID());
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }

        });
        jmsListenerEndpointRegistrar.registerEndpoint(endpoint2);

    }
}
