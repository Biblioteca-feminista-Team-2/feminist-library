package com.library.repository;

import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookRepositoryTest {

    @Mock Connection connection;
    @Mock PreparedStatement stmtBook;
    @Mock PreparedStatement stmtCheckAuthor;
    @Mock PreparedStatement stmtInsertAuthor;
    @Mock PreparedStatement stmtLinkAuthor;
    @Mock PreparedStatement stmtCheckGenre;
    @Mock PreparedStatement stmtInsertGenre;
    @Mock PreparedStatement stmtLinkGenre;
    @Mock PreparedStatement stmtDelete;
    @Mock PreparedStatement stmtFindBooks;
    @Mock PreparedStatement stmtFindAuthors;
    @Mock PreparedStatement stmtFindGenres;
    @Mock ResultSet rsBook;
    @Mock ResultSet rsCheckAuthor;
    @Mock ResultSet rsCheckGenre;
    @Mock ResultSet rsNewAuthor;
    @Mock ResultSet rsNewGenre;
    @Mock ResultSet rsBooks;
    @Mock ResultSet rsAuthResult;
    @Mock ResultSet rsGenreResult;

    @InjectMocks
    BookRepositoryImpl bookRepository;

    // ==================== createBook ====================

    @Test
    void createBook_shouldInsertAllFields() throws Exception {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmtBook);
        when(stmtBook.getGeneratedKeys()).thenReturn(rsBook);
        when(rsBook.next()).thenReturn(true);
        when(rsBook.getInt(1)).thenReturn(1);

        Book book = Book.builder()
                .isbnCode("978-1234567890")
                .title("Test Book")
                .description("A description")
                .publicationDate(LocalDate.of(2024, 1, 15))
                .editorial("Test Editorial")
                .pages(200)
                .idState(true)
                .build();

        bookRepository.createBook(book);

        verify(stmtBook).setString(1, "978-1234567890");
        verify(stmtBook).setString(2, "Test Book");
        verify(stmtBook).setString(3, "A description");
        verify(stmtBook).setDate(eq(4), any(Date.class));
        verify(stmtBook).setString(5, "Test Editorial");
        verify(stmtBook).setInt(6, 200);
        verify(stmtBook).setBoolean(7, true);
        verify(stmtBook).executeUpdate();
    }

    @Test
    void createBook_whenAuthorExists_shouldReuseId() throws Exception {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmtBook);
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtCheckAuthor)
                .thenReturn(stmtLinkAuthor);
        when(stmtBook.getGeneratedKeys()).thenReturn(rsBook);
        when(rsBook.next()).thenReturn(true);
        when(rsBook.getInt(1)).thenReturn(1);
        when(stmtCheckAuthor.executeQuery()).thenReturn(rsCheckAuthor);
        when(rsCheckAuthor.next()).thenReturn(true);
        when(rsCheckAuthor.getInt("id")).thenReturn(99);

        Book book = Book.builder()
                .isbnCode("111")
                .title("Test")
                .publicationDate(LocalDate.now())
                .pages(100)
                .idState(true)
                .build();
        book.getAuthors().add(new Author("Existing Author"));

        bookRepository.createBook(book);

        verify(stmtCheckAuthor).setString(1, "Existing Author");
        verify(stmtLinkAuthor).setInt(1, 1);
        verify(stmtLinkAuthor).setInt(2, 99);
        verify(stmtLinkAuthor).executeUpdate();
        verify(stmtInsertAuthor, never()).executeUpdate();
    }

    @Test
    void createBook_whenAuthorIsNew_shouldCreateIt() throws Exception {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(stmtBook)
                .thenReturn(stmtInsertAuthor);
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtCheckAuthor)
                .thenReturn(stmtLinkAuthor);
        when(stmtBook.getGeneratedKeys()).thenReturn(rsBook);
        when(rsBook.next()).thenReturn(true);
        when(rsBook.getInt(1)).thenReturn(1);
        when(stmtCheckAuthor.executeQuery()).thenReturn(rsCheckAuthor);
        when(rsCheckAuthor.next()).thenReturn(false);
        when(stmtInsertAuthor.getGeneratedKeys()).thenReturn(rsNewAuthor);
        when(rsNewAuthor.next()).thenReturn(true);
        when(rsNewAuthor.getInt(1)).thenReturn(101);

        Book book = Book.builder()
                .isbnCode("222")
                .title("Test")
                .publicationDate(LocalDate.now())
                .pages(100)
                .idState(true)
                .build();
        book.getAuthors().add(new Author("New Author"));

        bookRepository.createBook(book);

        verify(stmtInsertAuthor).setString(1, "New Author");
        verify(stmtInsertAuthor).executeUpdate();
        verify(stmtLinkAuthor).setInt(2, 101);
    }

    @Test
    void createBook_whenGenreIsNew_shouldCreateIt() throws Exception {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(stmtBook)
                .thenReturn(stmtInsertGenre);
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtCheckGenre)
                .thenReturn(stmtLinkGenre);
        when(stmtBook.getGeneratedKeys()).thenReturn(rsBook);
        when(rsBook.next()).thenReturn(true);
        when(rsBook.getInt(1)).thenReturn(1);
        when(stmtCheckGenre.executeQuery()).thenReturn(rsCheckGenre);
        when(rsCheckGenre.next()).thenReturn(false);
        when(stmtInsertGenre.getGeneratedKeys()).thenReturn(rsNewGenre);
        when(rsNewGenre.next()).thenReturn(true);
        when(rsNewGenre.getInt(1)).thenReturn(55);

        Book book = Book.builder()
                .isbnCode("333")
                .title("Test")
                .publicationDate(LocalDate.now())
                .pages(100)
                .idState(true)
                .build();
        book.getGenres().add(new Genre("New Genre"));

        bookRepository.createBook(book);

        verify(stmtInsertGenre).setString(1, "New Genre");
        verify(stmtInsertGenre).executeUpdate();
        verify(stmtLinkGenre).setInt(2, 55);
    }

    @Test
    void createBook_withMultipleAuthorsAndGenres_shouldLinkAll() throws Exception {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmtBook);
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtCheckAuthor).thenReturn(stmtLinkAuthor)
                .thenReturn(stmtCheckAuthor).thenReturn(stmtLinkAuthor)
                .thenReturn(stmtCheckGenre).thenReturn(stmtLinkGenre)
                .thenReturn(stmtCheckGenre).thenReturn(stmtLinkGenre);
        when(stmtBook.getGeneratedKeys()).thenReturn(rsBook);
        when(rsBook.next()).thenReturn(true);
        when(rsBook.getInt(1)).thenReturn(1);
        when(stmtCheckAuthor.executeQuery()).thenReturn(rsCheckAuthor);
        when(rsCheckAuthor.next()).thenReturn(true);
        when(rsCheckAuthor.getInt("id")).thenReturn(10, 20);
        when(stmtCheckGenre.executeQuery()).thenReturn(rsCheckGenre);
        when(rsCheckGenre.next()).thenReturn(true);
        when(rsCheckGenre.getInt("id")).thenReturn(30, 40);

        Book book = Book.builder()
                .isbnCode("444")
                .title("Test")
                .publicationDate(LocalDate.now())
                .pages(100)
                .idState(true)
                .build();
        book.getAuthors().add(new Author("Author 1"));
        book.getAuthors().add(new Author("Author 2"));
        book.getGenres().add(new Genre("Genre 1"));
        book.getGenres().add(new Genre("Genre 2"));

        bookRepository.createBook(book);

        verify(stmtLinkAuthor, times(2)).executeUpdate();
        verify(stmtLinkGenre, times(2)).executeUpdate();
    }

    @Test
    void createBook_whenInsertFails_shouldRollback() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(stmtBook);
        when(stmtBook.executeUpdate()).thenThrow(new SQLException("DB error"));

        Book book = Book.builder()
                .isbnCode("555")
                .title("Test")
                .publicationDate(LocalDate.now())
                .pages(100)
                .idState(true)
                .build();

        bookRepository.createBook(book);

        verify(connection).rollback();
    }

    // ==================== findAllBooks ====================

    @Test
    void findAllBooks_shouldReturnListWithAuthorsAndGenres() throws Exception {
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtFindBooks)
                .thenReturn(stmtFindAuthors)
                .thenReturn(stmtFindGenres);
        when(stmtFindBooks.executeQuery()).thenReturn(rsBooks);
        when(rsBooks.next()).thenReturn(true, false);
        when(rsBooks.getInt("id")).thenReturn(1);
        when(rsBooks.getString("isbn_code")).thenReturn("978-123");
        when(rsBooks.getString("title")).thenReturn("Book 1");
        when(rsBooks.getString("description")).thenReturn("Desc");
        when(rsBooks.getDate("publication_date")).thenReturn(Date.valueOf("2024-01-15"));
        when(rsBooks.getString("editorial")).thenReturn("Ed");
        when(rsBooks.getInt("pages")).thenReturn(200);
        when(rsBooks.getBoolean("Id_state")).thenReturn(true);

        when(stmtFindAuthors.executeQuery()).thenReturn(rsAuthResult);
        when(rsAuthResult.next()).thenReturn(true, false);
        when(rsAuthResult.getString("name")).thenReturn("Author 1");
        when(rsAuthResult.getInt("id")).thenReturn(1);

        when(stmtFindGenres.executeQuery()).thenReturn(rsGenreResult);
        when(rsGenreResult.next()).thenReturn(true, false);
        when(rsGenreResult.getString("name")).thenReturn("Genre 1");
        when(rsGenreResult.getInt("id")).thenReturn(1);

        List<Book> result = bookRepository.findAllBooks();

        assertEquals(1, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals(1, result.get(0).getAuthors().size());
        assertEquals("Author 1", result.get(0).getAuthors().get(0).getName());
        assertEquals(1, result.get(0).getGenres().size());
        assertEquals("Genre 1", result.get(0).getGenres().get(0).getName());
    }

    @Test
    void findAllBooks_whenTableIsEmpty_shouldReturnEmptyList() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(stmtFindBooks);
        when(stmtFindBooks.executeQuery()).thenReturn(rsBooks);
        when(rsBooks.next()).thenReturn(false);

        List<Book> result = bookRepository.findAllBooks();

        assertTrue(result.isEmpty());
    }

    // ==================== deleteBook ====================

    @Test
    void deleteBook_shouldDeleteJoinsAndBook() throws Exception {
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtDelete)
                .thenReturn(stmtDelete)
                .thenReturn(stmtDelete);
        when(stmtDelete.executeUpdate()).thenReturn(1);

        bookRepository.deleteBook(5);

        verify(connection).prepareStatement("DELETE FROM book_author WHERE book_id = ?");
        verify(connection).prepareStatement("DELETE FROM book_genre WHERE book_id = ?");
        verify(connection).prepareStatement("DELETE FROM book WHERE id = ?");
        verify(stmtDelete, times(3)).executeUpdate();
    }

    @Test
    void deleteBook_whenIdDoesNotExist_shouldNotThrow() throws Exception {
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtDelete)
                .thenReturn(stmtDelete)
                .thenReturn(stmtDelete);
        when(stmtDelete.executeUpdate()).thenReturn(1, 1, 0);

        bookRepository.deleteBook(999);
        verify(stmtDelete, times(3)).executeUpdate();
    }

    // ==================== findByTitleBook ====================

    @Test
    void findByTitleBook_shouldReturnMatchingBooks() throws Exception {
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtFindBooks)
                .thenReturn(stmtFindAuthors)
                .thenReturn(stmtFindGenres);
        when(stmtFindBooks.executeQuery()).thenReturn(rsBooks);
        when(rsBooks.next()).thenReturn(true, false);
        when(rsBooks.getInt("id")).thenReturn(1);
        when(rsBooks.getString("isbn_code")).thenReturn("978-123");
        when(rsBooks.getString("title")).thenReturn("Java Programming");
        when(rsBooks.getString("description")).thenReturn("A book");
        when(rsBooks.getDate("publication_date")).thenReturn(Date.valueOf("2024-01-15"));
        when(rsBooks.getString("editorial")).thenReturn("Tech Pub");
        when(rsBooks.getInt("pages")).thenReturn(300);
        when(rsBooks.getBoolean("Id_state")).thenReturn(true);

        when(stmtFindAuthors.executeQuery()).thenReturn(rsAuthResult);
        when(rsAuthResult.next()).thenReturn(false);
        when(stmtFindGenres.executeQuery()).thenReturn(rsGenreResult);
        when(rsGenreResult.next()).thenReturn(false);

        List<Book> result = bookRepository.findByTitleBook("Java");

        assertEquals(1, result.size());
        assertEquals("Java Programming", result.get(0).getTitle());
        verify(stmtFindBooks).setString(1, "%Java%");
    }

    @Test
    void findByTitleBook_withNoResults_shouldReturnEmpty() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(stmtFindBooks);
        when(stmtFindBooks.executeQuery()).thenReturn(rsBooks);
        when(rsBooks.next()).thenReturn(false);

        List<Book> result = bookRepository.findByTitleBook("NonExistent");

        assertTrue(result.isEmpty());
    }

    // ==================== findByAuthorBook ====================

    @Test
    void findByAuthorBook_shouldReturnMatchingBooks() throws Exception {
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtFindBooks)
                .thenReturn(stmtFindAuthors)
                .thenReturn(stmtFindGenres);
        when(stmtFindBooks.executeQuery()).thenReturn(rsBooks);
        when(rsBooks.next()).thenReturn(true, false);
        when(rsBooks.getInt("id")).thenReturn(1);
        when(rsBooks.getString("isbn_code")).thenReturn("978-123");
        when(rsBooks.getString("title")).thenReturn("Her Story");
        when(rsBooks.getString("description")).thenReturn("Desc");
        when(rsBooks.getDate("publication_date")).thenReturn(Date.valueOf("2024-01-15"));
        when(rsBooks.getString("editorial")).thenReturn("Ed");
        when(rsBooks.getInt("pages")).thenReturn(200);
        when(rsBooks.getBoolean("Id_state")).thenReturn(true);

        when(stmtFindAuthors.executeQuery()).thenReturn(rsAuthResult);
        when(rsAuthResult.next()).thenReturn(false);
        when(stmtFindGenres.executeQuery()).thenReturn(rsGenreResult);
        when(rsGenreResult.next()).thenReturn(false);

        List<Book> result = bookRepository.findByAuthorBook("Virginia");

        assertEquals(1, result.size());
        assertEquals("Her Story", result.get(0).getTitle());
        verify(stmtFindBooks).setString(1, "%Virginia%");
    }

    @Test
    void findByAuthorBook_withNoResults_shouldReturnEmpty() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(stmtFindBooks);
        when(stmtFindBooks.executeQuery()).thenReturn(rsBooks);
        when(rsBooks.next()).thenReturn(false);

        List<Book> result = bookRepository.findByAuthorBook("Unknown");

        assertTrue(result.isEmpty());
    }

    // ==================== findByGenreBook ====================

    @Test
    void findByGenreBook_shouldReturnMatchingBooks() throws Exception {
        when(connection.prepareStatement(anyString()))
                .thenReturn(stmtFindBooks)
                .thenReturn(stmtFindAuthors)
                .thenReturn(stmtFindGenres);
        when(stmtFindBooks.executeQuery()).thenReturn(rsBooks);
        when(rsBooks.next()).thenReturn(true, false);
        when(rsBooks.getInt("id")).thenReturn(1);
        when(rsBooks.getString("isbn_code")).thenReturn("978-123");
        when(rsBooks.getString("title")).thenReturn("Feminist Book");
        when(rsBooks.getString("description")).thenReturn("Desc");
        when(rsBooks.getDate("publication_date")).thenReturn(Date.valueOf("2024-01-15"));
        when(rsBooks.getString("editorial")).thenReturn("Ed");
        when(rsBooks.getInt("pages")).thenReturn(200);
        when(rsBooks.getBoolean("Id_state")).thenReturn(true);

        when(stmtFindAuthors.executeQuery()).thenReturn(rsAuthResult);
        when(rsAuthResult.next()).thenReturn(false);
        when(stmtFindGenres.executeQuery()).thenReturn(rsGenreResult);
        when(rsGenreResult.next()).thenReturn(false);

        List<Book> result = bookRepository.findByGenreBook("Feminism");

        assertEquals(1, result.size());
        assertEquals("Feminist Book", result.get(0).getTitle());
        verify(stmtFindBooks).setString(1, "%Feminism%");
    }

    @Test
    void findByGenreBook_withNoResults_shouldReturnEmpty() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(stmtFindBooks);
        when(stmtFindBooks.executeQuery()).thenReturn(rsBooks);
        when(rsBooks.next()).thenReturn(false);

        List<Book> result = bookRepository.findByGenreBook("NonExistent");

        assertTrue(result.isEmpty());
    }
}
