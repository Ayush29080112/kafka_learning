package com.learn.kafka.producer;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.domain.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventProducer {

	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	public void sentLibraryEvent(LibraryEvent event) throws JsonProcessingException {
		
		Integer key = event.getLibraryEventId();
		String value = objectMapper.writeValueAsString(event);
		ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.sendDefault(key,value);
		future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				// TODO Auto-generated method stub
				handleSuccessMethod(key,value,result);
			}

			

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub
				handleFailure(key,value, ex);
			}
		});
	}
	
	
public void sentLibraryEvent_WithTopic(LibraryEvent event) throws JsonProcessingException {
		
		Integer key = event.getLibraryEventId();
		String value = objectMapper.writeValueAsString(event);
		String topic ="library-events";
		
		ProducerRecord<Integer, String>record = buildProducerRecord(key,value,topic);
		ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(record);
		future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				// TODO Auto-generated method stub
				handleSuccessMethod(key,value,result);
			}

			

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub
				handleFailure(key,value, ex);
			}
		});
	}
	
	private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {
	// TODO Auto-generated method stub
		List<Header> headers = List.of(new RecordHeader("event-source", "scanner".getBytes()));
	return new ProducerRecord<Integer, String>(topic, null,key, value,headers);
}


	public SendResult<Integer, String> sentLibraryEventSynchronous(LibraryEvent event) throws ExecutionException,InterruptedException, JsonProcessingException {
			
			Integer key = event.getLibraryEventId();
			String value = objectMapper.writeValueAsString(event);
			SendResult<Integer, String > sendResult = null;
			try {
				sendResult = kafkaTemplate.sendDefault(key,value).get();
			} catch (ExecutionException| InterruptedException e) {
				// TODO Auto-generated catch block
				log.error("ExecutionException/ InterruptedException occured!!!{}",e.getMessage());
				
				throw e;
			} catch (Exception e) {
				log.error("Exception occured!!!{}",e.getMessage());
				e.printStackTrace();
			}
			return sendResult;
	}
	protected void handleFailure(Integer key, String value, Throwable ex) {
		// TODO Auto-generated method stub
		log.error("Error sending the message!!!",ex.getMessage());
	}
	protected void handleSuccessMethod(Integer key, String value, SendResult<Integer, String> result) {
		// TODO Auto-generated method stub
		log.info("Message sent successfully!!! {} {} {}",key,value,result.getRecordMetadata().partition());
	}
}
