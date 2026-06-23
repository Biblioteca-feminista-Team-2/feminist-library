
package com.view;

import com.library.controller.BookController;

import com.library.model.Book;

import java.time.LocalDate;

import java.util.Scanner;

public class ConsoleView {

    private BookController bookController;

    public ConsoleView(BookController bookController) {

        this.bookController = bookController;

    }

    public void createBook(Scanner scanner) {

        System.out.print("Introduce el ISBN: ");

        String isbn = scanner.nextLine();

        System.out.print("Introduce el título: ");

        String title = scanner.nextLine();

        System.out.print("Introduce la descripción (máx 200 caracteres): ");

        String description = scanner.nextLine();

        System.out.println("Introduce la fecha de publicación (yyyy-MM-dd): ");

        String publicationDate = scanner.nextLine();

        LocalDate date = LocalDate.parse(publicationDate);

        System.out.println("introduce el nombre de la editorial: ");

        String editorial = scanner.nextLine();

        System.out.println("Introduce las paginas: ");

        int pages = scanner.nextInt();

        System.out.println("¿El libro esta disponible?: ");

        boolean idState = scanner.nextBoolean();

        Book book = Book.builder().isbnCode(isbn).title(title).description(description).publicationDate(date)
                .editorial(editorial).pages(pages).idState(idState).build();

        bookController.createBook(book);

    }

}
