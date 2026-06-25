package com.library.controller;
import com.library.model.Book;
import com.library.repository.BookRepository;

public class BookController {

    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public void createBook(Book book){
        bookRepository.createBook(book);
    }

}