
// package com.view;


// import com.library.controller.BookController;
// import com.library.model.Author;
// import com.library.model.Book;
// import com.library.model.Genre;

// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Scanner;
// import com.library.config.util.ConsoleColors;

// public class ConsoleView {

//     private BookController bookController;

//     public ConsoleView(BookController bookController) {

//         this.bookController = bookController;

//     }

//     public void createBook(Scanner scanner) {

//         System.out.print("Introduce el ISBN: ");


//         String isbn = scanner.nextLine();

//         System.out.print("Introduce el título: ");

//         String title = scanner.nextLine();

//         System.out.print("Introduce la descripción (máx 200 caracteres): ");

//         String description = scanner.nextLine();

//         System.out.println("Introduce la fecha de publicación (yyyy-MM-dd): ");

//         String publicationDate = scanner.nextLine();

//         LocalDate date = LocalDate.parse(publicationDate);

//         System.out.println("introduce el nombre de la editorial: ");

//         String editorial = scanner.nextLine();

//         System.out.println("Introduce las paginas: ");

//         int pages = scanner.nextInt();

//         System.out.println("¿El libro esta disponible?: ");

//         boolean idState = scanner.nextBoolean();

//         Book book = Book.builder().isbnCode(isbn).title(title).description(description).publicationDate(date)
//                 .editorial(editorial).pages(pages).idState(idState).build();

//         bookController.createBook(book);


//         // ⚠️ MUY IMPORTANTE: Limpiar el buffer del scanner tras leer un boolean/int
//         scanner.nextLine(); 

//         // 1. CAPTURAR UNO O VARIOS AUTORES (Separados por comas)
//         System.out.print(CYAN + "Introduce el/los autores (separados por comas, ej: Virginia Woolf, Mary Shelley): " + RESET);
//         String authorsInput = scanner.nextLine();
//         List<Author> authorList = new ArrayList<>();
//         for (String name : authorsInput.split(",")) {
//             authorList.add(new Author(name.trim())); // El .trim() borra los espacios libres
//         }

//         // 2. CAPTURAR UNO O VARIOS GÉNEROS (Separados por comas)
//         System.out.print(CYAN + "Introduce el/los géneros (separados por comas, ej: Ensayo, Novela): " + RESET);
//         String genresInput = scanner.nextLine();
//         List<Genre> genreList = new ArrayList<>();
//         for (String name : genresInput.split(",")) {
//             genreList.add(new Genre(name.trim()));
//         }

//         // 3. CONSTRUIR EL OBJETO LIBRO CON LAS LISTAS INCLUIDAS
//         Book book = Book.builder()
//                 .isbnCode(isbn)
//                 .title(title)
//                 .description(description)
//                 .publicationDate(date)
//                 .editorial(editorial)
//                 .pages(pages)
//                 .idState(idState)
//                 .authors(authorList) // Pasamos la lista de autores
//                 .genres(genreList)   // Pasamos la lista de géneros
//                 .build();

//         // 4. ENVIAR AL CONTROLADOR
//         bookController.createBook(book);
//         System.out.println(VERDE + "\nProcesando envío al sistema..." + RESET);
//     }


// }
