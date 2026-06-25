package com.library.controller;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookController bookController;

    @Test
    void createBook_shouldDelegateToRepository() {
        Book book = Book.builder().title("Test").build();
        bookController.createBook(book);
        verify(bookRepository).createBook(book);
    }

    @Test
    void findAllBooks_shouldClearDescriptions() {
        Book book1 = Book.builder().title("Book 1").description("secret").build();
        Book book2 = Book.builder().title("Book 2").description("confidential").build();
        when(bookRepository.findAllBooks()).thenReturn(List.of(book1, book2));

        List<Book> result = bookController.findAllBooks();

        assertEquals("", result.get(0).getDescription());
        assertEquals("", result.get(1).getDescription());
        verify(bookRepository).findAllBooks();
    }

    @Test
    void deleteBook_shouldDelegateToRepository() {
        bookController.deleteBook(42);
        verify(bookRepository).deleteBook(42);
    }

    @Test
    void searchByTitle_shouldReturnResultsFromRepo() {
        Book book = Book.builder().title("Java").build();
        when(bookRepository.findByTitleBook("Java")).thenReturn(List.of(book));

        List<Book> result = bookController.searchByTitle("Java");

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getTitle());
    }

    @Test
    void searchByAuthor_shouldReturnResultsFromRepo() {
        Book book = Book.builder().title("Book").build();
        when(bookRepository.findByAuthorBook("Virginia")).thenReturn(List.of(book));

        List<Book> result = bookController.searchByAuthor("Virginia");

        assertEquals(1, result.size());
    }

    @Test
    void searchByGenre_shouldReturnResultsFromRepo() {
        Book book = Book.builder().title("Book").build();
        when(bookRepository.findByGenreBook("Feminism")).thenReturn(List.of(book));

        List<Book> result = bookController.searchByGenre("Feminism");

        assertEquals(1, result.size());
    }
}
