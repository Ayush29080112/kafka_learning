package com.learn.kafka.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonAutoDetect
@ToString
public class LibraryEvent {

	private LibraryEventType libraryEventType;
	private Integer libraryEventId;
	private Book book;
}
