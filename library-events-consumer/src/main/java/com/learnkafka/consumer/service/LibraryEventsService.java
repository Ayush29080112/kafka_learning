package com.learnkafka.consumer.service;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.consumer.entity.LibraryEvent;
import com.learnkafka.consumer.repository.LibraryEventsRepository;

import lombok.extern.slf4j.Slf4j;
import sun.jvm.hotspot.debugger.cdbg.TemplateType;

@Service
@Slf4j
public class LibraryEventsService {

	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	KafkaTemplate<Integer, String> template;
	
	@Autowired
	LibraryEventsRepository repo;
	
	public void processLibraryEvent(ConsumerRecord<Integer,String> record) throws JsonMappingException, JsonProcessingException {
		LibraryEvent event = mapper.readValue(record.value(), LibraryEvent.class);
		switch (event.getLibraryEventType()) {
		case NEW:
			event.getBook().setLibraryEvent(event);
			repo.save(event);
			log.info("Saved!!!");
			break;
		case UPDATE:
			//Update
			if(null == event.getLibraryEventId()) {
				throw new IllegalArgumentException();
			}
			Optional<LibraryEvent>existingRecord = repo.findById(event.getLibraryEventId());
			if (!existingRecord.isPresent()) {
				throw new IllegalArgumentException("Not a valid library event");
			}
			repo.save(event);
			break;
		default:
			break;
		}
	}
	
	public void handleRecovery(ConsumerRecord<Integer, String> record) {
		template.send("library-events", record.key(), record.value());
	}
}
