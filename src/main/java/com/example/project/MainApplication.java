package com.example.project;

import com.example.project.model.Book;
import com.example.project.service.BookService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("The Hobbit");
            book.setAuthor("J.R.R. Tolkien");
            book.setIsbn("978-0261102217");
            book.setPrice(BigDecimal.valueOf(14.99));
            book.setDescription("A fantasy novel and children's book");
            book.setCoverImage("hobbit.jpg");

            bookService.save(book);
            System.out.println("Saved book: " + book);

            System.out.println("All books in database: " + bookService.findAll());
        };
    }
}

