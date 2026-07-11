package com.example.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE books SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private BigDecimal price;

    private String description;

    private String coverImage;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @jakarta.persistence.ManyToMany(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinTable(
            name = "books_categories",
            joinColumns = @jakarta.persistence.JoinColumn(name = "book_id"),
            inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "category_id")
    )
    private java.util.Set<Category> categories = new java.util.HashSet<>();
}

