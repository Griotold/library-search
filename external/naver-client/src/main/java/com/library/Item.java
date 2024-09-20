package com.library;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class Item {
    private String title;
    private String link;
    private String image;
    private String author;
    private String discount;
    private String publisher;
    // json 에서는 "pubdate"으로 응답을 내려주고 있음
    @JsonProperty("pubdate")
    private String pubDate;
    private String isbn;
    private String description;
}
