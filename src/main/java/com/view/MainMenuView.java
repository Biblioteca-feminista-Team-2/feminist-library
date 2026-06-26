package com.view;

import com.library.controller.BookController;
import java.util.Scanner;

import static com.library.config.util.ConsoleColors.*;

public class MainMenuView {

    private final BookController bookController;
    private final ManageBooksView manageBooksView;
    private final SearchBooksView searchBooksView;

    public MainMenuView(BookController bookController) {
        this.bookController = bookController;

        this.manageBooksView = new ManageBooksView(bookController);
        this.searchBooksView = new SearchBooksView(bookController);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            showWelcomeAndMenu();
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    manageBooksView.render(scanner);
                    break;
                case "2":
                    searchBooksView.render(scanner);
                    break;
                case "0":
                    running = false;
                    System.out.println(RED + "\n[🚪] Saliendo del sistema. ¡Adiós!" + RESET);
                    break;
                default:
                    System.out.println(RED + "\n[!] Opción no válida. Selecciona 1, 2 o 0." + RESET);
            }
        }
        scanner.close();
    }

    // Renderiza visualmente el bloque de bienvenida y el cuadro del menú principal
    private void showWelcomeAndMenu() {
        // Encabezado de Bienvenida (Rosa/Magenta)
        System.out.println("\n" + BRIGHT_PURPLE + "┌──────────────────────────────────────────────┐" + RESET);
        System.out.println(BRIGHT_PURPLE + "│ 📚 " + BOLD +  "BIBLIOTECA FEMINISTA DEL BARRIO" + RESET + BRIGHT_PURPLE + " 📚        │" + RESET);
        System.out.println(BRIGHT_PURPLE + "│ bienvenid@ de nuevo, Administradora          │" + RESET);
        System.out.println(BRIGHT_PURPLE + "└──────────────────────────────────────────────┘" + RESET);

        // Opciones del Menú Principal (Amarillo)
        System.out.println(YELLOW + "┌── MENÚ PRINCIPAL ────────────────────────────┐" + RESET);
        System.out.println(YELLOW + "│" + RESET + " [" + BRIGHT_YELLOW + "1" + RESET + "] 📄 " + BRIGHT_GREEN + "Gestionar libros" + RESET + " ► submenú            " + BRIGHT_YELLOW + "│" + RESET);
        System.out.println(YELLOW + "│" + RESET + " [" + BRIGHT_YELLOW + "2" + RESET + "] 🔍 " + BRIGHT_CYAN + "Buscar libros" + RESET + " ► submenú               " + BRIGHT_YELLOW + "│" + RESET);
        System.out.println(YELLOW + "│" + RESET + " [" + BRIGHT_YELLOW + "0" + RESET + "] 🚪 Salir                                 " + BRIGHT_YELLOW + "│" + RESET);
        System.out.println(YELLOW + "└──────────────────────────────────────────────┘" + RESET);
        System.out.print(BRIGHT_YELLOW + "Selecciona una opción: " + RESET);
    }
}
