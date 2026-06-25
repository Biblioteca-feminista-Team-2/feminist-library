package com.library.model;
import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builderpublic Object authors(List<Author> authorList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authors'");
    }
public class Book {
    private final int id;
    private String isbnCode;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private String editorial;
    private int pages;
    private Boolean idState;

}
