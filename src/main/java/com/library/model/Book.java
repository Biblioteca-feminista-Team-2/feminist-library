package com.library.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private final int id;
    private String isbnCode;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private String editorial;
    private int pages;
    private Boolean idState;

    private List<Author> authors;
    private List<Genre> genres;
}
