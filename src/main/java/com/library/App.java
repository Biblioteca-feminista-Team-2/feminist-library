package com.library;

import java.sql.SQLException;
import java.time.LocalDate;

import com.library.config.DBManager;
import com.library.model.Book;
import com.library.repository.BookRepositoryImpl;

public class App {
    public static void main(String[] args) {
        Book book = Book.builder()
                .isbnCode("1222342343")
                .title("La Caperucita")
                .description("lorem ipsum")
                .publicationDate(LocalDate.now())
                .editorial("GG")
                .pages(120)
                .idState(true)
                .build();
        System.out.println(book.getTitle());

        BookRepositoryImpl bookRepositoryImpl = new BookRepositoryImpl();
            bookRepositoryImpl.createBook(book);
    }

}
