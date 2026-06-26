package com.library.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;

import com.library.config.DBManager;
import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Genre;

public class BookRepositoryImpl implements BookRepository {
    public BookRepositoryImpl() {
    }

    public BookRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    private Connection connection;
    PreparedStatement stmn;
    Statement statement;

    @Override
    public void createBook(Book book) {
        String sql = "INSERT INTO book (isbn_code, title, description, publication_date, editorial, pages, id_state) VALUES(?,?,?,?,?,?,?)";
        String sqlCheckAuthor = "SELECT id FROM author WHERE name = ?";
        String sqlInsertAuthor = "INSERT INTO author (name) VALUES (?)";
        String sqlInsertBookAuthor = "INSERT INTO book_author (book_id, author_id) VALUES (?, ?)";
        String sqlCheckGenre = "SELECT id FROM genre WHERE name = ?";
        String sqlInsertGenre = "INSERT INTO genre (name) VALUES (?)";
        String sqlInsertBookGenre = "INSERT INTO book_genre (book_id, genre_id) VALUES (?, ?)";
        try {
            if (this.connection == null) {
                connection = DBManager.getConnection();
            }
            connection.setAutoCommit(false);
            stmn = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmn.setString(1, book.getIsbnCode());
            stmn.setString(2, book.getTitle());
            stmn.setString(3, book.getDescription());
            stmn.setDate(4, Date.valueOf(book.getPublicationDate()));
            stmn.setString(5, book.getEditorial());
            stmn.setInt(6, book.getPages());
            stmn.setBoolean(7, book.getIdState());
            stmn.executeUpdate();

            ResultSet rsBook = stmn.getGeneratedKeys();
            int bookId = 0;
            if (rsBook.next()) {
                bookId = rsBook.getInt(1);
            }
            for (Author author : book.getAuthors()) {
                int authorId = 0;

                PreparedStatement stmnCheckAuth = connection.prepareStatement(sqlCheckAuthor);
                stmnCheckAuth.setString(1, author.getName());
                ResultSet rsAuth = stmnCheckAuth.executeQuery();

                if (rsAuth.next()) {
                    authorId = rsAuth.getInt("id");
                } else {
                    PreparedStatement stmnInsAuth = connection.prepareStatement(sqlInsertAuthor,
                            Statement.RETURN_GENERATED_KEYS);
                    stmnInsAuth.setString(1, author.getName());
                    stmnInsAuth.executeUpdate();
                    ResultSet rsNewAuth = stmnInsAuth.getGeneratedKeys();
                    if (rsNewAuth.next()) {
                        authorId = rsNewAuth.getInt(1);
                    }
                }

                PreparedStatement stmnLinkAuth = connection.prepareStatement(sqlInsertBookAuthor);
                stmnLinkAuth.setInt(1, bookId);
                stmnLinkAuth.setInt(2, authorId);
                stmnLinkAuth.executeUpdate();
            }
            for (Genre genre : book.getGenres()) {
                int genreId = 0;

                PreparedStatement stmnCheckGen = connection.prepareStatement(sqlCheckGenre);
                stmnCheckGen.setString(1, genre.getName());
                ResultSet rsGen = stmnCheckGen.executeQuery();

                if (rsGen.next()) {
                    genreId = rsGen.getInt("id");
                } else {
                    PreparedStatement stmnInsGen = connection.prepareStatement(sqlInsertGenre,
                            Statement.RETURN_GENERATED_KEYS);
                    stmnInsGen.setString(1, genre.getName());
                    stmnInsGen.executeUpdate();
                    ResultSet rsNewGen = stmnInsGen.getGeneratedKeys();
                    if (rsNewGen.next()) {
                        genreId = rsNewGen.getInt(1);
                    }
                }

                PreparedStatement stmnLinkGen = connection.prepareStatement(sqlInsertBookGenre);
                stmnLinkGen.setInt(1, bookId);
                stmnLinkGen.setInt(2, genreId);
                stmnLinkGen.executeUpdate();
            }

            connection.commit();
            System.out.println("Libro creado con exito");
        } catch (Exception e) {
            try {
                if (connection != null) connection.rollback();
            } catch (Exception ex) {
                System.out.println("Error en rollback: " + ex.getMessage());
            }
            System.out.println("No se ha podido crear el libro");
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
            } catch (Exception e) {
                // ignore
            }
            DBManager.closeConnection();
        }

    }

    @Override
    public List<Book> findAllBooks() {
        List<Book> bookList = new ArrayList<>();

        String sqlBooks = "SELECT * FROM book";
        String sqlAuthors = "SELECT a.* FROM author a JOIN book_author ba ON a.id = ba.author_id WHERE ba.book_id = ?";
        String sqlGenres = "SELECT g.* FROM genre g JOIN book_genre bg ON g.id = bg.genre_id WHERE bg.book_id = ?";

        try {
            if (this.connection == null) {
                connection = DBManager.getConnection();
            }
            stmn = connection.prepareStatement(sqlBooks);
            ResultSet rsBooks = stmn.executeQuery();

            while (rsBooks.next()) {
                Book book = Book.builder()
                        .id(rsBooks.getInt("id"))
                        .isbnCode(rsBooks.getString("isbn_code"))
                        .title(rsBooks.getString("title"))
                        .description(rsBooks.getString("description"))
                        .publicationDate(rsBooks.getDate("publication_date").toLocalDate())
                        .editorial(rsBooks.getString("editorial"))
                        .pages(rsBooks.getInt("pages"))
                        .idState(rsBooks.getBoolean("Id_state"))
                        .build();

                bookList.add(book);
            }

            rsBooks.close();
            stmn.close();

            for (Book book : bookList) {
                // --- Cargar Autores ---
                PreparedStatement stmnAuth = connection.prepareStatement(sqlAuthors);
                stmnAuth.setInt(1, book.getId());
                ResultSet rsAuth = stmnAuth.executeQuery();

                List<Author> authorList = new ArrayList<>();
                while (rsAuth.next()) {
                    Author author = new Author(rsAuth.getString("name"));
                    author.setId(rsAuth.getInt("id"));
                    authorList.add(author);
                }
                book.setAuthors(authorList);
                rsAuth.close();
                stmnAuth.close();

                // --- Cargar Géneros ---
                PreparedStatement stmnGen = connection.prepareStatement(sqlGenres);
                stmnGen.setInt(1, book.getId());
                ResultSet rsGen = stmnGen.executeQuery();

                List<Genre> genreList = new ArrayList<>();
                while (rsGen.next()) {
                    Genre genre = new Genre(rsGen.getString("name"));
                    genre.setId(rsGen.getInt("id"));
                    genreList.add(genre);
                }
                book.setGenres(genreList);
                rsGen.close();
                stmnGen.close();
            }

        } catch (Exception e) {
            System.out.println("Error al obtener la lista de libros");
            System.out.println(e.getMessage());
        } finally {
            DBManager.closeConnection();
        }

        return bookList;
    }

    @Override
    public void deleteBook(int id) {
        String sqlDeleteBookAuthor = "DELETE FROM book_author WHERE book_id = ?";
        String sqlDeleteBookGenre = "DELETE FROM book_genre WHERE book_id = ?";
        String sqlDeleteBook = "DELETE FROM book WHERE id = ?";

        try {
            if (this.connection == null) {
                connection = DBManager.getConnection();
            }
            stmn = connection.prepareStatement(sqlDeleteBookAuthor);
            stmn.setInt(1, id);
            stmn.executeUpdate();

            stmn = connection.prepareStatement(sqlDeleteBookGenre);
            stmn.setInt(1, id);
            stmn.executeUpdate();

            stmn = connection.prepareStatement(sqlDeleteBook);
            stmn.setInt(1, id);
            int rows = stmn.executeUpdate();

            if (rows > 0) {
                System.out.println("Libro eliminado con éxito");
            } else {
                System.out.println("No se encontró un libro con ID: " + id);
            }

        } catch (Exception e) {
            System.out.println("Error al eliminar el libro");
            System.out.println(e.getMessage());
        } finally {
            DBManager.closeConnection();
        }

    }

    private void loadAuthorsAndGenres(Connection connection, List<Book> bookList) throws SQLException {
        String sqlAuthors = "SELECT a.* FROM author a JOIN book_author ba ON a.id = ba.author_id WHERE ba.book_id = ?";
        String sqlGenres = "SELECT g.* FROM genre g JOIN book_genre bg ON g.id = bg.genre_id WHERE bg.book_id = ?";

        for (Book book : bookList) {
            // Cargar Autores
            try (PreparedStatement stmnAuth = connection.prepareStatement(sqlAuthors)) {
                stmnAuth.setInt(1, book.getId());
                try (ResultSet rsAuth = stmnAuth.executeQuery()) {
                    List<Author> authorList = new ArrayList<>();
                    while (rsAuth.next()) {
                        Author author = new Author(rsAuth.getString("name"));
                        author.setId(rsAuth.getInt("id"));
                        authorList.add(author);
                    }
                    book.setAuthors(authorList);
                }
            }

            // Cargar Géneros
            try (PreparedStatement stmnGen = connection.prepareStatement(sqlGenres)) {
                stmnGen.setInt(1, book.getId());
                try (ResultSet rsGen = stmnGen.executeQuery()) {
                    List<Genre> genreList = new ArrayList<>();
                    while (rsGen.next()) {
                        Genre genre = new Genre(rsGen.getString("name"));
                        genre.setId(rsGen.getInt("id"));
                        genreList.add(genre);
                    }
                    book.setGenres(genreList);
                }
            }
        }
    }

    @Override
    public List<Book> findByGenreBook(String genreName) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.* FROM book b " +
                "JOIN book_genre bg ON b.id = bg.book_id " +
                "JOIN genre g ON bg.genre_id = g.id " +
                "WHERE LOWER(g.name) LIKE LOWER(?)";
        try {
            if (this.connection == null) {
                connection = DBManager.getConnection();
            }
            stmn = connection.prepareStatement(sql);
            stmn.setString(1, "%" + genreName + "%");
            ResultSet rs = stmn.executeQuery();

            while (rs.next()) {
                Book book = Book.builder()
                        .id(rs.getInt("id"))
                        .isbnCode(rs.getString("isbn_code"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .publicationDate(rs.getDate("publication_date").toLocalDate())
                        .editorial(rs.getString("editorial"))
                        .pages(rs.getInt("pages"))
                        .idState(rs.getBoolean("Id_state"))
                        .build();
                bookList.add(book);
            }
            rs.close();
            stmn.close();

            loadAuthorsAndGenres(connection, bookList);

        } catch (Exception e) {
            System.out.println("Error al buscar libros por género: " + e.getMessage());
        } finally {
            DBManager.closeConnection();
        }
        return bookList;
    }

    @Override
    public List<Book> findByAuthorBook(String authorName) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT b.* FROM book b " +
                "JOIN book_author ba ON b.id = ba.book_id " +
                "JOIN author a ON ba.author_id = a.id " +
                "WHERE LOWER(a.name) LIKE LOWER(?)";
        try {
            if (this.connection == null) {
                connection = DBManager.getConnection();
            }
            stmn = connection.prepareStatement(sql);
            stmn.setString(1, "%" + authorName + "%");
            ResultSet rs = stmn.executeQuery();

            while (rs.next()) {
                Book book = Book.builder()
                        .id(rs.getInt("id"))
                        .isbnCode(rs.getString("isbn_code"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .publicationDate(rs.getDate("publication_date").toLocalDate())
                        .editorial(rs.getString("editorial"))
                        .pages(rs.getInt("pages"))
                        .idState(rs.getBoolean("Id_state"))
                        .build();
                bookList.add(book);
            }
            rs.close();
            stmn.close();

            loadAuthorsAndGenres(connection, bookList);

        } catch (Exception e) {
            System.out.println("Error al buscar libros por autor: " + e.getMessage());
        } finally {
            DBManager.closeConnection();
        }
        return bookList;
    }

    @Override
    public List<Book> findByTitleBook(String title) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE LOWER(title) LIKE LOWER(?)";
        try {
            if (this.connection == null) {
                connection = DBManager.getConnection();
            }
            stmn = connection.prepareStatement(sql);
            stmn.setString(1, "%" + title + "%");
            ResultSet rs = stmn.executeQuery();

            while (rs.next()) {
                Book book = Book.builder()
                        .id(rs.getInt("id"))
                        .isbnCode(rs.getString("isbn_code"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .publicationDate(rs.getDate("publication_date").toLocalDate())
                        .editorial(rs.getString("editorial"))
                        .pages(rs.getInt("pages"))
                        .idState(rs.getBoolean("Id_state"))
                        .build();
                bookList.add(book);
            }
            rs.close();
            stmn.close();

            loadAuthorsAndGenres(connection, bookList);

        } catch (Exception e) {
            System.out.println("Error al buscar libros por título: " + e.getMessage());
        } finally {
            DBManager.closeConnection();
        }
        return bookList;
    }
}
