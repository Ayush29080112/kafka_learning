package com.learnkafka.consumer.repository;

import org.springframework.data.repository.CrudRepository;

import com.learnkafka.consumer.entity.LibraryEvent;

public interface LibraryEventsRepository extends CrudRepository<LibraryEvent, Integer>{

	
}
