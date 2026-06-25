package com.view;

import com.library.controller.BookController;
import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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
            System.out.println(BRIGHT_GREEN + "│" + RESET + " [" + BRIGHT_GREEN + "1" + RESET + "] Ver todos los libros                        " + BRIGHT_GREEN + "│" + RESET);
            System.out.println(BRIGHT_GREEN + "│" + RESET + " [" + BRIGHT_GREEN + "2" + RESET + "] Añadir nuevo libro                          " + BRIGHT_GREEN + "│" + RESET);
            System.out.println(BRIGHT_GREEN + "│" + RESET + " [" + BRIGHT_GREEN + "3" + RESET + "] " + BRIGHT_YELLOW + "Editar libro existente" + RESET + "               " + BRIGHT_GREEN + "│" + RESET);
            System.out.println(BRIGHT_GREEN + "│" + RESET + " [" + BRIGHT_GREEN + "4" + RESET + "] " + RED + "Eliminar libro" + RESET + "                       " + BRIGHT_GREEN + "│" + RESET);
            System.out.println(BRIGHT_GREEN + "│" + RESET + ="] ↩ Volver al menú principal  " + BRIGHT_GREEN + = "] ↩ Volver al menú principal                 " +
            System.out.println(BRIGHT_GREEN += "] ↩ Volver al menú principal                 " +
            System.out.println(BRIGHT_GREEN += "] ↩ Volver al menú principal                 "

            System.out.println(GREEN + "└──────────────────────────────────────────────┘" + RESET);
            System.out.print(RED + "Selecciona una opción: " + RESET);

            String subOption = scanner.nextLine().trim();
            switch (subOption) {
                case "1":
                    System.out.println(CYAN + "\n--- Catálogo de Libros ---" + RESET);
                   
                    break;
                case "2":
                    createBookForm(scanner); 
                    break;
                case "3":
                    System.out.println(YELLOW + "\n--- Editar Libro Existing ---" + RESET);
                    
                    break;
                case "4":
                    System.out.println(RED + "\n--- Eliminar Libro ---" + RESET);
                    
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
        System.out.println( GREEN + "\n┌──────────────────────────────────────────────┐" + RESET);
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

        // 1. CAPTURAR UNO O VARIOS AUTORES (Separados por comas)
        System.out.print(CYAN + "Introduce el/los autores (separados por comas, ej: Virginia Woolf, Mary Shelley): " + RESET);
        String authorsInput = scanner.nextLine();
        List<Author> authorList = new ArrayList<>();
        for (String name : authorsInput.split(",")) {
            authorList.add(new Author(name.trim())); 
        }

        // 2. CAPTURAR UNO O VARIOS GÉNEROS (Separados por comas)
        System.out.print(CYAN + "Introduce el/los géneros (separados por comas, ej: Ensayo, Novela): " + RESET);
        String genresInput = scanner.nextLine();
        List<Genre> genreList = new ArrayList<>();
        for (String name : genresInput.split(",")) {
            genreList.add(new Genre(name.trim()));
        }

        // 3. CONSTRUIR EL OBJETO LIBRO UTILIZANDO EL PATRÓN BUILDER
        Book book = Book.builder()
                .isbnCode(isbn)
                .title(title)
                .description(description)
                .publicationDate(date)
                .editorial(editorial)
                .pages(pages)
                .idState(idState)
                .author(authorList) 
                .genres(genreList)   
                .build();

        // 4. ENVIAR AL CONTROLADOR (Lógica que usará tu equipo)
        System.out.println(YELLOW + "\nProcesando envío al sistema..." + RESET);
        bookController.createBook(book);
        
        System.out.println(GREEN + "[ÉXITO] ¡El libro ha sido guardado correctamente!" + RESET);
    }
}

