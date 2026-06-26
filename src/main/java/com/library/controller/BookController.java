package com.library.controller;
import com.library.model.Book;
import com.library.repository.BookRepository;

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

    public List<Book> findAllBooks() {
        List<Book> books= bookRepository.findAllBooks();

        for(Book book:books){
            book.setDescription("");
        }
        return books;
    }

    public void updateBook(Book book) {

    }
    public void deleteBook(int id) {
        bookRepository.deleteBook(id);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleBook(title);
    }

    public List<Book> searchByAuthor(String authorName) {
        return bookRepository.findByAuthorBook(authorName);
    }

    public List<Book> searchByGenre(String genreName) {
        return bookRepository.findByGenreBook(genreName);
    }

}
