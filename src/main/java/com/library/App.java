package com.library;

import java.io.Console;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

import com.library.config.DBManager;
import com.library.model.Book;
import com.library.repository.BookRepositoryImpl;
import com.library.controller.BookController;
import com.library.repository.BookRepository;
import com.view.ConsoleView;
public class App {
    public static void main(String[] args) {

        BookRepository bookRepository = new BookRepositoryImpl();
        BookController bookController = new BookController(bookRepository);
        ConsoleView bookView = new ConsoleView(bookController);
        Scanner scanner = new Scanner(System.in);

        bookView.createBook(scanner);
        bookView.deleteBook(scanner);

    }

}
