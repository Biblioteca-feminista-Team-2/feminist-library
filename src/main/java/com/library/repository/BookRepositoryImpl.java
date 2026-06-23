package com.library.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;

import com.library.config.DBManager;
import com.library.model.Book;


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
            System.out.println("Libro creado con exito");
        } catch (Exception e) {
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
