package com.view;

import com.library.controller.BookController;
import java.util.Scanner;

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
                    break;

                case "2":
                    System.out.println(BRIGHT_CYAN + "\n┌──────────────────────────────────────────────┐" + RESET);
                    System.out.println(BRIGHT_CYAN + "│ 🔍 BÚSQUEDA POR AUTORA                       │" + RESET);
                    System.out.println(BRIGHT_CYAN + "└──────────────────────────────────────────────┘" + RESET);
                    System.out.print("Introduce el nombre de la autora: ");
                    String author = scanner.nextLine().trim();
                    
                    System.out.println(BRIGHT_YELLOW + "\nBuscando libros de: \"" + author + "\"..." + RESET);
                    break;

                case "3":
                    System.out.println(BRIGHT_CYAN + "\n┌──────────────────────────────────────────────┐" + RESET);
                    System.out.println(BRIGHT_CYAN + "│ 🔍 BÚSQUEDA POR GÉNERO LITERARIO             │" + RESET);
                    System.out.println(BRIGHT_CYAN + "└──────────────────────────────────────────────┘" + RESET);
                    System.out.print("Introduce el género a filtrar: ");
                    String genre = scanner.nextLine().trim();
                    
                    System.out.println(BRIGHT_YELLOW + "\nFiltrando por el género: \"" + genre + "\"..." + RESET);
                    break;

                case "0":
                    inSubMenu = false;
                    break;

                default:
                    System.out.println(RED + "[!] Opción no válida. Elige 1, 2, 3 o 0." + RESET);
            }
        }
    }
}

