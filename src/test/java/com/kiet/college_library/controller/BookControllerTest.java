package com.kiet.college_library.controller;

import com.kiet.college_library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BookControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BookService bookService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        bookService.reset();
    }

    @Test
    void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void testGetBookByIdFound() throws Exception {
        mockMvc.perform(get("/api/books/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.title", notNullValue()));
    }

    @Test
    void testGetBookByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/books/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddNewBook() throws Exception {
        String json = """
                {
                    "title": "Distributed Systems",
                    "author": "T. Miller",
                    "category": "Computer Science",
                    "availableCopies": 12
                }
                """;

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Distributed Systems")))
                .andExpect(jsonPath("$.author", is("T. Miller")));
    }

    @Test
    void testUpdateBookFound() throws Exception {
        String json = """
                {
                    "id": 101,
                    "title": "Advanced Java Fundamentals",
                    "author": "J. Doe",
                    "category": "Programming",
                    "availableCopies": 20
                }
                """;

        mockMvc.perform(put("/api/books/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Advanced Java Fundamentals")))
                .andExpect(jsonPath("$.availableCopies", is(20)));
    }

    @Test
    void testUpdateBookNotFound() throws Exception {
        String json = """
                {
                    "id": 8888,
                    "title": "Unknown Book",
                    "author": "None",
                    "category": "None",
                    "availableCopies": 0
                }
                """;

        mockMvc.perform(put("/api/books/8888")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBookSuccess() throws Exception {
        mockMvc.perform(delete("/api/books/102"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));

        mockMvc.perform(get("/api/books/102"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBookNotFound() throws Exception {
        mockMvc.perform(delete("/api/books/7777"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found")));
    }
}
