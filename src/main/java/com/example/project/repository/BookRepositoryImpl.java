package com.example.project.repository;

import com.example.project.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }
}
