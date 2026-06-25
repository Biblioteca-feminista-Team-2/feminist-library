package com.view;

import com.library.controller.BookController;
import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Genre;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.library.config.util.ConsoleColors.*;

public class SearchBooksView {

    private final BookController bookController;

    public SearchBooksView(BookController bookController) {
        this.bookController = bookController;
    }

    public void render(Scanner scanner) {
        boolean inSubMenu = true;
        while (inSubMenu) {
            System.out.println("\n" + BRIGHT_CYAN + "┌── BUSCAR LIBROS ─────────────────────────────┐" + RESET);
            System.out.println(BRIGHT_CYAN + "│" + RESET + " [" + BRIGHT_CYAN + "1" + RESET + "] Buscar por título                           " + BRIGHT_CYAN + "│" + RESET);
            System.out.println(BRIGHT_CYAN + "│" + RESET + " [" + BRIGHT_CYAN + "2" + RESET + "] Buscar por autora                           " + BRIGHT_CYAN + "│" + RESET);
            System.out.println(BRIGHT_CYAN + "│" + RESET + " [" + BRIGHT_CYAN + "3" + RESET + "] Buscar por género literario                 " + BRIGHT_CYAN + "│" + RESET);
            System.out.println(BRIGHT_CYAN + "│" + RESET + " [" + BRIGHT_CYAN + "0" + RESET + "] ↩ Volver al menú principal                 " + BRIGHT_CYAN + "│" + RESET);
            System.out.println(BRIGHT_CYAN + "└──────────────────────────────────────────────┘" + RESET);
            System.out.print(BRIGHT_PURPLE + "Selecciona una opción: " + RESET);

            String subOption = scanner.nextLine().trim();
            switch (subOption) {
                case "1":
                    System.out.println(BRIGHT_CYAN + "\n┌──────────────────────────────────────────────┐" + RESET);
                    System.out.println(BRIGHT_CYAN + "│ 🔍 BÚSQUEDA POR TÍTULO                       │" + RESET);
                    System.out.println(BRIGHT_CYAN + "└──────────────────────────────────────────────┘" + RESET);
                    System.out.print("Introduce el título del libro: ");
                    String title = scanner.nextLine().trim();

                    System.out.println(BRIGHT_YELLOW + "\nBuscando coincidencias para: \"" + title + "\"..." + RESET);
                    List<Book> byTitle = bookController.searchByTitle(title);
                    showBooks(byTitle);
                    pressEnterToContinue(scanner);
                    break;

                case "2":
                    System.out.println(BRIGHT_CYAN + "\n┌──────────────────────────────────────────────┐" + RESET);
                    System.out.println(BRIGHT_CYAN + "│ 🔍 BÚSQUEDA POR AUTORA                       │" + RESET);
                    System.out.println(BRIGHT_CYAN + "└──────────────────────────────────────────────┘" + RESET);
                    System.out.print("Introduce el nombre de la autora: ");
                    String author = scanner.nextLine().trim();

                    System.out.println(BRIGHT_YELLOW + "\nBuscando libros de: \"" + author + "\"..." + RESET);
                    List<Book> byAuthor = bookController.searchByAuthor(author);
                    showBooks(byAuthor);
                    pressEnterToContinue(scanner);
                    break;

                case "3":
                    System.out.println(BRIGHT_CYAN + "\n┌──────────────────────────────────────────────┐" + RESET);
                    System.out.println(BRIGHT_CYAN + "│ 🔍 BÚSQUEDA POR GÉNERO LITERARIO             │" + RESET);
                    System.out.println(BRIGHT_CYAN + "└──────────────────────────────────────────────┘" + RESET);
                    System.out.print("Introduce el género a filtrar: ");
                    String genre = scanner.nextLine().trim();

                    System.out.println(BRIGHT_YELLOW + "\nFiltrando por el género: \"" + genre + "\"..." + RESET);
                    List<Book> byGenre = bookController.searchByGenre(genre);
                    showBooks(byGenre);
                    pressEnterToContinue(scanner);
                    break;

                case "0":
                    inSubMenu = false;
                    break;

                default:
                    System.out.println(RED + "[!] Opción no válida. Elige 1, 2, 3 o 0." + RESET);
            }
        }
    }

    private void showBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println(BRIGHT_YELLOW + "\n  No se encontraron libros." + RESET);
            return;
        }
        System.out.println();
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);

            System.out.println(BRIGHT_GREEN + "┌────────────────────────────────────────────────┐" + RESET);

            String titleLine = padRight(" " + BRIGHT_CYAN + "ID:" + RESET + " " + book.getId() + " | " + BOLD + book.getTitle() + RESET, 46);
            System.out.println(BRIGHT_GREEN + "│" + RESET + titleLine + BRIGHT_GREEN + "│" + RESET);

            String authors = book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", "));
            String authorLine = padRight(" " + BRIGHT_CYAN + "Autoras:" + RESET + " " + authors, 46);
            System.out.println(BRIGHT_GREEN + "│" + RESET + authorLine + BRIGHT_GREEN + "│" + RESET);

            String genres = book.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", "));
            String genreLine = padRight(" " + BRIGHT_CYAN + "Géneros:" + RESET + " " + genres, 46);
            System.out.println(BRIGHT_GREEN + "│" + RESET + genreLine + BRIGHT_GREEN + "│" + RESET);

            String estado = Boolean.TRUE.equals(book.getIdState())
                    ? BRIGHT_GREEN + "✅ Sí" + RESET
                    : BRIGHT_RED + "❌ No" + RESET;
            String extraLine = padRight(" " + BRIGHT_CYAN + "ISBN:" + RESET + " " + book.getIsbnCode() + " | " + book.getEditorial() + " | " + book.getPages() + "p | " + estado, 46);
            System.out.println(BRIGHT_GREEN + "│" + RESET + extraLine + BRIGHT_GREEN + "│" + RESET);

            if (i < books.size() - 1) {
                System.out.println(BRIGHT_GREEN + "├────────────────────────────────────────────────┤" + RESET);
            } else {
                System.out.println(BRIGHT_GREEN + "└────────────────────────────────────────────────┘" + RESET);
            }
        }
    }

    private String padRight(String text, int width) {
        String plain = text.replaceAll("\u001B\\[[;\\d]*m", "");
        int padding = width - plain.length();
        if (padding <= 0) return text;
        return text + " ".repeat(padding);
    }

    private void pressEnterToContinue(Scanner scanner) {
        System.out.println(BRIGHT_PURPLE + "\n  Presiona Enter para continuar..." + RESET);
        scanner.nextLine();
    }
}
