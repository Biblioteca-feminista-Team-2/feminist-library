package com.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Builder.Default 
    private List<Author> authors = new ArrayList<>();
    
    @Builder.Default 
    private List<Genre> genres = new ArrayList<>();

}
