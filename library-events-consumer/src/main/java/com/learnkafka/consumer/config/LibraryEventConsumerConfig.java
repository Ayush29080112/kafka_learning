package com.learnkafka.consumer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.learnkafka.consumer.service.LibraryEventsService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafka
@Slf4j
public class LibraryEventConsumerConfig {

	@Autowired
	LibraryEventsService service;
	@Bean
	ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ConsumerFactory<Object, Object> kafkaConsumerFactory) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		configurer.configure(factory, kafkaConsumerFactory);
		//Default is Batch this updates the value to Manual and hence we need to create a separate config and also create a new Listener which is implemented using AcknowledgingMessageListener
		//factory.getContainerProperties().setAckMode(AckMode.MANUAL);
		//Used to spin up 3 threads with the same instance of Kafka Listener
		//Below option is recommended when we are not running in a cloud like environment
		factory.setConcurrency(3);
		
		//Used to set error handler
		factory.setErrorHandler((exception,data)->{
			log.info("Exception {} occured while processing {}",exception.getMessage(),data);
		});
		
		factory.setRetryTemplate(retryTemplate());
		factory.setRecoveryCallback(context ->{
			if(context.getLastThrowable().getCause() instanceof Exception) {
				// invoke recovery logic
				log.info("Inside recoverable logic!!!");
				ConsumerRecord<Integer, String> record = (ConsumerRecord<Integer, String>) context.getAttribute("record");
				service.handleRecovery(record);
				
			}else {
				log.info("Inside non recoverable logic!!!");
				throw new RuntimeException();
			}
			return null;
		});
		return factory;
	}

	private RetryTemplate retryTemplate() {
		// TODO Auto-generated method stub
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(1000);
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(simpleRetryPolicy());
		retryTemplate.setBackOffPolicy(backOffPolicy);
		return retryTemplate;
	}

	private RetryPolicy simpleRetryPolicy() {
		// TODO Auto-generated method stub
//		SimpleRetryPolicy policy = new SimpleRetryPolicy();
//		policy.setMaxAttempts(3);
		Map<Class<? extends Throwable>, Boolean>map = new HashMap<Class<? extends Throwable>, Boolean>();
		map.put(IllegalArgumentException.class, false);
		map.put(Exception.class, true);
		SimpleRetryPolicy policy = new SimpleRetryPolicy(3,map,true);
		return policy;
	}
}
