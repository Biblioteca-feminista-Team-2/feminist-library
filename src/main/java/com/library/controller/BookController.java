package com.library.controller;

import java.util.ArrayList;
import java.util.List;

import com.library.model.Book;
import com.library.repository.BookRepository;


public class BookController {

    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void createBook(Book book) {
        bookRepository.createBook(book);
    }

    public List<Book> getAllBooks() {
        List<Book> mockList = new ArrayList<>();

    }

    public void updateBook(Book book) {

    }
    public void deleteBook(int id) {
    }

    public List<Book> searchByTitle(String title) {
        return new ArrayList<>();
    }

    public List<Book> searchByAuthor(String authorName) {
        return new ArrayList<>();
    }

    public List<Book> searchByGenre(String genreName) {
        return new ArrayList<>();

    }

}
