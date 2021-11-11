package com.rabbitmq.example.Sender;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

 import main.java.com.rabbitmq.example.Commons.Order;
 import main.java.com.rabbitmq.example.Commons.OrderType;

@SpringBootApplication
public class SenderApplication {

	private static Logger logger = Logger.getLogger("Sender");

	 @Autowired
	 private ConnectionFactory connectionFactory;

	 @Autowired
	 private RabbitTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(SenderApplication.class, args);
	}

	@PostConstruct
	public void send() {
		for (int i = 0; i < 100000; i++) {
			int id = new Random().nextInt(100000);
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			template.convertAndSend(new Order(id, "TEST"+id, OrderType.values()[(id%2)]));
		}
		logger.info("Sending completed.");
	}

	 @Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");
		connectionFactory.setAddresses("127.0.0.1:5672,127.0.0.1:5673,127.0.0.1:5674");
		return connectionFactory;
	}

	@Bean
		public RabbitTemplate template() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setExchange("hae1");
		return template;
	}

}
