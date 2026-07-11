package com.example.project.repository;

import com.example.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @org.springframework.data.jpa.repository.Query(
            "select b from Book b join fetch b.categories c where c.id = :categoryId"
    )
    java.util.List<Book> findAllByCategoryId(Long categoryId);
}

