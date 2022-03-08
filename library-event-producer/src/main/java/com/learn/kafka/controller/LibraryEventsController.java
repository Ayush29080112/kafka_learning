package com.learn.kafka.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learn.kafka.domain.LibraryEvent;
import com.learn.kafka.domain.LibraryEventType;
import com.learn.kafka.producer.LibraryEventProducer;

@RestController
public class LibraryEventsController {
	

	@Autowired
	LibraryEventProducer producer;
	@PostMapping(path = "/v1/libraryEvent", consumes =MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LibraryEvent> newBookEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException{
		
		//producer.sentLibraryEvent(libraryEvent);
		//producer.sentLibraryEventSynchronous(libraryEvent);
		libraryEvent.setLibraryEventType(LibraryEventType.NEW);
		producer.sentLibraryEvent_WithTopic(libraryEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
	
	
	@PutMapping(path = "/v1/libraryEvent", consumes =MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatet(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException{
		
		//producer.sentLibraryEvent(libraryEvent);
		//producer.sentLibraryEventSynchronous(libraryEvent);
		if(libraryEvent.getLibraryEventId()==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Library Event Id missing!!!");
		}
		libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
		producer.sentLibraryEvent_WithTopic(libraryEvent);
		return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
	}
}
