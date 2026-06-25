package com.library.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;

import com.library.config.DBManager;
import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Genre;

public class BookRepositoryImpl implements BookRepository {

    Connection connection;
    PreparedStatement stmn;

    @Override
    public void createBook(Book book) {
        String sql = "INSERT INTO book (isbn_code, title, description, publication_date, editorial, pages, id_state) VALUES(?,?,?,?,?,?,?)";

        try {
            connection = DBManager.getConnection();
            stmn = connection.prepareStatement(sql);
            stmn.setString(1, book.getIsbnCode());
            stmn.setString(2, book.getTitle());
            stmn.setString(3, book.getDescription());
            stmn.setDate(4, Date.valueOf(book.getPublicationDate()));
            stmn.setString(5, book.getEditorial());
            stmn.setInt(6, book.getPages());
            stmn.setBoolean(7, book.getIdState());
            stmn.executeUpdate();

            ResultSet rsBook = stmnBook.getGeneratedKeys();
        int bookId = 0;
        if (rsBook.next()) {
            bookId = rsBook.getInt(1);
        }
        for (Author author : book.getAuthors()) {
            int authorId = 0;

            // ¿Existe el autor?
            PreparedStatement stmnCheckAuth = connection.prepareStatement(sqlCheckAuthor);
            stmnCheckAuth.setString(1, author.getName());
            ResultSet rsAuth = stmnCheckAuth.executeQuery();

            if (rsAuth.next()) {
                // Si existe, tomamos su ID
                authorId = rsAuth.getInt("id");
            } else {
                // Si NO existe, lo creamos
                PreparedStatement stmnInsAuth = connection.prepareStatement(sqlInsertAuthor, Statement.RETURN_GENERATED_KEYS);
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
                // Si existe, tomamos su ID
                genreId = rsGen.getInt("id");
            } else {
                // Si NO existe, lo creamos
                PreparedStatement stmnInsGen = connection.prepareStatement(sqlInsertGenre, Statement.RETURN_GENERATED_KEYS);
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

            System.out.println("Libro creado con exito");
        } catch (Exception e) {
            try {
            if (connection != null) connection.rollback();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("No se ha podido crear el libro");
            System.out.println(e.getMessage());
        } finally {
            DBManager.closeConnection();
        }

       
    }

    // @Override
    // public void updateBook(Book book) {
       
    // }

    // @Override
    // public void deleteBook(String id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'deleteBook'");
    // }

    // @Override
    // public void findAllBook(String id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findAllBook'");
    // }

    // @Override
    // public void findByGenreBook(String id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findByGenreBook'");
    // }

    // @Override
    // public void findByAuthorBook(String id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findByAuthorBook'");
    // }

    // @Override
    // public void findByTitleBook(String id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findByTitleBook'");
    // }

  

    
}
