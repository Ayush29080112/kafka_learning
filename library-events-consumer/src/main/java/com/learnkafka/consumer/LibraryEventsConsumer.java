package com.learnkafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.learnkafka.consumer.service.LibraryEventsService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventsConsumer {

	@Autowired
	LibraryEventsService service;
	@KafkaListener(topics = {"library-events"})
	public void onMessage(ConsumerRecord<Integer, String> record) throws JsonMappingException, JsonProcessingException {
		log.info("Consumer Record: {}", record);
		service.processLibraryEvent(record);
	}
}
