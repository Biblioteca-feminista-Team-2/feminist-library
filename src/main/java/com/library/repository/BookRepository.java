package com.library.repository;

import java.util.List;

import com.library.model.Book;

public interface BookRepository {

    void createBook(Book book);

    // void updateBook(Book book);

    void deleteBook(int id);

    List<Book> findAllBooks();

    List<Book> findByGenreBook(String genreName);

    List<Book> findByAuthorBook(String authorName);

    List<Book> findByTitleBook(String title);




}
