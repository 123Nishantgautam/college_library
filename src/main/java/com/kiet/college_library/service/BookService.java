package com.kiet.college_library.service;

import com.kiet.college_library.model.Book;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BookService {

    private final Map<Integer, Book> bookStore = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(100);

    public BookService() {
        saveBook(new Book(101, "Java Fundamentals", "J. Doe", "Programming", 10));
        saveBook(new Book(102, "Cloud Computing Principles", "A. Smith", "Technology", 7));
    }

    private void saveBook(Book book) {
        bookStore.put(book.getId(), book);
        idCounter.updateAndGet(curr -> Math.max(curr, book.getId()));
    }

    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>(bookStore.values());
        list.sort(Comparator.comparing(Book::getId));
        return list;
    }

    public Optional<Book> getBookById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(bookStore.get(id));
    }

    public Book addBook(Book book) {
        if (book.getId() == null || book.getId() <= 0) {
            book.setId(idCounter.incrementAndGet());
        } else {
            idCounter.updateAndGet(curr -> Math.max(curr, book.getId()));
        }
        bookStore.put(book.getId(), book);
        return book;
    }

    public Optional<Book> updateBook(Integer id, Book updatedBook) {
        if (id == null || !bookStore.containsKey(id)) {
            return Optional.empty();
        }

        Book book = bookStore.get(id);
        if (updatedBook.getTitle() != null) {
            book.setTitle(updatedBook.getTitle());
        }
        if (updatedBook.getAuthor() != null) {
            book.setAuthor(updatedBook.getAuthor());
        }
        if (updatedBook.getCategory() != null) {
            book.setCategory(updatedBook.getCategory());
        }
        if (updatedBook.getAvailableCopies() != null) {
            book.setAvailableCopies(updatedBook.getAvailableCopies());
        }

        bookStore.put(id, book);
        return Optional.of(book);
    }

    public boolean deleteBook(Integer id) {
        if (id == null) {
            return false;
        }
        return bookStore.remove(id) != null;
    }

    public void reset() {
        bookStore.clear();
        idCounter.set(100);
        saveBook(new Book(101, "Java Fundamentals", "J. Doe", "Programming", 10));
        saveBook(new Book(102, "Cloud Computing Principles", "A. Smith", "Technology", 7));
    }
}
