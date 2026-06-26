package com.library;

import com.library.repository.BookRepositoryImpl;
import com.library.controller.BookController;
import com.library.repository.BookRepository;
import com.view.MainMenuView;
public class App {
    public static void main(String[] args) {

        BookRepository bookRepository = new BookRepositoryImpl();
        BookController bookController = new BookController(bookRepository);
        MainMenuView bookView = new MainMenuView(bookController);

        bookView.start();

    }

}
