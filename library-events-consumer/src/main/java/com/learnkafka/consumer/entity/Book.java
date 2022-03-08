package com.learnkafka.consumer.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@ToString
@JsonAutoDetect
@Entity
public class Book {
	@Id
	private Integer bookId;
	private String bookName;
	private String bookAuthor;
	@OneToOne
	@JoinColumn(name = "libraryEventId")
	private LibraryEvent libraryEvent;
}
