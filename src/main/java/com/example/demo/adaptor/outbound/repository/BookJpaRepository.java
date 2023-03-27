package com.example.demo.adaptor.outbound.repository;

import com.example.demo.adaptor.outbound.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, String> {

    Optional<BookEntity> findByTitle(String title);
}
