package com.jts.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Post {

	@Id
	private long id;

	private String title;

	private String comment;

}
