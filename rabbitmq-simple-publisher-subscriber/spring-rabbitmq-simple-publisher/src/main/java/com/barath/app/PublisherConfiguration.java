package com.barath.app;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PublisherConfiguration {
	
	@Value("${queue.name}")
	private String queueName;

	@Value("${sync.queue.name}")
	private  String syncQueueName;
	
	@Value("${exchange.name}")
	private String exchangeName;
	
	@Value("${spring.rabbitmq.host:localhost}")
	private String rabbitMQHost;
	
	
	@Value("${spring.rabbitmq.port:5672}")
	private int rabbitMQPort;


	@Value("${spring.rabbitmq.username:guest}")
	private String rabbitUserName;

	@Value("${spring.rabbitmq.password:guest}")
	private String rabbitPassword;
	
	
	@Bean
	public CachingConnectionFactory connectionFactory(){
		CachingConnectionFactory factory=new CachingConnectionFactory();
		factory.setHost(rabbitMQHost);
		factory.setPort(rabbitMQPort);
		factory.setUsername(rabbitUserName);
		factory.setPassword(rabbitPassword);
		return factory;
	}
	
	@Bean
	public Queue queue(){
		return new Queue(queueName);
	}

	@Bean
	public Queue syncQueue(){
		return new Queue(syncQueueName);
	}
	
	@Bean
	public TopicExchange topicExchange(){
		TopicExchange topicExchange=new TopicExchange(exchangeName);
		return topicExchange;
	}

	
	
	@Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }



	@Bean
	public Binding syncBinding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(syncQueueName);
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
		RabbitTemplate rabbitTemplate= new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		return rabbitTemplate;
	}

}
