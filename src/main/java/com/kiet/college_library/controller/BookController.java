package com.kiet.college_library.controller;

import com.kiet.college_library.model.Book;
import com.kiet.college_library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller exposing endpoints for College Library Book Management.
 * Mapped to both "/api/books" and "/books" for flexible access.
 */
@RestController
@RequestMapping({"/api/books", "/books"})
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * b) Implement a REST endpoint to add a new book using @PostMapping and @RequestBody.
     *
     * @param book The book details received in the request body.
     * @return HTTP 201 Created with the saved book details.
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book createdBook = bookService.addBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    /**
     * c) Implement an endpoint to retrieve all books using @GetMapping.
     *
     * @return HTTP 200 OK with the list of all books.
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * c) Implement an endpoint to retrieve a specific book by its ID using @GetMapping.
     *
     * @param id The ID of the book to retrieve.
     * @return HTTP 200 OK with book details if found, or HTTP 404 NOT FOUND if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent()) {
            return ResponseEntity.ok(bookOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book with ID " + id + " not found.");
    }

    /**
     * d) Implement an endpoint to update a book's details, including its title,
     *    author, category, and available copies, using @PutMapping.
     *
     * @param id The ID of the book to update.
     * @param updatedBook The updated book details received in the request body.
     * @return HTTP 200 OK with the updated book if found, or HTTP 404 NOT FOUND if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody Book updatedBook) {
        Optional<Book> bookOptional = bookService.updateBook(id, updatedBook);
        if (bookOptional.isPresent()) {
            return ResponseEntity.ok(bookOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book with ID " + id + " not found.");
    }

    /**
     * e) Implement an endpoint to delete a book by its ID using @DeleteMapping.
     *    Return appropriate HTTP responses for successful deletion and when the book is not found.
     *
     * @param id The ID of the book to delete.
     * @return HTTP 200 OK on successful deletion, or HTTP 404 NOT FOUND when the book is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            return ResponseEntity.ok("Book with ID " + id + " has been deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book with ID " + id + " not found.");
    }
}
