package com.learn.kafka.domain;

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
public class Book {

	private Integer bookId;
	private String bookName;
	private String bookAuthor;
}
