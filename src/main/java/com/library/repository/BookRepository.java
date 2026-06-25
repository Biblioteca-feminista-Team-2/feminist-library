package com.library.repository;

import java.util.List;

import com.library.model.Book;

public interface BookRepository {

    void createBook(Book book);

    // void updateBook(Book book);

    void deleteBook(int id);

    List<Book> findAllBooks();

    // void findByGenreBook(String id); //devuelve todos sin filtrar

    // void findByAuthorBook(String id);

    // void findByTitleBook(String id);




}
