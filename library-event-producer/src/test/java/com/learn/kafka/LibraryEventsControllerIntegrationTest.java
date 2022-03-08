package com.learn.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.learn.kafka.domain.Book;
import com.learn.kafka.domain.LibraryEvent;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"},partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}","spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class LibraryEventsControllerIntegrationTest {

	private Consumer<Integer, String> consumer;
	
	
	@Autowired
	TestRestTemplate restTemplate;
	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;
	
	@BeforeEach
	void setup() {
		Map<String, Object> configs = new HashMap(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
		consumer = new DefaultKafkaConsumerFactory(configs,new IntegerDeserializer(),new StringDeserializer()).createConsumer();
		embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
	}
	
	@AfterEach
	void tearDown() {
		consumer.close();
	}
	@Test
	void postLibraryEvent() {
		Book book = Book.builder().bookId(123).bookAuthor("Ayush").bookName("Kafka Using Spring boot").build();
		
		LibraryEvent libraryEvent = LibraryEvent.builder().libraryEventId(null).book(book).build();
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", MediaType.APPLICATION_JSON.toString());
		HttpEntity<LibraryEvent> httpEntity = new HttpEntity<LibraryEvent>(libraryEvent,headers);
		ResponseEntity<LibraryEvent> response = restTemplate.exchange("/v1/libraryEvent", HttpMethod.POST, httpEntity, LibraryEvent.class);
		assertEquals(HttpStatus.CREATED	, response.getStatusCode());
		ConsumerRecord<Integer, String>rec = KafkaTestUtils.getSingleRecord(consumer, "library-events");
		String val = rec.value();
		System.out.println(val);
	}
}
