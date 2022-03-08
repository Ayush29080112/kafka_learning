package com.learnkafka.consumer.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonAutoDetect
@ToString
@Entity
public class LibraryEvent {

	@Enumerated(EnumType.STRING)
	private LibraryEventType libraryEventType;
	@Id
	@GeneratedValue
	private Integer libraryEventId;
	
	@OneToOne(mappedBy = "libraryEvent", cascade = CascadeType.ALL)
	@ToString.Exclude
	private Book book;
}
