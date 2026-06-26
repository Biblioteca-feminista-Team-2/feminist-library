package com.view;

import com.library.controller.BookController;
import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.library.config.util.ConsoleColors.*;

public class ManageBooksView {

    private final BookController bookController;

    public ManageBooksView(BookController bookController) {
        this.bookController = bookController;
    }

    public void render(Scanner scanner) {
        boolean inSubMenu = true;
        while (inSubMenu) {
            System.out.println("\n" + BRIGHT_GREEN + "┌── GESTIONAR LIBROS ──────────────────────────┐" + RESET);
            System.out.println(BRIGHT_GREEN + "│" + RESET + " [" + BRIGHT_GREEN + "1" + RESET
                    + "] Ver todos los libros                     " + BRIGHT_GREEN + "│" + RESET);
            System.out.println(BRIGHT_GREEN + "│" + RESET + " [" + BRIGHT_GREEN + "2" + RESET
                    + "] Añadir nuevo libro                       " + BRIGHT_GREEN + "│" + RESET);
            System.out.println(BRIGHT_GREEN + "│" + RESET + " [" + BRIGHT_GREEN + "3" + RESET + "] " + RED
                    + "Eliminar libro" + RESET + "                           " + BRIGHT_GREEN + "│" + RESET);

            System.out.println(BRIGHT_GREEN + "│" + RESET + " [" + BRIGHT_GREEN + "0" + RESET
                    + "] ↩ Volver al menú principal               " + BRIGHT_GREEN + "│" + RESET);

            System.out.println(GREEN + "└──────────────────────────────────────────────┘" + RESET);
            System.out.print(RED + "Selecciona una opción: " + RESET);

            String subOption = scanner.nextLine().trim();
            switch (subOption) {
                case "1":
                    List<Book> allBooks = bookController.findAllBooks();
                    showBooks(allBooks);
                    pressEnterToContinue(scanner);
                    break;
                case "2":
                    createBookForm(scanner);
                    break;
                case "3":
                    System.out.print(RED + "Introduce el ID del libro a eliminar: " + RESET);
                    try {
                        int deleteId = Integer.parseInt(scanner.nextLine().trim());
                        bookController.deleteBook(deleteId);
                    } catch (NumberFormatException e) {
                        System.out.println(RED + "[!] ID no válido." + RESET);
                    }
                    break;
                case "0":
                    inSubMenu = false;
                    break;
                default:
                    System.out.println(RED + "[!] Opción no válida." + RESET);
            }
        }
    }

    public void createBookForm(Scanner scanner) {
        System.out.println(GREEN + "\n┌──────────────────────────────────────────────┐" + RESET);
        System.out.println(GREEN + "│ 📝 FORMULARIO: AÑADIR NUEVO LIBRO            │" + RESET);
        System.out.println(GREEN + "└──────────────────────────────────────────────┘" + RESET);

        System.out.print(CYAN + "Introduce el ISBN: " + RESET);
        String isbn = scanner.nextLine();

        System.out.print(CYAN + "Introduce el título: " + RESET);
        String title = scanner.nextLine();

        System.out.print(CYAN + "Introduce la descripción (máx 200 caracteres): " + RESET);
        String description = scanner.nextLine();

        System.out.print(CYAN + "Introduce la fecha de publicación (yyyy-MM-dd): " + RESET);
        String publicationDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(publicationDate);

        System.out.print(CYAN + "Introduce el nombre de la editorial: " + RESET);
        String editorial = scanner.nextLine();

        System.out.print(CYAN + "Introduce las páginas: " + RESET);
        int pages = Integer.parseInt(scanner.nextLine());

        System.out.print(CYAN + "¿El libro está disponible? (true/false): " + RESET);
        boolean idState = Boolean.parseBoolean(scanner.nextLine());

        System.out.print(
                CYAN + "Introduce el/los autores (separados por comas, ej: Virginia Woolf, Mary Shelley): " + RESET);
        String authorsInput = scanner.nextLine();
        List<Author> authorList = new ArrayList<>();
        for (String name : authorsInput.split(",")) {
            authorList.add(new Author(name.trim()));
        }

        System.out.print(CYAN + "Introduce el/los géneros (separados por comas, ej: Ensayo, Novela): " + RESET);
        String genresInput = scanner.nextLine();
        List<Genre> genreList = new ArrayList<>();
        for (String name : genresInput.split(",")) {
            genreList.add(new Genre(name.trim()));
        }

        Book book = Book.builder()
                .isbnCode(isbn)
                .title(title)
                .description(description)
                .publicationDate(date)
                .editorial(editorial)
                .pages(pages)
                .idState(idState)
                .authors(authorList)
                .genres(genreList)
                .build();

        System.out.println(YELLOW + "\nProcesando envío al sistema..." + RESET);
        bookController.createBook(book);

        System.out.println(GREEN + "[ÉXITO] ¡El libro ha sido guardado correctamente!" + RESET);
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

            String titleLine = padRight(" " + BRIGHT_CYAN + "ID:" + RESET + " " + book.getId() + " | " + BOLD + book.getTitle() + RESET, 70);
            System.out.println(BRIGHT_GREEN + "│" + RESET + titleLine + BRIGHT_GREEN + "│" + RESET);

            String authors = book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", "));
            String authorLine = padRight(" " + BRIGHT_CYAN + "Autoras:" + RESET + " " + authors, 70);
            System.out.println(BRIGHT_GREEN + "│" + RESET + authorLine + BRIGHT_GREEN + "│" + RESET);

            String genres = book.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", "));
            String genreLine = padRight(" " + BRIGHT_CYAN + "Géneros:" + RESET + " " + genres, 70);
            System.out.println(BRIGHT_GREEN + "│" + RESET + genreLine + BRIGHT_GREEN + "│" + RESET);

            String estado = Boolean.TRUE.equals(book.getIdState())
                    ? BRIGHT_GREEN + "✅ Sí" + RESET
                    : BRIGHT_RED + "❌ No" + RESET;
            String extraLine = padRight(" " + BRIGHT_CYAN + "ISBN:" + RESET + " " + book.getIsbnCode() + " | " + book.getEditorial() + " | " + book.getPages() + "p | " + estado, 70);
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
