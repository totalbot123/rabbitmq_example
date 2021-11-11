package com.rabbitmq.example.Listener;

import java.util.logging.Logger;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import main.java.com.rabbitmq.example.Commons.Order;
import main.java.com.rabbitmq.example.Commons.OrderType;

@EnableRabbit
@SpringBootApplication
public class ListenerApplication {

	private static Logger logger = Logger.getLogger("Sender");

	private Long timestamp;

	public static void main(String[] args) {
		SpringApplication.run(ListenerApplication.class, args);
	}

	@RabbitListener(queues = "haq1")
	public void onMessage(Order order) {
		if (timestamp == null)
			timestamp = System.currentTimeMillis();
		logger.info((System.currentTimeMillis() - timestamp) + " : " + order.toString());
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");
		connectionFactory.setAddresses("127.0.0.1:5672,127.0.0.1:5673,127.0.0.1:5674");
		connectionFactory.setChannelCacheSize(10);
		return connectionFactory;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrentConsumers(10);
		factory.setMaxConcurrentConsumers(20);
		return factory;
	}

	@Bean
	public Queue queue() {
		return new Queue("haq1");
	}

}
